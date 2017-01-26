package server2;

public class StartServer {
	
	public static Server connectFourServer;

	public static void main(String[] args) {
		String name = args[0];
		int port = Integer.parseInt(args[1]);
		
		connectFourServer = new Server(name, port);
		connectFourServer.run();
		
	}

}
