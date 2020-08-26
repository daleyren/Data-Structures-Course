package renD_Project10;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.beans.value.ObservableValue;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.scene.image.ImageView;
import javafx.scene.image.Image;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;

public class Blackjack_Client extends Application implements Blackjack_Constants {
	// Main Method For Launching Application
	public static void main(String[] args) {
		launch(args);
	}

	// Variables that were utilized across methods
	static HBox p1Cards = new HBox();
	static HBox p2Cards = new HBox();
	Text currScore = new Text();
	Text otherScore = new Text();
	Text blank1 = new Text();
	Text blank2 = new Text();
	VBox centerText = new VBox();
	HBox roundStatusWrap = new HBox();
	HBox hitWrap = new HBox();
	HBox stayWrap = new HBox();
	BorderPane mainPane = new BorderPane();
	Button hit;
	Button stay;
	int status = 5;

	static ArrayList<String> chosen1 = new ArrayList<String>();
	static ArrayList<String> cards1 = new ArrayList<String>();
	static ArrayList<String> chosen2 = new ArrayList<String>();
	static ArrayList<String> cards2 = new ArrayList<String>();
	static int player1Total = 0;
	static int player2Total = 0;

	private int playerMove;
	private int player;
	private boolean onPlayer1 = true;

	private DataInputStream fromServer;
	private DataOutputStream toServer;

	private boolean continueToPlay = true;

	private boolean waiting = true;

	private Text roundStatus = new Text();

	private static String currCard;

