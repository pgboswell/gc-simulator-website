import test from 'node:test';
import assert from 'node:assert/strict';
import {concentrationAxis,groupedPeakCallouts,axisTick} from '../public/assets/simulator/plot-utils.js';
test('Concentration axes support dilute through concentrated samples',()=>{
 for(const [n,unit] of [[1e-10,'fM'],[1e-5,'pM'],[.01,'nM'],[2,'µM'],[2000,'mM'],[2e6,'M']])assert.equal(concentrationAxis(0,n).unit,unit);
 assert.equal(axisTick(1.234567), '1.23');assert.equal(axisTick(.000012345),'.0000123'.replace(/^\./,'0.'));
});
test('Callouts group coincident centers, preserve every number, and separate distinct labels',()=>{
 const input=Array.from({length:102},(_,i)=>({index:i,x:50+i*2.1,value:i}));
 const labels=groupedPeakCallouts(input,45,280,s=>s.length*7);
 assert.equal(labels.length,102);assert.deepEqual(labels.flatMap(c=>c.members.map(p=>p.index)).sort((a,b)=>a-b),input.map(p=>p.index));
 for(const c of labels){assert.ok(c.labelX-c.width/2>=45-1e-6);assert.ok(c.labelX+c.width/2<=280+1e-6);}
 for(const row of new Set(labels.map(c=>c.row))){const groups=labels.filter(c=>c.row===row);for(let i=1;i<groups.length;i++)assert.ok(groups[i].labelX-groups[i].width/2>=groups[i-1].labelX+groups[i-1].width/2+7.99);}
 const combined=groupedPeakCallouts([{index:0,x:50,value:1},{index:3,x:51,value:2},{index:7,x:54,value:1}],45,280,s=>s.length*7);assert.equal(combined.length,2);assert.deepEqual(combined[0].lines,['1,4']);
});
