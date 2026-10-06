package org.gcsimulator; class LegacyMath { double m_dColumnLength=30;
	double calcGasViscosity (double dTempK, double dGasType)
	{
		// Determine gas viscosity parameters (pg. 72 Blumberg)
		double dEtast = 18.63; // (in uPa s)
		double dE0 = 0.6958; // (dimensionless)
		double dE1 = -0.0071; // (dimensionless)
		double dRefTempK = 273.15;
		
		if (dGasType == 0)
		{
			// Hydrogen
			dEtast = 8.382;
			dE0 = 0.6892;
			dE1 = 0.005;
		}
		else if (dGasType == 1)
		{
			// Helium
			dEtast = 18.63;
			dE0 = 0.6958;
			dE1 = -0.0071;
		}
		else if (dGasType == 2)
		{
			// Nitrogen
			dEtast = 16.62;
			dE0 = 0.7665;
			dE1 = -0.0378;
		}
		else if (dGasType == 3)
		{
			// Argon
			dEtast = 21.04;
			dE0 = 0.8131;
			dE1 = -0.0426;
		}
		
		double dEta = (dEtast * 0.000001) * Math.pow(dTempK / dRefTempK, dE0 + dE1 * ((dTempK - dRefTempK) / dRefTempK));
		return dEta;
	}
	
	/**
	 * Returns the gas self diffusion coefficient of a gas for a
	 * specified temperature and pressure. (Blumberg pg. 74) 
	 *
	 * @param  dTempK the temperature of the gas (in Kelvin)
	 * @param  dPressure the pressure of the gas (in Pa)
	 * @param  dGasType the type of gas (0 = H2, 1 = He, 2 = N2, 3 = Ar)
	 * @return      the gas diffusion coefficient in m^2 s^-1
	 */
	double calcGasSelfDiffusionCoefficient (double dTempK, double dPressure, double dGasType)
	{
		double dR = 8.3144621; // (in m^3?Pa?K^-1?mol^-1)
		double dM = 4.003;
		double dE = 0.685;
		
		if (dGasType == 0)
		{
			// Hydrogen
			dM = 2.016;
			dE = 0.698;
		}
		else if (dGasType == 1)
		{
			// Helium
			dM = 4.003;
			dE = 0.685;
		}
		else if (dGasType == 2)
		{
			// Nitrogen
			dM = 28.01;
			dE = 0.710;
		}
		else if (dGasType == 3)
		{
			// Argon
			dM = 39.95;
			dE = 0.750;
		}
		
		double dDgst = (6 * dR * 273.15 * this.calcGasViscosity(273.15, dGasType)) / (5 * (dM / 1000) * 101325);
		double dDg = dDgst * (101325 / dPressure) * Math.pow(dTempK / 273.15, 1 + dE);
		return dDg;
	}
	
	/**
	 * Returns the diffusion coefficient for a solute in a given gas at a
	 * specified temperature and pressure. Uses the Fuller-Giddings empirical formula. (Blumberg pg. 75) 
	 *
	 * @param  dTempK the temperature of the gas (in Kelvin)
	 * @param  dPressure the pressure of the gas (in Pa)
	 * @param  dGasType the type of gas (0 = H2, 1 = He, 2 = N2, 3 = Ar)
	 * @return      the gas diffusion coefficient in m^2 s^-1
	 */
	double calcSoluteDiffusivityInGas (double dTempK, double dPressure, double dGasType)
	{
		double dR = 8.3144621; // (in m^3?Pa?K^-1?mol^-1)
		double dMg = 4.003;
		double dVg = 2.67;
		
		if (dGasType == 0)
		{
			// Hydrogen
			dMg = 2.016;
			dVg = 6.12;
		}
		else if (dGasType == 1)
		{
			// Helium
			dMg = 4.003;
			dVg = 2.67;
		}
		else if (dGasType == 2)
		{
			// Nitrogen
			dMg = 28.01;
			dVg = 18.5;
		}
		else if (dGasType == 3)
		{
			// Argon
			dMg = 39.95;
			dVg = 16.2;
		}
		
		// TODO: Change these to actual values for the solutes
		double dMsol = 100;
		double dVsol = 200;
		// in cm^2/s
		double dDsol = ((100 * Math.sqrt((1 / dMg) + (1 / dMsol))) / Math.pow(Math.pow(dVg, 1.0/3.0) + Math.pow(dVsol, 1.0/3.0), 2)) * (1 / dPressure) * Math.pow(dTempK, 1.75);
		
		return dDsol * 0.0001; // in m^2/s
	}
	
	/**
	 * Returns the gas pressure at a given point along the column,
	 * with a given inlet and outlet pressure (Blumberg pg. 103) 
	 *
	 * @param  dZPos the z position along the column, 0 (inlet) to 1 (outlet)
	 * @param  dInletPressure the pressure at the column inlet (in Pa)
	 * @param  dOutletPressure the pressure at the column outlet (in Pa)
	 * @return      the gas pressure (in Pa)
	 */
	double calcGasPressure (double dZPos, double dInletPressure, double dOutletPressure)
	{
		double dLext = Math.pow(dInletPressure, 2) / (Math.pow(dInletPressure, 2) - Math.pow(dOutletPressure, 2));
		double dPressure = dInletPressure * Math.sqrt(1 - (dZPos / dLext));
		return dPressure;
	}
	
	/**
	 * Returns the hold-up time for a given temperature, inlet pressure,
	 * and outlet pressure.
	 *
	 * @param  dTempK the temperature of the gas (in Kelvin)
	 * @param  dGasType the type of gas (0 = H2, 1 = He, 2 = N2, 3 = Ar)
	 * @param  dInletPressure the pressure at the column inlet (in Pa)
	 * @param  dOutletPressure the pressure at the column outlet (in Pa)
	 * @param  dColumnLength the column length (in m)
	 * @param  dInnerDiameter the column inner diameter (in m)
	 * @return      the hold-up time (in s)
	 */
	double calcHoldUpTime (double dTempK, double dGasType, double dInletPressure, double dOutletPressure, double dColumnLength, double dInnerDiameter)
	{
		double dOmega = dColumnLength * calcGasViscosity(dTempK, dGasType) * (32.0 / Math.pow(dInnerDiameter, 2));
		double dDeadTime = (4 * dOmega * this.m_dColumnLength * (Math.pow(dInletPressure, 3) - Math.pow(dOutletPressure, 3))) / (3 * Math.pow(Math.pow(dInletPressure, 2) - Math.pow(dOutletPressure, 2), 2));				
		return dDeadTime;
	}

	/**
	 * Returns the velocity of a gas at a specific point along the column.
	 * (Blumberg pg. 103, 7.67, 7.50, 7.38) 
	 *
	 * @param  dZPos the z position along the column, 0 (inlet) to 1 (outlet)
	 * @param  dTempK the temperature of the gas (in Kelvin)
	 * @param  dGasType the type of gas (0 = H2, 1 = He, 2 = N2, 3 = Ar)
	 * @param  dInletPressure the pressure at the column inlet (in Pa)
	 * @param  dOutletPressure the pressure at the column outlet (in Pa)
	 * @param  dInnerDiameter the column inner diameter (in m)
	 * @param  dColumnLength the column length (in m)
	 * @return      the gas velocity in m/s
	 */
	double calcFlowVelocity (double dZPos, double dTempK, double dGasType, double dInletPressure, double dOutletPressure, double dInnerDiameter, double dColumnLength)
	{
		//if (dZPos > 0.99)
		//	dZPos = dZPos;
		double dOmega = dColumnLength * calcGasViscosity(dTempK, dGasType) * (32.0 / Math.pow(dInnerDiameter, 2));
		double dInletVelocity = (Math.pow(dInletPressure, 2) - Math.pow(dOutletPressure, 2)) / (2 * dOmega * dInletPressure);
		//BigDecimal bdInletPressure = BigDecimal.valueOf(dInletPressure).pow(2);
		//BigDecimal bdOutletPressure = BigDecimal.valueOf(dOutletPressure).pow(2);
		// Difference between inlet pressure and the difference between the inlet and outlet pressure is too small
		// It's ok if dLext rounds to 1. It doesn't cause significant error in dZPos/dLext.
		double dLext = (double)(Math.pow(dInletPressure, 2) / (Math.pow(dInletPressure, 2) - Math.pow(dOutletPressure, 2)));
		//BigDecimal bdLext = (bdInletPressure.divide(bdInletPressure.subtract(bdOutletPressure), BigDecimal.ROUND_HALF_UP));
		//double dShort = BigDecimal.valueOf(1).subtract(BigDecimal.valueOf(dZPos).divide(bdLext, BigDecimal.ROUND_HALF_UP)).doubleValue();
		double dVelocityAtZ = dInletVelocity / Math.sqrt(1 - (dZPos / dLext));
		//double dVelocityAtZ = dInletVelocity / Math.sqrt(dShort);
		return dVelocityAtZ;
	}
	
	/**
	 * Returns the inlet pressure for a specified flow rate.
	 * (Blumberg pg. 101-102, 7.60 and 7.53) 
	 *
	 * @param  dFlowRate the normalized volumetric flow rate (in mL/min)
	 * @param  dTempK the temperature of the gas (in Kelvin)
	 * @param  dGasType the type of gas (0 = H2, 1 = He, 2 = N2, 3 = Ar)
	 * @param  dOutletPressure the pressure at the column outlet (in Pa)
	 * @param  dInnerDiameter the column inner diameter (in m)
	 * @param  dColumnLength the column length (in m)
	 * @return      the inlet pressure in Pa
	 */
	double calcInletPressureAtConstFlowRate (double dFlowRate, double dTempK, double dGasType, double dOutletPressure, double dInnerDiameter, double dColumnLength)
	{
		double dRefTempK = 298.15;
		double dPst = 101325; // (in Pa)
		double dOmega = dColumnLength * calcGasViscosity(dTempK, dGasType) * (32.0 / Math.pow(dInnerDiameter, 2));
		double dSpecificFlowRate = (dTempK / dRefTempK) * ((dFlowRate / (1000000 * 60)) / dInnerDiameter); // in m^2 / min
		double dInletPressure = Math.sqrt(((dSpecificFlowRate * 8 * dPst * dOmega) / (Math.PI * dInnerDiameter)) + Math.pow(dOutletPressure, 2));
		return dInletPressure;
	}

	/**
	 * Returns the diffusion coefficient for a solute in the stationary
	 * phase at a given temperature. This is only a rough approximation.
	 * It is calculated by a fit to data from C.A. Cramers, C.E. Van Tilburg, C.P.M. Schutjes, J.A. Rijks, G.A. Rutten, R. De Nijs, Journal of Chromatography A 279 (1983) 83–89.
	 * It does not account for differences between solutes nor between stationary phases.
	 *
	 * @param  dTempK the temperature of the phase (in Kelvin)
	 * @return      the solute diffusion coefficient in m^2 s^-1
	 */
	double calcSoluteDiffusivityInStationaryPhase (double dTempK)
	{
		double dR = 8.3144621; // (in m^3?Pa?K^-1?mol^-1)
		// dK and dE were fit parameters from data in C.A. Cramers, C.E. Van Tilburg, C.P.M. Schutjes, J.A. Rijks, G.A. Rutten, R. De Nijs, Journal of Chromatography A 279 (1983) 83–89.

		double dK = -14.36381977;
		double dE = 22101.91968;
		double dDs = Math.exp(dK - (dE/(dR * dTempK)));
		
		return dDs; // in m^2/s
	}

	/**
	 * Returns the plate height using the Golay equation. (Blumberg pg. 225)
	 *
	 * @param  dSoluteDiffusionCoefficientInGas the solute diffusion coefficient in the mobile phase (in m^2/s)
	 * @param  dSoluteDiffusionCoefficientInStationaryPhase the solute diffusion coefficient in the stationary phase (in m^2/s)
	 * @param  dFlowVelocity the flow velocity at the position of the solute (in m/s)
	 * @param  dInnerDiameter the inner diameter of the column (in m)
	 * @param  dRetentionFactor the instantaneous retention factor of the solute
	 * @param  dFilmThickness the thickness of the stationary phase film (in m)
	 *
	 * @return      the plate height in m^2/m
	 */
	double calcPlateHeight (double dSoluteDiffusionCoefficientInGas, double dSoluteDiffusionCoefficientInStationaryPhase, double dFlowVelocity, double dInnerDiameter, double dRetentionFactor, double dFilmThickness)
	{
		double dTerm1 = (2 * dSoluteDiffusionCoefficientInGas) / dFlowVelocity;
		double dTerm2 = ((1 + 6 * dRetentionFactor + 11 * Math.pow(dRetentionFactor,2)) * Math.pow(dInnerDiameter, 2) * dFlowVelocity) / (96 * Math.pow(1 + dRetentionFactor, 2) * dSoluteDiffusionCoefficientInGas);
		double dTerm3 = (2 * Math.pow(dFilmThickness, 2) * dRetentionFactor * dFlowVelocity) / (3 * Math.pow(1 + dRetentionFactor, 2) * dSoluteDiffusionCoefficientInStationaryPhase);

		return dTerm1 + dTerm2 + dTerm3; // in m^2/m
	}
	
}