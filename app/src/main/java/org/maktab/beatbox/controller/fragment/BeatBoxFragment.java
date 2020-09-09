package org.maktab.beatbox.controller.fragment;

import android.content.res.AssetFileDescriptor;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.SeekBar;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.maktab.beatbox.R;
import org.maktab.beatbox.model.Sound;
import org.maktab.beatbox.repository.BeatBoxRepository;
import org.maktab.beatbox.repository.MusicRepository;

import java.util.List;

public class BeatBoxFragment extends Fragment {

    public static final String TAG = "BBF";
    private RecyclerView mRecyclerView;
    private BeatBoxRepository mRepository;
    private SeekBar mMusicSeeker;
    private MusicRepository mMusicRepository;
    private MediaPlayer mPlayer;

    public BeatBoxFragment() {
        // Required empty public constructor
    }

    public static BeatBoxFragment newInstance() {
        BeatBoxFragment fragment = new BeatBoxFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCrete");

        setRetainInstance(true);
        mRepository = BeatBoxRepository.getInstance(getContext());
        mMusicRepository = MusicRepository.getInstance(getContext());
        mPlayer = mMusicRepository.getMediaPlayer();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_beat_box, container, false);

        Log.d(TAG, "onCreteView");
        findViews(view);
        initViews();
        mMusicSeeker.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                mPlayer.seekTo(progress*1000);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();

        Log.d(TAG, "onDestroyView");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onDestroy");

        mRepository.getSoundPool().release();
    }

    private void findViews(View view) {
        mRecyclerView = view.findViewById(R.id.recycler_view_beat_box);
        mMusicSeeker = view.findViewById(R.id.music_seek);
    }

    private void initViews() {
        mRecyclerView.setLayoutManager(new GridLayoutManager(getContext(),
                getResources().getInteger(R.integer.number_of_grid_cell)));
        initUI();
    }

    private void initUI() {
        List<Sound> sounds = mMusicRepository.getMusics();
        SoundAdapter adapter = new SoundAdapter(sounds);
        mRecyclerView.setAdapter(adapter);
    }

    private class SoundHolder extends RecyclerView.ViewHolder {

        private Button mButtonSound;
        private Sound mSound;

        public SoundHolder(@NonNull View itemView) {
            super(itemView);

            mButtonSound = itemView.findViewById(R.id.list_item_button_sound);
            mButtonSound.setOnClickListener(new View.OnClickListener() {
                @RequiresApi(api = Build.VERSION_CODES.N)
                @Override
                public void onClick(View v) {
                    try {
                        mPlayer.reset();
                        if (mPlayer.isPlaying()) {
                            mPlayer.stop();
                        }
                        AssetFileDescriptor afd = getActivity().getAssets().openFd(mSound.getAssetPath());
                        mPlayer.setDataSource(afd);
                        mPlayer.prepare();
                        mPlayer.start();
                        setSeekBar();
                    } catch (Exception e) {
                        Log.e(BeatBoxRepository.TAG, e.getMessage(), e);
                    }
                }
            });
        }

        public void bindSound(Sound sound) {
            mSound = sound;
            mButtonSound.setText(mSound.getName());
        }
    }

    private void setSeekBar() {
        mMusicSeeker.setProgress(0);
        int currentPosition = mPlayer.getCurrentPosition();
        int total = mPlayer.getDuration();
        mMusicSeeker.setMax(total / 1000);
        final Handler mHandler = new Handler();
//Make sure you update Seekbar on UI thread
        getActivity().runOnUiThread(new Runnable() {

            @Override
            public void run() {
                if (mPlayer != null) {
                    int mCurrentPosition = mPlayer.getCurrentPosition() / 1000;
                    mMusicSeeker.setProgress(mCurrentPosition);
                }
                mHandler.postDelayed(this, 1000);
            }
        });
    }

    private class SoundAdapter extends RecyclerView.Adapter<SoundHolder> {

        private List<Sound> mSounds;

        public List<Sound> getSounds() {
            return mSounds;
        }

        public void setSounds(List<Sound> sounds) {
            mSounds = sounds;
        }

        public SoundAdapter(List<Sound> sounds) {
            mSounds = sounds;
        }

        @NonNull
        @Override
        public SoundHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(getActivity())
                    .inflate(R.layout.list_item_sound, parent, false);

            return new SoundHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull SoundHolder holder, int position) {
            Sound sound = mSounds.get(position);
            holder.bindSound(sound);
        }

        @Override
        public int getItemCount() {
            return mSounds.size();
        }
    }
}