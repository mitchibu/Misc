package jp.co.apcom.camera2;

import android.annotation.SuppressLint;
import android.content.Context;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraDevice;
import android.hardware.camera2.CameraManager;
import android.os.Handler;
import android.support.annotation.NonNull;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.functions.Cancellable;

public class RxCameraManager {
	private final Context context;

	private RxCameraDevice device = null;

	public RxCameraManager(Context context) {
		this.context = context;
	}

	public Observable<RxCameraDevice> openDevice(final String cameraId) {
		return Observable.create(new ObservableOnSubscribe<RxCameraDevice>() {
			@SuppressLint("MissingPermission")
			@Override
			public void subscribe(final ObservableEmitter<RxCameraDevice> emitter) throws Exception {
				CameraManager cm = (CameraManager)context.getSystemService(Context.CAMERA_SERVICE);
				cm.openCamera(cameraId, new CameraDevice.StateCallback() {
					@Override
					public void onOpened(@NonNull CameraDevice camera) {
						emitter.onNext(device = new RxCameraDevice(camera));
					}

					@Override
					public void onDisconnected(@NonNull CameraDevice camera) {
						emitter.onComplete();
					}

					@Override
					public void onError(@NonNull CameraDevice camera, int error) {
						emitter.onError(new CameraAccessException(error));
					}
				}, new Handler());

				emitter.setCancellable(new Cancellable() {
					@Override
					public void cancel() {
						if(device == null) return;
						device.close();
						device = null;
					}
				});
			}
		});
	}
}
