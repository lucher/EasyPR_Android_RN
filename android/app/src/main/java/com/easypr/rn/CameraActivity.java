package com.easypr.rn;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.ImageFormat;
import android.graphics.Matrix;
import android.graphics.Rect;
import android.graphics.YuvImage;
import android.hardware.Camera;
import android.hardware.Camera.Size;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.aiseminar.EasyPR.PlateRecognizer;
import com.easypr.rn.util.CameraUtil;
import com.easypr.rn.module.MyMapIntentModule;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import butterknife.Bind;
import butterknife.ButterKnife;

import static android.content.ContentValues.TAG;

/**
 * 拍照界面
 *
 * @author lucher
 */
public class CameraActivity extends Activity implements SurfaceHolder.Callback, Camera.AutoFocusCallback {

    // 相机
    private Camera mCamera;
    // 相机预览画面
//    private SurfaceView mSurfaceView;
    private SurfaceHolder mSurfaceHolder;

    @Bind(R.id.svCamera)
    SurfaceView mSurfaceView;

    @Bind(R.id.ivPlateRect)
    ImageView mIvPlateRect;

    @Bind(R.id.ivCapturePhoto)
    ImageView mIvCapturePhoto;

    @Bind(R.id.tvPlateResult)
    TextView mTvPlateResult;

    private PlateRecognizer mPlateRecognizer;
    public static Activity act;

