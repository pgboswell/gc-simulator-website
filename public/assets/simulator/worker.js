import {simulate,chromatogram} from './model.js';
self.onmessage=({data})=>{
 try{const result=simulate(data.settings),points=chromatogram(result);delete result.start.u;self.postMessage({id:data.id,result,points});}
 catch(error){self.postMessage({id:data.id,error:error.message});}
};
