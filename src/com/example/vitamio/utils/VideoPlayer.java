package com.example.vitamio.utils;

import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;

import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnBufferingUpdateListener;
import android.media.MediaPlayer.OnCompletionListener;
import android.media.MediaPlayer.OnPreparedListener;
import android.os.Handler;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceHolder.Callback;
import android.view.SurfaceView;
import android.widget.SeekBar;

/**
 * 视频播放器
 *
 */
public class VideoPlayer implements OnBufferingUpdateListener,
		OnCompletionListener, OnPreparedListener, Callback {
	public final static String TAG_STRING = VideoPlayer.class.getSimpleName();
	public MediaPlayer mediaPlayer;//视频播放器
    //播放器宽高
    private int videoWidht;
	private int videoHeight;
	private SurfaceHolder surfaceHolder;
    //播放器信息
	public final static int MSG_ERROR   = 0 ;
	public final static int MSG_PLAYING = 1 ;
	
	private SeekBar seekBar;
	private Timer mTimer = new Timer();

    /**
     * 空构造器
     */
	public VideoPlayer(){
		
	}

    /**
     *
     * @param surfaceView 图像显示在上面
     * @param seekBar 视频的进度条
     */
	public VideoPlayer(SurfaceView surfaceView, SeekBar seekBar){
		this.seekBar = seekBar;
		surfaceHolder = surfaceView.getHolder();
		surfaceHolder.addCallback(this);
		surfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
        Log.d(TAG_STRING,"______________the video player is init !______________________");
        //计时器开始计时
		mTimer.schedule(mTimeTask, 0, 1000);
	}
	/**
	 * 定时器 和handler来更新进度条
	 */
	TimerTask mTimeTask = new TimerTask() {
		
		@Override
		public void run() {
			if(mediaPlayer==null){
				return;
			}
			if(mediaPlayer.isPlaying() && seekBar.isPressed() == false ){
				handlerProgress.sendEmptyMessage(MSG_PLAYING);
			}
			
		}
	};
	final Handler handlerProgress = new Handler(){
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case MSG_PLAYING:
				int position = mediaPlayer.getCurrentPosition();
				int duration = mediaPlayer.getDuration();
				if(duration >= 0){
					long pos = seekBar.getMax()*position/duration;
					seekBar.setProgress((int)pos);
					Log.d(TAG_STRING, pos+"");
				}
				break;

			default:
				break;
			}
		};
	};
	public void play(){
		mediaPlayer.start();
	}
	public void playUrl(String videoUrl){
        Log.d(TAG_STRING,"the Video URL is"+ videoUrl);
		mediaPlayer.reset();
		try {
			mediaPlayer.setDataSource(videoUrl);
			mediaPlayer.prepare();
		} catch (IllegalStateException e) {
			e.printStackTrace();
			Log.d(TAG_STRING, "PlayUrl : IllegalStateException");
			
		} catch (IOException e) {
			e.printStackTrace();
			Log.d(TAG_STRING, "PlayUrl : IOException");
		}
	}
	public void pause(){
		mediaPlayer.pause();
	}
	public void stop(){
		if(mediaPlayer != null ){
			mediaPlayer.stop();
			mediaPlayer.release();
			mediaPlayer = null ;
		}
	}
	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int width,
			int height) {
		Log.d(TAG_STRING, "surfaceChanged");
	}

	@Override
	public void surfaceCreated(SurfaceHolder holder) {
		try{
			mediaPlayer = new MediaPlayer();
			mediaPlayer.setDisplay(surfaceHolder);
			mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
			mediaPlayer.setOnBufferingUpdateListener(this);
			mediaPlayer.setOnPreparedListener(this);
		}catch(Exception e){
			Log.d(TAG_STRING, "mediaPlayer is Error !!!");
		}
		Log.d(TAG_STRING, "surfaceCreated");
	}

	@Override
	public void surfaceDestroyed(SurfaceHolder holder) {
		Log.d(TAG_STRING, "surfaceDestroyed");
	}

	@Override
	public void onPrepared(MediaPlayer mp) {
		videoWidht = mediaPlayer.getVideoWidth();
		videoHeight = mediaPlayer.getVideoHeight();
		if(videoWidht != 0 || videoHeight != 0){
			mp.start();
		}
		Log.d(TAG_STRING, "onPrepared");
	}

	@Override
	public void onCompletion(MediaPlayer mp) {

	}

	@Override
	public void onBufferingUpdate(MediaPlayer mp, int percent) {
		seekBar.setSecondaryProgress(percent);
		int currentProgress = seekBar.getMax()*mediaPlayer.getCurrentPosition()/mediaPlayer.getDuration();
		Log.e(currentProgress+"% play", percent + "% buffer");

	}

}
