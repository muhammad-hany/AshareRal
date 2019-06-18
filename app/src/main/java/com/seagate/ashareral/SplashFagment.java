package com.seagate.ashareral;


import android.content.Intent;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.VideoView;

import com.google.android.material.appbar.CollapsingToolbarLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;


public class SplashFagment extends Fragment {


    private VideoView videoView;
    private NavController navController;

    public SplashFagment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_splash, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        videoView = view.findViewById(R.id.videoView);
        Uri uri = Uri.parse("android.resource://" + getActivity().getPackageName() + "/" + R.raw.splash);
        videoView.setVideoURI(uri);
        CollapsingToolbarLayout layout =
                getActivity().findViewById(R.id.collapsing_toolbar_layout);

        navController = Navigation.findNavController(view);
        videoView.setOnCompletionListener(mp -> {

            videoView.setVisibility(View.GONE);
            layout.setVisibility(View.VISIBLE);
            openApp();


        });






        videoView.setOnPreparedListener(mp -> {
            mp.setOnInfoListener((mp1, what, extra) -> {
                if (what == MediaPlayer.MEDIA_INFO_VIDEO_RENDERING_START) {
                    videoView.setBackgroundColor(Color.TRANSPARENT);
                    videoView.postDelayed(() -> {
                        if (!videoView.isPlaying()) return;
                        videoView.setBackgroundColor(Color.WHITE);

                    }, 3800);
                    return true;
                }
                return false;
            });
        });


        videoView.start();


    }


    private void openApp() {
        Intent intent=getActivity().getIntent();
        if (intent!=null) {

            if (intent.getStringExtra(Utils.NOTIFICATION_KEY)!=null) {
                Bundle bundle = new Bundle();
                switch (intent.getStringExtra(Utils.NOTIFICATION_KEY)) {
                    case Utils.NEWS_KEY:
                        News news = (News) intent.getSerializableExtra(Utils.NEWS_KEY);
                        bundle.putSerializable(Utils.NEWS_KEY, news);
                        navController.navigate(R.id.action_mainFragment_to_newsDetails2, bundle);
                        break;
                    case Utils.EVENT_KEY:
                        String timestamp= String.valueOf(intent.getStringExtra(Utils.EVENT_KEY));
                        bundle.putString(Utils.EVENT_TIMESTAMP_KEY,timestamp);
                        bundle.putString(Utils.CALENDAR_KEY,Utils.EVENT_KEY);
                        bundle.putString(Utils.ADMIN_ACTION_KEY,Utils.ACTION_VIEW);
                        navController.navigate(R.id.toEventsFragment,bundle);
                }
            }else {
                navController.navigate(R.id.toMainFragment);
            }
        }else {
            navController.navigate(R.id.toMainFragment);
        }
    }


}
