package edu.ruhhosp.view.utils;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.context.FacesContext;

import edu.ruhhosp.logging.Log;

@ManagedBean(name="redirectorBean")
@RequestScoped
public class RedirectorBean 
{
	/**
	 * @param pathFragment just the root-based path fragment without the context 
	 * (e.g. "/index.jsf" NOT "/MyWebApp/index.jsf")
	 */
	public void doRedirect(String pathFragment)
	{
		try
		{
			FacesContext.getCurrentInstance().getExternalContext().redirect(
				"/" + FacesContext.getCurrentInstance().getExternalContext().getContextName() + pathFragment);
		}
		catch (Exception e)
		{
			Log.out.error("Error while redirecting!", e);
		}
	}
}
