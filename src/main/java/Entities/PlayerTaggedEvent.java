package Entities;

import DAO.PlayerDAO;

public class PlayerTaggedEvent {
    private final int TRANSMIT_ID;
    private final int HIT_ID;
    private final String TRANSMIT_PLAYER;
    private final String HIT_PLAYER;

    public PlayerTaggedEvent(int transmitID, int hitID) {
        TRANSMIT_ID = transmitID;
        HIT_ID = hitID;
        TRANSMIT_PLAYER = PlayerDAO.getDAO().retrievePlayerName(TRANSMIT_ID);
        HIT_PLAYER = PlayerDAO.getDAO().retrievePlayerName(HIT_ID);
    }

    public int getHitID(){
        return HIT_ID;
    }

    public int getTransmitID() {
        return TRANSMIT_ID;
    }

    public String getTransmitPlayer() {
        return TRANSMIT_PLAYER;
    }
    
    public String getHitPlayer() {
        return HIT_PLAYER;
    }

    @Override
    public String toString() {
        return TRANSMIT_PLAYER + " hit " + HIT_PLAYER;
    }
}
