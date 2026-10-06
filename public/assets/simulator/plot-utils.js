// Shared display behavior adapted from the HPLC website.
export function concentrationAxis(min,max){
  const magnitude=Math.max(Math.abs(min),Math.abs(max));
  const units=[['fM',1e-9],['pM',1e-6],['nM',1e-3],['µM',1],['mM',1e3],['M',1e6]];
  let unit=units[0];
  for(const candidate of units){if(Number(magnitude.toPrecision(3))>=candidate[1])unit=candidate;}
  if(magnitude===0)unit=units[3];
  return {unit:unit[0],scale:unit[1]};
}
export function groupedPeakCallouts(peaks,left,right,measure){
  const available=Math.max(20,right-left);
  function describe(members){
    const anchor=members.reduce((a,b)=>a.value>=b.value?a:b);
    const numbers=members.map(p=>p.index+1).sort((a,b)=>a-b),lines=[];
    let line='';
    for(const number of numbers){
      const next=line?`${line},${number}`:String(number);
      if(line&&measure(next)>available){lines.push(line);line=String(number);}else line=next;
    }
    if(line)lines.push(line);
    const width=Math.max(...lines.map(measure));
    const labelX=Math.max(left+width/2,Math.min(right-width/2,anchor.x));
    return {...anchor,members,lines,width,labelX,rows:lines.length};
  }
  // Only combine centers that occupy essentially the same screen position.
  // Label width must never decide whether two peaks are distinguishable.
  const clusters=[];
  for(const peak of peaks.slice().sort((a,b)=>a.x-b.x)){
    const previous=clusters.at(-1);
    if(previous&&peak.x-previous[0].x<=2)previous.push(peak);
    else clusters.push([peak]);
  }
  const groups=clusters.map(describe),rows=[[]];
  let used=0;
  for(const group of groups){
    if(rows.at(-1).length&&used+8+group.width>available){rows.push([]);used=0;}
    group.row=rows.length-1;
    rows.at(-1).push(group);used+=(used?8:0)+group.width;
  }
  // Spread distinct labels without merging their compound numbers.
  for(const row of rows){
    const blocks=[];let offset=0;
    for(let i=0;i<row.length;i++){
      if(i)offset+=(row[i-1].width+row[i].width)/2+8;
      row[i].offset=offset;
      blocks.push({sum:row[i].x-offset,count:1});
      while(blocks.length>1){
        const a=blocks.at(-2),b=blocks.at(-1);
        if(a.sum/a.count<=b.sum/b.count)break;
        blocks.splice(-2,2,{sum:a.sum+b.sum,count:a.count+b.count});
      }
    }
    let i=0;
    for(const block of blocks){
      const base=Math.max(left+row[0].width/2,Math.min(right-row.at(-1).width/2-offset,block.sum/block.count));
      for(let j=0;j<block.count;j++,i++)row[i].labelX=base+row[i].offset;
    }
  }
  return groups;
}
export function axisTick(value){return String(Number(value.toPrecision(3)));}
