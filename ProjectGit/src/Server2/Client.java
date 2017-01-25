package server2;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;


/**
 * P2 prac wk4. <br>
 * Client. 
 * @author  Theo Ruys
 * @version 2005.02.21
 */
public class Client extends Thread {
	private String clientName;
	private Socket sock;
//	private BufferedReader in;
//	private BufferedWriter out;
	
	static DataInputStream in;
	static DataOutputStream out;
	public InetAddress address;

	/**
	 * Constructs a Client-object and tries to make a socket connection
	 */
	public Client(String name, InetAddress host, int port)
			throws IOException {

		try {
            sock = new Socket(host, port);
            clientName = name;
            in = new DataInputStream(sock.getInputStream());
            
//            in = new BufferedReader(new InputStreamReader(sock.getInputStream()));
//    		out = new BufferedWriter(new OutputStreamWriter(sock.getOutputStream()));
    		
    		System.out.println("Waiting for server capabilities.");
    		String serverCapabilities = in.readUTF();
    		System.out.println(serverCapabilities);
    		
    		out = new DataOutputStream(sock.getOutputStream());
    		out.writeUTF(sendPreferences(serverCapabilities));
            
    		runGame();
    		
        } catch (IOException e) {
            System.out.println("ERROR: could not create a socket on " + host
                    + " and port " + port);
        }
		
	}

	/**
	 * Reads the messages in the socket connection. 
	 * Each message will be forwarded to the MessageUI
	 */
	public void runGame() {
		String line = null;
		
		try {
			while ((line = in.readUTF()) != null) {
				System.out.println(line);
			}
		} catch (IOException e) {
			System.out.println("Something went wrong while reading from socket. "
					+ "Turning off connection.");
			shutdown();
		}
	}
	
	public String sendPreferences(String serverCapabilities){
		Scanner prefs = new Scanner(System.in);
		
		//TODO: compare with serverCapabilities and give errors if values are out of bounds.
		System.out.println("Enter your game preferences. Make sure your preferences fit within the server capabilities!");
		System.out.println("With how many players do you want to play?");
		String players = prefs.nextLine();
		System.out.println("What is your game name? (Don't use spaces!)");
		String name = prefs.nextLine();
		System.out.println("Do you support rooms? Yes = 1, No = 0");
		String roomSupport = prefs.nextLine();
		System.out.println("With which dimentions do you want to play a game?");
		System.out.println("Enter lenght dimension x: ");
		String maxRoomDimensionX = prefs.nextLine();
		System.out.println("Enter lenght dimension y: ");
		String maxRoomDimensionY = prefs.nextLine();
		System.out.println("Enter lenght dimension z: ");
		String maxRoomDimensionZ = prefs.nextLine();
		System.out.println("How long does a winning connection have to be?"
				+ "(This length cannot be bigger than the smallest dimention)");
		String maxLengthToWin = prefs.nextLine();
		System.out.println("Do you support chats? Yes = 1, No = 0");
		String chatSupport = prefs.nextLine();
		
		String preferences = "sendCapabilities " + players + " " + name + " " + roomSupport + 
				" " + maxRoomDimensionX + " " + maxRoomDimensionY + " " + maxRoomDimensionZ +
				" " + maxLengthToWin + " " + chatSupport;
		
		System.out.println("You are sending these preferences to the server: " + preferences);
		
		return preferences;
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
