package io.github.eng1g4;

import com.badlogic.gdx.utils.Timer;
import com.badlogic.gdx.utils.Timer.Task;

public class CountdownTimer {

    // Time remaining in the game, in seconds
    private int timeRemaining = 300;
    private final Timer.Task countdownTimer;

    public CountdownTimer(Main main) {
        this.countdownTimer = new Task() {
            @Override
            public void run() {
                timeRemaining -= 1;

                if (timeRemaining == 0) {
                    main.endGame();
                }
            }
        };
    }

    public void start() {
        // Decrement timeRemaining after 1 second, every 1 second, timeRemaining times.
        Timer.schedule(this.countdownTimer, 1, 1, this.timeRemaining);
    }

    public void stop() {
        this.countdownTimer.cancel();
    }

    public String getTimeRemaining() {
        return "%d:%02d".formatted(this.timeRemaining / 60, this.timeRemaining % 60);
    }

}
