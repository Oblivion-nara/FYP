package mokapot;

import java.io.IOException;
import java.net.InetAddress;

import xyz.acygn.mokapot.CommunicationAddress;
import xyz.acygn.mokapot.DistributedCommunicator;
import xyz.acygn.mokapot.TCPCommunicationAddress;

public class SetupServer {

	private CommunicationAddress remoteAddress;
	private DistributedCommunicator communicator;
	private Game game;

	public SetupServer() throws IllegalStateException, IOException {
		// Start a communicator on this JVM listening on port 15239.
		communicator = new DistributedCommunicator(
				TCPCommunicationAddress.fromInetAddress(InetAddress.getLoopbackAddress(), 15239));
		communicator.startCommunication();
		// Configure the address of the remote communicator.
		remoteAddress = TCPCommunicationAddress.fromInetAddress(InetAddress.getLoopbackAddress(), 15238);

		int players = 0, ais = 1, aiDifficulty = 3;
		int trackWidth = 40, trackLength = 40, trackSegLength = 80;

		game = DistributedCommunicator.getCommunicator().runRemotely(
				() -> new Game(players, trackWidth, trackLength, trackSegLength, ais, aiDifficulty), remoteAddress);

	}
	
	public DistributedCommunicator getComms(){
		return communicator;
	}

	public Game game() {

		return game;
	}

}