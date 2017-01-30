package server;

import java.util.Scanner;

public class ClientTUI extends Thread {
	
	private Scanner scanner;
	private Client client;
	
	public ClientTUI(Client client) {
		this.client = client;
	}
	
	public void start() {
		scanner = new Scanner(System.in);
		boolean loop = true;
		
		while (loop) {
			String input = scanner.next();
			client.sendMessage(input);
		}
	}
	
	public void show(String msg) {
		System.out.println(msg);
	}
	

}
