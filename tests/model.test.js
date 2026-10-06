import test from 'node:test';
import assert from 'node:assert/strict';
import fs from 'node:fs';
import {compounds,defaults,clone,validate,logK,viscosity,inletPressure,holdUp,velocity,gasDiffusion,phaseDiffusion,plateHeight,simulate,temperatureAt,chromatogram,signal,exportMethod,importMethod} from '../public/assets/simulator/model.js';
const reference=JSON.parse(fs.readFileSync(new URL('./legacy-reference.json',import.meta.url)));
const close=(a,b,tol=1e-10)=>assert.ok(Math.abs(a-b)<=tol*Math.max(1e-15,Math.abs(b)),`${a} ≠ ${b}`);
test('All 102 compound splines match the original Java interpolation at 14 temperatures',()=>{
 assert.equal(compounds.length,102);
 for(const c of reference.compounds)for(let i=0;i<c.reference.length;i++)close(logK(compounds[c.id],60+20*i),c.reference[i],1e-8);
});
test('24 reference cases match original Java transport and Golay formulas',()=>{
 for(const [gas,T,po,pi,eta,t0,u,dg,ds,h] of reference.physics){close(inletPressure(1,T,gas,po,.0002495,30),pi);close(viscosity(T,gas),eta);close(holdUp(T,gas,pi,po,.0002495,30),t0);close(velocity(.5,T,gas,pi,po,.0002495,30),u);close(gasDiffusion(T,pi,gas),dg);close(phaseDiffusion(T),ds);close(plateHeight(.00001,.00000001,.5,.0002495,5,.00000025),h);}
});
test('Isothermal retention agrees with analytical t0(1+k), both pressure and flow control',()=>{
 for(const gas of [0,1,2,3])for(const control of ['flow','pressure'])for(const outlet of [0,101.325]){
  const s={...clone(defaults),gas,control,outlet};const r=simulate(s);
  for(const p of r.peaks){close(p.time,r.start.holdUp*(1+p.k),1e-5);assert.ok(p.sigma>0&&p.plates>0);}
 }
});
test('Temperature program honors ramp boundaries, holds, and final temperature',()=>{
 const s={...clone(defaults),mode:'program',ramps:[{rate:20,temperature:100,hold:2},{rate:10,temperature:200,hold:1}]};
 for(const [t,temp]of [[0,60],[60,60],[120,80],[180,100],[240,100],[300,100],[600,150],[900,200],[2000,200]])close(temperatureAt(s,t),temp);
});
test('Constant temperature program agrees with isothermal simulation',()=>{
 const a=simulate(defaults),s={...clone(defaults),mode:'program',initialTemperature:200,ramps:[{rate:20,temperature:200,hold:5}]},b=simulate(s);
 for(let i=0;i<a.peaks.length;i++){close(a.peaks[i].time,b.peaks[i].time,1e-5);close(a.peaks[i].sigma,b.peaks[i].sigma,1e-5);}
});
test('Programmed integration converges with refined position steps',()=>{
 const s={...clone(defaults),mode:'program',outlet:101.325};const a=simulate(s),b=simulate(s,{slices:2400});
 for(let i=0;i<a.peaks.length;i++){close(a.peaks[i].time,b.peaks[i].time,.002);close(a.peaks[i].sigma,b.peaks[i].sigma,.015);}
});
test('Split ratio conserves injected amount and detector Gaussian area',()=>{
 const s={...clone(defaults),noise:0,sample:[{id:2,concentration:30}]},r=simulate(s),p=r.peaks[0];close(p.amount,30/101);
 const dt=p.sigma/50;let area=0;for(let t=p.time-10*p.sigma;t<=p.time+10*p.sigma;t+=dt)area+=signal(r,t)*dt;
 close(area,p.amount*60/s.flow,1e-8);
 const splitless=simulate({...s,inlet:'splitless'});close(splitless.peaks[0].amount,30);assert.ok(splitless.peaks[0].sigma>p.sigma);
});
test('Non-elution terminates, empty samples are supported, high settings stay finite',()=>{
 const cold=simulate({...clone(defaults),temperature:40,sample:[{id:101,concentration:30}]});assert.ok(cold.peaks[0].time===null||cold.peaks[0].time<=86400);
 const empty=simulate({...clone(defaults),sample:[]});assert.equal(empty.peaks.length,0);assert.ok(chromatogram(empty).every(p=>p.every(Number.isFinite)));
 for(const gas of [0,1,2,3]){const r=simulate({...clone(defaults),gas,temperature:350});assert.ok(r.peaks.every(p=>Number.isFinite(p.sigma)&&p.sigma>0));}
});
test('Saved methods round-trip, malformed and impossible inputs are rejected',()=>{
 assert.deepEqual(importMethod(exportMethod(defaults)),defaults);
 for(const patch of [{flow:0},{temperature:NaN},{gas:1.5},{mode:'invalid'},{samplingRate:NaN},{control:'pressure',pressure:5,outlet:200},{ramps:[{rate:0,temperature:260,hold:1}]},{sample:[{id:500,concentration:1}]},{sample:[{id:1,concentration:1},{id:1,concentration:2}]}])assert.throws(()=>validate({...clone(defaults),...patch}));
 assert.throws(()=>importMethod('{"format":"other","version":1}'));
});
test('Sampling uses uniform intervals, fresh noise, and reproducible explicit seeds',()=>{
 const r=simulate(defaults),a=chromatogram(r,{seed:42});assert.deepEqual(a,chromatogram(r,{seed:42}));assert.notDeepEqual(a,chromatogram(r,{seed:43}));assert.notDeepEqual(chromatogram(r),chromatogram(r));assert.equal(a[0][0],0);assert.equal(a.at(-1)[0],r.end);assert.equal(a.length,r.count);
 for(let i=1;i<a.length;i++)close(a[i][0]-a[i-1][0],1/defaults.samplingRate,1e-10);
});
test('Automatic end follows last peak in either mode, ignoring long final holds',()=>{
 for(const mode of ['isothermal','program']){const r=simulate({...clone(defaults),mode,ramps:[{rate:20,temperature:260,hold:120}]});const expected=Math.max(...r.peaks.filter(p=>p.time!==null).map(p=>1.05*(p.time+3*p.sigma)));assert.ok(r.end>=expected);assert.ok(r.end-expected<1/r.settings.samplingRate+1e-8);}
});
test('Acquisition truncates at 100,000 samples and reports the limitation',()=>{
 const r=simulate({...clone(defaults),autoTime:false,duration:1440,samplingRate:1000});assert.equal(r.count,100000);assert.equal(r.end,99.999);assert.ok(r.truncated);assert.match(r.warnings.join(' '),/point limit/);
});
test('Resolution is the smaller adjacent-pair value and identifies its neighbor',()=>{
 const r=simulate(defaults),sorted=[...r.peaks].sort((a,b)=>a.time-b.time);
 sorted.forEach((p,i)=>{const adjacent=[sorted[i-1],sorted[i+1]].filter(Boolean);const expected=adjacent.map(n=>({id:n.id,rs:Math.abs(p.time-n.time)/(2*(p.sigma+n.sigma))})).sort((a,b)=>a.rs-b.rs)[0];close(p.resolution,expected.rs);assert.equal(p.resolutionNeighbor,expected.id);});
});
test('Dilute concentrations retain their signal and version 1 methods migrate',()=>{
 const s={...clone(defaults),noise:0,sample:[{id:2,concentration:.0001}]},r=simulate(s);assert.ok(signal(r,r.peaks[0].time)>0);assert.ok(r.peaks[0].height<1);
 const old=clone(defaults);delete old.samplingRate;old.points=6000;const imported=importMethod(JSON.stringify({format:'gc-simulator-web',version:1,settings:old}));close(imported.samplingRate,5999/900);assert.ok(!('points' in imported));
});
