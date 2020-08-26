/*
 * Class: CMSC214 
 * Instructor: Mr.Estep
 * Description: Listens for a client's input of annual interest rate, number of years, and loan amount,
   calculates the total and monthly payments, and sends them to the client.
 * Due: 8/2/2020
 * I pledge that I have completed the programming assignment independently.
   I have not copied the code from a student or any source.
   I have not given my code to any student.
   Print your Name here: Dale Ren
*/

package renD_Project08;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Date;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.stage.Stage;

public class Exercise30_1Server extends Application {
	private int runs = 0;
	@Override // Override the start method in the Application class
	public void start(Stage primaryStage) {
		// Text area for displaying contents
		TextArea ta = new TextArea();

		// Create a scene and place it in the stage
		Scene scene = new Scene(new ScrollPane(ta), 450, 200);
		primaryStage.setTitle("Exercise30_1Server"); // Set the stage title
		primaryStage.setScene(scene); // Place the scene in the stage
		primaryStage.show(); // Display the stage

		new Thread(() -> {
			try {
				// Create a server socket
				ServerSocket serverSocket = new ServerSocket(8999);
				Platform.runLater(() -> ta.appendText("Exercise30_1Server started at " + new Date() + '\n'));

				// Listen for a connection request
				Socket socket = serverSocket.accept();
				
				// Create data input and output streams
				DataInputStream inputFromClient = new DataInputStream(socket.getInputStream());
				DataOutputStream outputToClient = new DataOutputStream(socket.getOutputStream());

				while (true) {
					InetAddress inet = socket.getInetAddress();
					if (runs == 0) {
						Platform.runLater(() -> {
							ta.appendText("Starting thread for client 1 at: " + new Date() + '\n');
							ta.appendText("Client 1's host name is " + inet.getHostName() + '\n');
							ta.appendText("Client 1's IP Address is " + inet.getHostAddress() + '\n');
						});
					}
					
					
					// Receive data from the client
					double interest = inputFromClient.readDouble();
					double years = inputFromClient.readDouble();
					double loan = inputFromClient.readDouble();
					
					// Process data from client
					double totalPayment = loan*Math.pow((1+(interest/100)),years)-loan;
					double monthlyPayment = totalPayment/(12*years);

					// Send totalPayment back to the client
					outputToClient.writeDouble(totalPayment);
					outputToClient.writeDouble(monthlyPayment);

					Platform.runLater(() -> {
						ta.appendText("Annual Interest Rate: " + interest + ", Number of Years: " + years + ", Loan Amount: " + loan +'\n');
						ta.appendText("monthlyPayment: " + monthlyPayment + ", totalPayment: " + totalPayment +'\n');
					});
					runs += 1;
				}
			} catch (IOException ex) {
				ex.printStackTrace();
			}
		}).start();
	}

	/**
	 * The main method is only needed for the IDE with limited JavaFX support. Not
	 * needed for running from the command line.
	 */
	public static void main(String[] args) {
		launch(args);
	}
}