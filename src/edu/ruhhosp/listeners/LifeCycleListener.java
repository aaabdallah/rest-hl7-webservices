package edu.ruhhosp.listeners;

import java.util.Enumeration;

import javax.faces.event.PhaseEvent;
import javax.faces.event.PhaseId;
import javax.faces.event.PhaseListener;
import javax.servlet.http.HttpServletRequest;

import edu.ruhhosp.logging.Log;


public class LifeCycleListener implements PhaseListener
{
	private static final long serialVersionUID = 1000L;
	private static long counter = 0;

	public void beforePhase(PhaseEvent event)
	{
		if (event.getPhaseId().equals(PhaseId.RESTORE_VIEW))
			Log.out.trace(
					prepareStartTracingMessage()
					.append(prepareStartPhaseMessage(event))
					.append(prepareRequestInformation(event)));
		else
			Log.out.trace(prepareStartPhaseMessage(event));
	}

	public void afterPhase(PhaseEvent event)
	{
	}
	
	public PhaseId getPhaseId()
	{
		return PhaseId.ANY_PHASE;
	}
	
	public void displayTrace()
	{
		try
		{
			throw new Exception();
		}
		catch (Exception e)
		{
			Log.out.trace("Display trace counter: " + (++counter), e);
		}
	}

	public StringBuffer prepareStartPhaseMessage(PhaseEvent event)
	{
		StringBuffer buff = new StringBuffer("\n\n++++++++++++++++ PHASE  :  ");
		buff.append(event.getPhaseId()).append("  ++++++++++++++++++++++++++++\n");
		
		return buff;
	}

	public StringBuffer prepareStartTracingMessage()
	{
		StringBuffer buff = 
			new StringBuffer("\n########################################################################");
		buff.append("\n|--------------- BEGIN TRACING A NEW JSF CYCLE'S PHASES ---------------|");
		buff.append("\n########################################################################");
		return buff;
	}

	public StringBuffer prepareRequestInformation(PhaseEvent event)
	{
		HttpServletRequest request = 
			(HttpServletRequest) event.getFacesContext().getExternalContext().getRequest();
		Enumeration<String> enumeration = request.getParameterNames();
		StringBuffer buff = new StringBuffer("\nREQUEST PARAMETERS FROM HTTPSERVLETREQUEST\n");
		while (enumeration.hasMoreElements())
		{
			String name = (String) enumeration.nextElement();
			buff.append("\tParameter[ ").append(name).append(" ] has ")
				.append(request.getParameterValues(name).length).append(" value(s): ");

			//for (int i=0; i<request.getParameterValues(name).length; i++)
			for (String value : request.getParameterValues(name))
			{
				buff.append('{').append(value).append('}').append(' ');
			}
			buff.append('\n');
		}
		buff.append('\n').append('\n');
		return buff;
	}
}
