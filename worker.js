import {onRequest} from './functions/api/contact.js';
const hosts=new Set(['gcsimulator.org','www.gcsimulator.org','gcsimulator-website.pgboswell.workers.dev']);
const pages=['simulator','resources','development','about','contact'];
export default {async fetch(request,env){
 const url=new URL(request.url);
 if(/^\/resources(?:\.html|\.php|\/index\.php|\/)?$/.test(url.pathname))return Response.redirect(new URL('/'+url.search,url).href,302);
 if(hosts.has(url.hostname)){
  const target=new URL(url);target.protocol='https:';target.host='gcsimulator.org';
  if(['/index.php','/index.html'].includes(target.pathname))target.pathname='/';
  for(const p of pages)if([`/${p}.php`,`/${p}/index.php`,`/${p}.html`,`/${p}/`].includes(target.pathname))target.pathname='/'+p;
  if(target.href!==url.href)return Response.redirect(target.href,['GET','HEAD'].includes(request.method)?301:308);
 }
 if(url.pathname.replace(/\/$/,'')==='/api/contact')return onRequest({request,env});
 return env.ASSETS.fetch(request);
}};
