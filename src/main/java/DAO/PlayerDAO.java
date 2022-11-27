package DAO;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class PlayerDAO {
    private static PlayerDAO playerDAOEntity;
    private Connection databaseConnection;

    // private constructor accessed by the getDAO function.
    private PlayerDAO() {
        createConnection();
    }

    /** Returns the DAO singleton. */
    public static PlayerDAO getDAO() {
        if (playerDAOEntity == null) {
            // Creates a new playerDAO object if none exists
            playerDAOEntity = new PlayerDAO();
        }
        // Returns existing playerDAO object
        return playerDAOEntity;
    }

    // Establishes the connection to the database.
    private void createConnection() {
        try {
            databaseConnection = DriverManager.getConnection("jdbc:postgresql://ec2-54-167-186-198.compute-1.amazonaws.com/dft3j59ofknctu?password=ca9dd4b2c75d650ec6feaa7d00fdc21ed253cf047fe2a61e4112a24ba9b152a9&sslmode=require&user=nvxbgefwqaulsc");
            System.out.println("Database connection established successfully!");
        } catch(Exception e){
            System.err.println("Failed to establish database connection: " + e);
            System.exit(-1);
        }
    }

    /** Saves a player with the given id and codename to the database.
     * @return A boolean indicating whether or not the save was successful.
     * @param id The id of the player to be saved.
     * @param codename The codename of the player being saved. */
    public boolean savePlayer(int id, String codename) {
        try {
            String insertString = "INSERT INTO player VALUES(?, NULL, NULL, ?);";
            PreparedStatement insertStatement = databaseConnection.prepareStatement(insertString);
            insertStatement.setInt(1, id);
            insertStatement.setString(2, codename);
            insertStatement.executeUpdate();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /** Checks if a player with the given id exists in the database.
     * @return A boolean indicating whether or not the player exists.
     * @param id The id to check for. */
    public boolean playerExists(int id) {
        try {
            String existsString = "SELECT codename FROM player WHERE id = ?";
            PreparedStatement existsStatement = databaseConnection.prepareStatement(existsString);
            existsStatement.setInt(1, id);
            ResultSet queryResult = existsStatement.executeQuery();
            return queryResult.next();
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /** Returns the player name for a given id
     * @return A string for the player name of the given id
     * @param id The id to check for. */
    public String retrievePlayerName(int id) {
        try {
            String existsString = "SELECT codename FROM player WHERE id = ?";
            PreparedStatement existsStatement = databaseConnection.prepareStatement(existsString);
            existsStatement.setInt(1, id);
            ResultSet queryResult = existsStatement.executeQuery();
            queryResult.next();
            return queryResult.getString(1);
        } catch (Exception e) {
            e.printStackTrace();
            return "Invalid ID for ID" + id;
        }
    }
}
