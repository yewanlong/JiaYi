package com.maning.mnvideoplayerlibrary.player;

import android.app.Activity;
import android.content.Context;
import android.content.res.Configuration;
import android.content.res.TypedArray;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.maning.mnvideoplayerlibrary.R;
import com.maning.mnvideoplayerlibrary.listener.OnCompletionListener;
import com.maning.mnvideoplayerlibrary.listener.OnScreenOrientationListener;
import com.maning.mnvideoplayerlibrary.utils.PlayerUtils;

import java.io.IOException;
import java.util.Timer;


/**
 * Created by maning on 16/6/14.
 * 播放器
 */
public class MNViderPlayer extends FrameLayout implements
        SurfaceHolder.Callback, MediaPlayer.OnCompletionListener,
        MediaPlayer.OnPreparedListener {

    private static final String TAG = "MNViderPlayer";
    private Context context;
    private Activity activity;

    static final Handler myHandler = new Handler(Looper.getMainLooper()) {
    };

    // SurfaceView的创建比较耗时，要注意
    private SurfaceHolder surfaceHolder;
    private MediaPlayer mediaPlayer;
    private SurfaceView mn_palyer_surfaceView;
    private LinearLayout mn_player_surface_bg;
    private RelativeLayout layout_item;
    //标记暂停和播放状态
    private boolean isPlaying = false;

    //地址
    private String videoPath;
    private int video_position = 0;

    //控件的位置信息
    private float mediaPlayerX;
    private float mediaPlayerY;
    private int playerViewW;
    private int playerViewH;


    //默认宽高比16:9
    private int defaultWidthProportion = 16;
    private int defaultHeightProportion = 9;


    public MNViderPlayer(Context context) {
        this(context, null);
    }

    public MNViderPlayer(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MNViderPlayer(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
        activity = (Activity) this.context;
        //自定义属性相关
        //其他
        init();
    }


    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);

        int screenWidth = PlayerUtils.getScreenWidth(activity);
        int screenHeight = PlayerUtils.getScreenHeight(activity);
        ViewGroup.LayoutParams layoutParams = getLayoutParams();

        //newConfig.orientation获得当前屏幕状态是横向或者竖向
        //Configuration.ORIENTATION_PORTRAIT 表示竖向
        //Configuration.ORIENTATION_LANDSCAPE 表示横屏
        if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT) {
            activity.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
            //计算视频的大小16：9
            layoutParams.width = screenWidth;
            layoutParams.height = screenWidth * defaultHeightProportion / defaultWidthProportion;
            setX(mediaPlayerX);
            setY(mediaPlayerY);
        }
        if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            activity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
            layoutParams.width = screenWidth;
            layoutParams.height = screenHeight;

            setX(0);
            setY(0);
            //横屏通知
        }
        setLayoutParams(layoutParams);

        playerViewW = screenWidth;
        playerViewH = layoutParams.height;

        //适配大小
        fitVideoSize();
    }

    //初始化
    private void init() {
        View inflate = View.inflate(context, R.layout.mn_player_view, this);
        mn_palyer_surfaceView = (SurfaceView) inflate.findViewById(R.id.mn_palyer_surfaceView);
        mn_player_surface_bg = (LinearLayout) inflate.findViewById(R.id.mn_player_surface_bg);
        layout_item = (RelativeLayout) inflate.findViewById(R.id.layout_item);
        layout_item.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                pauseVideo();
                if (onScreenOrientationListener != null) {
                    onScreenOrientationListener.onClickVideo();
                }
            }
        });
        //初始化SurfaceView
        initSurfaceView();


        //存储控件的位置信息
        myHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                mediaPlayerX = getX();
                mediaPlayerY = getY();
                playerViewW = getWidth();
                playerViewH = getHeight();
                Log.i(TAG, "控件信息---X：" + mediaPlayerX + "，Y：" + mediaPlayerY);
                Log.i(TAG, "控件信息---playerViewW：" + playerViewW + "，playerViewH：" + playerViewH);
            }
        }, 1000);
    }


    private void initSurfaceView() {
        Log.i(TAG, "initSurfaceView");
        // 得到SurfaceView容器，播放的内容就是显示在这个容器里面
        surfaceHolder = mn_palyer_surfaceView.getHolder();
        surfaceHolder.setKeepScreenOn(true);
        // SurfaceView的一个回调方法
        surfaceHolder.addCallback(this);
    }


    //播放
    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        Log.i(TAG, "surfaceCreated");
        mediaPlayer = new MediaPlayer();
        mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
        mediaPlayer.setDisplay(holder); // 添加到容器中
        //播放完成的监听
        mediaPlayer.setOnCompletionListener(this);
        // 异步准备的一个监听函数，准备好了就调用里面的方法
        mediaPlayer.setOnPreparedListener(this);
        //播放错误的监听
        myHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (videoPath != null)
                    try {
                        mediaPlayer.setDataSource(videoPath);
                        // 准备开始,异步准备，自动在子线程中
                        mediaPlayer.prepareAsync();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
            }
        }, 1200);
        //添加播放路径

    }


    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        Log.i(TAG, "surfaceChanged---width：" + width + ",height:" + height);
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        //保存播放位置
        if (mediaPlayer != null) {
            mediaPlayer.pause();
            video_position = mediaPlayer.getCurrentPosition();
        }
        Log.i(TAG, "surfaceDestroyed---video_position：" + video_position);
    }

    //MediaPlayer
    @Override
    public void onCompletion(MediaPlayer mediaPlayer) {
        isPlaying = false;
        video_position = 0;
        if (onCompletionListener != null) {
            onCompletionListener.onCompletion(mediaPlayer);
        }
    }


    @Override
    public void onPrepared(final MediaPlayer mediaPlayer) {

        mediaPlayer.start(); // 开始播放
        //是否开始播放
        if (!isPlaying) {
            mediaPlayer.pause();
        }
        // 把得到的总长度和进度条的匹配
        //延时：避免出现上一个视频的画面闪屏
        myHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                //跳转指定位置
                if (video_position > 0) {
                    Log.i(TAG, "onPrepared---video_position:" + video_position);
                    MNViderPlayer.this.mediaPlayer.seekTo(video_position);
                    video_position = 0;
                }
            }
        }, 500);
        //适配大小
        fitVideoSize();

        //恢复显示,隐藏列缩图
        mn_palyer_surfaceView.setAlpha(1);

    }

    private void fitVideoSize() {
        if (mediaPlayer == null) {
            return;
        }
        //适配视频的高度
        int videoWidth = mediaPlayer.getVideoWidth();
        int videoHeight = mediaPlayer.getVideoHeight();
        int parentWidth = playerViewW;
        int parentHeight = playerViewH;
        int screenWidth = PlayerUtils.getScreenWidth(activity);
        int screenHeight = PlayerUtils.getScreenHeight(activity);

        //判断视频宽高和父布局的宽高
        int surfaceViewW;
        int surfaceViewH;
        if ((float) videoWidth / (float) videoHeight > (float) parentWidth / (float) parentHeight) {
            surfaceViewW = parentWidth;
            surfaceViewH = videoHeight * surfaceViewW / videoWidth;
        } else {
            surfaceViewH = parentHeight;
            surfaceViewW = videoWidth * parentHeight / videoHeight;
        }

        Log.i(TAG, "fitVideoSize---" +
                "videoWidth：" + videoWidth + ",videoHeight:" + videoHeight +
                ",parentWidth:" + parentWidth + ",parentHeight:" + parentHeight +
                ",screenWidth:" + screenWidth + ",screenHeight:" + screenHeight +
                ",surfaceViewW:" + surfaceViewW + ",surfaceViewH:" + surfaceViewH
        );
        //改变surfaceView的大小
        ViewGroup.LayoutParams params = mn_player_surface_bg.getLayoutParams();
        params.height = surfaceViewH;
        params.width = surfaceViewW;
        mn_player_surface_bg.setLayoutParams(params);
    }


    //--------------------------------------------------------------------------------------
    // ######## 对外提供的方法 ########
    //--------------------------------------------------------------------------------------

    /**
     * 设置宽高比:默认16:9
     */
    public void setWidthAndHeightProportion(int width, int height) {
        this.defaultWidthProportion = width;
        this.defaultHeightProportion = height;
    }

    /**
     * 设置视频信息
     *
     * @param url 视频地址
     */
    public void setDataSource(String url) {
        //赋值
        videoPath = url;
    }

    /**
     * 播放视频
     *
     * @param url 视频地址
     */
    public void playVideo(String url) {
        playVideo(url, video_position);
    }

    /**
     * 播放视频（支持上次播放位置）
     * 自己记录上一次播放的位置，然后传递position进来就可以了
     *
     * @param url      视频地址
     * @param position 视频跳转的位置(毫秒)
     */
    public void playVideo(String url, int position) {
        //地址判空处理
        if (TextUtils.isEmpty(url)) {
            Toast.makeText(context, context.getString(R.string.mnPlayerUrlEmptyHint), Toast.LENGTH_SHORT).show();
            return;
        }
        //销毁ControllerView

        //赋值
        videoPath = url;
        video_position = position;
        isPlaying = true;


        //重置MediaPlayer
        resetMediaPlayer();


    }

    private void resetMediaPlayer() {
        try {
            if (mediaPlayer != null) {
                if (mediaPlayer.isPlaying()) {
                    mediaPlayer.pause();
                    mediaPlayer.stop();
                }
                //重置mediaPlayer
                mediaPlayer.reset();
                //添加播放路径
                mediaPlayer.setDataSource(videoPath);
                // 准备开始,异步准备，自动在子线程中
                mediaPlayer.prepareAsync();
                //视频缩放模式
                mediaPlayer.setVideoScalingMode(android.media.MediaPlayer.VIDEO_SCALING_MODE_SCALE_TO_FIT);
            } else {
                //TODO:播放器初始化失败后怎么操作
                Toast.makeText(context, "播放器初始化失败", Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 播放视频
     */
    public void startVideo() {
        if (mediaPlayer != null) {
            mediaPlayer.start();
            isPlaying = true;
        }
    }

    /**
     * 暂停视频
     */
    public void pauseVideo() {
        if (mediaPlayer != null) {
            mediaPlayer.pause();
            video_position = mediaPlayer.getCurrentPosition();
            isPlaying = false;
        }
    }

    /**
     * 获取管理者
     */
    public MediaPlayer getMediaPlayer() {
        return mediaPlayer;
    }

    /**
     * 销毁资源
     */
    public void destroyVideo() {
        if (mediaPlayer != null) {
            mediaPlayer.stop();
            mediaPlayer.release();// 释放资源
            mediaPlayer = null;
        }
        surfaceHolder = null;
        mn_palyer_surfaceView = null;
        video_position = 0;
        removeAllListener();
        myHandler.removeCallbacksAndMessages(null);
    }


    //--------------------------------------------------------------------------------------
    // ######## 自定义回调 ########
    //--------------------------------------------------------------------------------------

    private void removeAllListener() {
        if (onCompletionListener != null) {
            onCompletionListener = null;
        }
    }


    //-----------------------播放完回调
    private OnCompletionListener onCompletionListener;

    public void setOnCompletionListener(OnCompletionListener onCompletionListener) {
        this.onCompletionListener = onCompletionListener;
    }

    //-----------------------屏幕方向的监听
    private OnScreenOrientationListener onScreenOrientationListener;

    public void setOnScreenOrientationListener(OnScreenOrientationListener onScreenOrientationListener) {
        this.onScreenOrientationListener = onScreenOrientationListener;
    }

}