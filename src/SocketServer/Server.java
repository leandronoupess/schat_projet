package SocketServer;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Scanner;

public class Server extends Thread {
	public static Scanner sc;


	
	private List<ServerWorker> userlist = new ArrayList<>();

	public List<ServerWorker> getClientList() {
		return userlist;
	}

	@Override
	public void run() {

		try {
			sc = new Scanner(System.in);
			System.out.println("Enter Port:");
			int port = sc.nextInt();
	    	
			ServerSocket ss = new ServerSocket(port);
		
			 System.out.println("About to accept client connection...");
			while (true) {

				Socket socket = ss.accept();
				 System.out.println("Accepted connection from " + socket);
				ServerWorker worker = new ServerWorker(this, socket);
				userlist.add(worker);
				worker.start();

			}
		} catch (Exception e) {
			System.out.println(e);
		}
	}


	
	 //Offline User aus List entfernt
	 
	public static void main(String[] args) {
	      
	
   new Server().start();
    }
	public void removeWorker(ServerWorker serverWorker) {

		userlist.remove(serverWorker);

	}

}
