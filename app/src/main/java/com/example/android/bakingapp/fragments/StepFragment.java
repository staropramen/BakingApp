package com.example.android.bakingapp.fragments;


import android.databinding.DataBindingUtil;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.android.bakingapp.R;
import com.example.android.bakingapp.databinding.FragmentStepBinding;
import com.example.android.bakingapp.model.Step;
import com.google.android.exoplayer2.DefaultLoadControl;
import com.google.android.exoplayer2.ExoPlaybackException;
import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.LoadControl;
import com.google.android.exoplayer2.PlaybackParameters;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.Timeline;
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.TrackGroupArray;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelectionArray;
import com.google.android.exoplayer2.trackselection.TrackSelector;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.util.Util;
import com.squareup.picasso.Picasso;

import java.net.URLConnection;


/**
 * A simple {@link Fragment} subclass.
 */
public class StepFragment extends Fragment implements ExoPlayer.EventListener {

    private static String TAG = StepFragment.class.getSimpleName();
    private View rootView;
    private FragmentStepBinding mBinding;
    private String STEP_KEY = "step-key";
    private SimpleExoPlayer exoPlayer;
    private String EXO_VIDEO_PLAYER = "exoVideoPlayer";

    //Boolean to check if the Player is initialized, default = false
    private boolean playerIsInitialized = false;

    public StepFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        //Data binding
        mBinding = DataBindingUtil.inflate(
                inflater, R.layout.fragment_step, container, false);
        rootView = mBinding.getRoot();

        //Get the Step
        Bundle data = getArguments();
        Step step = (Step) data.getSerializable(STEP_KEY);

        //Set the title
        Toolbar toolbar = (Toolbar) getActivity().findViewById(R.id.toolbar);
        toolbar.setTitle(getStepTitle(step));

        //Initialize Exoplayer if there is a media source
        if(!TextUtils.isEmpty(step.getVideoURL())){
            Uri mediaUri = Uri.parse(step.getVideoURL());
            initializePlayer(mediaUri);
        } else {
            mBinding.playerView.setVisibility(View.GONE);
            //Load thumbnail instead of video
            mBinding.ivThumbnail.setVisibility(View.VISIBLE);
            if(!TextUtils.isEmpty(step.getThumbnailURL()) && isImageFile(step.getThumbnailURL())){
                Uri thumbnailUri = Uri.parse(step.getThumbnailURL());
                Picasso.get().load(thumbnailUri).into(mBinding.ivThumbnail);
            } else if(isVideoFile(step.getThumbnailURL())) {
                mBinding.playerView.setVisibility(View.VISIBLE);
                mBinding.ivThumbnail.setVisibility(View.GONE);
                Uri mediaUri = Uri.parse(step.getThumbnailURL());
                initializePlayer(mediaUri);
            }else{
                Picasso.get().load(R.drawable.default_thumbnail).into(mBinding.ivThumbnail);
            }
        }


        return rootView;
    }

    //Release ExoPlayer if Fragment is destroyed
    @Override
    public void onDestroy() {
        super.onDestroy();
        if(playerIsInitialized){
            releasePlayer();
        }
    }

    //Build the Title for Step
    private String getStepTitle(Step step){
        int stepNumber = step.getStepId();
        String stepNumberString;
        if(stepNumber == 0){
            stepNumberString = getString(R.string.intro);
        } else {
            stepNumberString = getString(R.string.step) + " " + String.valueOf(stepNumber);
        }
        return stepNumberString;
    }

    //Initialize Exo Player Method
    private void initializePlayer(Uri mediaUri){
        if(exoPlayer == null){
            playerIsInitialized = true;
            TrackSelector trackSelector = new DefaultTrackSelector();
            LoadControl loadControl = new DefaultLoadControl();
            exoPlayer = ExoPlayerFactory.newSimpleInstance(getActivity(), trackSelector, loadControl);
            mBinding.playerView.setPlayer(exoPlayer);
            String userAgent = Util.getUserAgent(getActivity(), EXO_VIDEO_PLAYER);
            MediaSource mediaSource = new ExtractorMediaSource(mediaUri, new DefaultDataSourceFactory(
                    getActivity(), userAgent), new DefaultExtractorsFactory(), null, null);
            exoPlayer.prepare(mediaSource);
            exoPlayer.setPlayWhenReady(true);
        }

    }

    //Relese Media Player
    private void releasePlayer(){
        exoPlayer.stop();
        exoPlayer.release();
        exoPlayer = null;
    }

    //Check type of file in thumbnail
    public static boolean isVideoFile(String path) {
        String mimeType = URLConnection.guessContentTypeFromName(path);
        return mimeType != null && mimeType.startsWith("video");
    }
    public static boolean isImageFile(String path) {
        String mimeType = URLConnection.guessContentTypeFromName(path);
        return mimeType != null && mimeType.startsWith("image");
    }

    @Override
    public void onTimelineChanged(Timeline timeline, Object manifest) {

    }

    @Override
    public void onTracksChanged(TrackGroupArray trackGroups, TrackSelectionArray trackSelections) {

    }

    @Override
    public void onLoadingChanged(boolean isLoading) {

    }

    @Override
    public void onPlayerStateChanged(boolean playWhenReady, int playbackState) {
        //Hide the View as long is buffering
        if(playbackState == ExoPlayer.STATE_READY){
            mBinding.playerView.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onRepeatModeChanged(int repeatMode) {

    }

    @Override
    public void onShuffleModeEnabledChanged(boolean shuffleModeEnabled) {

    }

    @Override
    public void onPlayerError(ExoPlaybackException error) {

    }

    @Override
    public void onPositionDiscontinuity(int reason) {

    }

    @Override
    public void onPlaybackParametersChanged(PlaybackParameters playbackParameters) {

    }

    @Override
    public void onSeekProcessed() {

    }
}
