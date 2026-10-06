"""Extract the supplied GC database and run the original Java interpolation/math."""
from pathlib import Path
import re, json, subprocess, shutil

ROOT = Path(__file__).resolve().parents[1]
SOURCE = ROOT/'legacy-source/src/org/gcsimulator'
OUT = ROOT/'tests/java/org/gcsimulator'
OUT.mkdir(parents=True, exist_ok=True)
for name in ['Globals.java','InterpolationFunction.java','LinearInterpolationFunction.java']:
    shutil.copyfile(SOURCE/name, OUT/name)
source = (SOURCE/'GCSimulatorApp.java').read_text(encoding='cp1252')
start = source.index('\tdouble calcGasViscosity ')
end = source.index('\tpublic void performCalculations()')
math = source[start:end]
(OUT/'LegacyMath.java').write_text('package org.gcsimulator; class LegacyMath { double m_dColumnLength=30;\n'+math+'}',encoding='utf-8')
(OUT/'Export.java').write_text('''package org.gcsimulator;
public class Export {
 public static void main(String[] args) {
  System.out.print("{\\"compounds\\":[");
  for(int i=0;i<Globals.CompoundNameArray[0].length;i++){
   if(i>0)System.out.print(",");
   InterpolationFunction f=new InterpolationFunction(Globals.CompoundIsothermalDataArray[0][i]);
   System.out.print("{\\"id\\":"+i+",\\"name\\":\\""+Globals.CompoundNameArray[0][i]+"\\",\\"data\\":");
   System.out.print(java.util.Arrays.deepToString(Globals.CompoundIsothermalDataArray[0][i]));
   System.out.print(",\\"coefficients\\":"+java.util.Arrays.deepToString(f.dInterpolationParameters));
   System.out.print(",\\"ranges\\":"+java.util.Arrays.toString(f.dRanges));
   System.out.print(",\\"reference\\":[");
   for(int t=60;t<=320;t+=20){if(t>60)System.out.print(",");System.out.print(f.getAt(t));}
   System.out.print("]}");
  }
  System.out.print("],\\"physics\\":[");
  LegacyMath m=new LegacyMath(); boolean first=true;
  for(int gas=0;gas<4;gas++)for(double temp:new double[]{333.15,473.15,593.15})for(double outlet:new double[]{0.00001,101325}){
   if(!first)System.out.print(","); first=false;
   double p=m.calcInletPressureAtConstFlowRate(1,temp,gas,outlet,0.0002495,30);
   System.out.print(java.util.Arrays.toString(new double[]{gas,temp,outlet,p,m.calcGasViscosity(temp,gas),m.calcHoldUpTime(temp,gas,p,outlet,30,0.0002495),m.calcFlowVelocity(.5,temp,gas,p,outlet,.0002495,30),m.calcSoluteDiffusivityInGas(temp,p,gas),m.calcSoluteDiffusivityInStationaryPhase(temp),m.calcPlateHeight(.00001,.00000001,.5,.0002495,5,.00000025)}));
  }System.out.print("]}");
 }
}''',encoding='utf-8')
javac = shutil.which('javac')
java = str(Path(javac).with_name('java.exe'))
subprocess.run([javac,'-encoding','UTF-8',*[str(p) for p in OUT.glob('*.java')]],check=True)
result=json.loads(subprocess.check_output([java,'-cp',str(ROOT/'tests/java'),'org.gcsimulator.Export'],text=True))
(ROOT/'tests/legacy-reference.json').write_text(json.dumps(result),encoding='utf-8')
assets=ROOT/'public/assets/simulator'; assets.mkdir(parents=True,exist_ok=True)
compounds=[{k:v for k,v in c.items() if k!='reference'} for c in result['compounds']]
(assets/'compounds.js').write_text('// Extracted from the original GC Simulator Globals.java and Java spline.\nexport const compounds = '+json.dumps(compounds,ensure_ascii=False)+';\n',encoding='utf-8')
print(f'Extracted {len(compounds)} compounds and {len(result["physics"])} Java reference cases.')
