package com.fenghuang.caipiaobao.utils;

import android.app.Activity;
import android.content.Context;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Build;

/**
 * @ Author  QinTian
 * @ Date  2020/5/7
 * @ Describe
 */
public class SoundPoolHelper {
    private SoundPool mainSoundPool;
    private AudioManager mainAudioManager;
    private float volume;
    // Maximumn sound stream.
    private static final int MAX_STREAMS = 5;
    // Stream type.
    private static final int streamType = AudioManager.STREAM_MUSIC;
    private int soundId;
    private int resId;
    private Context mainContext;
    public SoundPoolHelper(Context context){
        this.mainContext=context;
    }

    //鎾斁
    public void playSoundWithRedId(int resId){
        this.resId=resId;
        init();
    }

    //init settings
    private void init(){
        // AudioManager audio settings for adjusting the volume
        mainAudioManager = (AudioManager)this.mainContext. getSystemService(Context.AUDIO_SERVICE);

        // Current volumn Index of particular stream type.
        float currentVolumeIndex = (float) mainAudioManager.getStreamVolume(streamType);

        // Get the maximum volume index for a particular stream type.
        float maxVolumeIndex  = (float) mainAudioManager.getStreamMaxVolume(streamType);

        // Volumn (0 --> 1)
        this.volume = currentVolumeIndex / maxVolumeIndex;

        // Suggests an audio stream whose volume should be changed by
        // the hardware volume controls.
        ((Activity)this.mainContext).setVolumeControlStream(streamType);

        // For Android SDK >= 21
        if (Build.VERSION.SDK_INT >= 21 ) {

            AudioAttributes audioAttrib = new AudioAttributes.Builder()
                    .setUsage(AudioAttributes.USAGE_GAME)
                    .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                    .build();

            SoundPool.Builder builder= new SoundPool.Builder();
            builder.setAudioAttributes(audioAttrib).setMaxStreams(MAX_STREAMS);

            this.mainSoundPool = builder.build();
        }
        // for Android SDK < 21
        else {
            // SoundPool(int maxStreams, int streamType, int srcQuality)
            this.mainSoundPool = new SoundPool(MAX_STREAMS, AudioManager.STREAM_MUSIC, 0);
        }

        // When Sound Pool load complete.
        this.mainSoundPool.setOnLoadCompleteListener((soundPool, sampleId, status) -> playSound());

        //load res
        this.soundId=this.mainSoundPool.load(this.mainContext,this.resId,1);
    }

    //play the sound res
    private void playSound(){
        float leftVolumn = volume;
        float rightVolumn = volume;
        // Play sound of gunfire. Returns the ID of the new stream.
        int streamId = this.mainSoundPool.play(this.soundId,leftVolumn, rightVolumn, 1, 0, 1f);
    }

}
