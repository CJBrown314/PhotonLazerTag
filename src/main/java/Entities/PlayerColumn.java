package Entities;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;


public class PlayerColumn {
    private final StringProperty leftPlayerID;
    private final StringProperty rightPlayerID;

    public PlayerColumn() {
        leftPlayerID = new SimpleStringProperty("");
        rightPlayerID = new SimpleStringProperty("");
    }

    public PlayerColumn(String lpid, String rpid) {
        leftPlayerID = new SimpleStringProperty(lpid);
        rightPlayerID = new SimpleStringProperty(rpid);
    }

    public String getLeftPlayerID() {
        return leftPlayerID.get();
    }

    public String getRightPlayerID() {
        return rightPlayerID.get();
    }

    public void setLeftPlayerID(String pid) {
        leftPlayerID.set(pid);
    }

    public void setRightPlayerID(String pid) {
        rightPlayerID.set(pid);
    }

    public final StringProperty playeridpProperty()
    {
        return leftPlayerID;
    }
}
