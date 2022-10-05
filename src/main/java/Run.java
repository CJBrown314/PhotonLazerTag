import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import java.net.URI;
import java.net.URISyntaxException;
import java.sql.*;

public class Run extends Application {

    //Creates the Java application screen
    @Override
    public void start(Stage stage) {
        Scene scene = new Scene(new StackPane(), 640, 480);
        stage.setScene(scene);
        stage.show();
    } 

    //Connects to the postgresql database
    private static Connection getConnection() throws URISyntaxException, SQLException {
        return DriverManager.getConnection("jdbc:postgresql://ec2-54-167-186-198.compute-1.amazonaws.com:5432/dft3j59ofknctu?password=ca9dd4b2c75d650ec6feaa7d00fdc21ed253cf047fe2a61e4112a24ba9b152a9&sslmode=require&user=nvxbgefwqaulsc");
    }

    public static void main(String[] args) throws Exception {
        //Attempt a connection, and abort if it fails 
        Connection connection;
        try {
            connection = getConnection();
            System.out.println("DB connection successful");
        }
        catch (Exception e) {
            System.out.println("DB connection failed");
            return;
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

        //Launch the Java application
        launch();
    }
}
