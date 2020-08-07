package edu.ruhhosp.hl7server;

import ca.uhn.hl7v2.app.Connection;
import ca.uhn.hl7v2.app.ConnectionListener;
import edu.ruhhosp.logging.Log;

public class LoggingConnectionListener implements ConnectionListener
{
	@Override
	public void connectionDiscarded(Connection connection)
	{
		Log.out.info("Lost connection from: " + connection.getRemoteAddress() + ":" + connection.getRemotePort());
	}

	@Override
	public void connectionReceived(Connection connection)
	{
		Log.out.info("Received connection from: " + connection.getRemoteAddress() + ":" + connection.getRemotePort());
	}
}
