package com.example.android.bakingapp.fragments;


import android.databinding.DataBindingUtil;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;

import com.example.android.bakingapp.MainActivity;
import com.example.android.bakingapp.R;
import com.example.android.bakingapp.databinding.FragmentStepBinding;
import com.example.android.bakingapp.model.Step;
import com.example.android.bakingapp.utils.DeviceUtils;
import com.google.android.exoplayer2.DefaultLoadControl;
import com.google.android.exoplayer2.ExoPlaybackException;
import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.LoadControl;
import com.google.android.exoplayer2.PlaybackParameters;
import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.Timeline;
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.TrackGroupArray;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelectionArray;
import com.google.android.exoplayer2.trackselection.TrackSelector;
import com.google.android.exoplayer2.ui.AspectRatioFrameLayout;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.util.Util;
import com.squareup.picasso.Picasso;

import java.io.Serializable;
import java.net.URLConnection;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class StepFragment extends Fragment implements Player.EventListener {

    private static String TAG = StepFragment.class.getSimpleName();
    private View rootView;
    private FragmentStepBinding mBinding;
    private String STEP_KEY = "step-key";
    private String POSITION_KEY = "position-key";
    private SimpleExoPlayer exoPlayer;
    private String EXO_VIDEO_PLAYER = "exoVideoPlayer";
    private List<Step> steps;
    private int position;

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
        steps = (List<Step>) data.getSerializable(STEP_KEY);
        position = data.getInt(POSITION_KEY);
        Step step = steps.get(position);

        //Populate the UI
        if(DeviceUtils.isPhone && DeviceUtils.isLandscape()){
            populateUiLandscape(step);
        }else {
            populateUi(step);
            setVisibilityOfPreviousAndNext(position, steps);

            //Set onClick for previous and next
            mBinding.ivPrevious.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    position--;
                    previousStep(position, steps);
                }
            });

            mBinding.ivNext.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    position++;
                    nextStep(position, steps);
                }
            });
        }

        return rootView;
    }

    //Release ExoPlayer if Fragment is paused
    @Override
    public void onPause() {
        super.onPause();
        releasePlayer();
    }

    @Override
    public void onResume() {
        super.onResume();
        //If Phone and Landscape we hide the Actonabar and the Appbar Layout to have a Full Screen Video
        if(DeviceUtils.isPhone && DeviceUtils.isLandscape()){
            ((MainActivity)getActivity()).getSupportActionBar().hide();
            MainActivity.appBar.setVisibility(View.GONE);
            mBinding.playerView.setResizeMode(AspectRatioFrameLayout.RESIZE_MODE_FILL);
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

    //Check if there is a video and launch Exoplaxer
    private void checkForVideo(Step step){
        //Initialize ExoPlayer if there is a media source
        if(!TextUtils.isEmpty(step.getVideoURL())){
            Uri mediaUri = Uri.parse(step.getVideoURL());
            initializePlayer(mediaUri);
            mBinding.ivThumbnail.setVisibility(View.GONE);
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
    }

    //Initialize Exo Player Method
    private void initializePlayer(Uri mediaUri){
        if(exoPlayer == null){
            playerIsInitialized = true;
            TrackSelector trackSelector = new DefaultTrackSelector();
            LoadControl loadControl = new DefaultLoadControl();
            exoPlayer = ExoPlayerFactory.newSimpleInstance(getActivity(), trackSelector, loadControl);
            mBinding.playerView.setPlayer(exoPlayer);
            exoPlayer.addListener(this);
            String userAgent = Util.getUserAgent(getActivity(), EXO_VIDEO_PLAYER);
            MediaSource mediaSource = new ExtractorMediaSource(mediaUri, new DefaultDataSourceFactory(
                    getActivity(), userAgent), new DefaultExtractorsFactory(), null, null);
            exoPlayer.prepare(mediaSource);
            exoPlayer.setPlayWhenReady(false);
        }

    }

    //Release Media Player
    private void releasePlayer(){
        if(playerIsInitialized){
            exoPlayer.stop();
            exoPlayer.release();
            exoPlayer = null;
            playerIsInitialized = false;
        }
    }

    //Populate the Ui
    private void populateUi(Step step){
        //Set the title
        Toolbar toolbar = (Toolbar) getActivity().findViewById(R.id.toolbar);
        toolbar.setTitle(getStepTitle(step));
        mBinding.stepShortDescription.setText(step.getShortDescription());
        mBinding.stepDescription.setText(step.getDescription());
        checkForVideo(step);
    }

    //Populate Ui for Phone in Landscape
    private void populateUiLandscape(Step step){
        checkForVideo(step);
    }

    //Check position and set visibility of previous/next
    private void setVisibilityOfPreviousAndNext(int position, List<Step> steps){
        if(position == 0){
            mBinding.ivPrevious.setVisibility(View.INVISIBLE);
            mBinding.ivNext.setVisibility(View.VISIBLE);
        } else if(position == steps.size() -1){
            mBinding.ivPrevious.setVisibility(View.VISIBLE);
            mBinding.ivNext.setVisibility(View.INVISIBLE);
        } else {
            mBinding.ivPrevious.setVisibility(View.VISIBLE);
            mBinding.ivNext.setVisibility(View.VISIBLE);
        }
    }

    //Set the Button OnClick Actions
    private void nextStep(int position, List<Step> steps){
        releasePlayer();
        Step step = steps.get(position);
        populateUi(step);
        setVisibilityOfPreviousAndNext(position, steps);
    }

    private void previousStep(int position, List<Step> steps){
        releasePlayer();
        Step step = steps.get(position);
        populateUi(step);
        setVisibilityOfPreviousAndNext(position, steps);
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

    //Exo Player Events
    @Override
    public void onTimelineChanged(Timeline timeline, Object manifest, int reason) {

    }

    @Override
    public void onTracksChanged(TrackGroupArray trackGroups, TrackSelectionArray trackSelections) {

    }

    @Override
    public void onLoadingChanged(boolean isLoading) {
        if(isLoading){
            //Hide Player while loading
            mBinding.playerView.setVisibility(View.INVISIBLE);
            mBinding.pbVideoLoading.setVisibility(View.VISIBLE);
        }else {
            //Show Player on lOade completed
            mBinding.playerView.setVisibility(View.VISIBLE);
            mBinding.pbVideoLoading.setVisibility(View.INVISIBLE);
        }

    }

    @Override
    public void onPlayerStateChanged(boolean playWhenReady, int playbackState) {

    }

    @Override
    public void onRepeatModeChanged(int repeatMode) {

    }

    @Override
    public void onShuffleModeEnabledChanged(boolean shuffleModeEnabled) {

    }

    @Override
    public void onPlayerError(ExoPlaybackException error) {
        //Hide the Player if there is an Error
        mBinding.playerView.setVisibility(View.GONE);
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
