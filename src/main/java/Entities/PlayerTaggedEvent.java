package Entities;

public class PlayerTaggedEvent {
    private final int TRANSMIT_ID;
    private final int HIT_ID;

    public PlayerTaggedEvent(int transmitID, int hitID) {
        TRANSMIT_ID = transmitID;
        HIT_ID = hitID;
    }

    public int getHitID(){
        return HIT_ID;
    }

    public int getTransmitID() {
        return TRANSMIT_ID;
    }

    @Override
    public String toString() {
        return "PlayerTaggedEvent{" +
                "TRANSMIT_ID=" + TRANSMIT_ID +
                ", HIT_ID=" + HIT_ID +
                '}';
    }
}
