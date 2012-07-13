package com.example.vitamio.ui;

import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.*;
import android.widget.*;
import com.example.vitamio.R;
import com.example.vitamio.utils.VideoPlayer;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by IntelliJ IDEA.
 * User: William
 * Date: 11-8-31
 * Time: 上午10:46
 * To change this template use File | Settings | File Templates.
 */
public class VideoPlayer_v1 extends Activity {
    private final static String TAG = VideoPlayer_v1.class.getSimpleName();
    private Button btnPause, btnPlayUrl, btnStop;
    private LinearLayout videoControlBar;
    private SeekBar seekBar;
    private VideoPlayer player;
    private SurfaceView surfaceView;
    private Bundle bundle = null;
    private String path = "";

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Window win = getWindow();
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        win.requestFeature(Window.FEATURE_LEFT_ICON);

        setContentView(R.layout.videoplayer_v1);
        win.setFeatureDrawableResource(Window.FEATURE_LEFT_ICON, R.drawable.icon);
        this.setTitle("Mini-VideoPlayer");
        bundle = getIntent().getExtras();

        if (bundle != null) {
            path = bundle.getString("path");
            Log.d(TAG, path + "");
        } else {
            Toast.makeText(VideoPlayer_v1.this, "错误", 1).show();
            this.finish();
        }
        init();

    }

    private void init() {
        surfaceView = (SurfaceView) this.findViewById(R.id.videoSurfaceView);
        btnPlayUrl = (Button) this.findViewById(R.id.btnPlayUrl);
        btnPlayUrl.setOnClickListener(new ClickEvent());

        btnPause = (Button) this.findViewById(R.id.btnPause);
        btnPause.setOnClickListener(new ClickEvent());

        btnStop = (Button) this.findViewById(R.id.btnStop);
        btnStop.setOnClickListener(new ClickEvent());

        seekBar = (SeekBar) this.findViewById(R.id.skbProgress);
        seekBar.setOnSeekBarChangeListener(new SeekBarChangeEvent());

        videoControlBar = (LinearLayout) this.findViewById(R.id.videoControlBar);

        player = new VideoPlayer(surfaceView, seekBar);

    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
                if (this.videoControlBar.getVisibility() == View.INVISIBLE) {
                    videoControlBar.setVisibility(View.VISIBLE);
                } else if (this.videoControlBar.getVisibility() == View.VISIBLE){
                    if(player.mediaPlayer.isPlaying()){
                        this.videoControlBar.setVisibility(View.INVISIBLE);
                    }
                }
            }
        return false;
    }

    class ClickEvent implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            if (v == btnPause) {
                player.pause();
            } else if (v == btnStop) {
                player.stop();
                VideoPlayer_v1.this.finish();
            } else if (v == btnPlayUrl) {
                String str = "";//"http://www.pocketjourney.com/downloads/pj/video/famous.3gp";
//                str = path;
                player.playUrl(str);
//                player.
                videoControlBar.setVisibility(View.INVISIBLE);
            }
        }
    }

    class SeekBarChangeEvent implements SeekBar.OnSeekBarChangeListener {
        int progress;

        @Override
        public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
            this.progress = progress * player.mediaPlayer.getDuration();
            seekBar.getMax();
        }

        @Override
        public void onStartTrackingTouch(SeekBar seekBar) {
        }

        @Override
        public void onStopTrackingTouch(SeekBar seekBar) {
            player.mediaPlayer.seekTo(progress);



        }
    }
    @Override
    public void onBackPressed() {
//        super.onBackPressed();    //To change body of overridden methods use File | Settings | File Templates.
        moveTaskToBack(true);

    }

    @Override
    protected void onPause() {
        VideoPlayer_v1.this.finish();
        super.onPause();    //To change body of overridden methods use File | Settings | File Templates.

    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
//            moveTaskToBack(true);
            return true;
        } else {
            return super.onKeyDown(keyCode, event);
        }
    }

}