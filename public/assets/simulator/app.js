import {concentrationAxis,groupedPeakCallouts,axisTick} from './plot-utils.js';
import {refreshHelp} from './help.js';
import {defaults,clone,compounds,temperatureAt,environment,pressureAt,logK,signal,importMethod,exportMethod} from './model.js';
const $=s=>document.querySelector(s),escape=s=>String(s).replace(/[&<>"']/g,c=>({'&':'&amp;','<':'&lt;','>':'&gt;','"':'&quot;',"'":'&#39;'}[c]));
const fmt=(n,d=2)=>n==null||!Number.isFinite(n)?'—':n!==0&&Math.abs(n)<10**(-d)?n.toExponential(2):n.toLocaleString('en-US',{maximumFractionDigits:d,minimumFractionDigits:d});
let settings=clone(defaults),result,points,reference=null,selected=2,neighbor=null,view=[0,1],valid=true,timer,revision=0;
const calculationWorker=new Worker(new URL('./worker.js',import.meta.url),{type:'module'});
try{const stored=localStorage.getItem('gc-simulator-method');if(stored)settings=importMethod(stored);}catch{}
const lesson=new URLSearchParams(location.search).get('lesson');if(lesson){settings=clone(defaults);if(lesson==='program')settings.mode='program';}
const fields={
 'program-fields':[['initialTemperature','Initial temperature','°C',40,350,1],['initialHold','Initial hold','min',0,120,.1]],
 'column-fields':[['length','Length','m',1,150,1],['diameter','Inner diameter','mm',.05,1,.01],['film','Film thickness','µm',.01,10,.01]],
 'gas-fields':[['flow','Flow rate','mL/min',.05,20,.05],['pressure','Inlet pressure','kPa gauge',.1,2000,1],['outlet','Outlet pressure','kPa absolute',0,500,1]],
 'injection-fields':[['injection','Injection volume','µL',.01,100,.1],['split','Split ratio','to 1',1,10000,1],['linerLength','Liner length','mm',1,200,1],['linerDiameter','Liner inner diameter','mm',.1,10,.1]],
 'detector-fields':[['timeConstant','Time constant','s',.001,10,.01],['noise','Noise amplitude','µM · √s',0,1000,.1],['offset','Signal offset','µM',-10000,10000,1]],
 'duration-fields':[['duration','Run duration','min',.1,1440,.1],['samplingRate','Sampling rate','samples/s',.1,1000,.1]]
};
for(const [container,list] of Object.entries(fields))$('#'+container).innerHTML=list.map(([key,label,unit,min,max,step])=>`<label class="field">${label} <span class="unit">${unit}</span><input data-key="${key}" type="number" min="${min}" max="${max}" step="${step}"></label>`).join('');
function sync(){
 document.querySelectorAll('[data-key]').forEach(el=>el.type==='checkbox'?el.checked=settings[el.dataset.key]:el.value=settings[el.dataset.key]);
 $('#temperature-range').value=settings.temperature;
 $('#ramps').innerHTML=settings.ramps.map((r,i)=>`<div class="ramp-row">${[['rate','°C/min'],['temperature','°C'],['hold','min']].map(([k,u])=>`<label>${u}<input type="number" data-ramp="${i}" data-param="${k}" value="${r[k]}" aria-label="Ramp ${i+1} ${k}" step="any"></label>`).join('')}<button data-remove-ramp="${i}" aria-label="Remove ramp ${i+1}" ${settings.ramps.length===1?'disabled':''}>×</button></div>`).join('');
 $('#add-ramp').disabled=settings.ramps.length>=12;syncMode();refreshHelp();
}
function syncMode(){
 $('#iso-controls').hidden=settings.mode!=='isothermal';$('#program-controls').hidden=settings.mode!=='program';
 for(const mode of ['isothermal','program'])$('#'+mode).setAttribute('aria-pressed',String(settings.mode===mode));
 for(const [k,disabled] of [['flow',settings.control!=='flow'],['pressure',settings.control!=='pressure'],['split',settings.inlet!=='split'],['duration',settings.autoTime]])$(`[data-key="${k}"]`).disabled=disabled;
}
function calculate(){
 const id=++revision,started=performance.now();
 valid=false;for(const key of ['save','csv','png','reference'])$('#'+key).disabled=true;
 $('#status').textContent='Calculating separation…';
 calculationWorker.onmessage=({data})=>{
 if(data.id!==revision)return;
 try{
  if(data.error)throw Error(data.error);
  const previousEnd=result?.end,wasFull=!result||(view[0]===0&&Math.abs(view[1]-previousEnd)<.001);result=data.result;points=data.points;valid=true;$('#error').hidden=true;if(wasFull)view=[0,result.end];else {const span=Math.min(view[1]-view[0],result.end);view[0]=Math.max(0,Math.min(view[0],result.end-span));view[1]=view[0]+span;}neighbor=null;
  if(!result.peaks.some(p=>p.id===selected))selected=result.peaks[0]?.id??null;
  try{localStorage.setItem('gc-simulator-method',exportMethod(settings));}catch{}
  renderResults();draw();$('#status').textContent=`${settings.mode==='program'?'Temperature-programmed':'Isothermal'} separation · ${settings.sample.length} compounds · updated in ${Math.round(performance.now()-started)} ms`;
 }catch(e){valid=false;$('#error').hidden=false;$('#error').textContent=e.message;$('#status').textContent='Check the settings below. The plot shows the last valid method.';}
 for(const id of ['save','csv','png','reference'])$('#'+id).disabled=!valid;
 };
 calculationWorker.postMessage({id,settings});
}
calculationWorker.onerror=()=>{$('#status').textContent='Calculation could not start.';$('#error').hidden=false;$('#error').textContent='The calculation worker could not load. Reload the page to try again.';};
function schedule(){clearTimeout(timer);revision++;valid=false;for(const id of ['save','csv','png','reference'])$('#'+id).disabled=true;timer=setTimeout(calculate,100);}
document.querySelectorAll('[data-key]').forEach(el=>el.addEventListener('input',()=>{const k=el.dataset.key;settings[k]=el.type==='checkbox'?el.checked:(el.type==='number'||k==='gas')?(el.value===''?NaN:Number(el.value)):el.value;if(k==='temperature')$('#temperature-range').value=el.value;syncMode();schedule();}));
$('#temperature-range').addEventListener('input',e=>{settings.temperature=Number(e.target.value);$('#temperature').value=settings.temperature;schedule();});
for(const mode of ['isothermal','program'])$('#'+mode).onclick=()=>{settings.mode=mode;syncMode();calculate();};
$('#ramps').addEventListener('input',e=>{if(e.target.dataset.ramp!==undefined){settings.ramps[Number(e.target.dataset.ramp)][e.target.dataset.param]=e.target.value===''?NaN:Number(e.target.value);schedule();}});
$('#ramps').addEventListener('click',e=>{const b=e.target.closest('[data-remove-ramp]');if(b){settings.ramps.splice(Number(b.dataset.removeRamp),1);sync();calculate();}});
$('#add-ramp').onclick=()=>{settings.ramps.push({rate:20,temperature:settings.ramps.at(-1).temperature,hold:5});sync();calculate();};
function library(){const search=$('#compound-search').value.toLowerCase(),used=new Set(settings.sample.map(c=>c.id));const available=compounds.filter(c=>!used.has(c.id)&&c.name.toLowerCase().includes(search));$('#compound-library').innerHTML=available.length?available.map(c=>`<option value="${c.id}">${escape(c.name)}</option>`).join(''):'<option>No matching compounds</option>';$('#add-compound').disabled=!available.length;}
$('#compound-search').oninput=library;
$('#add-compound').onclick=()=>{const id=Number($('#compound-library').value);if(!Number.isInteger(id)||!compounds[id])return;settings.sample.push({id,concentration:30});selected=id;calculate();};
$('#sample-body').addEventListener('change',e=>{const id=e.target.dataset.concentration;if(id!==undefined){settings.sample.find(c=>c.id===Number(id)).concentration=e.target.value===''?NaN:Number(e.target.value);calculate();}});
$('#sample-body').addEventListener('click',e=>{const remove=e.target.closest('[data-remove]'),select=e.target.closest('[data-select]');if(remove){settings.sample=settings.sample.filter(c=>c.id!==Number(remove.dataset.remove));calculate();}if(select){neighbor=null;selected=Number(select.dataset.select);renderResults();draw();}const rs=e.target.closest('[data-resolution]');if(rs){selected=Number(rs.dataset.resolution);neighbor=result.peaks.find(p=>p.id===selected)?.resolutionNeighbor??null;renderResults();draw();}});
function renderResults(){
 $('#sample-count').textContent=result.peaks.length;
 $('#sample-body').innerHTML=result.peaks.length?result.peaks.map((p,index)=>`<tr class="${p.id===selected?'selected-row':p.id===neighbor?'neighbor-row':''}"><td><button class="compound-select" data-select="${p.id}" aria-pressed="${p.id===selected}" title="${escape(p.name)}">${index+1}. ${escape(p.name)}</button></td><td><input aria-label="${escape(p.name)} concentration µM" type="number" min="0" max="100000" step="any" data-concentration="${p.id}" value="${p.concentration}"></td><td>${p.time===null?'Not eluted':fmt(p.time/60,3)}</td><td>${fmt(p.sigma,3)}</td><td>${fmt(p.k,2)}</td><td>${fmt(p.amount,2)}</td><td>${p.resolutionNeighbor===null?'—':`<button class="resolution-button" data-resolution="${p.id}" title="Compare with #${result.peaks.findIndex(n=>n.id===p.resolutionNeighbor)+1}: ${escape(result.peaks.find(n=>n.id===p.resolutionNeighbor)?.name)}">${fmt(p.resolution,2)}</button>`}</td><td><button class="remove-compound" data-remove="${p.id}" aria-label="Remove ${escape(p.name)}">×</button></td></tr>`).join(''):'<tr><td colspan="8">Your sample is empty. Add a compound above to get started.</td></tr>';
 const e=result.start,peaks=result.peaks.filter(p=>p.time!==null),hetp=peaks.reduce((sum,p)=>sum+p.hetp,0)/peaks.length;
 $('#metrics').innerHTML=[['Hold-up time <i>t</i><sub>0</sub>',fmt(e.holdUp/60),'min'],['Inlet pressure',fmt(e.pi/1000-101.325,1),'kPa gauge'],['Theoretical plates',fmt(settings.length/hetp,0),''],['Minimum <i>R<sub>s</sub></i>',fmt(Math.min(...peaks.filter(p=>p.resolution!==null).map(p=>p.resolution)),2),'']].map(([a,b,c])=>`<div><span>${a}</span><strong>${b}</strong><small>${c}</small></div>`).join('');
 $('#derived').innerHTML=[['Average velocity',fmt(settings.length/e.holdUp*100,1)+' cm/s'],['Gas viscosity',fmt(e.eta*1e6,3)+' µPa·s'],['Column gas volume',fmt(Math.PI*(e.d/2)**2*settings.length*1e6,3)+' mL'],['Phase ratio',fmt(result.beta,2)],['Average plate height',fmt(hetp*1000,3)+' mm'],['Normalized flow',fmt(e.flow,3)+' mL/min'],['Properties evaluated at',fmt(temperatureAt(settings,0),1)+' °C (initial conditions)']].map(([a,b])=>`<dt>${a}</dt><dd>${b}</dd>`).join('');
 $('#warnings').textContent=result.warnings.join(' ');$('#sampling-result').textContent=`Recorded duration: ${axisTick(result.end/60)} min · ${points.length.toLocaleString()} samples`;$('#neighbor-legend').hidden=neighbor===null;library();refreshHelp();
}
function download(name,blob){const a=document.createElement('a'),url=URL.createObjectURL(blob);a.href=url;a.download=name;a.click();setTimeout(()=>URL.revokeObjectURL(url),1000);}
$('#save').onclick=()=>download('gc-method.json',new Blob([exportMethod(settings)],{type:'application/json'}));
$('#load').onclick=()=>$('#method-file').click();
$('#method-file').onchange=async e=>{try{const f=e.target.files[0];if(!f)return;if(f.size>1e6)throw Error('Method files must be smaller than 1 MB.');const loaded=importMethod(await f.text());settings=clone(loaded);sync();calculate();}catch(err){$('#error').hidden=false;$('#error').textContent=err.message;}finally{e.target.value='';}};
$('#reset').onclick=()=>{settings=clone(defaults);reference=null;$('#clear-reference').hidden=true;$('#reference-legend').hidden=true;$('#compound-search').value='';sync();calculate();};
$('#reference').onclick=()=>{reference={points:points.map(p=>[...p])};$('#clear-reference').hidden=false;$('#reference-legend').hidden=false;draw();};
$('#clear-reference').onclick=()=>{reference=null;$('#clear-reference').hidden=true;$('#reference-legend').hidden=true;draw();};
$('#csv').onclick=()=>{const quote=x=>'"'+String(x??'').replaceAll('"','""')+'"';let csv='Time (min),Signal (µM)\r\n'+points.map(([t,y])=>`${t/60},${y}`).join('\r\n');csv+='\r\n\r\nCompound,Concentration (µM),Retention (min),Sigma (s),k at elution,On column (pmol),Resolution\r\n'+result.peaks.map(p=>[p.name,p.concentration,p.time===null?'Not eluted':p.time/60,p.sigma,p.k,p.amount,p.resolution].map(quote).join(',')).join('\r\n');download('gc-chromatogram.csv',new Blob(['\ufeff'+csv],{type:'text/csv;charset=utf-8'}));};
$('#png').onclick=()=>$('#plot').toBlob(blob=>{if(blob)download('gc-chromatogram.png',blob);});
const overlayNames={temperature:'Oven temperature (°C)',holdup:'Hold-up time (min)',viscosity:'Gas viscosity (µPa·s)',velocity:'Gas velocity (cm/s)',density:'Gas density (mol/m³)',pressure:'Gas pressure (kPa absolute)',retention:'Selected compound k',position:'Selected compound position (m)'};
function interp(trace,t,col){if(!trace.length)return 0;if(t<=trace[0][0])return trace[0][col];for(let i=1;i<trace.length;i++)if(t<=trace[i][0]){const [a,b]=[trace[i-1],trace[i]];return a[col]+(b[col]-a[col])*(t-a[0])/(b[0]-a[0]);}return trace.at(-1)[col];}
function overlayValue(type,t){const temp=temperatureAt(result.settings,t),e=environment(result.settings,temp),z=Number($('#column-position').value)/100,p=result.peaks.find(p=>p.id===selected);switch(type){case'temperature':return temp;case'holdup':return e.holdUp/60;case'viscosity':return e.eta*1e6;case'velocity':return e.u(z)*100;case'density':return pressureAt(z,e.pi,e.po)/(8.3144621*e.T);case'pressure':return pressureAt(z,e.pi,e.po)/1000;case'retention':return p?10**Math.max(-12,Math.min(12,logK(compounds[p.id],temp)))*(249.25025025025025/result.beta):0;case'position':return p?interp(p.trace,t,1):0;default:return 0;}}
$('#overlay').onchange=()=>{const type=$('#overlay').value;$('#position-control').hidden=!['velocity','density','pressure'].includes(type);$('#overlay-legend').hidden=type==='none';$('#overlay-label').textContent=overlayNames[type]||'';draw();};$('#column-position').oninput=draw;
let geom,yBounds;
const detectorAt=t=>points[Math.max(0,Math.min(points.length-1,Math.round(t*result.settings.samplingRate)))][1];
function draw(){
 if(!result)return;
 const canvas=$('#plot'),w=canvas.clientWidth,h=canvas.clientHeight,dpr=devicePixelRatio||1;if(w<50||h<50)return;
 canvas.width=Math.round(w*dpr);canvas.height=Math.round(h*dpr);const ctx=canvas.getContext('2d');ctx.scale(dpr,dpr);ctx.fillStyle='#fff';ctx.fillRect(0,0,w,h);ctx.font='14px Segoe UI, sans-serif';
 const type=$('#overlay').value,ov=type!=='none',compact=w<520,top=25,bottom=43;
 const visible=points.filter(([t])=>t>=view[0]-1/result.settings.samplingRate&&t<=view[1]+1/result.settings.samplingRate);
 let min=0,max=0;for(const [,v] of visible){min=Math.min(min,v);max=Math.max(max,v);}if(reference)for(const [t,v] of reference.points)if(t>=view[0]&&t<=view[1]){min=Math.min(min,v);max=Math.max(max,v);}
 if(min===max)max=min+.05;const range=max-min;max+=range*($('#peak-labels').checked?.3:.12);min-=range*.04;
 if(!$('#auto-y').checked&&yBounds)[min,max]=yBounds;else yBounds=[min,max];
 const unit=concentrationAxis(min,max),ticks=Array.from({length:5},(_,i)=>axisTick((max-(max-min)*i/4)/unit.scale));
 const left=Math.max(compact?48:63,...ticks.map(t=>ctx.measureText(t).width+(compact?24:32))),right=ov?(compact?55:78):16,pw=w-left-right,ph=h-top-bottom;
 const x=t=>left+(t-view[0])/(view[1]-view[0])*pw,y=v=>top+ph-(v-min)/(max-min)*ph;geom={left,pw,w,top,ph};canvas.setAttribute('aria-label',`Chromatogram from ${axisTick(view[0]/60)} to ${axisTick(view[1]/60)} minutes. Signal from ${axisTick(min/unit.scale)} to ${axisTick(max/unit.scale)} ${unit.unit}. ${result.peaks.length} compounds. Selected: ${result.peaks.find(p=>p.id===selected)?.name||'none'}. Use plus/minus to zoom, arrow keys to pan, Home to fit.`);
 ctx.lineWidth=1;for(let i=0;i<=4;i++){const py=top+ph*i/4;ctx.strokeStyle='#f3ece7';ctx.beginPath();ctx.moveTo(left,py);ctx.lineTo(w-right,py);ctx.stroke();ctx.fillStyle='#826a59';ctx.textAlign='right';ctx.fillText(ticks[i],left-6,py+4);}
 for(let i=0;i<=4;i++){const px=left+pw*i/4;ctx.textAlign='center';ctx.fillText(axisTick((view[0]+(view[1]-view[0])*i/4)/60),px,h-24);}
 ctx.save();ctx.translate(compact?12:16,top+ph/2);ctx.rotate(-Math.PI/2);ctx.textAlign='center';ctx.fillText('Signal ('+unit.unit+')',0,0);ctx.restore();ctx.textAlign='center';ctx.fillText('Time (min)',left+pw/2,h-5);
 const line=(arr,color,width=1.4,mapY=y,dash=[])=>{ctx.save();ctx.beginPath();ctx.rect(left,top,pw,ph);ctx.clip();ctx.beginPath();let started=false;for(const [t,v]of arr){if(t<view[0]-1/result.settings.samplingRate||t>view[1]+1/result.settings.samplingRate)continue;const py=mapY(v);if(!Number.isFinite(py))continue;if(!started){ctx.moveTo(x(t),py);started=true;}else ctx.lineTo(x(t),py);}ctx.strokeStyle=color;ctx.lineWidth=width;ctx.setLineDash(dash);ctx.stroke();ctx.restore();};
 if(ov){const arr=Array.from({length:240},(_,i)=>{const t=view[0]+(view[1]-view[0])*i/239;return[t,overlayValue(type,t)];}),maxO=Math.max(1,...arr.map(p=>p[1]))*1.1;
 line(arr,'#437d79',1.5,v=>top+ph-v/maxO*ph);ctx.fillStyle='#356863';ctx.textAlign='left';for(let i=0;i<=4;i++)ctx.fillText(axisTick(maxO*i/4),w-right+5,top+ph-ph*i/4+4);ctx.save();ctx.translate(w-10,top+ph/2);ctx.rotate(Math.PI/2);ctx.textAlign='center';ctx.fillText(overlayNames[type],0,0);ctx.restore();}
 if(reference)line(reference.points,'#ae9e92',1.3,y,[4,3]);line(points,'#3b1900');
 for(const [id,color] of [[neighbor,'#873da0'],[selected,'#f27616']])if(id!==null)line(visible.map(([t])=>[t,signal(result,t,id)]),color,1.7);
 if($('#peak-labels').checked){ctx.font='13px Segoe UI, sans-serif';const peaks=result.peaks.map((p,index)=>({...p,index,x:x(p.time),value:p.time===null?0:detectorAt(p.time)})).filter(p=>p.time!==null&&p.time>=view[0]&&p.time<=view[1]);
 for(const group of groupedPeakCallouts(peaks,left+3,w-right-3,t=>ctx.measureText(t).width)){if(group.value<min||group.value>max)continue;const start=y(group.value),bend=Math.abs(group.labelX-group.x),labelY=Math.max(top+10+group.row*18,start-26-bend-(group.rows-1)*18),labelBottom=labelY+(group.rows-1)*18+4;ctx.strokeStyle='#948174';ctx.lineWidth=.8;ctx.beginPath();ctx.moveTo(group.x,start-2);ctx.lineTo(group.x,start-7);ctx.lineTo(group.labelX,Math.max(labelBottom,start-7-bend));ctx.lineTo(group.labelX,labelBottom);ctx.stroke();ctx.textAlign='center';ctx.fillStyle=group.members.some(p=>p.id===selected)?'#ae4200':'#594334';group.lines.forEach((line,i)=>ctx.fillText(line,group.labelX,labelY+i*18));}
 }
}
function setView(lo,span){span=Math.min(result.end,Math.max(1/result.settings.samplingRate,result.end/10000,span));lo=Math.max(0,Math.min(result.end-span,lo));view=[lo,lo+span];draw();}
function zoom(factor,anchor=.5){const span=view[1]-view[0],next=span*factor;setView(view[0]+span*anchor-next*anchor,next);}
function pan(direction){const span=view[1]-view[0];setView(view[0]+span*.25*direction,span);}
$('#zoom-in').onclick=()=>{if(result)zoom(.5);};$('#zoom-out').onclick=()=>{if(result)zoom(2);};$('#pan-left').onclick=()=>{if(result)pan(-1);};$('#pan-right').onclick=()=>{if(result)pan(1);};$('#fit').onclick=()=>{if(result){view=[0,result.end];draw();}};
$('#auto-y').onchange=draw;$('#peak-labels').onchange=draw;
$('#plot').ondblclick=()=>$('#fit').click();$('#plot').onkeydown=e=>{if(result&&['+','=','-','ArrowLeft','ArrowRight','Home'].includes(e.key)){e.preventDefault();if(e.key==='Home')$('#fit').click();else if(e.key==='ArrowLeft')pan(-1);else if(e.key==='ArrowRight')pan(1);else zoom(e.key==='-'?2:.5);}};
$('#plot').addEventListener('wheel',e=>{if(!result||!geom)return;e.preventDefault();zoom(Math.exp(Math.max(-500,Math.min(500,e.deltaY))*.002),Math.max(0,Math.min(1,(e.offsetX-geom.left)/geom.pw)));},{passive:false});
let drag=null;
const timeAt=x=>view[0]+Math.max(0,Math.min(1,(x-geom.left)/geom.pw))*(view[1]-view[0]);
function nearby(x){const t=timeAt(x),p=result.peaks.filter(p=>p.time!==null).reduce((best,p)=>!best||Math.abs(p.time-t)<Math.abs(best.time-t)?p:best,null);return p&&Math.abs(p.time-t)<=Math.max(p.sigma*3,(view[1]-view[0])/geom.pw*8)?p:null;}
$('#plot').onpointerdown=e=>{if(e.pointerType==='touch'||!result)return;drag={x:e.clientX,view:[...view],moved:false};$('#plot').setPointerCapture(e.pointerId);};
$('#plot').onpointerup=e=>{if(drag&&!drag.moved){const p=nearby(e.offsetX);if(p){selected=p.id;neighbor=null;renderResults();draw();}}drag=null;};$('#plot').onpointercancel=()=>{drag=null;};
$('#plot').onpointermove=e=>{if(!geom||!result)return;if(drag&&Math.abs(e.clientX-drag.x)>3){drag.moved=true;setView(drag.view[0]-(e.clientX-drag.x)/geom.pw*(drag.view[1]-drag.view[0]),drag.view[1]-drag.view[0]);}
 const inside=e.offsetX>=geom.left&&e.offsetX<=geom.left+geom.pw;const cursor=$('#plot-cursor');cursor.hidden=!inside;cursor.style.left=e.offsetX+'px';cursor.style.top=geom.top+'px';cursor.style.height=geom.ph+'px';const p=inside&&!drag?.moved?nearby(e.offsetX):null;$('#tooltip').hidden=!p;if(p){const t=timeAt(e.offsetX),v=detectorAt(t),unit=concentrationAxis(v,v);$('#tooltip').textContent=`#${result.peaks.indexOf(p)+1} ${p.name}\n${axisTick(t/60)} min · ${axisTick(v/unit.scale)} ${unit.unit}`;}};
$('#plot').onpointerleave=()=>{$('#tooltip').hidden=true;$('#plot-cursor').hidden=true;};
const slot=$('.plot-slot'),wrap=$('.plot-wrap');let docked=false;
function dock(){const rect=slot.getBoundingClientRect(),bottom=$('.sim-workspace').getBoundingClientRect().bottom;const next=matchMedia('(max-width:550px)').matches&&rect.top<0&&bottom>280;if(next===docked)return;if(next)slot.style.height=rect.height+'px';else slot.style.height='';docked=next;wrap.classList.toggle('docked',next);draw();}
window.addEventListener('scroll',dock,{passive:true});window.addEventListener('resize',dock);
new ResizeObserver(draw).observe($('#plot'));sync();calculate();
