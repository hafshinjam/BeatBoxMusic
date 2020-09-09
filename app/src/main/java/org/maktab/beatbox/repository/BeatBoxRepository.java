package org.maktab.beatbox.repository;

import android.content.Context;
import android.content.res.AssetManager;
import android.util.Log;

import org.maktab.beatbox.model.Sound;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class BeatBoxRepository {

    private static final String ASSET_SOUND_FOLDER = "sample_sounds";
    public static final String TAG = "BeatBox";
    private static BeatBoxRepository sInstance;

    public static BeatBoxRepository getInstance(Context context) {
        if (sInstance == null)
            sInstance = new BeatBoxRepository(context);

        return sInstance;
    }

    private Context mContext;
    private AssetManager mAssetManager;
    private List<Sound> mSounds;

    public static void setInstance(BeatBoxRepository instance) {
        sInstance = instance;
    }

    public List<Sound> getSounds() {
        return mSounds;
    }

    public void setSounds(List<Sound> sounds) {
        mSounds = sounds;
    }

    private BeatBoxRepository(Context context) {
        mContext = context.getApplicationContext();
        mAssetManager = mContext.getAssets();

        mSounds = new ArrayList<>();
        try {
            String[] fileNames = mAssetManager.list(ASSET_SOUND_FOLDER);
            for (String fileName: fileNames) {
                Log.d(TAG, fileName);
                String assetPath = ASSET_SOUND_FOLDER + File.separator + fileName;

                Sound sound = new Sound(assetPath);
                mSounds.add(sound);
            }
        } catch (IOException e) {
            Log.e(TAG, e.getMessage(), e);
        }
    }
}
