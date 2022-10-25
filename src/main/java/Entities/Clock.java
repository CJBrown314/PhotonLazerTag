package Entities;

public class Clock {
    private int minutes;
    private int seconds;

    public Clock(int minutes, int seconds) {
        this.minutes = minutes;
        this.seconds = seconds;
    }

    public String decrement() {
        if (seconds == 0 && minutes > 0) {
            seconds = 59;
            minutes--;
        } else if (seconds > 0) {
            seconds--;
        }

        return getMessage();
    }

    public String getMessage () {
        String result;
        if (seconds < 10) {
            result = minutes + ":0" + seconds;
        } else {
            result = minutes + ":" + seconds;
        }
        return result;
    }

    public void stopClock () {
        minutes = 0;
        seconds = 0;
    }
}
