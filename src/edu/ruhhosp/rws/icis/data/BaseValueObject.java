package edu.ruhhosp.rws.icis.data;

import java.io.Serializable;


public class BaseValueObject implements Serializable
{
	private static final long serialVersionUID = -2877459731349845580L;

	protected String version = "1.0.0";

	public String getVersion()
	{
		return version;
	}

	public void setVersion(String version)
	{
		this.version = version;
	}
}
