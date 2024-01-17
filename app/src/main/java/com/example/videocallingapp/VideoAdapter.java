package com.example.videocallingapp;

import android.app.Application;
import android.net.Uri;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory;
import com.google.android.exoplayer2.extractor.ExtractorsFactory;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.trackselection.AdaptiveTrackSelection;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelector;
import com.google.android.exoplayer2.ui.PlayerView;
import com.google.android.exoplayer2.upstream.BandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultHttpDataSourceFactory;

public class VideoAdapter extends RecyclerView.ViewHolder {
    SimpleExoPlayer exoPlayer;
    PlayerView playerView;
    public VideoAdapter(@NonNull View itemView) {
        super(itemView);
    }
    public void setExoplayer(Application application, String videoName, String videoUrl){
        TextView textView=itemView.findViewById(R.id.tv_item);
        playerView=itemView.findViewById(R.id.exoplayer_item);
        textView.setText(videoName);
        try{
            BandwidthMeter bandwidthMeter=new DefaultBandwidthMeter.Builder(application).build();
            TrackSelector trackSelector = new DefaultTrackSelector(new AdaptiveTrackSelection.Factory(bandwidthMeter));
            exoPlayer=(SimpleExoPlayer) ExoPlayerFactory.newSimpleInstance(application);
            Uri videos= Uri.parse(videoUrl);
            DefaultHttpDataSourceFactory dataSourceFactory= new DefaultHttpDataSourceFactory("videos");
            ExtractorsFactory extractorsFactory=new DefaultExtractorsFactory();
            MediaSource mediaSource=new ExtractorMediaSource(videos,dataSourceFactory,extractorsFactory,null,null);
            playerView.setPlayer(exoPlayer);
            exoPlayer.prepare(mediaSource);
            exoPlayer.setPlayWhenReady(false);
        }catch (Exception e){
            Log.e("VideoAdapter","ExoPlayer Error"+e.toString());
        }
    }
}