	// Start Override of the Application Superclass
	public void start(Stage primaryStage) throws Exception {
		// HBoxes Styling
		p1Cards.setFillHeight(false);
		p2Cards.setFillHeight(false);
		p1Cards.setMinHeight(100.5142);
		p2Cards.setMinHeight(100.5142);
		p1Cards.setStyle("-fx-border-color: #cf8ad4; -fx-border-width: 5px;");
		p2Cards.setStyle("-fx-border-color: #cf8ad4; -fx-border-width: 5px;");
		p1Cards.setAlignment(Pos.CENTER);
		p2Cards.setAlignment(Pos.CENTER);

		// Adds cards to the ArrayList
		ArrayList<String> suits = new ArrayList<String>();
		suits.add("C");
		suits.add("D");
		suits.add("H");
		suits.add("S");

		// Make a full deck of cards
		for (String suit : suits) {
			for (int i = 1; i < 14; i++) {
				cards1.add(i + suit);
			}
		}

		// split deck into two subsets
		for (int j = 0; j < 26; j++) {
			int currIndex = (int) (Math.random() * cards1.size());
			cards2.add(cards1.get(currIndex));
			cards1.remove(cards1.get(currIndex));
		}

		System.out.println(cards1);
		System.out.println(cards2);

		// Establish Panes in Scene
		BorderPane mainPane = new BorderPane();
		mainPane.setStyle("-fx-border-color: #cf8ad4; -fx-background-color: rgba(252, 214, 255, 1);");

		mainPane.setTop(p2Cards);
		mainPane.setBottom(p1Cards);
		BorderPane.setAlignment(p1Cards, Pos.CENTER);
		BorderPane.setAlignment(p2Cards, Pos.TOP_CENTER);

		// Establishes Hit and Stay Buttons
		hit = new Button();
		Label hitL = new Label("H I T");
		hitL.setFont(Font.font("Helvetica", FontWeight.NORMAL, 20));
		hitL.setRotate(-90);

		hit.setGraphic(new Group(hitL));
		hit.setMinWidth(50);
		hit.setMinHeight(296.9716);
		hit.setStyle(
				"-fx-border-width: 4px; -fx-base: #c2faff; -fx-background-color: -fx-shadow-highlight-color, -fx-outer-border, -fx-inner-border, -fx-body-color;");

		stay = new Button();
		Label stayL = new Label("S T A Y");
		stayL.setFont(Font.font("Helvetica", FontWeight.NORMAL, 20));
		stayL.setRotate(-90);

		stay.setGraphic(new Group(stayL));
		stay.setMinWidth(50);
		stay.setMinHeight(296.9716);
		stay.setStyle(
				"-fx-border-width: 4px; -fx-base: #c2faff; -fx-background-color: -fx-shadow-highlight-color, -fx-outer-border, -fx-inner-border, -fx-body-color;");

//		hitWrap = new HBox();
		hitWrap.getChildren().add(hit);
		hitWrap.setAlignment(Pos.CENTER);

//		stayWrap = new HBox();
		stayWrap.getChildren().add(stay);
		stayWrap.setAlignment(Pos.CENTER);

		mainPane.setLeft(hitWrap);
		mainPane.setRight(stayWrap);
		hit.setAlignment(Pos.CENTER);
		stay.setAlignment(Pos.CENTER);

		roundStatus = new Text("Connected"); // Shows "Opponents Turn...." "Your Turn..." "You Won!" "You Lost!"
		roundStatus.setFont(Font.font("Helvetica", FontWeight.NORMAL, 20));
		otherScore.setFont(Font.font("Helvetica", FontWeight.BOLD, 12));
		currScore.setFont(Font.font("Helvetica", FontWeight.BOLD, 12));

		roundStatusWrap.getChildren().add(roundStatus);
		roundStatusWrap.setAlignment(Pos.CENTER);
		roundStatusWrap.setMaxWidth(400);
		roundStatusWrap.setMaxHeight(100);
		roundStatusWrap.setMinHeight(100);
		roundStatusWrap.setStyle(
				"-fx-border-color: #cf8ad4; -fx-border-width: 5px; -fx-background-color: rgba(252, 214, 255, 1);");

		blank1 = new Text("");
		blank2 = new Text("");
		blank1.setFont(Font.font("", FontWeight.NORMAL, 60));
		blank2.setFont(Font.font("", FontWeight.NORMAL, 60));

//		centerText.getChildren().addAll(otherScore, blank1, roundStatusWrap, blank2, currScore);
		centerText.setAlignment(Pos.CENTER);
		mainPane.setCenter(centerText);

		// Sets Up Scene
		Scene scene = new Scene(mainPane, 749, 500);
		// Binds button heights
		ObservableValue<? extends Number> currHeight = scene.heightProperty();
		hit.prefHeightProperty().bind(currHeight);
		stay.prefHeightProperty().bind(currHeight);

		// Runs a method to connect to server and interact with the server
		connectToServer();

//		primaryStage.setResizable(false);
		primaryStage.setTitle("Blackjack");
		primaryStage.setScene(scene);
		primaryStage.show();

		// Action Events of the Hit and Stay Buttons
		hit.setOnAction(e -> {
			try {
				playerMove = moveHit;
				if (player == player1Num) {
					hitt(p1Cards);
				} else {
					hitt(p2Cards);
				}
				waiting = false;
			} catch (Exception ex) {
				System.err.println(ex);
			}
		});

		stay.setOnAction(e -> {
			try {
				playerMove = moveStay;
				waiting = false;
			} catch (Exception ex) {
				System.err.println(ex);
			}
		});
	}

	// Some variables that needed to be accessed and used outside the hitt method
	int currIndex1;
	int currIndex2;
	boolean otherCard;

	public void hitt(HBox hand) {
		currIndex1 = (int) (Math.random() * cards1.size());
		currIndex2 = (int) (Math.random() * cards2.size());
		if (player == player1Num) {
			otherCard = false;
			currCard = cards1.get(currIndex1);
			chosen1.add(currCard);
			cards1.remove(currIndex1);
			System.out.println(currCard);
			String temp = currCard + ".png";

			String input = "/renD_Project10/cardsImages/" + temp;

			Image cardImage = new Image(input);

			ImageView cardView = new ImageView(cardImage);
			cardView.setFitWidth(59.2284);
			cardView.setFitHeight(90.5142);
			hand.getChildren().add(cardView);
		}
		if (player == player2Num) {
			otherCard = true;
			currCard = cards2.get(currIndex2);
			chosen2.add(currCard);
			cards2.remove(currIndex2);
			System.out.println(currCard);
			String temp = currCard + ".png";

			String input = "/renD_Project10/cardsImages/" + temp;

			Image cardImage = new Image(input);

			ImageView cardView = new ImageView(cardImage);
			cardView.setFitWidth(59.2284);
			cardView.setFitHeight(90.5142);
			hand.getChildren().add(cardView);
		}
	}

