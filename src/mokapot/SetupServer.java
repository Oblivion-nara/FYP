package mokapot;

import java.io.IOException;
import java.net.InetAddress;

import xyz.acygn.mokapot.CommunicationAddress;
import xyz.acygn.mokapot.DistributedCommunicator;
import xyz.acygn.mokapot.TCPCommunicationAddress;

public class SetupServer {

	public Game setup() throws IllegalStateException, IOException {
		// Start a communicator on this JVM listening on port 15239.
		DistributedCommunicator communicator = new DistributedCommunicator(
				TCPCommunicationAddress.fromInetAddress(InetAddress.getLoopbackAddress(), 15239));
		communicator.startCommunication();
		// Configure the address of the remote communicator.
		CommunicationAddress remoteAddress = TCPCommunicationAddress.fromInetAddress(InetAddress.getLoopbackAddress(),
				15238);

		int players = 0, ais = 1, trackWidth = 40, trackLength = 40, trackSegLength = 80, aiDifficulty = 3;
		return DistributedCommunicator.getCommunicator().runRemotely(
				() -> new Game(players, ais, trackWidth, trackLength, trackSegLength, aiDifficulty), remoteAddress);
	}

}
