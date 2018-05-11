package com.allsopg.game.sound;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.utils.IntMap;

/**
 * Created by Harry on 07/05/2018.
 */

public class SoundLink
{

    private IntMap<Sound> soundKeys; // Creates the IntMap

    public SoundLink()
    {
        soundKeys = new IntMap<Sound>(); // Initializes the IntMap

        Sound whooshSnd = Gdx.audio.newSound(Gdx.files.internal("sfx/Whoosh.ogg")); // Retrieve the following sound
        Sound fireSnd = Gdx.audio.newSound(Gdx.files.internal("sfx/Fire.ogg")); // Retrieve the following sound
        Sound armourSnd = Gdx.audio.newSound(Gdx.files.internal("sfx/Armour.ogg")); // Retrieve the following sound
        Sound bottleSnd = Gdx.audio.newSound(Gdx.files.internal("sfx/Bottle.ogg")); // Retrieve the following sound

        soundKeys.put(1, whooshSnd); // Add the following sound to the IntMap
        soundKeys.put(2, fireSnd); // Add the following sound to the IntMap
        soundKeys.put(3, armourSnd); // Add the following sound to the IntMap
        soundKeys.put(4, bottleSnd); // Add the following sound to the IntMap
    }

    public boolean play(int keyCode)
    {
        Sound sound = soundKeys.get(keyCode); // Retrieves the key code from soundKeys
        if(sound != null) // If the key code is not null
        {
            sound.play(); // Play the sound associated with the key code
            return true; // Return true
        }
        return false; // Else do nothing and return false
    }

    public void dispose()
    {
        for(Sound sound : soundKeys.values()) // For the values of soundKeys...
        {
            sound.dispose(); // Dispose of the sound
        }
    }
}
