package edu.ruhhosp.hl7server;

import java.io.IOException;
import java.util.Map;

import ca.uhn.hl7v2.DefaultHapiContext;
import ca.uhn.hl7v2.HL7Exception;
import ca.uhn.hl7v2.model.Message;
import ca.uhn.hl7v2.protocol.ReceivingApplication;
import ca.uhn.hl7v2.protocol.ReceivingApplicationException;
import edu.ruhhosp.logging.Log;

public class Receiver implements ReceivingApplication
{

	@Override
	public boolean canProcess(Message input)
	{
		return true;
	}

	@Override
	public Message processMessage(Message message, Map<String, Object> metadata) throws ReceivingApplicationException, HL7Exception
	{
		String encodedMessage = new DefaultHapiContext().getPipeParser().encode(message);
		Log.out.info("Received message:\n" + encodedMessage + "\n\n");
		
		try
		{
			return message.generateACK();
		}
		catch (IOException e)
		{
			Log.out.error("Exception processing message", e);
			throw new HL7Exception(e);
		}
	}
}
