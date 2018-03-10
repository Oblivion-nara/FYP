package mokapot;

import java.io.IOException;
import java.net.InetAddress;

import xyz.acygn.mokapot.CommunicationAddress;
import xyz.acygn.mokapot.DistributedCommunicator;
import xyz.acygn.mokapot.TCPCommunicationAddress;

public class SetupServer {

	public CommunicationAddress setup() throws IllegalStateException, IOException {
		// Start a communicator on this JVM listening on port 15239.
		DistributedCommunicator communicator = new DistributedCommunicator(
				TCPCommunicationAddress.fromInetAddress(InetAddress.getLoopbackAddress(), 15239));
		communicator.startCommunication();
		// Configure the address of the remote communicator.
		return TCPCommunicationAddress.fromInetAddress(InetAddress.getLoopbackAddress(),
				15238);

	}

}
