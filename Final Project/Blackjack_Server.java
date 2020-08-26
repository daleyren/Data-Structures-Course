package renD_Project10;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Date;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.stage.Stage;

public class Blackjack_Server extends Application implements Blackjack_Constants {
	private int sessionNo = 1;

	static int player1Total = 0;
	static int player2Total = 0;
	static int player1Score = 0;
	static int player2Score = 0;
	static boolean player1Stay = false;
	static boolean player2Stay = false;
	static int player1Cards = 0;
	static int player2Cards = 0;
	static boolean player1Turn = true;
	static boolean player2Turn = true;
	TextArea taLog = new TextArea();

	public static void main(String[] args) {
		launch(args);
	}

	public void start(Stage primaryStage) {

		// Create a scene and place it in the stage
		Scene scene = new Scene(new ScrollPane(taLog), 450, 200);
		primaryStage.setTitle("BlackjackServer"); // Set the stage title
		primaryStage.setScene(scene); // Place the scene in the stage
		primaryStage.show(); // Display the stage

		new Thread(() -> {
			try {
				// Create a server socket
				ServerSocket serverSocket = new ServerSocket(3000);
				Platform.runLater(() -> taLog.appendText(new Date() + ": Server started at socket 3000\n"));

				// Ready to create a session for every two players
				while (true) {
					Platform.runLater(() -> taLog
							.appendText(new Date() + ": Wait for players to join session " + sessionNo + '\n'));

					// Connect to player 1
					Socket player1 = serverSocket.accept();

					Platform.runLater(() -> {
						taLog.appendText(new Date() + ": Player 1 joined session " + sessionNo + '\n');
						taLog.appendText("Player 1's IP address" + player1.getInetAddress().getHostAddress() + '\n');
					});

					// Notify that the player is Player 1
					new DataOutputStream(player1.getOutputStream()).writeInt(player1Num);

					// Connect to player 2
					Socket player2 = serverSocket.accept();

					Platform.runLater(() -> {
						taLog.appendText(new Date() + ": Player 2 joined session " + sessionNo + '\n');
						taLog.appendText("Player 2's IP address" + player2.getInetAddress().getHostAddress() + '\n');
					});

					// Notify that the player is Player 2
					new DataOutputStream(player2.getOutputStream()).writeInt(player2Num);

					// Display this session and increment session number
					Platform.runLater(
							() -> taLog.appendText(new Date() + ": Start a thread for session " + sessionNo++ + '\n'));

					// Launch a new thread for this session of two players

					new Thread(new newSession(player1, player2)).start();
				}
			} catch (Exception ex) {
				System.out.println(ex);
			}
		}).start();
	}

	class newSession implements Runnable, Blackjack_Constants {
		private Socket player1;
		private Socket player2;

		private DataInputStream fromPlayer1;
		private DataOutputStream toPlayer1;
		private DataInputStream fromPlayer2;
		private DataOutputStream toPlayer2;

		private boolean nextRound = true;
		private int currCard;
//		private int aces = 0;
//		private int aceHigh = 0;
		public int winner = 0;

		private int aces1 = 0;
		private int aceHigh1 = 0;
		private int aces2 = 0;
		private int aceHigh2 = 0;

		public newSession(Socket player1, Socket player2) {
			this.player1 = player1;
			this.player2 = player2;
		}

