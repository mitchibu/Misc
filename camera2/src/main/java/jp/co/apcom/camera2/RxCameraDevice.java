package jp.co.apcom.camera2;

import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraCaptureSession;
import android.hardware.camera2.CameraDevice;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.view.Surface;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import io.reactivex.Single;
import io.reactivex.SingleEmitter;
import io.reactivex.SingleOnSubscribe;
import io.reactivex.functions.Cancellable;

public class RxCameraDevice {
	private final CameraDevice device;

	private RxCameraCaptureSession session = null;

	RxCameraDevice(CameraDevice device) {
		this.device = device;
	}

	public Single<RxCameraCaptureSession> createSession(final Surface... outputs) {
		return Single.create(new SingleOnSubscribe<RxCameraCaptureSession>() {
			@Override
			public void subscribe(final SingleEmitter<RxCameraCaptureSession> emitter) throws Exception {
				device.createCaptureSession(Arrays.asList(outputs), new CameraCaptureSession.StateCallback() {
					@Override
					public void onConfigured(@NonNull CameraCaptureSession session) {
						emitter.onSuccess(RxCameraDevice.this.session = new RxCameraCaptureSession(session));
					}

					@Override
					public void onConfigureFailed(@NonNull CameraCaptureSession session) {
						emitter.onError(new CameraAccessException(CameraAccessException.CAMERA_ERROR));
					}
				}, new Handler());
			}
		});
	}

	public void close() {
		if(session != null) {
			session.close();
			session = null;
		}
		device.close();
	}
}
