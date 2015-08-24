package com.washhous.comman;

import java.util.List;

import com.parse.ParseObject;

public class CalculationRate {

	/*< Wash & Iron >
	1.Basic Iron items :  Selected Q'ty x $2.99/each
	2.Additional option charge :  Selected Q'ty x $0.05/each for Eco Detergent, Selected Q'ty x $0.05/each for Scent Free Softener*/
	public static double washAndIron_basicIronRatePerItom=2.99;
	public static double washAndIron_EcoDetegentRatePerItem=0.05;
	public static double washAndIron_ScentFreeRatePerItem=0.05;
	
	/*< Dry Cleaning >
	1.Basic Dry Cleaning :  Selected Q'ty x Unit price/each;*/
	public static double dryCleenUnitPrice=2.99;

	public float getBasicvalue(int weightTotal) {
		// TODO Auto-generated method stub
		float total=0;
		
		try {
			total=(float) (weightTotal*1.25);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		return total;
	}

	public float getaddtionalCharger(String iseco_dtergent, String scent_free,
			int weightTotal) {
		float total = 0;
		try {
			if(iseco_dtergent.equals("true"))
			{
				total=(float) (weightTotal*0.05);
			}
			else if(scent_free.equals("true"))
			{
				total=(float) (weightTotal*0.05);
			}
			if(iseco_dtergent.equals("true") && scent_free.equals("true"))
			{
				total=0;
				total=(float) ((float) (weightTotal*0.05)+ weightTotal*0.05);
				
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		// TODO Auto-generated method stub
		return total;
	}
	
	// calculation for dry Cleaning
	
	
}
