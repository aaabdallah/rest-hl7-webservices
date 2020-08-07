package edu.ruhhosp.hl7server;

import javax.servlet.Servlet;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;

public class HL7ServerServlet extends HttpServlet 
{
	private static final long serialVersionUID = 1L;
       
	/**
	 * @see Servlet#init(ServletConfig)
	 */
	public void init(ServletConfig config) throws ServletException 
	{
		super.init(config);
		if (HL7Server.getInstance().initialize(
			Integer.parseInt(getInitParameter("sendingPort")), 
			Boolean.parseBoolean(getInitParameter("sendingUseTls")),
			Integer.parseInt(getInitParameter("receivingPort")), 
			Boolean.parseBoolean(getInitParameter("receivingUseTls"))))
		{
			HL7Server.getInstance().start();
		}
	}
	
	@Override public void destroy()
	{
		super.destroy();
		HL7Server.getInstance().stop();
	}
}
