package edu.ruhhosp.hl7server;

import java.io.IOException;

import ca.uhn.hl7v2.DefaultHapiContext;
import ca.uhn.hl7v2.HL7Exception;
import ca.uhn.hl7v2.HapiContext;
import ca.uhn.hl7v2.app.Connection;
import ca.uhn.hl7v2.app.HL7Service;
import ca.uhn.hl7v2.app.Initiator;
import ca.uhn.hl7v2.llp.LLPException;
import ca.uhn.hl7v2.model.Message;
import ca.uhn.hl7v2.parser.Parser;
import edu.ruhhosp.logging.Log;

public class HL7Server
{
	private static HL7Server singleton;

	public static HL7Server getInstance()
	{
		if (singleton == null)
		{
			singleton = new HL7Server(); 
		}
		return singleton;
	}

	protected HL7Server() {}

	private HapiContext context;
	private HL7Service server;
	private int sendingPort = 15101, receivingPort = 15102;
	private boolean sendingUseTls = false, receivingUseTls = false;
	private boolean isStarted = false;
	
	public boolean initialize(int sendingPort, boolean sendingUseTls, int receivingPort, boolean receivingUseTls)
	{
		try
		{
			context = new DefaultHapiContext();
	
			this.sendingPort = sendingPort;
			this.sendingUseTls = sendingUseTls;
			server = context.newServer(this.receivingPort, this.receivingUseTls);
	
			server.registerConnectionListener(new LoggingConnectionListener());
			
			server.setExceptionHandler(new LoggingExceptionHandler());
			
			server.registerApplication("SIU", "S12", new Receiver());
			
			return true;
		}
		catch (Exception e)
		{
			Log.out.error("Unable to initialize server.", e);
		}
		return false;
	}
	
	public void start()
	{
		try
		{
			server.startAndWait();
			isStarted = true;
		}
		catch (InterruptedException e)
		{
			Log.out.error("Unable to start server", e);
		}
	}
	
	public void stop()
	{
		if (isStarted)
			server.stopAndWait();
	}
	
	public Message toMessage(String stringMessage)
	{
		if (stringMessage == null)
			return null;
		
		try
		{
			Parser parser = context.getPipeParser();

			return parser.parse(stringMessage);
		}
		catch (HL7Exception e)
		{
			Log.out.error("Unable to convert string [" + stringMessage + "] to message.", e);
		}
		return null;
	}
	
	public String toString(Message message)
	{
		if (message == null)
			return null;
		
		try
		{
			Parser parser = context.getPipeParser();

			return parser.encode(message);
		}
		catch (HL7Exception e)
		{
			Log.out.error("Unable to convert message [" + message + "] to string.", e);
		}
		return null;
	}

	public Message sendMessage(Message request, String destinationHost, String destinationPort)
	{
		try
		{
			if (isStarted)
			{
				Connection connection  = context.newClient(destinationHost, Integer.parseInt(destinationPort), sendingUseTls);
				
				Initiator initiator = connection.getInitiator();
				Message response = initiator.sendAndReceive(request);
				
				Parser parser = context.getPipeParser();
				String encodedResponse = parser.encode(response);
				Log.out.info("Received response:\n\n" + encodedResponse + "\n\n");
				return response;
			}
			else
				throw new IllegalStateException("HL7 server not started.");
		}
		catch (HL7Exception | LLPException | IOException e)
		{
			Log.out.error("Unable to send message.", e);
		}
		return null;
	}
}
