/*
 * Class: CMSC214 
 * Instructor: Mr.Estep
 * Description: Create a interface that allows the user to interact with a database for storing staff information.
 * Due: 8/09/2018
 * I pledge that I have completed the programming assignment independently.
   I have not copied the code from a student or any source.
   I have not given my code to any student.
   Print your Name here: Dale Ren
*/

package renD_Project09;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.application.Application;
import javafx.scene.control.Button;

public class renD_Project09 extends Application {
	public static void main(String[] args) throws Exception {
		getConnection();
//		createTable();
		launch(args);
	}

	public static Connection getConnection() throws Exception {
		String driver = "com.mysql.jdbc.Driver";
		String url = "jdbc:mysql://localhost:3306/staff";
		String username = "sa";
		String password = "Password1";
		try {
			Class.forName(driver);
			Connection conn = DriverManager.getConnection(url, username, password);

			System.out.println("Connected to SQL Server!");
			return conn;
		} catch (Exception e) {
			System.out.println("Connection Error: " + e);
		}

		return null;
	}

	public static void createTable() throws Exception {
		try {
			Connection conn = getConnection();
			PreparedStatement table = conn.prepareStatement(
					"CREATE TABLE Staff ( id char(9) not null, lastName varchar(15), firstName varchar(15), mi char(1), address varchar(20), city varchar(20), state char(2), telephone char(10), email varchar(40), primary key (id) );");
			table.executeUpdate();
		} catch (Exception ex) {
			System.out.println("Create Table Error" + ex);
		} finally {
			System.out.println("Table Created");
		}
	}

