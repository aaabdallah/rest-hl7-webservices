package edu.ruhhosp.view.beans;

import java.util.ArrayList;
import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;

import edu.ruhhosp.logging.Log;

@ManagedBean
@RequestScoped
public class AllItemsBean
{
	private List<ItemBean> allItems = new ArrayList<ItemBean>();
	
	public AllItemsBean()
	{
		Log.out.trace("Creating all items...");
		populateWithItems("ItemOne", "ItemTwo", "ItemThree", "ItemFour");
	}

	public List<ItemBean> getAllItems()
	{
		return allItems;
	}

	public void setAllItems(List<ItemBean> allItems)
	{
		this.allItems = allItems;
	}

	public void populateWithItems(String ... itemValues)
	{
		for (String itemValue : itemValues)
		{
			ItemBean item = new ItemBean();
			item.setItemLabel(itemValue);
			item.setItemValue(itemValue);
			
			allItems.add(item);
		}
	}
}
