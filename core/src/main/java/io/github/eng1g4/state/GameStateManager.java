package io.github.eng1g4.state;

import io.github.eng1g4.CountdownTimer;

public class GameStateManager {

    private GameState currentState = GameState.PAUSED;
    private final CountdownTimer countdownTimer = new CountdownTimer(this);

    public boolean isGameOver() {
        return this.currentState == GameState.OVER;
    }

    public boolean isGamePaused() {
        return this.currentState == GameState.PAUSED;
    }

    public void togglePaused() {
        if (currentState == GameState.OVER) return;

        if (currentState == GameState.PAUSED) {
            currentState = GameState.PLAYING;
            countdownTimer.start();
        } else {
            currentState = GameState.PAUSED;
            countdownTimer.stop();
        }
    }

    public void setGameOver() {
        this.currentState = GameState.OVER;
    }

    public CountdownTimer getCountdownTimer() {
        return countdownTimer;
    }
}
