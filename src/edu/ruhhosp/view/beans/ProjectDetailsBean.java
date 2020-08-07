package edu.ruhhosp.view.beans;

import java.util.ArrayList;
import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

@ManagedBean
@ViewScoped
public class ProjectDetailsBean
{
	private List<String> projectStrategiesProgramsCodes;

	public ProjectDetailsBean()
	{
	    projectStrategiesProgramsCodes=new ArrayList<String>();
	    projectStrategiesProgramsCodes.add("ItemOne");
	    projectStrategiesProgramsCodes.add("ItemTwo");
	    projectStrategiesProgramsCodes.add("ItemThree");
	}

	public List<String> getProjectStrategiesProgramsCodes() 
	{
		return projectStrategiesProgramsCodes;
	}

	public void setProjectStrategiesProgramsCodes(List<String> projectStrategiesProgramsCodes) 
	{
		this.projectStrategiesProgramsCodes = projectStrategiesProgramsCodes;
	} 
}
