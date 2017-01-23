package Server2;

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Collection;
import java.util.Vector;




/**
 * P2 prac wk5. <br>
 * Server. A Thread class that listens to a socket connection on a 
 * specified port. For every socket connection with a Client, a new 
 * ClientHandler thread is started. 
 * @author  Theo Ruys
 * @version 2005.02.21
 */
public class Server extends Thread {
    private String name;
	private int port;
    private MessageUI mui;
    private Collection<ClientHandler> threads;
    private String hostAddress;
    private ServerSocket serverSock;
    public Socket clientSock;
    
    //---- server capabilities----
    int amountOfPlayers = 2;
    int roomSupport = 0;
    int maxRoomDimensionX = 0;
    int maxRoomDimensionY = 0;
    int maxRoomDimensionZ = 0;
    int maxLengthToWin = 4;
    int chatSupport = 0;

    /** Constructs a new Server object */
    public Server(String name, int portArg) {
    	this.port = portArg;
    	this.name = name;
    	
    	try {
			hostAddress = InetAddress.getLocalHost().getHostAddress();
			System.out.println("Sever has started on IP-address: " + hostAddress);
		} catch (UnknownHostException e) {
			// TODO throw unknow hostexception
			System.out.println("Invalid local address: " + hostAddress);
		}
    }

    /**
     * Listens to a port of this Server if there are any Clients that 
     * would like to connect. For every new socket connection a new
     * ClientHandler thread is started that takes care of the further
     * communication with the Client.
     */
    public void run() {
		
    	try {
			serverSock = new ServerSocket(port);
			System.out.println(serverSock);
    		while(true){
    			clientSock = serverSock.accept();
//    			System.out.println(clientSock);
//    			ClientHandler newClient = new ClientHandler(this, clientSock);
//    			newClient.start();
    			
//    			threads.add(newClient);
//    			this.serverCapabilities(newClient);
    		}
    	} catch(Exception e) {
    		System.out.println("fout1");
    		//TODO: exception
    	}
    }

    public void serverCapabilities(ClientHandler client){
    	String capabilities = ("serverCapabilities" + amountOfPlayers + roomSupport + maxRoomDimensionX 
    			+ maxRoomDimensionY + maxRoomDimensionZ + maxLengthToWin + chatSupport);
    	
    	client.sendMessage(capabilities);
    }
    

    
    
    /**
     * Sends a message using the collection of connected ClientHandlers
     * to all connected Clients.
     * @param msg message that is send
     */
    public void broadcast(String msg) {
        for(ClientHandler client : threads){
        	client.sendMessage(msg);
        }
    }

    /**
     * Add a ClientHandler to the collection of ClientHandlers.
     * @param handler ClientHandler that will be added
     */
    public void addHandler(ClientHandler handler) {
    	if(!threads.contains(handler)){
    		threads.add(handler);
    	} else {
    		//TODO: throw exception
    		System.out.println("This ClientHandler allready exists.");
    	}
    }

    /**
     * Remove a ClientHandler from the collection of ClientHanlders. 
     * @param handler ClientHandler that will be removed
     */
    public void removeHandler(ClientHandler handler) {
    	if(threads.contains(handler)){
    		threads.remove(handler);
    	} else {
    		//TODO: throw exception clienthandler not in the list
    	}
    }
}

