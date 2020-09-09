package org.maktab.beatbox.repository;

import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.content.res.AssetManager;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Build;
import android.util.Log;

import androidx.annotation.RequiresApi;

import org.maktab.beatbox.model.Sound;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * This is not a repository class. it only mimic the repository behaviour.
 */
public class BeatBoxRepository {

    private static final String ASSET_SOUND_FOLDER = "sample_sounds";
    public static final String TAG = "BeatBox";
    public static final int MAX_STREAMS = 5;
    public static final int SOUND_PRIORITY = 1;
    private static BeatBoxRepository sInstance;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public static BeatBoxRepository getInstance(Context context) {
        if (sInstance == null)
            sInstance = new BeatBoxRepository(context);

        return sInstance;
    }

    private Context mContext;
    private AssetManager mAssetManager;
    private List<Sound> mSounds;
    private SoundPool mSoundPool;

    public static void setInstance(BeatBoxRepository instance) {
        sInstance = instance;
    }

    public List<Sound> getSounds() {
        return mSounds;
    }

    public void setSounds(List<Sound> sounds) {
        mSounds = sounds;
    }

    public SoundPool getSoundPool() {
        return mSoundPool;
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private BeatBoxRepository(Context context) {
        mContext = context.getApplicationContext();
        mAssetManager = mContext.getAssets();
        SoundPool.Builder builder = new SoundPool.Builder();
        AudioAttributes.Builder audioAttributesBuilder = new AudioAttributes.Builder();
        AudioAttributes audioAttributes = audioAttributesBuilder.setUsage(AudioAttributes.USAGE_GAME).
                setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                .build();
        mSoundPool =builder.setMaxStreams(MAX_STREAMS).
                setAudioAttributes(audioAttributes).build() ;
             /*   new SoundPool(MAX_STREAMS, AudioManager.STREAM_MUSIC, 0);*/

        mSounds = new ArrayList<>();
        try {
            String[] fileNames = mAssetManager.list(ASSET_SOUND_FOLDER);
            for (String fileName: fileNames) {
                Log.d(TAG, fileName);
                String assetPath = ASSET_SOUND_FOLDER + File.separator + fileName;

                Sound sound = new Sound(assetPath);

                AssetFileDescriptor afd = mAssetManager.openFd(assetPath);
                int soundId = mSoundPool.load(afd, SOUND_PRIORITY);
                sound.setSoundId(soundId);

                mSounds.add(sound);
            }
        } catch (IOException e) {
            Log.e(TAG, e.getMessage(), e);
        }
    }
}
