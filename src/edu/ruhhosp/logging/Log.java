package edu.ruhhosp.logging;

import java.net.URL;
import java.util.Enumeration;

import org.apache.log4j.Appender;
import org.apache.log4j.Logger;
import org.apache.log4j.helpers.Loader;
import org.apache.log4j.xml.DOMConfigurator;

import edu.ruhhosp.view.beans.HL7DemoBean;

public class Log
{
	public static Logger out;
	
	static
	{
		// Force loading of the configuration file searching via classloader
		// starting at the root of this application.
		URL url = null;

//		System.out.println(" ############################################### ");
//		url = HL7DemoBean.class.getResource("/log4j.xml");
//		System.out.println(url != null ? url.toString() : "null");
//		url = HL7DemoBean.class.getResource("log4j.xml");
//		System.out.println(url != null ? url.toString() : "null");
//		url = HL7DemoBean.class.getResource("../../../log4j.xml");
//		System.out.println(url != null ? url.toString() : "null");
//		System.out.println(" ############################################### ");
		url = Loader.getResource("/log4j.xml");
//		System.out.println(url != null ? url.toString() : "null");
//		url = Loader.getResource("log4j.xml");
//		System.out.println(url != null ? url.toString() : "null");
//		url = Loader.getResource("../../../log4j.xml");
//		System.out.println(url != null ? url.toString() : "null");
//		System.out.println(" ############################################### ");
		
		DOMConfigurator.configure(url);

		out = Logger.getLogger("iciswebservices");
	}

	public static void main(String args[])
	{
		Log.out.info("Test");
	}
}
