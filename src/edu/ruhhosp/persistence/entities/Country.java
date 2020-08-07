package edu.ruhhosp.persistence.entities;

public class Country
{
	String iso2;
	String iso3;
	String englishName;

	public Country(String iso2, String iso3, String englishName)
	{
		setIso2(iso2);
		setIso3(iso3);
		setEnglishName(englishName);
	}
	public String getIso2()
	{
		return iso2;
	}
	public void setIso2(String iso2)
	{
		this.iso2 = iso2;
	}
	public String getIso3()
	{
		return iso3;
	}
	public void setIso3(String iso3)
	{
		this.iso3 = iso3;
	}
	public String getEnglishName()
	{
		return englishName;
	}
	public void setEnglishName(String englishName)
	{
		this.englishName = englishName;
	}
}
