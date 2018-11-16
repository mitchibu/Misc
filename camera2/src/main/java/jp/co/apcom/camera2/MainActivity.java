package jp.co.apcom.camera2;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import io.reactivex.CompletableObserver;
import io.reactivex.CompletableSource;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;

public class MainActivity extends AppCompatActivity {
	private SurfaceView view;
	RxCameraManager cm;
	RxCameraDevice device;
	Disposable disposable;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(view = new SurfaceView(this));

		cm = new RxCameraManager(this);

//		disposable = cm.openDevice("1")
//				.subscribe(new Consumer<RxCameraDevice>() {
//					@Override
//					public void accept(RxCameraDevice rxCameraDevice) throws Exception {
//						device = rxCameraDevice;
//						if(view.getHolder().isCreating()) {
//							startPreview(view.getHolder());
//						}
//					}
//				}, new Consumer<Throwable>() {
//					@Override
//					public void accept(Throwable throwable) throws Exception {
//						throwable.printStackTrace();
//					}
//				}, new Action() {
//					@Override
//					public void run() throws Exception {
//					}
//				});

		view.getHolder().addCallback(new SurfaceHolder.Callback() {
			@Override
			public void surfaceCreated(SurfaceHolder holder) {
//				if(device != null) {
//					startPreview(holder);
//				}
				disposable = cm.openDevice("1")
						.subscribe(new Consumer<RxCameraDevice>() {
							@Override
							public void accept(RxCameraDevice rxCameraDevice) throws Exception {
								device = rxCameraDevice;
								startPreview(view.getHolder());
//								rxCameraDevice.createSession(view.getHolder().getSurface())
//										.flatMapCompletable(new Function<RxCameraCaptureSession, CompletableSource>() {
//											@Override
//											public CompletableSource apply(RxCameraCaptureSession rxCameraCaptureSession) throws Exception {
//												return rxCameraCaptureSession.setRepeatingRequest(view.getHolder().getSurface());
//											}
//										})
//										.subscribe(new CompletableObserver() {
//											@Override
//											public void onSubscribe(Disposable d) {
//											}
//
//											@Override
//											public void onComplete() {
//											}
//
//											@Override
//											public void onError(Throwable e) {
//												e.printStackTrace();
//											}
//										});
							}
						}, new Consumer<Throwable>() {
							@Override
							public void accept(Throwable throwable) throws Exception {
								throwable.printStackTrace();
							}
						}, new Action() {
							@Override
							public void run() throws Exception {
							}
						});
			}

			@Override
			public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
				holder.setFixedSize(width, height);
			}

			@Override
			public void surfaceDestroyed(SurfaceHolder holder) {
				disposable.dispose();
			}
		});
	}

	private void startPreview(final SurfaceHolder holder) {
		android.util.Log.v("test", "startPreview");
		device.createSession(holder.getSurface())
				.flatMapCompletable(new Function<RxCameraCaptureSession, CompletableSource>() {
					@Override
					public CompletableSource apply(RxCameraCaptureSession rxCameraCaptureSession) throws Exception {
						return rxCameraCaptureSession.setRepeatingRequest(holder.getSurface());
					}
				})
				.subscribe(new CompletableObserver() {
					@Override
					public void onSubscribe(Disposable d) {
					}

					@Override
					public void onComplete() {
					}

					@Override
					public void onError(Throwable e) {
						e.printStackTrace();
					}
				});
	}
}
