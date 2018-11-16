package jp.co.apcom.camera2;

import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraCaptureSession;
import android.hardware.camera2.CameraDevice;
import android.hardware.camera2.CaptureFailure;
import android.hardware.camera2.CaptureRequest;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.view.Surface;

import io.reactivex.Completable;
import io.reactivex.CompletableEmitter;
import io.reactivex.CompletableOnSubscribe;

public class RxCameraCaptureSession {
	private final CameraCaptureSession session;

	RxCameraCaptureSession(CameraCaptureSession session) {
		this.session = session;
	}

	public Completable setRepeatingRequest(final Surface surface) {
		return Completable.create(new CompletableOnSubscribe() {
			@Override
			public void subscribe(final CompletableEmitter emitter) throws Exception {
				CaptureRequest.Builder builder = session.getDevice().createCaptureRequest(CameraDevice.TEMPLATE_PREVIEW);
				builder.set(CaptureRequest.CONTROL_AF_MODE, CaptureRequest.CONTROL_AF_MODE_CONTINUOUS_PICTURE);
				builder.set(CaptureRequest.CONTROL_AE_MODE, CaptureRequest.CONTROL_AE_MODE_ON_AUTO_FLASH);
				builder.addTarget(surface);
				session.setRepeatingRequest(builder.build(), new CameraCaptureSession.CaptureCallback() {
					@Override
					public void onCaptureStarted(@NonNull CameraCaptureSession session, @NonNull CaptureRequest request, long timestamp, long frameNumber) {
						emitter.onComplete();
					}

					@Override
					public void onCaptureFailed(@NonNull CameraCaptureSession session, @NonNull CaptureRequest request, @NonNull CaptureFailure failure) {
						emitter.onError(new CameraAccessException(failure.getReason()));
					}
				}, new Handler());
			}
		});
	}

	public void close() {
		session.close();
	}
}