	private void connectToServer() {
		try {
			// Create a socket to connect to the server
			Socket socket = new Socket("localhost", 3000);
			// Create an input stream
			fromServer = new DataInputStream(socket.getInputStream());
			// Create an output stream
			toServer = new DataOutputStream(socket.getOutputStream());

			new Thread(() -> {
				try {
					// Get notification from the server
					player = fromServer.readInt();

					// Determine which player
					if (player == player1Num) {
						hit.setDisable(true);
						stay.setDisable(true);
						// SET INTERFACE FOR PLAYER1!
						currScore = new Text("Y o u r  T o t a l : " + player1Total);
						otherScore = new Text("O p p o n e n t' s  T o t a l : ?");
						centerText.getChildren().addAll(otherScore, blank1, roundStatusWrap, blank2, currScore);

						Platform.runLater(() -> {
							roundStatus.setText("Waiting for player 2 to join");
						});

						// Receive startup notification from the server
						int fix = fromServer.readInt(); // Whatever read is ignored
						hit.setDisable(false);
						stay.setDisable(false);
						System.out.println("Start: " + fix);
						// The other player has joined
						Platform.runLater(() -> {
							roundStatus.setText("Player 2 has joined");
							try {
								Thread.sleep(1500);
							} catch (InterruptedException e) {
								e.printStackTrace();
							}
							roundStatus.setText("Your Turn");
						});

					} else if (player == player2Num) {
						// SET INTERFACE FOR PLAYER2!
						hit.setDisable(true);
						stay.setDisable(true);
						currScore = new Text("Y o u r  T o t a l : " + player2Total);
						otherScore = new Text("O p p o n e n t' s  T o t a l : ?");
						centerText.getChildren().addAll(currScore, blank1, roundStatusWrap, blank2, otherScore);

						Platform.runLater(() -> {
							roundStatus.setText("Connected.");
							try {
								Thread.sleep(1500);
							} catch (InterruptedException e) {
								e.printStackTrace();
							}
							roundStatus.setText("Waiting for Opponent");
						});
					}
					while (true) {
						// Goes through player 1's turn
						while (continueToPlay) {
							if (player == player1Num) {
								waitForPlayerAction(); // Wait for player 1 to move
								sendMove(); // Send the move to the server
							}
							receiveInfoFromServer(); // Receive info from the server

						}
						continueToPlay = true;
						// Goes Tthrough player 2's turn
						while (continueToPlay) {
							if (player == player2Num) {
								waitForPlayerAction(); // Wait for player 2 to move
								sendMove(); // Send the move to the server
							}
							receiveInfoFromServer(); // Receive info from the server
						}
						continueToPlay = false;
						Thread.sleep(2000);
						System.out.println("Client New? Round.");

						/* I was orignially trying to start a new round that began automatically
						 * after the round was over but due to many unforeseen difficulties and a
						 *  sleepless night, I determined that it was too much for me to handle in
						 *  the given timeframe and circumstances.
						 */

						/*player1Total = 0; player2Total = 0; cards1.addAll(chosen1);
						 * cards2.addAll(chosen2); Platform.runLater(() -> {
						 * p1Cards.getChildren().clear(); p2Cards.getChildren().clear(); });
						 */
						
						// ADD THE CHOSEN CARDS BACK TO THE DECK "cards"
						receiveScore();
						
						/*if (player == player1Num) { hit.setDisable(false); stay.setDisable(false);
						 * roundStatus.setText("Your Turn"); } if (player == player2Num) {
						 * roundStatus.setText("Waiting for Opponent"); hit.setDisable(true);
						 * stay.setDisable(true); }
						 */ }

				} catch (Exception ex) {
					ex.printStackTrace();
				}
			}).start();
		} catch (

		Exception ex) {
			System.out.println("Connection Error: " + ex);
		}
	}
	
