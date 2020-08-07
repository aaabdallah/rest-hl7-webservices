package edu.ruhhosp.persistence.entities;

import java.util.ArrayList;
import java.util.List;

public class MockData
{
	private static List<Country> countries;
	
	static
	{
		countries = new ArrayList<Country>();
		countries.add(new Country("DZ", "DZA", "Algeria"));
		countries.add(new Country("BH", "BHR", "Bahrain"));
		countries.add(new Country("EG", "EGY", "Egypt"));
		countries.add(new Country("JO", "JOR", "Jordan"));
		countries.add(new Country("KW", "KWT", "Kuwait"));
		countries.add(new Country("LB", "LBN", "Lebanon"));
		countries.add(new Country("LY", "LBY", "Libya"));
		countries.add(new Country("MA", "MAR", "Morocco"));
		countries.add(new Country("OM", "OMN", "Oman"));
		countries.add(new Country("QA", "QAT", "Qatar"));
		countries.add(new Country("SA", "SAU", "Saudi Arabia"));
		countries.add(new Country("SY", "SYR", "Syria"));
		countries.add(new Country("TN", "TUN", "Tunisia"));
		countries.add(new Country("AE", "ARE", "United Arab Emirates"));
		countries.add(new Country("YE", "YEM", "Yemen"));
	}

	public static List<Country> getCountries()
	{
		return countries;
	}
}