		// Override Runnable
		public void run() {
			try {
				// Create data input and output streams
				DataInputStream fromPlayer1 = new DataInputStream(player1.getInputStream());
				DataOutputStream toPlayer1 = new DataOutputStream(player1.getOutputStream());
				DataInputStream fromPlayer2 = new DataInputStream(player2.getInputStream());
				DataOutputStream toPlayer2 = new DataOutputStream(player2.getOutputStream());

				// Write anything to notify player 1 to start
				// This is just to let player 1 know to start
				toPlayer1.writeInt(100);

				// Continuously serve the players and determine and report
				// the game status to the players
				
					while (player1Turn) {
						int currMove1 = fromPlayer1.readInt();
						taLog.appendText("Player 1's Move: " + currMove1 + '\n');
						if (currMove1 == moveHit) {
							currCard = fromPlayer1.readInt();
//							player2Cards += 1;
//							toPlayer1.writeInt(2);
							taLog.appendText("Player 1's Card Picked Up: " + currCard + '\n');
							// Checks if the deck has an Ace
							if (currCard == 1) {
								aces1 += 1;
							}
							// Checks if currCard is a 10, J, Q, K
							if (currCard >= 10) {
								player1Total = player1Total + 10;
							}
							// Checks for cards between 2 and 9
							else if (2 <= currCard && currCard <= 9) {
								player1Total += currCard;
							}
							// Checks ace values
							else if (currCard == 1) {
								if ((player1Total + 11) <= 21) {
									aceHigh1 += 1;
									player1Total += 11;
								} else if ((player1Total + 11) > 21) {
									player1Total += 1;
								}
							}

							System.out.println(player1Total);

							// Check if Player1 Total still valid
							if (player1Total > 21 && aces1 == 0) {
								toPlayer1.writeInt(player1Total);
								toPlayer2.writeInt(player1Total);
								winner = 2;
								player1Turn = false;
								toPlayer1.writeInt(player2_Won);
								toPlayer2.writeInt(player2_Won);
							}
							if (player1Total > 21 && aces1 > 0) {
								if (aceHigh1 == 1) {
									if ((player1Total - 10) <= 21) {
										player1Total -= 10;
										aceHigh1 -= 1;
									} else {
										toPlayer1.writeInt(player1Total);
										toPlayer2.writeInt(player1Total);
										winner = 2;
										player1Turn = false;
										toPlayer1.writeInt(player2_Won);
										toPlayer2.writeInt(player2_Won);
									}
								} else {
									toPlayer1.writeInt(player1Total);
									toPlayer2.writeInt(player1Total);
									winner = 2;
									player1Turn = false;
									toPlayer1.writeInt(player2_Won);
									toPlayer2.writeInt(player2_Won);
								}

							} else {
								toPlayer1.writeInt(player1Total);
								toPlayer2.writeInt(player1Total);

								toPlayer1.writeInt(5);
								toPlayer2.writeInt(5);
							}

						} else {
							System.out.println("Attempted Stay For Player 1");
							toPlayer1.writeInt(player1Total);
							toPlayer2.writeInt(player1Total);

							toPlayer1.writeInt(next);
							toPlayer2.writeInt(next);
							System.out.println("Successfully Stayed For Player 1");
							currCard = 0;
							player1Stay = true;
							player1Turn = false;
						}

					} // End of first While

					System.out.println("Winner = " + winner);
					while (player2Turn) {
						if (winner == 0) {

							// Receives Player 2's Move
							System.out.println("Testing, Waiting");
							int currMove2 = fromPlayer2.readInt();
							taLog.appendText("Player 2's Move: " + currMove2 + '\n');
							if (currMove2 == moveHit) {
								currCard = fromPlayer2.readInt();
								player2Cards += 1;
//								toPlayer1.writeInt(1);
								taLog.appendText("Player 2's Card Picked Up: " + currCard + '\n');
								// Checks if the deck has an Ace
								if (currCard == 1) {
									aces2 += 1;
								}
								// Checks if currCard is a 10, J, Q, K
								if (currCard >= 10) {
									player2Total = player2Total + 10;
								}
								// Checks for cards between 2 and 9
								else if (2 <= currCard && currCard <= 9) {
									player2Total += currCard;
								}
								// Checks ace values
								else if (currCard == 1) {
									if ((player2Total + 11) <= 21) {
										aceHigh2 += 1;
										player2Total += 11;
									} else if ((player2Total + 11) > 21) {
										player2Total += 1;
									}
								}

								System.out.println(player2Total);

								// Check if Player2 Total still valid
								if (player2Total > 21 && aces2 == 0) {
									toPlayer1.writeInt(player2Total);
									toPlayer2.writeInt(player2Total);
									winner = 1;
									player2Turn = false;
									toPlayer1.writeInt(player1_Won);
									toPlayer2.writeInt(player1_Won);
								}
								if (player2Total > 21 && aces2 > 0) {
									if (aceHigh2 == 1) {
										if ((player2Total - 10) <= 21) {
											player2Total -= 10;
											aceHigh2 -= 1;
										} else {
											toPlayer1.writeInt(player2Total);
											toPlayer2.writeInt(player2Total);
											winner = 1;
											player2Turn = false;
											toPlayer1.writeInt(player1_Won);
											toPlayer2.writeInt(player1_Won);
										}
									} else {
										toPlayer1.writeInt(player2Total);
										toPlayer2.writeInt(player2Total);
										winner = 1;
										player2Turn = false;
										toPlayer1.writeInt(player1_Won);
										toPlayer2.writeInt(player1_Won);
									}
								} else {
									toPlayer1.writeInt(player2Total);
									toPlayer2.writeInt(player2Total);
									toPlayer1.writeInt(5);
									toPlayer2.writeInt(5);
								}
							} else {
								System.out.println("Attempted Stay For Player 2");
								toPlayer1.writeInt(player2Total);
								toPlayer2.writeInt(player2Total);
								currCard = 0;
								player2Stay = true;
								player2Turn = false;
								System.out.println("Successfully Stayed For Player 2");

							}

						}

					} // End of Second While
					System.out.println("Round Over");
					if (winner == 1) {
						toPlayer1.writeInt(player1_Won);
						toPlayer2.writeInt(player1_Won);
						System.out.println("Player 1 Won");
					} else if (winner == 2) {
						toPlayer1.writeInt(player2_Won);
						toPlayer2.writeInt(player2_Won);
						System.out.println("Player 2 Won");
					} else {
						if (player1Total > player2Total) {
							toPlayer1.writeInt(player1_Won);
							toPlayer2.writeInt(player1_Won);
							System.out.println("Player 1 Won");
						} else if (player1Total < player2Total) {
							toPlayer1.writeInt(player2_Won);
							toPlayer2.writeInt(player2_Won);
							System.out.println("Player 2 Won");
						} else if (player1Total == player2Total) {
							toPlayer1.writeInt(draw);
							toPlayer2.writeInt(draw);
							System.out.println("Tie");
						}
					}
					//Failed Attempt to creates repeat round.
					// Variable Reset
//					Thread.sleep(100);
//					winner = 0;
//					aces1 = 0;
//					aceHigh1 = 0;
//					aces2 = 0;
//					aceHigh2 = 0;
//					currCard = 0;
//					player1Turn = true;
//					player2Turn = true;
//					player1Total = 0;
//					player2Total = 0;
//					player1Score = 0;
//					player2Score = 0;
//					player1Stay = false;
//					player2Stay = false;
//					player1Cards = 2;
//					player2Cards = 2;
			
			} catch (IOException ex) {
				System.out.println(ex.toString());

			}
		}
	}
}
