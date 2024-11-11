package io.github.eng1g4;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.utils.Disposable;

public class SoundManager implements Disposable {

    private final Sound buildingPlacementSound;
    private final Music backgroundMusic;

    private float soundEffectVolume;
    private float musicVolume;

    public SoundManager(){

        buildingPlacementSound = Gdx.audio.newSound(Gdx.files.internal("sounds/building_place.mp3"));
        backgroundMusic = Gdx.audio.newMusic(Gdx.files.internal("sounds/background_music.mp3"));
        backgroundMusic.setLooping(true);

        soundEffectVolume = 0.5f;
        musicVolume = 0.5f;

        setMusicVolume(musicVolume);
    }

    public void playBuildingPlacementSound(){
        buildingPlacementSound.play(soundEffectVolume);
    }

    public void playBackgroundMusic(){
        backgroundMusic.play();
    }

    public void pauseBackgroundMusic(){
        if (backgroundMusic.isPlaying()) {
            backgroundMusic.pause();
        }
    }

    public void setMusicVolume(float volume){
        musicVolume = volume;
        backgroundMusic.setVolume(volume);
    }

    public void setSoundEffectVolume(float volume){
        soundEffectVolume = volume;
    }

    public float getSoundEffectVolume(){
        return soundEffectVolume;
    }

    public float getMusicVolume(){
        return musicVolume;
    }

    public void dispose(){
        buildingPlacementSound.dispose();
        backgroundMusic.dispose();
    }
}
