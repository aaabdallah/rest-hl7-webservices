package edu.ruhhosp.view.beans;

import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.model.DataModel;
import javax.faces.model.ListDataModel;

import edu.ruhhosp.logging.Log;
import edu.ruhhosp.persistence.entities.Country;
import edu.ruhhosp.persistence.entities.MockData;
import edu.ruhhosp.view.utils.Paginator;

@ManagedBean(name="countriesPaginatorBean")
@RequestScoped
public class CountriesPaginatorBean extends Paginator
{
	private List<Country> data = null;
	private int accessCounter = 0; // for debugging/learning purposes. See below.

	/**
	 * To set up pagination, you should:
	 * - FIRST set the 'totalSize' with the length of the afore-mentioned list.
	 * - SECOND set the 'data' with a list of the data you want to display
	 * Do this in the constructor typically.
	 * 
	 * NOTE THAT YOU SHOULD SET THE TOTALSIZE FIRST BEFORE THE PAGESIZE.
	 */
	public CountriesPaginatorBean()
	{
		data = MockData.getCountries();
		setTotalSize(data.size()); // MAKE SURE TO SET TOTALSIZE FIRST...
		setPageSize(4); // ...AND THEN THE PAGE SIZE.
	}

	/**
	 * To display a page, assume the 'data' and 'totalSize' members have
	 * been set. The 'pageStart' and 'pageSize' members should also be
	 * set appropriately:
	 * - 'pageStart' is passed in from the xhtml page typically
	 * - 'pageSize' is typically fixed and so hardcoded (as in constructor above)
	 *
	 * @return the sublist of data
	 */
	public DataModel<Country> getCountries()
	{
		Log.out.trace("Retrieving countries from: " + getPageStart() 
			+ " to " + (getPageStart()+getSafePageSize()) + ". Access number: " + accessCounter++);
		return new ListDataModel<Country>(data.subList(getPageStart(), getPageStart()+getSafePageSize()));
	}
}
