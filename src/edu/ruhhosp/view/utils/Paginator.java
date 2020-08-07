package edu.ruhhosp.view.utils;

import javax.faces.context.FacesContext;


/**
 * A simple paginator for a list of data displayed in an h:dataTable.
 * 
 * @author ahmed
 */
public class Paginator
{
	private int pageStart = 0; // THE ZERO-BASED ITEM INDEX WHERE THE PAGE STARTS
	private int pageSize = 1; // default minimum
	private int totalSize = 1; // default minimum
	
	/**
	 * @return ZERO-based index of the item in the total list where the page is starting
	 */
	public int getPageStart()
	{
		return pageStart;
	}

	public void setPageStart(int start)
	{
		pageStart = start;
		if (pageStart + pageSize >= totalSize)
			pageStart = totalSize - (totalSize % pageSize);
		if (pageStart < 0) // final sanity check.
			pageStart = 0;
	}
	
	public int getPageSize()
	{
		return pageSize;
	}
	
	/**
	 * This is useful for the case of the last page: sometimes it will not have a total of 'pageSize'
	 * items. This method is guaranteed to give the caller a "safe page size", so it will truncate
	 * as necessary for the last page.
	 * 
	 * @return a safe page size (even for the last page)
	 */
	public int getSafePageSize()
	{
		if (pageStart + pageSize >= totalSize)
			return totalSize % pageSize;
		return pageSize;
	}

	/**
	 * Will leave pageSize unchanged if the passed in parameter is less than or
	 * equal to zero OR if it is greater than totalSize. 
	 * 
	 * KEY WARNINGS:
	 * 1. MAKE SURE TO SET THE TOTAL SIZE OF THE LIST BEFORE SETTING THE PAGE
	 * SIZE BECAUSE OF THE INTERNAL CHECK THAT OCCURS (previous sentence!).
	 * 
	 * 2. IT IS THE CALLER'S RESPONSIBILITY TO RESET pageStart TO A PROPER VALUE 
	 * (e.g. to zero to move all the way back to the top of the list of items) 
	 * IF THE PAGE SIZE IS CHANGED.
	 * 
	 * @param size new page size (must be greater than zero and less than or
	 * equal to totalSize - for this reason set the totalSize FIRST)
	 */
	public void setPageSize(int size)
	{
		if (size > 0 && size <= totalSize)
			pageSize = size;
	}

	public int getTotalSize()
	{
		return totalSize;
	}

	/**
	 * Will leave total size unchanged if the passed in parameter is less
	 * than or equal to zero
	 * 
	 * @param totalSize MUST be greater than zero
	 */
	public void setTotalSize(int totalSize)
	{
		if (totalSize > 0)
			this.totalSize = totalSize;
	}
	
	/**
	 * @return ZERO-based index of current page being displayed based on current
	 * values of pageStart and pageSize. Recall that pageStart indicates the item
	 * index which is at the top/start of the page.
	 */
	public int getPageNumber()
	{
		if (pageStart <= 0)
			return 0;
		return pageStart / pageSize;
	}

	public void goToFirstPage()
	{
		pageStart = 0;
	}
	
	public void goToLastPage()
	{
		setPageStart(totalSize - pageSize);
	}

	public void goToNextPage()
	{
		setPageStart(pageStart + pageSize);
	}

	public void goToPrevPage()
	{
		setPageStart(pageStart - pageSize);
	}
	
	public boolean isHasNextPage()
	{
		// The first condition in the following is necessary to use this method effectively with
		// 'disabled' attributes in JSF. Otherwise, the method evaluates to true in the 'Apply Request
		// Values' phase (number 2), and then any associated action is NOT called in the 'Invoke Action'
		// phase (number 5). Most of the time, we want this method to evaluate false only in the last
		// phase 'Render Response' (number 6).
		if (FacesContext.getCurrentInstance().getCurrentPhaseId().getOrdinal() == 6 && pageStart >= totalSize - pageSize)
			return false;
		return true;
	}

	public boolean isHasPrevPage()
	{
		// The first condition in the following is necessary to use this method effectively with
		// 'disabled' attributes in JSF. Otherwise, the method evaluates to true in the 'Apply Request
		// Values' phase (number 2), and then any associated action is NOT called in the 'Invoke Action'
		// phase (number 5). Most of the time, we want this method to evaluate false only in the last
		// phase 'Render Response' (number 6).
		if (FacesContext.getCurrentInstance().getCurrentPhaseId().getOrdinal() == 6 && pageStart <= 0)
			return false;
		return true;
	}
}
