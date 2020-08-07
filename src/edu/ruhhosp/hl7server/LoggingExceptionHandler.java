package edu.ruhhosp.hl7server;

import java.util.Map;

import ca.uhn.hl7v2.HL7Exception;
import ca.uhn.hl7v2.protocol.ReceivingApplicationExceptionHandler;
import edu.ruhhosp.logging.Log;

public class LoggingExceptionHandler implements ReceivingApplicationExceptionHandler
{

	@Override
	public String processException(String incomingMessage, Map<String, Object> incomingMetadata, 
		String outgoingMessage, Exception e) throws HL7Exception
	{
		Log.out.info("Logging outgoing exception message: \n\n" + outgoingMessage, e);

		return outgoingMessage;
	}
}