	//Create a constant wait until a button is pressed and waiting is set false
	private void waitForPlayerAction() throws InterruptedException {
		while (waiting) {
			Thread.sleep(100);
		}
		waiting = true;
	}

	//Sends the card information to the server
	private void sendMove() throws IOException {
		System.out.println("1.....");
		String temp;
		int tempNum = 0;
		System.out.println("About To Player Move");
		toServer.writeInt(playerMove);
		System.out.println("Passed Player Move");
		if (playerMove == moveHit) {
			if (currCard.length() == 3) {
				temp = "" + currCard.charAt(0) + currCard.charAt(1);
				tempNum = Integer.parseInt(temp.trim());
			}
			if (currCard.length() == 2) {
				temp = "" + currCard.charAt(0);
				tempNum = Integer.parseInt(temp.trim());
			}
			System.out.println("Card Value: " + tempNum);
			toServer.writeInt(tempNum); // Send the card
		}
		System.out.println("2.....");
	}

	//Takes back information from the server: current player scores and round status
	private void receiveInfoFromServer() throws IOException {
		System.out.println("3....." + onPlayer1);
		if (onPlayer1) {
			player1Total = fromServer.readInt();
			System.out.println("3....1");
		} else if (onPlayer1 == false) {
			player2Total = fromServer.readInt();
			System.out.println("3....2");
			System.out.println("Player1Total: " + player1Total);
//			System.out.println("Player2Total: " + player2Total);
		}
		receiveScore();

		status = fromServer.readInt();
		System.out.println("Status: " + status);

		if (status == player1_Won) {
			// Player 1 won, stop playing
			continueToPlay = false;
			if (player == player1Num) {
				Platform.runLater(() -> roundStatus.setText("You Won"));
			}
			if (player == player2Num) {
				Platform.runLater(() -> roundStatus.setText("You Lost"));
			}

		} else if (status == player2_Won) {
			// Player 2 won, stop playing
			continueToPlay = false;
			if (player == player2Num) {
				Platform.runLater(() -> roundStatus.setText("You Won"));
			}
			if (player == player1Num) {
				Platform.runLater(() -> roundStatus.setText("You Lost"));
			}

		} else if (status == draw) {
			// No winner, game is over
			continueToPlay = false;
			Platform.runLater(() -> roundStatus.setText("Tie!"));

		} else if (status == next) {
			// Continue to player 2's turn
			onPlayer1 = false;
			if (player == player2Num) {
				continueToPlay = false;
				hit.setDisable(false);
				stay.setDisable(false);
				Platform.runLater(() -> roundStatus.setText("Your Turn"));
			}
			if (player == player1Num) {
				continueToPlay = false;
				hit.setDisable(true);
				stay.setDisable(true);
				Platform.runLater(() -> roundStatus.setText("Waiting on Opponent"));
			}
		}
		receiveScore();
		System.out.println("4.....");
	}

	//Change the scoring texts to be appropriate with the situation and keep score
	private void receiveScore() throws IOException {
		if (player == player2Num) {
			currScore.setText("Y o u r  T o t a l : " + player2Total);
			if (status == 1 || status == 2 || status == 3) {
				otherScore.setText("O p p o n e n t' s  T o t a l : " + player1Total);
			} else {
				otherScore.setText("O p p o n e n t' s  T o t a l : ?");
			}

		}
		if (player == player1Num) {
			currScore.setText("Y o u r  T o t a l : " + player1Total);
			if (status == 1 || status == 2 || status == 3) {
				otherScore.setText("O p p o n e n t' s  T o t a l : " + player2Total);
			} else {
				otherScore.setText("O p p o n e n t' s  T o t a l : ?");
			}
		}
	}
}
