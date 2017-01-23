package Server2;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;


/**
 * P2 prac wk4. <br>
 * Client. 
 * @author  Theo Ruys
 * @version 2005.02.21
 */
public class Client extends Thread {
	private String clientName;
	private Socket sock;
	private BufferedReader in;
	private BufferedWriter out;
	
	public InetAddress address;

	/**
	 * Constructs a Client-object and tries to make a socket connection
	 */
	public Client(String name, InetAddress host, int port)
			throws IOException {

		try {
            sock = new Socket(host, port);
        } catch (IOException e) {
            System.out.println("ERROR: could not create a socket on " + host
                    + " and port " + port);
        }
		
		this.runBeforeGame();
	}

	/**
	 * Reads the messages in the socket connection. 
	 * Each message will be forwarded to the MessageUI
	 */
	public void runBeforeGame() {
		
		
		
		this.runGame();
	}
	
	public void runGame() {
		
	}

	/** send a message to a ClientHandler. */
	public void sendMessage(String msg) {
		// TODO Add implementation
	}

	/** close the socket connection. */
	public void shutdown() {
		// TODO Add implementation
	}

	/** returns the client name */
	public String getClientName() {
		return clientName;
	}

}
