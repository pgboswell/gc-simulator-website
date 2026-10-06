# GC Simulator model notes

## Compound data and retention

The simulator contains temperature-dependent retention data for 102 compounds on Agilent DB-5MS UI. The default sample contains 15 compounds. Retention factors are calculated by interpolating log10(k) against temperature with cubic splines; datasets containing only two measurements use linear interpolation. Retention factors are adjusted for the column's phase ratio, which depends on inner diameter and stationary-phase film thickness.

At a constant oven temperature, retention time follows `tR = t0 × (1 + k)`, where `t0` is the hold-up time and `k` is the retention factor.

For temperature-programmed runs, the simulator follows each compound through 1,200 position slices. A numerical solve determines the transit time through each slice using its midpoint temperature and velocity. The oven remains at the final programmed temperature after the last hold. Compounds that do not elute within the 24-hour calculation limit are reported as not eluted.

Retention estimates outside a compound's measured temperature range are flagged. Log10(k) is bounded at ±12 to prevent numerical overflow. Extrapolated values are approximations, not experimentally validated predictions.

## Carrier gas and column flow

Carrier-gas viscosity, compressible pressure and velocity profiles, and hold-up time are calculated from the selected gas, oven temperature, column dimensions, and inlet/outlet conditions. The simulator supports hydrogen, helium, nitrogen, and argon.

Flow is normalized to 25 °C and one atmosphere. Constant-flow operation adjusts inlet pressure as temperature changes; constant-pressure operation allows flow to change. Inlet pressure is gauge pressure, while outlet pressure is absolute. Column diameter and film thickness are converted from millimeters and micrometers to meters for the calculations.

## Peak width

Peak width includes Golay column broadening, injection-liner broadening, gas decompression, and detector response. The calculation accumulates spatial variance along the column, scales it with local velocity changes, and converts it to a time variance at elution. Detector response adds the square of the detector time constant to that variance.

Gas-phase diffusion uses the Fuller–Giddings relationship with an approximate solute mass of 100 g/mol and diffusion volume of 200. Stationary-phase diffusion uses a common temperature-dependent approximation. These parameters are not compound-specific measured diffusivities.

Each compound produces a Gaussian peak with standard deviation `σ`. Its approximate baseline width is `4σ`. For adjacent peaks, resolution is calculated as:

`Rs = |tR,2 − tR,1| / [2 × (σ1 + σ2)]`

The results table reports the smaller resolution to the preceding or following eluting compound. The first and last peaks use their single neighbor.

## Injection and detector signal

Amount on column is sample concentration in µM multiplied by injection volume in µL, giving picomoles. Split injection divides this amount by `(split ratio + 1)`; splitless injection uses the full amount.

The Gaussian signal is expressed in µM using the normalized carrier flow at elution, including the calculated flow in constant-pressure mode. The detector assumes the same molar response for every compound. It does not represent a calibrated FID or MS response, which would depend on compound chemistry.

Gaussian noise is regenerated on each recalculation. Its standard deviation is the noise-amplitude setting divided by the square root of the detector time constant. The signal offset adds a constant baseline. Changing the view, selected compound, or overlay reuses the recorded signal; a saved reference retains its recorded noise.

## Time range and sampling

Detector samples are uniformly spaced at the requested samples-per-second rate. The default rate is 10 samples/s. The graph and CSV export use the same detector samples. Very narrow peaks may require a higher sampling rate.

In either oven mode, automatic duration is `1.05 × max(tR + 3σ)`, subject to the 24-hour calculation limit and rounded up to a sample interval. The final programmed hold does not force a longer recording. A manual duration can be set by turning off automatic duration.

Acquisition is capped at 100,000 points. If the requested run requires more samples, recording stops early and a warning appears. Reducing the sampling rate allows a longer recording.

## Assumptions and limitations

The simulator is intended for education and exploration. It does not model column overload, adsorption, peak tailing, oven thermal lag, or compound-specific detector chemistry. Diffusion and retention extrapolation introduce additional approximations. Controls and imported methods are checked for finite values and allowed physical ranges.

Methods can be saved as JSON files containing the settings and sample, then loaded to reproduce a simulation. Detector noise is regenerated when the method is recalculated.

## Verification

Automated checks cover retention interpolation, gas transport, analytical isothermal retention, temperature-program boundaries, and agreement between constant-temperature programs and isothermal runs. Refining the integration steps checks numerical convergence.

Additional checks cover injected amount and Gaussian peak area, non-elution limits, empty samples, uniform acquisition, noise generation, sample-count limits, adjacent-peak resolution, dilute concentrations, input validation, and saved-method round trips. Run these checks with `npm test` from the project directory.

Numerical consistency and convergence do not establish agreement with experimental measurements.

Model and data by Paul Boswell. CC BY-NC-SA 3.0 US.
