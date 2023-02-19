package SocketServer;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.HashSet;
import java.util.List;



public class ServerWorker extends Thread {

	//Attribute 
	private final Socket socket;
	private final Server serverWorker;
	private String login = null;
	private OutputStream os;
	private InputStream is;
	private HashSet<String> joinSet = new HashSet<String>();
	
	//Time when user send msg
	LocalTime time = LocalTime.now();
	String t ;

	//constructor  
	public ServerWorker(Server server, Socket socket) {
		this.socket = socket;
		this.serverWorker = server;
	}

	//getMethode
	public String getLogin() {
		return login;
	}

	@Override
	public void run() {

		try {
			this.is = this.socket.getInputStream();
			this.os = this.socket.getOutputStream();

			InputStreamReader isr = new InputStreamReader(is);
			BufferedReader br = new BufferedReader(isr);

			PrintWriter pw = new PrintWriter(os, true);
			//System.out.println("Connection ...");


			while (true) {

				String req=null;
				try {
					//nachricht von client wird gelesen
					req = (br.readLine()).trim();
				} catch (Exception e) {
				
					e.printStackTrace();
					
				}

			//	if(req!=null) {System.out.println("No Request");break;}
					
				String[] tokens = req.split(" ");
				
				if (this.login == null) {
					//die code von protocol
					String cmd = tokens[0];

					if (cmd.equalsIgnoreCase("login") && tokens.length == 2) {

						String username = tokens[1];
						boolean exist = false;
						for (ServerWorker c : serverWorker.getClientList()) {

							if (username.equalsIgnoreCase(c.getLogin()))
								exist = true;
						}
						if (exist) {
							String msg = "exist \n";
							os.write(msg.getBytes());
						} else {

							username = Character.toUpperCase(username.charAt(0)) + username.substring(1);
							this.login = username;
							String msg = "Welcome " + username + "\n";
							System.out.println("Connection " + this.login + " successful");
							os.write(msg.getBytes());

							List<ServerWorker> list = serverWorker.getClientList();
							
							for (ServerWorker c : list) {
								if (c.getLogin() != null) {
									if (this.login != c.getLogin()) {
										String onlineMsg = "Online " + this.login + "\n";
										c.send(onlineMsg);
									}
								}
							}

							for (ServerWorker c : list) {
								if (c.getLogin() != null) {
									if (this.login != c.getLogin()) {
										String onlineMsg = "Online " + c.getLogin() + "\n";
										send(onlineMsg);
									}
								}

							}
						}

					} else {
						String msg = "unknown " + cmd + "\n";
						os.write(msg.getBytes());
					}

				} else if (tokens != null && tokens.length > 0) {

					String cmd = tokens[0];

					if (cmd.equalsIgnoreCase("logout")) {
						logout();

					} else if (cmd.equalsIgnoreCase("_msg") && tokens.length > 2) {

						handleMessage(tokens);

					} else if (cmd.equalsIgnoreCase("join") && tokens.length > 1) {
						handleJoin(tokens);
					} else if (cmd.equalsIgnoreCase("leave") && tokens.length > 1) {
						handelLeave(tokens);
					} else {
						broadcastMessage(req);
					}

				}

			}

		} catch (IOException e) {

			e.printStackTrace();
		}

	}

    //Join Gruppe
	private void handelLeave(String[] tokens) {

		joinSet.remove(tokens[1]);

	}

	
	 // Leave Gruppe 
	
	private void handleJoin(String[] tokens) {
		joinSet.add(tokens[1]);
	}

	
	// checkt if user is Member gruppe
	private boolean isMember(String topic) {
		return joinSet.contains(topic);
	}

	
	
       
	
	
	//Send BroadcastMessage To All Users
	private void broadcastMessage(String tokens) throws IOException {
	
		
		//send to all online user 
		for (ServerWorker c : serverWorker.getClientList()) {
             t= time.format(DateTimeFormatter.ofPattern("HH:mm"));
             time = LocalTime.now();
			if (c.getLogin() != login) {
				String outMsg = "("+t+") "+login + " : " + tokens + "\n";
				c.send(outMsg);
			} else {
				String outMsg = "("+t+") "+"You :" + tokens + "\n";
				c.send(outMsg);
			}

		}

	}

	//On Send Click handle
	private void handleMessage(String[] tokens) throws IOException {

		String sendTo = tokens[1];

		String body = "";

		boolean isTopic = sendTo.charAt(0) == '@';

		for (int i = 2; i < tokens.length; i++) {

			body = String.join(" ", body, tokens[i]);
		}

	
		if ((exist(sendTo) || isTopic)) {
			for (ServerWorker c : serverWorker.getClientList()) {
				  t= time.format(DateTimeFormatter.ofPattern("HH:mm"));
		             time = LocalTime.now();
				if (isTopic) {
					if (c.isMember(sendTo)) {
						if (!this.login.equalsIgnoreCase(c.getLogin())) {
							String outMsg = "("+t+")"+" Room (" + sendTo + ") From(" + login + ") : " + body.trim() + "\n";
							c.send(outMsg);
						} else {
							String outMsg = "("+t+")"+" You :To All In Room (" + sendTo + ") : " + body.trim() + "\n";
							c.send(outMsg);
						}
					}

				} else {
					if (sendTo.equalsIgnoreCase(c.getLogin())) {
						String out = "("+t+")"+"Private Msg From (" + login + ") : " + body.trim() + "\n";
						c.send(out);
					} else if (this.login.equalsIgnoreCase(c.getLogin())) {

						String msgout = "("+t+")"+" You: To(" + sendTo + ") : " + body.trim() + "\n";
						c.send(msgout);
					}
				}

			}

		} else {
		
			String msg = "User nicht Online \n";
			os.write(msg.getBytes());
		}

	}

	
	//logout
	private void logout() throws IOException {
		System.out.println("Logout " + this.login);
		serverWorker.removeWorker(this);
		List<ServerWorker> list = serverWorker.getClientList();
		for (ServerWorker c : list) {
			if (this.login != c.getLogin()) {
				String offlineMsg = "Offline " + this.login + "\n";
				c.send(offlineMsg);
			}

		}
try {
		this.socket.close();
}catch(Exception e) {
	e.printStackTrace();	
}
}

	// send msg to user
	private void send(String msg) throws IOException {
		if (login != null) {
			os.write(msg.getBytes());
		}

	}


//check if user online 
	private boolean exist(String sendTo) {
		boolean e = false;
		for (ServerWorker c : serverWorker.getClientList()) {

			if (sendTo.equalsIgnoreCase(c.getLogin()))
				e = true;
		}
		if (e)
			return true;
		else
			return false;
	}
}