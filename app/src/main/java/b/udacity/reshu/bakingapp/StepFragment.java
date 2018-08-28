package b.udacity.reshu.bakingapp;


import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.ui.PlayerView;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.util.Util;

import b.udacity.reshu.bakingapp.model.Steps;
import butterknife.BindView;


/**
 * A simple {@link Fragment} subclass.
 */
public class StepFragment extends Fragment {

    public static final String ITEM_ID = "item_id";
    private static final String TAG = "StepDetail";

    private PlayerView playerView;
    private TextView mStepDetail;
    private SimpleExoPlayer exoPlayer;
    Steps step;
    String description;


    public StepFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_step, container, false);

        playerView = (PlayerView)view.findViewById(R.id.player_view);
        mStepDetail = (TextView)view.findViewById(R.id.step_detail);
        mStepDetail.setText("Step Instructions here");

        step = new Steps();

        if (getArguments().containsKey(ITEM_ID)) {
            step = getArguments().getParcelable(ITEM_ID);
            description = getArguments().getString("description");
            Log.e("DESCRIPTION",":::::::"+step.getDescription());
            mStepDetail.setText(step.getDescription());
        }
        if (step.getVideoURL().length() > 0) {
            playerView.setVisibility(View.VISIBLE);
            exoPlayer = ExoPlayerFactory.newSimpleInstance(getContext(), new DefaultTrackSelector());
            playerView.setPlayer(exoPlayer);
            DefaultDataSourceFactory dataSourceFactory = new DefaultDataSourceFactory(getContext(), Util.getUserAgent(getContext(), "exo-demo"));
            ExtractorMediaSource mediaSource = new ExtractorMediaSource.Factory(dataSourceFactory).createMediaSource(Uri.parse(step.getVideoURL()));
            exoPlayer.prepare(mediaSource);
            exoPlayer.setPlayWhenReady(true);

        } else {
            playerView.setVisibility(View.GONE);
            mStepDetail.append("Video tutorial for this step will be uploaded soon. Check again after you are done with the cooking and the eating part as well :) .");
        }

        if (savedInstanceState != null && exoPlayer != null) {
            exoPlayer.seekTo(savedInstanceState.getLong("current_position"));
            exoPlayer.setPlayWhenReady(savedInstanceState.getBoolean("play_state"));
        }
        return view;
    }


    @Override
    public void onStart() {
        super.onStart();

    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        if (exoPlayer != null) {
            outState.putLong("current_position", exoPlayer.getCurrentPosition());
            outState.putBoolean("play_state", exoPlayer.getPlayWhenReady());
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        Log.i(TAG, "onStop:called ");
        playerView.setPlayer(null);
        if (exoPlayer != null)
            exoPlayer.release();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.i(TAG, "onDestroy:called ");
        playerView.setPlayer(null);

    }

}
