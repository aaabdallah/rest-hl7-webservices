package edu.ruhhosp.view.beans;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;

import edu.ruhhosp.logging.Log;

@ManagedBean
@RequestScoped
public class SelectedItemsBean
{
	private String selectedItems[] = {"ItemOne", "ItemThree"};
	
	public SelectedItemsBean()
	{
		Log.out.trace("Creating two selected items...");
	}

	public String[] getSelectedItems()
	{
		return selectedItems;
	}

	public void setSelectedItems(String[] selectedItems)
	{
		this.selectedItems = selectedItems;
	}
}
