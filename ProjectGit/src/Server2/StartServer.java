package Server2;

public class StartServer {
	
	public static Server server;

	public static void main(String[] args) {
		String name = args[0];
		int port = Integer.parseInt(args[1]);
		
		server = new Server(name, port);
		server.run();
		
	}

}
