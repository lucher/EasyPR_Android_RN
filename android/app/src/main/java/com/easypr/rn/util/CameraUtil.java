package com.easypr.rn.util;

import android.app.Activity;
import android.hardware.Camera;
import android.view.Surface;

/**
 * 相机工具类
 * 
 * @author lucher
 * 
 */
public class CameraUtil {
	/**
	 * 获取前置摄像头id
	 * 
	 * @return
	 */
	public static int findFrontCamera() {
		int cameraCount = 0;
		Camera.CameraInfo cameraInfo = new Camera.CameraInfo();
		cameraCount = Camera.getNumberOfCameras(); // get cameras number

		for (int camIdx = 0; camIdx < cameraCount; camIdx++) {
			Camera.getCameraInfo(camIdx, cameraInfo); // get camerainfo
			if (cameraInfo.facing == Camera.CameraInfo.CAMERA_FACING_FRONT) {
				// 代表摄像头的方位，目前有定义值两个分别为CAMERA_FACING_FRONT前置和CAMERA_FACING_BACK后置
				return camIdx;
			}
		}
		return -1;
	}

	/**
	 * 获取后置摄像头id
	 * 
	 * @return
	 */
	public static int findBackCamera() {
		int cameraCount = 0;
		Camera.CameraInfo cameraInfo = new Camera.CameraInfo();
		cameraCount = Camera.getNumberOfCameras(); // get cameras number

		for (int camIdx = 0; camIdx < cameraCount; camIdx++) {
			Camera.getCameraInfo(camIdx, cameraInfo); // get camerainfo
			if (cameraInfo.facing == Camera.CameraInfo.CAMERA_FACING_BACK) {
				// 代表摄像头的方位，目前有定义值两个分别为CAMERA_FACING_FRONT前置和CAMERA_FACING_BACK后置
				return camIdx;
			}
		}
		return -1;
	}

	/**
	 * 获取相机，先获取后置摄像头，没有再获取前置摄像头
	 * 
	 * @return
	 */
	public static Camera getCamera() {
		int CammeraIndex = findBackCamera();
		if (CammeraIndex == -1) {
			CammeraIndex = findFrontCamera();
		}
		Camera camera = Camera.open(CammeraIndex);
		return camera;
	}

	/**
	 * 获取相机id，先获取后置摄像头，没有再获取前置摄像头
	 * 
	 * @return
	 */
	public static int getCameraId() {
		int CammeraIndex = findBackCamera();
		if (CammeraIndex == -1) {
			CammeraIndex = findFrontCamera();
		}
		return CammeraIndex;
	}

	/**
	 * 摄像头默认重力感应是横向的，如果在拍照的时候不是这个方向就会导致画面角度不对的问题， 解决办法是使用setDisplayOrientation来设置PreviewDisplay的方向
	 * 该方法来源于网路
	 * 参考官网介绍： https://developer.android.com/training/camera/cameradirect.html#camera-preview
	 * @param activity
	 * @param camera
	 * @param cameraId
	 */
	public static void adjustCameraOrientation(Activity activity, Camera camera, int cameraId) {
		Camera.CameraInfo info = new Camera.CameraInfo();
		Camera.getCameraInfo(cameraId, info);
		int rotation = activity.getWindowManager().getDefaultDisplay().getRotation();
		int degrees = 0;
		switch (rotation) {
		case Surface.ROTATION_0:
			degrees = 0;
			break;
		case Surface.ROTATION_90:
			degrees = 90;
			break;
		case Surface.ROTATION_180:
			degrees = 180;
			break;
		case Surface.ROTATION_270:
			degrees = 270;
			break;
		}
		int result;
		if (info.facing == Camera.CameraInfo.CAMERA_FACING_FRONT) {
			result = (info.orientation + degrees) % 360;
			result = (360 - result) % 360; // compensate the mirror  
		} else {
			// back-facing  
			result = (info.orientation - degrees + 360) % 360;
		}
		camera.setDisplayOrientation(result);
	}

}
