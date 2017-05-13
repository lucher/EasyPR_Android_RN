package com.easypr.rn.util;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;

import com.easypr.rn.CameraActivity;

import java.io.File;
import java.util.Date;

/**
 * Created by ares on 6/20/16.
 */
public class FileUtil {
    public static final int FILE_TYPE_IMAGE = 1;
    public static final int FILE_TYPE_PLATE = 2;
    public static final int FILE_TYPE_SVM_MODEL = 3;
    public static final int FILE_TYPE_ANN_MODEL = 4;

    /**
     * Create a File for saving an image or video
     */
    public static File getOutputMediaFile(int type) {
        // To be safe, you should check that the SDCard is mounted
        // using Environment.getExternalStorageState() before doing this.
        File mediaStorageDir = null;
        switch (type) {
            case FILE_TYPE_IMAGE: {
//                mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),
//                        "PlateRcognizer");
//                mediaStorageDir = new File(CameraActivity.act.getCacheDir(),"PlateRcognizer");
                mediaStorageDir = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/temp/");

                break;
            }
            case FILE_TYPE_PLATE: {
//                mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),
//                        "PlateRcognizer/PlateRect");
//                mediaStorageDir = new File(CameraActivity.act.getCacheDir(),"PlateRcognizer/PlateRect");
                mediaStorageDir = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/temp/");
                break;
            }
            case FILE_TYPE_ANN_MODEL:
            case FILE_TYPE_SVM_MODEL: {
//                mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS),
//                        "PlateRcognizer");
//                mediaStorageDir = new File(CameraActivity.act.getCacheDir(),"PlateRcognizer");
                mediaStorageDir = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/temp/");
                break;
            }
            default:
                return null;
        }
        // This location works best if you want the created images to be shared
        // between applications and persist after your app has been uninstalled.

        // Create the storage directory if it does not exist

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && ContextCompat.checkSelfPermission(CameraActivity.act, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(CameraActivity.act, new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
        } else {

        }
        if (!mediaStorageDir.exists()) {
            if (!mediaStorageDir.mkdirs()) {
                Log.d("PlateRcognizer", "failed to create directory");
                return null;
            }
        }

        // Create a media file name
        String timeStamp = DateUtil.getDateFormatString(new Date());
        File mediaFile;
        switch (type) {
            case FILE_TYPE_IMAGE: {
                mediaFile = new File(mediaStorageDir.getPath() + File.separator + "RPK_.jpg");
                break;
            }
            case FILE_TYPE_PLATE: {
                mediaFile = new File(mediaStorageDir.getPath() + File.separator + "RP_.jpg");
                break;
            }
            case FILE_TYPE_ANN_MODEL: {
                mediaFile = new File(mediaStorageDir.getPath() + File.separator + "ann.xml");
                break;
            }
            case FILE_TYPE_SVM_MODEL: {
                mediaFile = new File(mediaStorageDir.getPath() + File.separator + "svm.xml");
                break;
            }
            default:
                return null;
        }

        return mediaFile;
    }

    public static String getMediaFilePath(int type) {
        File mediaStorageDir = null;
        File mediaFile;
        switch (type) {
            case FILE_TYPE_ANN_MODEL: {
//                mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS),
//                        "PlateRcognizer");
//                mediaStorageDir = new File(CameraActivity.act.getCacheDir(),"PlateRcognizer");
                mediaStorageDir = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/temp/");
                mediaFile = new File(mediaStorageDir.getPath() + File.separator + "ann.xml");
                break;
            }
            case FILE_TYPE_SVM_MODEL: {
//                mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS),
//                        "PlateRcognizer");
//                mediaStorageDir = new File(CameraActivity.act.getCacheDir(),"PlateRcognizer");
                mediaStorageDir = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/temp/");
                mediaFile = new File(mediaStorageDir.getPath() + File.separator + "svm.xml");


                break;
            }
            default:
                return null;
        }
        return mediaFile.getAbsolutePath();
    }
}
