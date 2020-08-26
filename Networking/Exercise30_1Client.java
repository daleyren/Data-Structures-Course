/*
 * Class: CMSC214 
 * Instructor: Mr.Estep
 * Description: The program is designed to create a client for the Exercise30_1Server's server,
   and allows for the input of the necessary things to calculate a payment 
 * Due: 8/2/2020
 * I pledge that I have completed the programming assignment independently.
   I have not copied the code from a student or any source.
   I have not given my code to any student.
   Print your Name here: Dale Ren
*/

package renD_Project08;

import java.io.*;
import java.net.*;
import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class Exercise30_1Client extends Application {
	// IO streams
	private DataOutputStream toServer = null;
	private DataInputStream fromServer = null;

	@Override // Override the start method in the Application class
	public void start(Stage primaryStage) {

		BorderPane mainPane = new BorderPane();
		GridPane bulk = new GridPane();
		// Prompt 1
		Text t = new Text("Annual Interest Rate            ");
		t.setStyle("-fx-font-weight: bold");

		TextField tf = new TextField();
		tf.setAlignment(Pos.BOTTOM_RIGHT);

		// Prompt 2
		Text t1 = new Text("Number Of Years");
		t1.setStyle("-fx-font-weight: bold");

		TextField tf1 = new TextField();
		tf1.setAlignment(Pos.BOTTOM_RIGHT);

		// Prompt 3
		Text t2 = new Text("Loan Amount");
		t2.setStyle("-fx-font-weight: bold");

		TextField tf2 = new TextField();
		tf2.setAlignment(Pos.BOTTOM_RIGHT);

		// Sets up main window
		Button submit = new Button("Submit");
		submit.setMinWidth(75);
		submit.setMinHeight(75);
		// Text area to display contents
		TextArea ta = new TextArea();

		bulk.add(t, 0, 0);
		bulk.add(tf, 1, 0);
		bulk.add(t1, 0, 1);
		bulk.add(tf1, 1, 1);
		bulk.add(t2, 0, 2);
		bulk.add(tf2, 1, 2);
		mainPane.setCenter(bulk);
		mainPane.setBottom(new ScrollPane(ta));
		mainPane.setRight(submit);

		// Create a scene and place it in the stage
		Scene scene = new Scene(mainPane, 450, 200);
		primaryStage.setTitle("Exercise30_1Client"); // Set the stage title
		primaryStage.setScene(scene); // Place the scene in the stage
		primaryStage.show(); // Display the stage

		submit.setOnAction(e -> {
			try {
				// Get the data from the text fields
				double interest = Double.parseDouble(tf.getText().trim());
				double years = Double.parseDouble(tf1.getText().trim());
				double loan = Double.parseDouble(tf2.getText().trim());

				// Send data to the server
				toServer.writeDouble(interest);
				toServer.writeDouble(years);
				toServer.writeDouble(loan);
				toServer.flush();
				
				// Get outputs from the server
				double totalPayment = fromServer.readDouble();
				double monthlyPayment = fromServer.readDouble();

				// Display to the text area
				ta.appendText("Annual Interest Rate: " + interest + ", Number of Years: " + years + ", Loan Amount: "
						+ loan + '\n');
				ta.appendText("monthlyPayment: " + monthlyPayment + ", totalPayment: " + totalPayment + '\n');
			} catch (IOException ex) {
				System.err.println(ex);
			}
		});

		try {
			// Create a socket to connect to the server
			Socket socket = new Socket("localhost", 8999);

			// Create an input stream to receive data from the server
			fromServer = new DataInputStream(socket.getInputStream());

			// Create an output stream to send data to the server
			toServer = new DataOutputStream(socket.getOutputStream());
		} catch (IOException ex) {
			ta.appendText(ex.toString() + '\n');
		}
	}

	/**
	 * The main method is only needed for the IDE with limited JavaFX support. Not
	 * needed for running from the command line.
	 */
	public static void main(String[] args) {
		launch(args);
	}
}
