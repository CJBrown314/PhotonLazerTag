import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import java.net.URI;
import java.net.URISyntaxException;
import java.sql.*;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.scene.control.*;
import javafx.scene.control.TableColumn.CellEditEvent;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.layout.*;
import javafx.scene.image.*;
import java.io.*;
import javafx.geometry.*;
import javafx.scene.paint.*;

public class Run extends Application {

    private TableView table = new TableView();
	 private final ObservableList<Player> data =FXCollections.observableArrayList();
	 private final int TABLE_SIZE = 16; // total number of table fields
	 @Override
     public void start(Stage stage) {
		 try
		 {
			// set title for the stage
            stage.setTitle("Photon LazerTag");
            //Create root borderpane layout
            BorderPane root = new BorderPane();
            // create a vertical box layout to put in the center pane of the borderpane layout
            VBox centerPane = new VBox();
            // set the alignment of the center pane vbox layout to center all items
            centerPane.setAlignment(Pos.CENTER);
            // Add space between items in the vbox
            centerPane.setSpacing(10);


            // create a scene with the borderpane as the parent
            Scene scene = new Scene(root, 1366, 768);

            // create a label and add it to the center pane vbox
            Label label = new Label("Add Players to the Table: ");
            label.setTextFill(Color.BLACK);
            centerPane.getChildren().add(label);
  
            // create a button and add it to the center pane vbox
            Button button = new Button("Start Game");
            centerPane.getChildren().add(button);
            
            //create input stream for image
            FileInputStream input = new FileInputStream("C:\\Users\\deess\\OneDrive - University of Arkansas\\Software engineering\\LaserTag.png");

            // create the image
            Image image = new Image(input);
  
            // Creates the background size in such a way that the image fits the screen
            BackgroundSize size = new BackgroundSize(image.getWidth(), image.getHeight(), false, false, false, true);
            // Creates a background image from the fetched image
            BackgroundImage backgroundimage = new BackgroundImage(image, BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.CENTER, size);

            // create Background
            Background background = new Background(backgroundimage);
  
            // set the background of the borderpane
            root.setBackground(background);
            
            TableColumn<Player, String> greenteamplayers = new TableColumn("Green Team Players");
            greenteamplayers.setMinWidth(100);
            greenteamplayers.setCellValueFactory(
                new PropertyValueFactory<Player, String>("playerid"));
            greenteamplayers.setCellFactory(TextFieldTableCell.forTableColumn());
            greenteamplayers.setOnEditCommit(
                new EventHandler<CellEditEvent<Player, String>>() {
                	@Override
                    public void handle(CellEditEvent<Player, String> t) {
                        ((Player) t.getTableView().getItems().get(
                                t.getTablePosition().getRow())
                                ).setPlayerID(t.getNewValue());
                    }
                }
            );
            TableColumn<Player, String> blueteamplayers = new TableColumn("Blue Team Players");
            blueteamplayers.setMinWidth(100);
            blueteamplayers.setCellValueFactory(
                new PropertyValueFactory<Player, String>("playerid"));
            blueteamplayers.setCellFactory(TextFieldTableCell.forTableColumn());
            blueteamplayers.setOnEditCommit(
                new EventHandler<CellEditEvent<Player, String>>() {
                	@Override
                    public void handle(CellEditEvent<Player, String> t) {
                        ((Player) t.getTableView().getItems().get(
                                t.getTablePosition().getRow())
                                ).setPlayerID(t.getNewValue());
                    }
                }
            );
            // create columns
            //TableColumn<Player, String> greenteamplayers = new TableColumn<Player, String>("Green Team Players");
            greenteamplayers.setPrefWidth(root.getWidth()/4);
            //greenteamplayers.setCellValueFactory(cellData -> cellData.getValue().getPlayerID());
            
            //TableColumn<Player, String> blueteamplayers = new TableColumn<Player, String>("Blue Team Players");
            blueteamplayers.setPrefWidth(root.getWidth()/4);
            //blueteamplayers.setCellValueFactory(cellData -> cellData.getValue().getPlayerID());

            for(int i = 0; i < TABLE_SIZE; i++)
            {
            	data.add(new Player());
            }
            
            table.setItems(data);
            
            // set table properties
            table.getColumns().addAll(greenteamplayers, blueteamplayers);
            //table.getItems().add(new TextField());
            table.setEditable(true);
            table.setMaxHeight(root.getHeight()/2);
            table.setMaxWidth(root.getWidth()/2);
            centerPane.getChildren().add(table);
            
            

            root.setCenter(centerPane);

            // set the scene
            stage.setScene(scene);
  
            stage.show();
		 }
		 catch (Exception e) {
			 System.out.println(e.getMessage());
		 }
	 }
	 
	 public static class Player {
	 	private final SimpleStringProperty playerid;
	 	
	 	Player() {
	 		playerid = new SimpleStringProperty("");
	 	}
	 	
	 	Player(String pid) {
	 		playerid = new SimpleStringProperty(pid);
	 	}
	 	
	 	public SimpleStringProperty getPlayerID() {
	 		return playerid;
	 	}
	 	
	 	public void setPlayerID(String pid) {
	 		playerid.set(pid);
	 	}
	 }

    //Connects to the postgresql database
    private static Connection getConnection() throws URISyntaxException, SQLException {
        return DriverManager.getConnection("jdbc:postgresql://ec2-54-167-186-198.compute-1.amazonaws.com:5432/dft3j59ofknctu?password=ca9dd4b2c75d650ec6feaa7d00fdc21ed253cf047fe2a61e4112a24ba9b152a9&sslmode=require&user=nvxbgefwqaulsc");
    }

    public ResultSet queryDatabase() throws Exception {
        //Attempt a connection, and abort if it fails 
        Connection connection;
        try {
            connection = getConnection();
            System.out.println("DB connection successful");
        }
        catch (Exception e) {
            System.out.println("DB connection failed");
            return null;
        }

        //Statements are an interface representing SQL statements
        Statement stmt = connection.createStatement();

        //Structure of updating table (Inside quotes, provide the SQL update statement)
        //stmt.executeUpdate("");

        //ResultSets are a table of data storing a result set
        //Below is an example of querying the whole player table
        ResultSet resultSet = stmt.executeQuery("SELECT * FROM player");

        //ResultSetMetaData stores info about table metadata (like column info)
        ResultSetMetaData rsmd = resultSet.getMetaData();
        int columnsNumber = rsmd.getColumnCount();

        //Iterate through and print items of table
        while (resultSet.next()) {
            for (int i = 1; i <= columnsNumber; i++) {
                if (i > 1) System.out.print(",  ");
                String columnValue = resultSet.getString(i);
                System.out.print(columnValue + " " + rsmd.getColumnName(i));
            }
            System.out.println("");
        }

        return resultSet;
    }

    public static void main(String[] args) throws Exception {
        //Launch the Java application
        launch();
    }
}
