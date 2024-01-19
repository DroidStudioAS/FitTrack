package com.aa.fittracker.presentation;

import android.content.Context;
import android.media.MediaPlayer;

import com.aa.fittracker.R;

public class SfxHelper {
    public static void playBloop(Context context){
        MediaPlayer mp = MediaPlayer.create(context, R.raw.bloop);
        if(mp.isPlaying()){
            mp.stop();
        }
        mp.start();
    }
}
