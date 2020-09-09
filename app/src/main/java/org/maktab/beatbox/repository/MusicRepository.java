package org.maktab.beatbox.repository;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.content.res.AssetManager;
import android.media.AudioAttributes;
import android.media.MediaPlayer;
import android.os.Build;
import android.util.Log;

import androidx.annotation.RequiresApi;

import org.maktab.beatbox.model.Sound;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static android.content.ContentValues.TAG;

public class MusicRepository {
    private static MusicRepository sMusicRepository;
    private final String MUSIC_FOLDER_PATH = "musics";
    private Context mContext;
    private List<Sound> mMusics;
    private static MediaPlayer mediaPlayer;
    private AssetManager mAssetManager;

    public static MediaPlayer getMediaPlayer() {
        return mediaPlayer;
    }

    public List<Sound> getMusics() {
        return mMusics;
    }

    public void setMusics(List<Sound> musics) {
        mMusics = musics;
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public static MusicRepository getInstance(Context context) {
        if (sMusicRepository == null) {
            sMusicRepository = new MusicRepository(context);
        }
        return sMusicRepository;
    }

    @SuppressLint("NewApi")
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public MusicRepository(Context context) {
        mContext = context.getApplicationContext();
        mAssetManager = context.getAssets();
        mediaPlayer = new MediaPlayer();
        mediaPlayer.setAudioAttributes(new AudioAttributes.Builder().setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                .setUsage(AudioAttributes.USAGE_MEDIA).build());
        mMusics = new ArrayList<>();
        try {
            String[] list = mAssetManager.list(MUSIC_FOLDER_PATH);
            for (String fileName : list) {
                String assetPath = MUSIC_FOLDER_PATH + File.separator + fileName;
                Sound sound = new Sound(assetPath);
                AssetFileDescriptor afd = mAssetManager.openFd(assetPath);

                mMusics.add(sound);

            }
        } catch (IOException e) {
            Log.e(TAG, "MusicRepository: " + e.getMessage(), e.getCause());
        }
    }
}