	public void start(Stage primaryStage) {
		BorderPane mainPane = new BorderPane();
		Text staffInfo = new Text("Staff Information");
		mainPane.setTop(staffInfo);
		GridPane info = new GridPane();
		mainPane.setCenter(info);

		Text id_t = new Text("ID");
		TextField id_tf = new TextField();
		info.add(id_t, 0, 0);
		info.add(id_tf, 1, 0);

		Text last_t = new Text("Last Name");
		TextField lastName_tf = new TextField();
		info.add(last_t, 0, 1);
		info.add(lastName_tf, 1, 1);

		Text first_t = new Text("First Name");
		TextField firstName_tf = new TextField();
		info.add(first_t, 0, 2);
		info.add(firstName_tf, 1, 2);

		Text mi_t = new Text("mi");
		TextField mi_tf = new TextField();
		info.add(mi_t, 0, 3);
		info.add(mi_tf, 1, 3);

		Text address_t = new Text("Address");
		TextField address_tf = new TextField();
		info.add(address_t, 0, 4);
		info.add(address_tf, 1, 4);

		Text city_t = new Text("City");
		TextField city_tf = new TextField();
		info.add(city_t, 0, 5);
		info.add(city_tf, 1, 5);

		Text state_t = new Text("State");
		TextField state_tf = new TextField();
		info.add(state_t, 0, 6);
		info.add(state_tf, 1, 6);

		Text telephone_t = new Text("Telephone");
		TextField telephone_tf = new TextField();
		info.add(telephone_t, 0, 7);
		info.add(telephone_tf, 1, 7);

		Text email_t = new Text("Email");
		TextField email_tf = new TextField();
		info.add(email_t, 0, 8);
		info.add(email_tf, 1, 8);

		HBox buttons = new HBox();
		mainPane.setBottom(buttons);
		buttons.setAlignment(Pos.CENTER);
		Button view = new Button("View");
		Button insert = new Button("Insert");
		Button update = new Button("Update");
		Button clear = new Button("Clear");
		buttons.getChildren().addAll(view, insert, update, clear);

		Scene scene = new Scene(mainPane, 200, 270);
		primaryStage.setTitle("Project 09"); // Set the stage title
		primaryStage.setScene(scene); // Place the scene in the stage
		primaryStage.show(); // Display the stage

		view.setOnAction(e -> {
			try {
				ArrayList<TextField> fields = new ArrayList<TextField>();
				fields.add(lastName_tf);
				fields.add(firstName_tf);
				fields.add(mi_tf);
				fields.add(address_tf);
				fields.add(city_tf);
				fields.add(state_tf);
				fields.add(telephone_tf);
				fields.add(email_tf);

				ArrayList<String> fieldNames = new ArrayList<String>();
				fieldNames.add("lastName");
				fieldNames.add("firstName");
				fieldNames.add("mi");
				fieldNames.add("address");
				fieldNames.add("city");
				fieldNames.add("state");
				fieldNames.add("telephone");
				fieldNames.add("email");

				Connection conn = getConnection();
				PreparedStatement statement = conn.prepareStatement("SELECT id FROM Staff");

				ResultSet result = statement.executeQuery();
				ArrayList<Integer> array = new ArrayList<Integer>(); // Might be int
				while (result.next()) {
					System.out.println(Integer.parseInt(result.getString("id")));

					array.add(Integer.parseInt(result.getString("id")));
				}
				int x = array.indexOf(Integer.parseInt(result.getString("id")));
				int y = 0;
				for (TextField field : fields) {
					String curName = fieldNames.get(y);
					for (int i = 0; i < x; i++) {
						result.next();
					}
					field.setText(result.getString(curName));
					y++;
				}
			} catch (Exception ex) {
				System.out.println("View Error: " + ex);
			}
		});

		insert.setOnAction(e -> {
			int curId = Integer.parseInt(id_tf.getText().trim());
			String curFirst = firstName_tf.getText().trim();
			String curLast = lastName_tf.getText().trim();
			String curMi = mi_tf.getText().trim();
			String curAddress = address_tf.getText().trim();
			String curCity = city_tf.getText().trim();
			String curState = state_tf.getText().trim();
			String curTelephone = telephone_tf.getText().trim();
			String curEmail = email_tf.getText().trim();

			try {
				Connection conn = getConnection();
				PreparedStatement inserted = conn.prepareStatement(
						"INSET INTO Staff (id, lastName, firstName, mi, address, city, state, telephone, email)"
								+ " VALUES ('" + curId + "', '" + curFirst + "', '" + curLast + "', '" + curMi + "', '"
								+ curAddress + "', '" + curCity + "', '" + curState + "', '" + curTelephone + "', '"
								+ curEmail + "')");
				inserted.executeUpdate();
			} catch (Exception ex) {
				System.out.println("Insert Error: " + ex);
			}
		});

		update.setOnAction(e -> { // MIGHT NEED CHANGING TO WORK WITH A PRIMARY KEY
			int curId = Integer.parseInt(id_tf.getText().trim());
			String curFirst = firstName_tf.getText().trim();
			String curLast = lastName_tf.getText().trim();
			String curMi = mi_tf.getText().trim();
			String curAddress = address_tf.getText().trim();
			String curCity = city_tf.getText().trim();
			String curState = state_tf.getText().trim();
			String curTelephone = telephone_tf.getText().trim();
			String curEmail = email_tf.getText().trim();
			try {
				ArrayList<TextField> fields = new ArrayList<TextField>();
				fields.add(lastName_tf);
				fields.add(firstName_tf);
				fields.add(mi_tf);
				fields.add(address_tf);
				fields.add(city_tf);
				fields.add(state_tf);
				fields.add(telephone_tf);
				fields.add(email_tf);

				ArrayList<String> fieldNames = new ArrayList<String>();
				fieldNames.add("lastName");
				fieldNames.add("firstName");
				fieldNames.add("mi");
				fieldNames.add("address");
				fieldNames.add("city");
				fieldNames.add("state");
				fieldNames.add("telephone");
				fieldNames.add("email");

				Connection conn = getConnection();
				PreparedStatement delete = conn.prepareStatement("DELETE FROM Staff WHERE id = " + curId + ";");
				delete.executeUpdate();

				PreparedStatement inserted = conn.prepareStatement(
						"INSET INTO Staff (id, lastName, firstName, mi, address, city, state, telephone, email)"
								+ " VALUES ('" + curId + "', '" + curFirst + "', '" + curLast + "', '" + curMi + "', '"
								+ curAddress + "', '" + curCity + "', '" + curState + "', '" + curTelephone + "', '"
								+ curEmail + "')");
				inserted.executeUpdate();

//				ArrayList<Integer> array = new ArrayList<Integer>(); //Might be int
//				while(result.next()) {
//					System.out.println(Integer.parseInt(result.getString("id")));
//					
//					array.add(Integer.parseInt(result.getString("id")));
//				}
//				int x = array.indexOf(Integer.parseInt(result.getString("id")));
//				int y = 0;
//				for (TextField field : fields) {
//					String curName = fieldNames.get(y);
//					for (int i = 0; i < x; i++) {
//						result.next();
//					}
//					field.setText(result.getString(curName));
//					y++;
//				}
			} catch (Exception ex) {
				System.out.println("View Error: " + ex);
			}
		});

		clear.setOnAction(e -> {
			int curId = Integer.parseInt(id_tf.getText().trim());
			try {
				Connection conn = getConnection();
				PreparedStatement delete = conn.prepareStatement("DELETE FROM Staff WHERE id = " + curId + ";");
				delete.executeUpdate();
				id_tf.setText("");
				firstName_tf.setText("");
				lastName_tf.setText("");
				mi_tf.setText("");
				address_tf.setText("");
				city_tf.setText("");
				state_tf.setText("");
				telephone_tf.setText("");
				email_tf.setText("");
			} catch (Exception ex) {

			}
		});
	}

}