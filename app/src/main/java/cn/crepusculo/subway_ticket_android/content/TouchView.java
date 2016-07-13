//package cn.crepusculo.subway_ticket_android.content;
//
//import android.content.Context;
//import android.content.res.TypedArray;
//import android.graphics.Bitmap;
//import android.graphics.BitmapFactory;
//import android.graphics.Canvas;
//import android.graphics.Matrix;
//import android.graphics.Movie;
//import android.graphics.PointF;
//import android.graphics.drawable.Drawable;
//import android.os.SystemClock;
//import android.util.AttributeSet;
//import android.util.Log;
//import android.view.MotionEvent;
//import android.view.View;
//import android.view.WindowManager;
//import android.view.animation.TranslateAnimation;
//import android.widget.ImageView;
//
//import java.io.InputStream;
//
//import cn.crepusculo.subway_ticket_android.R;
//import cn.crepusculo.subway_ticket_android.ui.activity.MainActivity;
//
//
//public class TouchView extends ImageView implements View.OnClickListener {
//    private Context mContext;
//
//    /**
//     * 是否全屏
//     */
//    private boolean isFullScreen;
//
//    /**
//     * 是否支持拖动缩放等事件
//     */
//    private boolean isDragable;
//
//    /**
//     * 播放GIF动画的关键类
//     */
//    private Movie mMovie;
//
//    /**
//     * 开始播放按钮图片
//     */
//    private Bitmap mStartButton;
//
//    /**
//     * 记录动画开始的时间
//     */
//    private long mMovieStart;
//
//    /**
//     * GIF图片的宽度
//     */
//    private int mImageWidth;
//
//    /**
//     * GIF图片的高度
//     */
//    private int mImageHeight;
//
//    /**
//     * 图片是否正在播放
//     */
//    private boolean isPlaying;
//
//    /**
//     * 是否允许自动播放
//     */
//    private boolean isAutoPlay;
//
//    private boolean isGif;
//
//    private Drawable mGif;
//
//    //在代码中实例，会调用此构造
//    public TouchView(Context context) {
//        super(context);
//        setupView();
//    }
//
//    //在XML中定义，会调用此构造
//    public TouchView(Context context, AttributeSet attrs) {
//        this(context, attrs, 0);
//    }
//
//    /**
//     * PowerImageView构造函数，在这里完成所有必要的初始化操作。
//     * 自己显式调用，第三个参数是default style
//     *
//     * @param context     */
//    public TouchView(Context context, AttributeSet attrs, int defStyle) {
//        super(context, attrs, defStyle);
//        this.mContext = context;
//        TypedArray a = context.obtainStyledAttributes(attrs,R.styleable.TouchView);
//        isFullScreen = a.getBoolean(R.styleable.MyImageView_isFullScreen, false);
//        isAutoPlay = a.getBoolean(R.styleable.MyImageView_autoplay, true);
//        isDragable = a.getBoolean(R.styleable.MyImageView_dragable, false);
//
//        //获得gif资源的id，在attrs.xml 定义文件中，src的index 为 4
//        int resourceId = a.getResourceId(4, 0);
//        if (resourceId != 0) {
//            // 当资源id不等于0时，就去获取该资源的流
//            InputStream is = getResources().openRawResource(resourceId);
//            // 使用Movie类对流进行解码
//            mMovie = Movie.decodeStream(is);
//            if (mMovie != null) {
//                // 如果返回值不等于null，就说明这是一个GIF图片，下面获取是否自动播放的属性
//                isAutoPlay = a.getBoolean(R.styleable.MyImageView_autoplay, false);
//                Bitmap bitmap = BitmapFactory.decodeStream(is);
//                mImageWidth = bitmap.getWidth();
//                mImageHeight = bitmap.getHeight();
//                bitmap.recycle();
//                if (!isAutoPlay) {
//                    // 当不允许自动播放的时候，得到开始播放按钮的图片，并注册点击事件
//                    isGif = true;
////                  BitmapDrawable bd = (BitmapDrawable)gifPlayBt;
////                  mStartButton = bd.getBitmap();
//                    mStartButton = BitmapFactory.decodeResource(getResources(), R.drawable.gif_play);
//                    setOnClickListener(this);
//                }
//            }
//        }
//        a.recycle();
//        setupView();
//    }
//
//    //自己显式调用，第三个参数是default style,第四个是style资源文件，只有在第三个参数无效时，才起作用
//    public TouchView(Context context, AttributeSet attrs, int defStyleAttr,int defStyleRes) {
//        //TODO do it later
//        this(context, attrs, defStyleAttr);
////      super(context, attrs, defStyleAttr, defStyleRes);
//    }
//
//    private float spacing(MotionEvent event){
//        float deltaX = (event.getX(0) - event.getX(1));
//        float deltaY = (event.getY(0) - event.getY(1));
//        return (float) Math.sqrt((deltaX * deltaX) + (deltaY * deltaY));
//    }
//
//    private PointF midPoint(MotionEvent event){
//        float midX = (event.getX(0) + event.getX(1));
//        float midY = (event.getY(0) + event.getY(1));
//        return new PointF(midX/2, midY/2);
//    }
//
//    private void setupView() {
//        //如果可拖动切非Gif动图，设置监听，处理拖动
//        if (isDragable && !isGif) {
//            this.setOnTouchListener(new OntouchListener());
//        }
//
//        //如果XML 设置为isFullScreen，
//        if (isFullScreen) {
//            this.setScaleType(ScaleType.CENTER_CROP);
//        } else {
//            this.setScaleType(ScaleType.CENTER);
//        }
//    }
//
//    public class OntouchListener implements OnTouchListener {
//
//        //定义动作
//        private static final int NONE = 0;
//        private static final int ZOOM = 1;
//        private static final int DRAG = 2;
//        private static final int FULL = 3;
//        private static final int NORMAL = 4;
//        private int mode = 0;
//        private Matrix mMatrix = new Matrix();
//        private Matrix savedMatrix = new Matrix();
//
//        private PointF mStart;
//        private PointF mEnd;
//        private float mOldDist = 0;
//        private float mNewDist = 0;
//        private PointF mMidPoint;
//
//        private long tick = 0;
//        private long tock = 0;
//        private int count = 0;
//        private boolean isDB = false;
//        private boolean isFull;
//
//        @Override
//        public boolean onTouch(View v, MotionEvent event) {
//            ImageView iv = (ImageView)v;
//            iv.setScaleType(ScaleType.MATRIX);
//            switch (event.getActionMasked()) {
//                case MotionEvent.ACTION_DOWN:
//                    Log.i("LIS", "ACTION_DOWN");
////              MyImageView.this.setScaleType(ScaleType.CENTER_CROP);
//
//                    mMatrix.set(iv.getImageMatrix());
//                    savedMatrix.set(mMatrix);
//                    mStart = new PointF(event.getX(), event.getY());
//                    mode = DRAG;
//                    break;
//                case MotionEvent.ACTION_UP:
//                    Log.i("LIS", "ACTION_UP");
//                    if (count == 0) {
//                        tick = System.currentTimeMillis();
//                        count++;
//                    } else if (count == 1) {//双击事件
//                        tock = System.currentTimeMillis();
//                        long delta = tock - tick;
//                        if (delta < 200) {
//                            isDB = true;
//                        }
//                        count = 0;
//                        tick = 0;
//                        tock = 0;
//                    }
//                    mode = NONE;
//                    break;
//                case MotionEvent.ACTION_POINTER_DOWN:
//                    Log.i("LIS", "ACTION_POINTER_DOWN");
//                    mOldDist = spacing(event);
//                    if (mOldDist > 15f) {
//
//                        mMidPoint = midPoint(event);
//                        mode = ZOOM;
//                    }
//                    break;
//                case MotionEvent.ACTION_POINTER_UP:
//                    Log.i("LIS", "ACTION_POINTER_UP");
//                    mode = NONE;
//                    break;
//                case MotionEvent.ACTION_MOVE:
//                    Log.i("LIS", "ACTION_MOVE");
//                    if (mode == DRAG) {
//                        mMatrix.set(savedMatrix);
//                        mEnd = new PointF(event.getX(), event.getY());
//                        mMatrix.postTranslate(mEnd.x - mStart.x, mEnd.y
//                                - mStart.y);
//                    } else if (mode == ZOOM) {
//                        mMatrix.set(savedMatrix);
//                        mNewDist = spacing(event);
//                        if (mNewDist > 15f) {
//                            float scale = mNewDist / mOldDist;
//                            mMatrix.postScale(scale, scale, mMidPoint.x,
//                                    mMidPoint.y);
//
//                        }
//                    }
//                    break;
//            }
//            if (!isDB) {
//                TouchView.this.setImageMatrix(mMatrix);
//            }
//            else{
//                if (isFull) {
//                    TouchView.this.setScaleType(ScaleType.CENTER);
//                    ((MainActivity)mContext).getWindow().setFlags(WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN,
//                            WindowManager.LayoutParams.FLAG_FULLSCREEN);
//                    Log.i("DB", "NORMAL");
//                    isFull = false;
//                }else{
//                    TouchView.this.setScaleType(ScaleType.CENTER_CROP);
//                    ((MainActivity)mContext).getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
//                            WindowManager.LayoutParams.FLAG_FULLSCREEN);
//
//                    isFull = true;
//                }
//                isDB = false;
//            }
//            return true;
//        }
//
//    }
//
//    @Override
//    public void onClick(View v) {
//        if (v.getId() == getId()) {
//            // 当用户点击图片时，开始播放GIF动画
//            isPlaying = true;
//            invalidate();
//        }
//    }
//
//    @Override
//    protected void onDraw(Canvas canvas) {
//        if (mMovie == null) {
//            // mMovie等于null，说明是张普通的图片，则直接调用父类的onDraw()方法
//            super.onDraw(canvas);
//        } else {
//            // mMovie不等于null，说明是张GIF图片
//            if (isAutoPlay) {
//                // 如果允许自动播放，就调用playMovie()方法播放GIF动画
//                playMovie(canvas);
//                invalidate();
//            } else {
//                // 不允许自动播放时，判断当前图片是否正在播放
//                if (isPlaying) {
//                    // 正在播放就继续调用playMovie()方法，一直到动画播放结束为止
//                    if (playMovie(canvas)) {
//                        isPlaying = false;
//                    }
//                    invalidate();
//                } else {
//                    // 还没开始播放就只绘制GIF图片的第一帧，并绘制一个开始按钮
//                    mMovie.setTime(0);
//                    mMovie.draw(canvas, 0, 0);
//                    int offsetW = (mImageWidth - mStartButton.getWidth()) / 2;
//                    int offsetH = (mImageHeight - mStartButton.getHeight()) / 2;
//                    canvas.drawBitmap(mStartButton, offsetW, offsetH, null);
//                }
//            }
//        }
//    }
//
//    @Override
//    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
//        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
//        if (mMovie != null) {
//            // 如果是GIF图片则重写设定PowerImageView的大小
//            setMeasuredDimension(mImageWidth, mImageHeight);
//        }
//    }
//
//    /**
//     * 开始播放GIF动画，播放完成返回true，未完成返回false。
//     *
//     * @param canvas
//     * @return 播放完成返回true，未完成返回false。
//     */
//    private boolean playMovie(Canvas canvas) {
//        long now = SystemClock.uptimeMillis();
//        if (mMovieStart == 0) {
//            mMovieStart = now;
//        }
//        int duration = mMovie.duration();
//        if (duration == 0) {
//            duration = 1000;
//        }
//        int relTime = (int) ((now - mMovieStart) % duration);
//        mMovie.setTime(relTime);
//        mMovie.draw(canvas, 0, 0);
//        if ((now - mMovieStart) >= duration) {
//            mMovieStart = 0;
//            return true;
//        }
//        return false;
//    }
//
//}