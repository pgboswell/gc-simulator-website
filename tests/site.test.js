import test from 'node:test';
import assert from 'node:assert/strict';
import fs from 'node:fs';
import path from 'node:path';
import worker from '../worker.js';
import {onRequest} from '../functions/api/contact.js';
const pages=['index','simulator','development','about','contact'];
test('All published pages have descriptions, canonical URLs, accessible navigation, and valid local assets/links',()=>{
 for(const p of pages){const html=fs.readFileSync(`public/${p}.html`,'utf8');assert.match(html,/<meta name="description"/);assert.match(html,/<link rel="canonical"/);assert.match(html,/aria-current="page"/);assert.match(html,/href="#main"/);
  for(const m of html.matchAll(/(?:href|src)="(\/[^"?#]*)(?:[?#][^"]*)?"/g)){const url=m[1];if(url==='/api/contact')continue;const target=url==='/'?'index.html':decodeURIComponent(url.slice(1));assert.ok(fs.existsSync(path.join('public',target)),`${p}: ${url}`);}
 }
});
test('Production redirects preserve query strings and request methods; local URLs stay local',async()=>{
 const env={ASSETS:{fetch:async()=>new Response('ok')}};
 for(const [from,to] of [['http://www.gcsimulator.org/index.php','https://gcsimulator.org/'],['https://gcsimulator.org/simulator.html','https://gcsimulator.org/simulator']]){const r=await worker.fetch(new Request(from),env);assert.equal(r.status,301);assert.equal(r.headers.get('location'),to);assert.equal((await worker.fetch(new Request(to),env)).status,200);}
 const post=await worker.fetch(new Request('http://gcsimulator.org/api/contact',{method:'POST',body:'x'}),env);assert.equal(post.status,308);
 assert.equal((await worker.fetch(new Request('http://gcsimulator/simulator.html'),env)).status,200);
});
test('Contact fails safely when unconfigured and rejects cross-origin posts',async()=>{
 const get=await onRequest({request:new Request('https://gcsimulator.org/api/contact'),env:{}});assert.deepEqual(await get.json(),{ready:false,siteKey:null});
 const unavailable=await onRequest({request:new Request('https://gcsimulator.org/api/contact',{method:'POST',headers:{Origin:'https://gcsimulator.org'}}),env:{}});assert.equal(unavailable.status,503);
 const cross=await onRequest({request:new Request('https://gcsimulator.org/api/contact',{method:'POST',headers:{Origin:'https://other.example'}}),env:{}});assert.equal(cross.status,403);
});
