import {compounds} from './compounds.js';
export {compounds};
export const defaults = {mode:'isothermal',temperature:200,initialTemperature:60,initialHold:1,ramps:[{rate:20,temperature:260,hold:5}],gas:1,control:'flow',flow:1,pressure:50,outlet:0,length:30,diameter:.25,film:.25,injection:1,split:100,inlet:'split',linerLength:78,linerDiameter:2,timeConstant:.1,noise:.5,offset:0,duration:15,autoTime:true,samplingRate:10,sample:[[2,30],[5,60],[12,30],[14,50],[17,100],[18,20],[19,50],[24,40],[30,30],[35,10],[40,100],[45,30],[50,60],[55,40],[60,20]].map(([id,concentration])=>({id,concentration}))};
export const MAX_DATA_POINTS=100000;
export const clone = x=>JSON.parse(JSON.stringify(x));
export function validate(s){
 const bounds={temperature:[40,350],initialTemperature:[40,350],initialHold:[0,120],gas:[0,3],flow:[.05,20],pressure:[.1,2000],outlet:[0,500],length:[1,150],diameter:[.05,1],film:[.01,10],injection:[.01,100],split:[1,10000],linerLength:[1,200],linerDiameter:[.1,10],timeConstant:[.001,10],noise:[0,1000],offset:[-10000,10000],duration:[.1,1440],samplingRate:[.1,1000]};
 for(const [k,[lo,hi]] of Object.entries(bounds)) if(typeof s[k]!=='number'||!Number.isFinite(s[k])||s[k]<lo||s[k]>hi)throw Error(`${k}: enter a number from ${lo} to ${hi}.`);
 for(const [key,values] of Object.entries({mode:['isothermal','program'],control:['flow','pressure'],inlet:['split','splitless']}))if(!values.includes(s[key]))throw Error(`Invalid ${key}.`);
 if(!Number.isInteger(s.gas)||typeof s.autoTime!=='boolean')throw Error('Invalid method settings.');
 if(s.film*2>=s.diameter*1000)throw Error('Film must be thinner than the column radius.');
 if(s.control==='pressure'&&s.pressure+101.325<=s.outlet)throw Error('Inlet pressure must exceed outlet pressure.');
 if(!Array.isArray(s.ramps)||s.ramps.length<1||s.ramps.length>12)throw Error('Use 1–12 temperature ramps.');
 let last=s.initialTemperature;
 for(const r of s.ramps){if(!r||![r.rate,r.temperature,r.hold].every(Number.isFinite)||r.rate<=0||r.rate>200||r.temperature<last||r.temperature>350||r.hold<0||r.hold>120)throw Error('Ramps need a positive rate (up to 200 °C/min), a rising temperature (up to 350 °C), and a hold of 0–120 min.');last=r.temperature;}
 if(!Array.isArray(s.sample)||s.sample.length>102)throw Error('Use up to 102 compounds.');
 const ids=new Set();for(const c of s.sample){if(!c||!Number.isInteger(c.id)||!compounds[c.id]||!Number.isFinite(c.concentration)||c.concentration<=0||c.concentration>100000||ids.has(c.id))throw Error('Each sample compound must be unique with a concentration above 0 and at most 100,000 µM.');ids.add(c.id);}
 return s;
}
export function logK(c,t){
 if(!c.coefficients){const [a,b]=c.data;return a[1]+(t-a[0])*(b[1]-a[1])/(b[0]-a[0]);}
 let i=0;while(i<c.ranges.length&&t>c.ranges[i])i++;
 const a=c.coefficients[i];return a[0]+a[1]*t+a[2]*t*t+a[3]*t*t*t;
}
export function viscosity(T,g){const [a,b,c]=[[8.382,.6892,.005],[18.63,.6958,-.0071],[16.62,.7665,-.0378],[21.04,.8131,-.0426]][g];return a*1e-6*(T/273.15)**(b+c*(T-273.15)/273.15);}
export function gasDiffusion(T,p,g){const [m,v]=[[2.016,6.12],[4.003,2.67],[28.01,18.5],[39.95,16.2]][g];return 100*Math.sqrt(1/m+1/100)/(Math.cbrt(v)+Math.cbrt(200))**2/p*T**1.75*1e-4;}
export const phaseDiffusion=T=>Math.exp(-14.36381977-22101.91968/(8.3144621*T));
export const plateHeight=(dg,ds,u,d,k,f)=>2*dg/u+(1+6*k+11*k*k)*d*d*u/(96*(1+k)**2*dg)+2*f*f*k*u/(3*(1+k)**2*ds);
export const inletPressure=(flow,T,g,po,d,L)=>Math.sqrt(T/298.15*flow/60e6/d*8*101325*(L*viscosity(T,g)*32/d**2)/(Math.PI*d)+po**2);
export const pressureAt=(z,pi,po)=>Math.sqrt(pi*pi*(1-z)+po*po*z);
export const velocity=(z,T,g,pi,po,d,L)=>(pi*pi-po*po)/(2*L*viscosity(T,g)*32/d**2*pressureAt(z,pi,po));
export const holdUp=(T,g,pi,po,d,L)=>4*(L*viscosity(T,g)*32/d**2)*L*(pi**3-po**3)/(3*(pi*pi-po*po)**2);
export function temperatureAt(s,seconds){
 if(s.mode==='isothermal')return s.temperature;
 let time=seconds/60-s.initialHold,prev=s.initialTemperature;if(time<=0)return prev;
 for(const r of s.ramps){const ramp=(r.temperature-prev)/r.rate;if(time<ramp)return prev+time*r.rate;time-=ramp;if(time<r.hold)return r.temperature;time-=r.hold;prev=r.temperature;}return prev;
}
export function environment(s,temp){
 const T=temp+273.15,d=s.diameter/1000-2*s.film/1e6,po=Math.max(1e-5,s.outlet*1000),L=s.length;
 const pi=s.control==='flow'?inletPressure(s.flow,T,s.gas,po,d,L):(s.pressure+101.325)*1000;
 const eta=viscosity(T,s.gas),omega=L*eta*32/d**2;
 const flow=(pi*pi-po*po)*Math.PI*d*d/(8*101325*omega)*298.15/T*60e6;
 return {T,d,po,pi,eta,flow,holdUp:holdUp(T,s.gas,pi,po,d,L),u:z=>velocity(z,T,s.gas,pi,po,d,L)};
}
const beta=(d,f)=>(d/2-f)**2/((d/2)**2-(d/2-f)**2);
export function simulate(input,{slices=1200}={}){
 const s=validate(clone(input)),ratio=beta(.00025,.00000025)/beta(s.diameter/1000,s.film/1e6),start=environment(s,temperatureAt(s,0));
 const limit=86400, warnings=new Set(),peaks=[];
 // Integrate in position. The pressure profile is constant in normalized
 // coordinates at each instant; midpoint stepping resolves temperature ramps.
 for(const sample of s.sample){
  const c=compounds[sample.id],kAt=temp=>10**Math.max(-12,Math.min(12,logK(c,temp)))*ratio;
  let t=0,variance=0,sumH=0,lastU=0,k=kAt(temperatureAt(s,0)),u=0;
  const area=Math.PI*(start.d/2)**2,liner=Math.PI*(s.linerDiameter/2000)**2*s.linerLength/1000;
  variance=(liner/area/(s.inlet==='split'?s.split+1:1)/(1+k))**2/12;
  const trace=[[0,0,k]],dz=1/slices;
  for(let n=0;n<slices;n++){
   const z=n*dz,ta=temperatureAt(s,t),ea=environment(s,ta),ka=kAt(ta);
   let dt=dz*s.length*(1+ka)/ea.u(z+dz/2);
   // For cold starts, don't jump across an entire temperature ramp.
   if(s.mode==='program'){
    let lo=0,hi=Math.min(dt,limit);
    for(let j=0;j<22;j++){let mid=(lo+hi)/2,tm=temperatureAt(s,t+mid/2),em=environment(s,tm);if(mid*em.u(z+dz/2)/(1+kAt(tm))<dz*s.length)lo=mid;else hi=mid;}
    dt=(lo+hi)/2;
   }
   if(!Number.isFinite(dt)||t+dt>limit){t=Infinity;break;}
   const temp=temperatureAt(s,t+dt/2),e=environment(s,temp);k=kAt(temp);u=e.u(z+dz/2);
   const h=plateHeight(gasDiffusion(e.T,pressureAt(z+dz/2,e.pi,e.po),s.gas),phaseDiffusion(e.T),u,e.d,k,s.film/1e6);
   // Exact multiplicative velocity scaling avoids negative variance on cooling.
   if(lastU>0)variance*=(u/lastU)**2;
   variance+=h*dz*s.length;sumH+=h*dz;lastU=u;t+=dt;
   if(n%8===0)trace.push([t,(z+dz)*s.length,k]);
  }
  if(!Number.isFinite(t)){warnings.add('Some compounds do not elute within 24 hours. Raise the oven temperature or adjust the method.');peaks.push({...sample,name:c.name,time:null,sigma:null,resolution:null,resolutionNeighbor:null,trace});continue;}
  trace.push([t,s.length,k]);
  const sigma=Math.sqrt(variance/(u/(1+k))**2+s.timeConstant**2),amount=s.injection*sample.concentration/(s.inlet==='split'?s.split+1:1);
  const flow=environment(s,temperatureAt(s,t)).flow;
  peaks.push({...sample,name:c.name,time:t,sigma,k,amount,height:amount*60/(Math.sqrt(2*Math.PI)*sigma*flow),plates:s.length/sumH,hetp:sumH,trace});
  if(temperatureAt(s,0)<c.data[0][0]||temperatureAt(s,t)>c.data.at(-1)[0])warnings.add('Some retention values extrapolate beyond the measured temperature range.');
 }
 const eluted=peaks.filter(p=>p.time!==null);
 const requestedEnd=s.autoTime?Math.min(limit,Math.max(1,...eluted.map(p=>1.05*(p.time+3*p.sigma)))):s.duration*60;
 const count=Math.min(MAX_DATA_POINTS,Math.ceil(requestedEnd*s.samplingRate)+1),end=(count-1)/s.samplingRate;
 const truncated=requestedEnd>end+1e-8;
 if(truncated)warnings.add(`The ${MAX_DATA_POINTS.toLocaleString()}-point limit was reached. The chromatogram stops early at ${(end/60).toPrecision(3)} min. Reduce the sampling rate to record a longer run.`);
 if(eluted.some(p=>p.time<=end&&p.sigma<2/s.samplingRate))warnings.add('Narrow peaks may be undersampled. Increase the sampling rate.');
 const sorted=[...eluted].sort((a,b)=>a.time-b.time);
 sorted.forEach((p,i)=>{p.resolution=null;p.resolutionNeighbor=null;for(const neighbor of [sorted[i-1],sorted[i+1]])if(neighbor){const rs=Math.abs(p.time-neighbor.time)/(2*(p.sigma+neighbor.sigma));if(p.resolution===null||rs<p.resolution){p.resolution=rs;p.resolutionNeighbor=neighbor.id;}}});
 return {settings:s,start,peaks,end,requestedEnd,count,truncated,warnings:[...warnings],beta:beta(s.diameter/1000,s.film/1e6)};
}
export function signal(result,t,id=null){let y=result.settings.offset;for(const p of result.peaks)if(p.time!==null&&(id===null||p.id===id))y+=p.height*Math.exp(-.5*((t-p.time)/p.sigma)**2);return y;}
export function chromatogram(result,{seed=Math.floor(Math.random()*4294967296)}={}){
 const rand=()=>{seed=(Math.imul(seed,1664525)+1013904223)>>>0;return (seed+.5)/4294967296;};
 return Array.from({length:result.count},(_,i)=>{const t=i/result.settings.samplingRate;return [t,signal(result,t)+Math.sqrt(-2*Math.log(rand()))*Math.cos(2*Math.PI*rand())*result.settings.noise/Math.sqrt(result.settings.timeConstant)];});
}
export function importMethod(text){
 const data=JSON.parse(text);if(data.format!=='gc-simulator-web'||![1,2].includes(data.version))throw Error('Choose a GC Simulator web method (.json). Legacy Java files are not supported.');
 const s=data.settings;if(data.version===1){if(!s||!Number.isFinite(s.points)||s.points<2||!Number.isFinite(s.duration)||s.duration<=0)throw Error('Invalid legacy acquisition settings.');s.samplingRate=Math.min(1000,Math.max(.1,(s.points-1)/(s.duration*60)));delete s.points;}
 return validate(s);
}
export const exportMethod=s=>JSON.stringify({format:'gc-simulator-web',version:2,settings:validate(s)},null,2);
