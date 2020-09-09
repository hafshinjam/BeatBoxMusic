package org.maktab.beatbox.repository;

import org.maktab.beatbox.model.Sound;

import java.util.ArrayList;
import java.util.List;

public class BeatBoxRepository {

    private static BeatBoxRepository sInstance;

    public static BeatBoxRepository getInstance() {
        if (sInstance == null)
            sInstance = new BeatBoxRepository();

        return sInstance;
    }

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

    private BeatBoxRepository() {
        //TODO: read data from asset and fill the sounds list.
        mSounds = new ArrayList<>();
    }
}
