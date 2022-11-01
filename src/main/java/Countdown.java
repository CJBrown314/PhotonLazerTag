import Entities.Clock;
import javafx.beans.property.ReadOnlyStringProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.concurrent.Task;

public class Countdown extends Task<Integer> {
    private final Clock COUNTDOWN;

    public Countdown(int minute, int second) {
        this.COUNTDOWN = new Clock(minute, second);
        updateMessage(minute + ":" + second);
    }

    /** The countdown logic. The timer can be started by running the start() function.*/
    @Override
    public Integer call() {
        Clock currentCount = COUNTDOWN;
        updateMessage(String.valueOf(currentCount.getMessage()));
        try {

            while (!currentCount.decrement().equals("0:0")) {
                Thread.sleep(1000);
                updateMessage(String.valueOf(currentCount.getMessage()));
            }
        } catch (InterruptedException e) {
            currentCount.stopClock();
        }

        return 0;
    }

    /** Returns the count property used to bind the counter to a labels text property. */
    public ReadOnlyStringProperty getCountProperty(){
        return messageProperty();
    }
}
