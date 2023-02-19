package SocketClient;


import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.print.Printer;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class ClientUI extends Application {

	PrintWriter pw;

	//username
	private String login = null;
	//zu wem wird nachricht geschickt
	private String sendTo = null;
	private Notification ntf = Notification.getInstanz();
	public Button send;
	

	@Override
	public void start(Stage primaryStage) {
		
		//**************** JAVAFX*********************
		BorderPane bp = new BorderPane();
	
	
		//****** Login Seite ******
		 
		  GridPane grid = new GridPane();
		   
		   //background 
		   grid.setStyle("-fx-background-color: #82b4b4;");
	       
		   grid.setAlignment(Pos.CENTER);
	        grid.setHgap(10);
	        grid.setVgap(10);
	        grid.setPadding(new Insets(25, 25, 25, 25));

	        Text scenetitle = new Text("Welcome ");
	       
	        scenetitle.setFont(Font.font("Tahoma", FontWeight.NORMAL, 30));
	        grid.add(scenetitle, 0, 0, 2, 1);

	        Label userName = new Label("UserName:");
	        grid.add(userName, 0, 1);
	        userName.setFont(new Font("Comic sans MS", 16));
	        
	        //textfeld fuer username
	        TextField usn = new TextField();
	        usn.setPrefWidth(240);
	        grid.add(usn, 1, 1);

	        Label ho = new Label("Host:");
	        grid.add(ho, 0, 2);
           ho.setFont(new Font("Comic sans MS", 16));
	        //textfeld fuer host
	        TextField adr = new TextField();
	        grid.add(adr, 1, 2);

	        
	        Label p = new Label("port:");
	        grid.add(p, 0, 3);
	        p.setFont(new Font("Comic sans MS", 16));
	        //textfeld fuer port
	        TextField port = new TextField();
	        grid.add(port, 1, 3);

	        
	        Button login = new Button("Sign in");
	        HBox hbBtn = new HBox(10);
	        hbBtn.setAlignment(Pos.BOTTOM_RIGHT);
	        hbBtn.getChildren().add(login);
	        grid.add(hbBtn, 1, 4);

	 
	        Label error = new Label();
	        error.setTextFill(Color.RED);
			error.setFont(new Font("Comic Sans MS", 14));
			error.setPrefHeight(30);
	        grid.add(error, 1, 6);

	    	port.setOnMouseClicked(e -> {
				error.setText("");
			});
			usn.setOnMouseClicked(e -> {
				error.setText("");
			});
			adr.setOnMouseClicked(e -> {
				error.setText("");
			});
			
	        bp.setCenter(grid);
		
	        // ****** End LoginSeite ******
	        
	      
	        
	        
    //page nach login und verbindung mit Server  
		//Footer
	        HBox hbox1 = new HBox();
			Button send = new Button("Senden");
			hbox1.setSpacing(10);
			hbox1.setPadding(new Insets(10));

			TextField message = new TextField();
			message.setPrefSize(400, 30);
			send.setPrefSize(90, 30);
			send.setFont(new Font("Comic sans MS", 13));
			hbox1.setStyle("-fx-background-color: #E1ECF4;");
			
			Label error1 = new Label();
	        error1.setTextFill(Color.RED);
			error1.setFont(new Font("Comic Sans MS", 14));
			error1.setPrefHeight(30);
			
			
			hbox1.getChildren().addAll(message, send,error1);

			hbox1.setStyle("-fx-background-color: #82b4b4;");
			

          // End  Footer		

		
		// Center ( Messages )

			ObservableList<String> list = FXCollections.observableArrayList();
			HBox hbox2 = new HBox();
			hbox2.setPadding(new Insets(0, 0, 0, 0));

			ListView<String> listView = new ListView<String>(list);
			hbox2.setStyle("-fx-background-color: #FFFFFF;");
			
		
		
			hbox2.getChildren().addAll(listView);
			hbox2.setStyle("-fx-padding: 1;" + "-fx-border-style: solid inside;" + "-fx-border-width: 5;"
					+ "-fx-border-insets: 2;" + "-fx-border-radius: 2;" + "-fx-border-color: #c0c0c0;");

			
			listView.setStyle("-fx-background-insets: 1; -fx-padding: 1; ");
			listView.prefWidthProperty();


			  listView.setPrefWidth(520);
			  listView.setPrefHeight(580);
			
			//hbox2.setPrefWidth(50);// prefWidth
			//hbox2.setPrefHeight(100);
		
		// END  Center ( Messages )

			
			//  Left ( Online User )
			VBox vbox3 = new VBox();
			Label title1 = new Label("Room");
			title1.setFont(new Font("Comic Sans MS", 16));
			title1.setTextFill(Color.web("#745330"));
			vbox3.setStyle("-fx-background-color: #FFFFFF;");
			ObservableList<String> list1 = FXCollections.observableArrayList();
			ListView<String> listView1 = new ListView<String>(list1);

			vbox3.setStyle("-fx-padding: 4;" + "-fx-border-style: solid inside;" + "-fx-border-width: 1;"
					+ "-fx-border-insets: 5;" + "-fx-border-radius: 2;" + "-fx-border-color: #c0c0c0;");

			listView1.setStyle("-fx-background-insets: 2; -fx-padding: 1; ");

			listView1.setOnMouseClicked(e -> {
				String s = listView1.getSelectionModel().getSelectedItem();
				sendTo = s;

			});


			  listView1.setPrefWidth(100);
			  listView1.setPrefHeight(500);
			
			vbox3.getChildren().addAll(title1, listView1);
			vbox3.setPrefWidth(300);// prefWidth
			vbox3.setPrefHeight(350);
			
			
			VBox vbox4 = new VBox();
			
			Label title2 = new Label("Online User");
			title2.setFont(new Font("Comic Sans MS", 16));
			//vbox3.setStyle("-fx-background-color: #FFFFFF;");
		
			title2.setTextFill(Color.web("#745330"));
			ObservableList<String> list2 = FXCollections.observableArrayList();
			ListView<String> listView2 = new ListView<String>(list2);
			
		
			
			vbox4.setStyle("-fx-padding: 4;" + "-fx-border-style: solid inside;" + "-fx-border-width: 1;"
					+ "-fx-border-insets: 5;" + "-fx-border-radius: 2;" + "-fx-border-color: #c0c0c0;");

			listView2.setStyle("-fx-background-insets: 2; -fx-padding: 1; ");
			
			
			listView2.setOnMouseClicked(e -> {
				String s = listView2.getSelectionModel().getSelectedItem();
				sendTo = s;

			});

			  listView2.setPrefWidth(200);
			  listView2.setPrefHeight(500);
			  
			vbox4.getChildren().addAll(title2, listView2);
			// END  Left ( User Online )

			// logout Top
			HBox hbox3=new HBox();
			
			
			hbox3.setStyle("-fx-background-color: #E1ECF4;");
			hbox3.getChildren().addAll(vbox3,vbox4);

			hbox3.setPrefWidth(380);// prefWidth
			hbox3.setPrefHeight(350);
			
			
			hbox3.setStyle("-fx-padding: 1;" + "-fx-border-style: solid inside;" + "-fx-border-width: 5;"
					+ "-fx-border-insets: 2;" + "-fx-border-radius: 2;" + "-fx-border-color: #c0c0c0;");

			HBox hbox4 = new HBox();
			hbox4.setSpacing(10);
			hbox4.setPadding(new Insets(10));
			Label label = new Label();
			Button logoff = new Button("Logout");

			HBox gruppe = new HBox();
			gruppe.setPadding(new Insets(0, 0, 0, 180));
			gruppe.setSpacing(5);

			Button join = new Button("Join");
			
			Button leave = new Button("Leave");
			
			
			TextField m = new TextField("");
			m.setOnMouseClicked(e -> {
				m.setText("");
			});

			m.setPrefSize(200, 30);
			join.setPrefHeight(30);
			join.setPrefWidth(80);
			join.setFont(new Font("Comic sans MS", 13));
			leave.setPrefHeight(30);
			leave.setPrefWidth(80);
			leave.setFont(new Font("Comic sans MS", 13));
			Label label1 = new Label("Chat Room");
			//label1.setStyle("-fx-color: #FF3358;");
			label1.setTextFill(Color.web("#3d403a"));
			
			label1.setPrefHeight(30);
			label1.setPrefWidth(90);
			label1.setFont(new Font("Comic Sans MS", 16));
			
			gruppe.getChildren().addAll(label1,m, join, leave);

			logoff.setPrefHeight(30);
			logoff.setPrefWidth(100);
			logoff.setFont(new Font("Comic sans MS", 13));
			label.setPrefHeight(30);
			label.setPrefWidth(150);
			label.setFont(new Font("Comic Sans MS", 16));
			label.setTextFill(Color.web("#3d403a"));
			hbox4.setStyle("-fx-background-color: #82b4b4;");
			
			
			
			
			hbox4.getChildren().addAll(logoff,label, gruppe);

			// END  logout Top

		
	
		
		
		login.setOnAction((evt) -> {

			if((usn.getText().trim()=="")|| (adr.getText().trim()=="" ) ||(port.getText().trim()=="")) {
				ntf.setText("Please fill out all required fields ");
	
				error.setText(ntf.getText());
			}
			else if(!(checkport(port.getText()))) {
				ntf.setText("Port contains only Numbers !!!");
				
				port.setText("");
				error.setText(ntf.getText());
			}
			
			else if(!(checkusername(usn.getText()))) {
				ntf.setText("Special characters are not allowed ");
				usn.setText("");
				error.setText(ntf.getText());
			}
			else {
			try {

				Socket socket = new Socket(adr.getText(), Integer.parseInt(port.getText()));
				InputStream inputstream = socket.getInputStream();
				OutputStream outputstream = socket.getOutputStream();
				InputStreamReader instreamreader = new InputStreamReader(inputstream);
				BufferedReader br = new BufferedReader(instreamreader);
				pw = new PrintWriter(socket.getOutputStream(), true);


				new Thread(() -> {

					try {

						
						//user ´name darf nicht besondere zeichen enthalten
						String username = usn.getText().trim();
						if (checkusername(username)) {
							//zu server geschickt 
							pw.println("login " + username);
							usn.setText("");
						} else {
							Platform.runLater(() -> {
								ntf.setText("No special characters allowed {!?@$%& ...");
								usn.setText("");
								error.setText(ntf.getText());

							});
						}

						while (true) {

							try {
								String response = br.readLine();
								
								String[] resp = response.split(" ");
								String cmd = resp[0];
								
								//online user in liste hinzufuegen
								if (cmd.equalsIgnoreCase("Online")) {
									Platform.runLater(() -> {
//								
										list2.add(resp[1].trim());
									});
									//wenn user offline dann von ,liste entfernen
								} else if (cmd.equalsIgnoreCase("Offline")) {
									Platform.runLater(() -> {
										list2.remove(resp[1].trim());
//								
									});
								} else if (cmd.equalsIgnoreCase("Welcome")) {

									// erfolgreiche Login Client UI zeigen 
									Platform.runLater(() -> {
										this.login = resp[1];
										bp.setTop(hbox4);
										bp.setBottom(hbox1);
										bp.setCenter(hbox2);
										bp.setRight(hbox3);
										label.setText(response);
									});

								} else if (cmd.equalsIgnoreCase("exist")) {

									Platform.runLater(() -> {
										ntf.setText("Error Username already exists, please choose another one");
										usn.setText("");
										error.setText(ntf.getText());
									});

								} else if (cmd.equalsIgnoreCase("unknown")) {

									Platform.runLater(() -> {
										Alert alert = new Alert(Alert.AlertType.ERROR);
										alert.setTitle("Unbekannte Eingabe");
										alert.setContentText("Unbekannte Eingabe");
										alert.setHeaderText(null);
										alert.setGraphic(null);

										alert.show();
									});

								} else if (cmd.equalsIgnoreCase("User")) {

									Platform.runLater(() -> {
										Alert alert = new Alert(Alert.AlertType.ERROR);
										alert.setTitle("Senden fehlgeschlagen");
										alert.setContentText(response);
										alert.setHeaderText(null);
										alert.setGraphic(null);
										alert.show();
									});

								} else {
									Platform.runLater(() -> {
										list.add(response);
									});
								}
							} catch (Exception e1) {
							
								e1.printStackTrace();
								try {
								socket.close();
								}catch(Exception e) {
									
									//ausfall Server 
									System.out.println("Socket Is Closed !!!");
									System.exit(0);
								}
								System.exit(0);
								
							}

						}
					} catch (Exception e) {
						ntf.setText(e.getMessage());
						error.setText(ntf.getText());
					}

				}).start();

			} catch (Exception e) {
				ntf.setText("Connection Failed");
				error.setText(ntf.getText());
			}
			}
		
		});

		send.setOnAction((evt) -> {

			String msg = message.getText().trim();

			if (msg.length() > 0) {
				if (this.sendTo != null && list2.size() != 0) {

					pw.println("_msg " + sendTo + " " + msg);
					message.setText("");

				} else {
					pw.println(msg);
					message.setText("");
				}

			}

			sendTo = null;

		});

		logoff.setOnAction((evt) -> {
			pw.println("logout");
			System.exit(0);
		});

		join.setOnAction((evt) -> {
			String topic = m.getText();
			if(!(m.getText().equalsIgnoreCase(""))) {
			pw.println("join @" + topic);
			list1.add("@" + topic);
			m.setText("");
			}else {
				// error1.setText("Room should have Name");
		       
			}
		});

		leave.setOnAction((e) -> {

			String topic = m.getText();
			pw.println("leave @" + topic);
			list1.remove("@" + topic);
			String s = "@" + topic;

			if (s.equalsIgnoreCase(this.sendTo)) {
				this.sendTo = null;
			}
			m.setText("");
		});
		
		
		
		
		
		
// falls Unterbrechung einer Client-Verbindung
		primaryStage.setOnCloseRequest(evt -> {
			if (this.login != null) {
				pw.println("logout");
			}
			System.exit(0);
		});

		primaryStage.setTitle("Chat Application");
		Scene scene = new Scene(bp, 900, 560);
		primaryStage.setScene(scene);
		primaryStage.show();

	}

	
	private boolean checkport(String port) {
 
		String num="0123456789";
		String p[]=port.split("");
		
		for(int i=0;i<port.length();i++) {
			if ( !(num.contains(p[i]) )){
				return false;
			}
		}
		
		return true;
	}


	public boolean checkusername(String name) {
		
		
		  String specialCharacters=" !#$%&'()*+,-./:;<=>?@[]^_`{|}~";
		

		  String username[]=name.split("");
		  
		    for (int i=0;i<username.length;i++)
		    {
		    if (specialCharacters.contains(username[i]))
		    
		        return false;
		        
		    }
		   
		return true;
	}
	
	
	public static void main(String[] args) {
		launch(args);
	}

}