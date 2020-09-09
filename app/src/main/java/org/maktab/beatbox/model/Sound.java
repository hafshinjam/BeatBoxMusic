package org.maktab.beatbox.model;

public class Sound {

    private String mName;
    private String mAssetPath;

    public String getName() {
        return mName;
    }

    public void setName(String name) {
        mName = name;
    }

    public String getAssetPath() {
        return mAssetPath;
    }

    public void setAssetPath(String assetPath) {
        mAssetPath = assetPath;
    }

    public Sound(String name, String assetPath) {
        mName = name;
        mAssetPath = assetPath;
    }
}