    private LocalBroadcastManager lbm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_camera);

        lbm = LocalBroadcastManager.getInstance(this);
        act = this;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.CAMERA}, 1);
        }

        ButterKnife.bind(this);
        mPlateRecognizer = new PlateRecognizer(this);

        initViews();
        openCamaraSafely();
    }

    /**
     * 初始化视图
     */
    private void initViews() {
        mSurfaceView = (SurfaceView) this.findViewById(R.id.svCamera);
        mSurfaceHolder = mSurfaceView.getHolder();
        mSurfaceHolder.addCallback(this);
        mSurfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
    }

    /**
     * 打开相机
     *
     * @return
     */
    private boolean openCamaraSafely() {
        boolean opened = false;
        try {
            release();
            int cameraId = CameraUtil.getCameraId();
            mCamera = Camera.open(cameraId);
            opened = mCamera != null;
            if (opened) {// 调整预览方向
                CameraUtil.adjustCameraOrientation(this, mCamera, cameraId);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return opened;
    }

    /**
     * 释放相关资源
     */
    private void release() {
        if (mCamera != null) {
            mCamera.stopPreview();
            mCamera.release();
            mCamera = null;
        }
    }

    public void btnOnclick(View view) {
        mCamera.autoFocus(CameraActivity.this);
        Intent intent = new Intent(MyMapIntentModule.LOCAL_ACTION);
        intent.putExtra("msg", "开始识别拍照");
        lbm.sendBroadcast(intent);
    }


    @Override
    public void onAutoFocus(boolean success, Camera camera) {
        if (success) {
            camera.takePicture(shutter, raw, jpeg);
        }
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        try {
            mCamera.setPreviewDisplay(holder);
            mCamera.startPreview();
        } catch (Exception e) {
            Log.d(TAG, "Error starting camera preview: " + e.getMessage());
        }
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        // Camera.Parameters parameters = mCamera.getParameters();
        // parameters.setPreviewSize(width, height);
        // mCamera.setParameters(parameters);
        try {
            mCamera.setPreviewDisplay(holder);
            mCamera.startPreview();
        } catch (Exception e) {
            Log.d(TAG, "Error starting camera preview: " + e.getMessage());
        }
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {

    }

    /**
     * 快门按下时的回调
     */
    Camera.ShutterCallback shutter = new Camera.ShutterCallback() {
        public void onShutter() {
            // shootSound();
            mCamera.setOneShotPreviewCallback(previewCallback);
        }
    };

    /**
     * 播放系统拍照声音
     */
    // private void shootSound() {
    // AudioManager meng = (AudioManager)
    // this.getSystemService(Context.AUDIO_SERVICE);
    // int volume = meng.getStreamVolume( AudioManager.STREAM_NOTIFICATION);
    //
    // if (volume != 0) {
    // if (mShootMP == null)
    // mShootMP = MediaPlayer.create(this,
    // Uri.parse("file:///system/media/audio/ui/camera_click.ogg"));
    // if (mShootMP != null)
    // mShootMP.start();
    // }
    // }

    /**
     * 获取Preview界面的截图，并存储
     */
    Camera.PreviewCallback previewCallback = new Camera.PreviewCallback() {
        @Override
        public void onPreviewFrame(byte[] data, Camera camera) {
            Size size = camera.getParameters().getPreviewSize();
            try {
                YuvImage image = new YuvImage(data, ImageFormat.NV21, size.width, size.height, null);
                if (image != null) {
                    ByteArrayOutputStream stream = new ByteArrayOutputStream();
                    image.compressToJpeg(new Rect(0, 0, size.width, size.height), 100, stream);
                    Bitmap bmp = BitmapFactory.decodeByteArray(stream.toByteArray(), 0, stream.size());
                    stream.close();

                    Bitmap normalBitmap = createRotateBitmap(bmp);
                    saveBitmap(normalBitmap, "shutter.png");
                    cropBitmapAndRecognize(normalBitmap);
                    bmp.recycle();
                    normalBitmap.recycle();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    };

    // 拍照原始数据的回调
    Camera.PictureCallback raw = new Camera.PictureCallback() {
        public void onPictureTaken(byte[] imgData, Camera camera) {
            saveBitmap(imgData, "raw.png");
            // camera.startPreview();
        }
    };

    // 经过压缩成jpg格式的图像数据
    Camera.PictureCallback jpeg = new Camera.PictureCallback() {
        public void onPictureTaken(byte[] imgData, Camera camera) {
            saveBitmap(imgData, "jpeg.png");
            camera.startPreview();
        }
    };

    /**
     * 保存图片
     *
     * @param imgData
     * @param file
     */
    private void saveBitmap(byte[] imgData, String file) {
        if (imgData != null) {
            File dir = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/temp/");
            if (!dir.exists()) {
                dir.mkdirs();
            }
            File tmp = new File(dir, file);

            try {
                FileOutputStream fos = new FileOutputStream(tmp);
                // 照片转方向
                Bitmap bitmap = BitmapFactory.decodeByteArray(imgData, 0, imgData.length);
                Bitmap normalBitmap = createRotateBitmap(bitmap);
                normalBitmap.compress(Bitmap.CompressFormat.PNG, 100, fos);
                fos.close();
                // 最后通知图库更新
                notifyMedia(tmp.getAbsolutePath());
//                Toast.makeText(CameraActivity.this, "图像已保存。", Toast.LENGTH_SHORT).show();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }

    /**
     * bitmap旋转90度
     *
     * @param bitmap
     * @return
     */
    public static Bitmap createRotateBitmap(Bitmap bitmap) {
        if (bitmap != null) {
            Matrix m = new Matrix();
            try {
                m.postRotate(90);
                Bitmap bmp2 = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), m, false);
                bitmap.recycle();
                bitmap = bmp2;
            } catch (Exception ex) {
                System.out.print("创建图片失败！" + ex);
            }
        }
        return bitmap;
    }

    public void cropBitmapAndRecognize(Bitmap originalBitmap) {
        // 裁剪出关注区域
        DisplayMetrics metric = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metric);
        int width = metric.widthPixels; // 屏幕宽度（像素）
        int height = metric.heightPixels; // 屏幕高度（像素）
        Bitmap sizeBitmap = Bitmap.createScaledBitmap(originalBitmap, width, height, true);

        int rectWidth = (int) (mIvPlateRect.getWidth() * 1.5);
        int rectHight = (int) (mIvPlateRect.getHeight() * 1.5);
        int[] location = new int[2];
        mIvPlateRect.getLocationOnScreen(location);
        location[0] -= mIvPlateRect.getWidth() * 0.5 / 2;
        location[1] -= mIvPlateRect.getHeight() * 0.5 / 2;
        Bitmap normalBitmap = Bitmap.createBitmap(sizeBitmap, location[0], location[1], rectWidth, rectHight);

        // 保存图片并进行车牌识别
        File pictureFile = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/temp", "cut.png");
        if (pictureFile == null) {
            Log.d("lucher", "Error creating media file, check storage permissions: ");
            return;
        }

        try {
            mTvPlateResult.setText("正在识别...");
            FileOutputStream fos = new FileOutputStream(pictureFile);
            normalBitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);
            fos.close();

            // 进行车牌识别
            String plate = mPlateRecognizer.recognize(pictureFile.getAbsolutePath());
            if (null != plate && !plate.equalsIgnoreCase("0")) {
                mTvPlateResult.setText(plate);
                Intent intent = new Intent(MyMapIntentModule.LOCAL_ACTION);
                intent.putExtra("msg", "识别成功：" + plate);
                lbm.sendBroadcast(intent);
                finish();
            } else {
                mTvPlateResult.setText("请调整角度");
            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 保存bitmap到sd卡
     *
     * @param bitmap
     * @param file
     */
    public void saveBitmap(Bitmap bitmap, String file) {
        FileOutputStream fOut;
        try {
            File dir = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/temp/");
            if (!dir.exists()) {
                dir.mkdir();
            }

            File tmp = new File(dir, file);
            fOut = new FileOutputStream(tmp);
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, fOut);
            fOut.flush();
            fOut.close();

            notifyMedia(tmp.getAbsolutePath());
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 通知媒体文件更新
     *
     * @param path
     */
    public void notifyMedia(String path) {
        Intent intent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        Uri contentUri = Uri.fromFile(new File(path));
        intent.setData(contentUri);
        sendBroadcast(intent);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        release();
    }
}