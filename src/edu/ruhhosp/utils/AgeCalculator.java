package edu.ruhhosp.utils;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class AgeCalculator 
{
	public static boolean debug;

	/**
	 * @param args
	 */
	public static void main(String[] args) 
	{
		debug = true;
		System.out.println("Date: " + calculateApproximateAgeAsOfToday("04/12/2011"));
	}
	
	/**
	 * Return the APPROXIMATE AGE IN YEARS AND MONTHS of a person born on the passed in 
	 * birth date. This method assumes that months are always 30.5 days long, and that
	 * is why this method is APPROXIMATE!!!
	 * 
	 * @param birthDateAsString dd/MM/yyyy (must always send in this exact format)
	 * @return age as a string looking like "NY NM" where the 'N's are numbers. 
	 * The months are rounded DOWN. If there's an error, returns null.
	 * Note that "0Y 0M" means the person was just born in the last month, (awwww how cuuute!!!)
	 */
	public static String calculateApproximateAgeAsOfToday(String birthDateAsString)
	{
		long ONEYEARMILLIS = 1000L * 60 * 60 * 24 * 365;
		long ONEMONTHMILLIS = (long) (1000D * 60 * 60 * 24 * 30.5); // note: assume 30.5-day months.
		
		// Do not change the above to '30' instead of '30.5'. It would break the calculation
		// for people who have almost reached their birthday (within days), so that this
		// method would incorrectly say "12 months".

		try
		{
			// Assumes that birthDate is in dd/mm/yyyy format
			SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
			formatter.setLenient(false); // NO PLAYING, THE FORMAT IS STRICTLY ENFORCED

			Calendar birthday = Calendar.getInstance(Locale.US);
			birthday.setTime(formatter.parse(birthDateAsString));

			Calendar today = Calendar.getInstance(Locale.US);
			today.setTimeInMillis(System.currentTimeMillis()); // + (1000L * 3600 * 24 * 30)); // useful to add an offset
			
			if (today.compareTo(birthday) < 0)
				throw new IllegalArgumentException("Cannot compute age of a person born in the future.");
			
			long years = (today.getTimeInMillis() - birthday.getTimeInMillis()) / ONEYEARMILLIS;
			long months = ((today.getTimeInMillis() - birthday.getTimeInMillis()) - (years*ONEYEARMILLIS)) / ONEMONTHMILLIS;

			if (debug)
			{
				System.out.println(
					"Birthday: " + formatter.format(new Date(birthday.getTimeInMillis())) 
					+ "\n   Today: " + formatter.format(new Date(today.getTimeInMillis()))
					+ " --> " + years + "Y " + months + "M");
			}
			return years + "Y " + months + "M";
		}
		catch (Exception e)
		{
			System.out.println("Unable to process birth date: " + birthDateAsString);
			e.printStackTrace();
			return null;
		}
	}
}
