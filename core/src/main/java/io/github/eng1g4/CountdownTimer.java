package io.github.eng1g4;

import com.badlogic.gdx.utils.Timer;
import com.badlogic.gdx.utils.Timer.Task;

public class CountdownTimer {

    // Time remaining in the game, in seconds
    private int timeRemaining = 300;
    private Timer.Task countdownTimer;

    public CountdownTimer() { }

    public void start() {
        // Decrement timeRemaining after 1 second, every 1 second, timeRemaining times.
        this.countdownTimer = Timer.schedule(new Task() {
            @Override
            public void run() {
                timeRemaining -= 1;
            }
        }, 1, 1, this.timeRemaining);
    }

    public void stop() {
        this.countdownTimer.cancel();
    }

    public String getTimeRemaining() {
        return "%d:%02d".formatted(this.timeRemaining / 60, this.timeRemaining % 60);
    }

}
