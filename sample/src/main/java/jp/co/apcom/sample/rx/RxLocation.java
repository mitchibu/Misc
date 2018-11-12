package jp.co.apcom.sample.rx;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.IntentSender;
import android.location.Location;
import android.os.Looper;
import android.support.annotation.NonNull;

import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.ResolvableApiException;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResponse;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.android.gms.location.SettingsClient;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Single;
import io.reactivex.SingleEmitter;
import io.reactivex.SingleOnSubscribe;
import io.reactivex.functions.Cancellable;

//https://akira-watson.com/android/fusedlocationproviderapi.html
//https://developers-jp.googleblog.com/2017/07/reduce-friction-with-new-location-apis.html
//https://qiita.com/jabachan/items/0811f040944a2ff2d731
public class RxLocation {
	public static void error(Throwable throwable, Activity activity) {
		if(throwable instanceof ApiException) {
			switch(((ApiException)throwable).getStatusCode()) {
			case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
				try {
					// ユーザに位置情報設定を変更してもらうためのダイアログを表示する
					((ResolvableApiException)throwable).startResolutionForResult(activity, 100);
				} catch(IntentSender.SendIntentException e) {
					// Ignore the error.
				} catch(ClassCastException e) {
					// Ignore, should be an impossible error.
				}
				break;
			case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
				// 位置情報が取得できず、その状態からの復帰も難しい時呼ばれる
				break;
			}
		}
	}

	private final Context context;

	private LocationRequest locationRequest = null;

	public RxLocation(Context context) {
		this.context = context;
		LocationRequest locationRequest = new LocationRequest();
		locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
		locationRequest.setInterval(0);
		locationRequest.setFastestInterval(0);
		setLocationRequest(locationRequest);
	}

	public RxLocation setLocationRequest(LocationRequest locationRequest) {
		this.locationRequest = locationRequest;
		return this;
	}

	public Single<LocationSettingsResponse> getCheckLocationSettings() {
		return Single.create(new SingleOnSubscribe<LocationSettingsResponse>() {
			@Override
			public void subscribe(final SingleEmitter<LocationSettingsResponse> emitter) {
				SettingsClient settingsClient = LocationServices.getSettingsClient(context);
				LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder();
				builder.addLocationRequest(locationRequest);
				settingsClient.checkLocationSettings(builder.build())
						.addOnSuccessListener(new OnSuccessListener<LocationSettingsResponse>() {
							@Override
							public void onSuccess(LocationSettingsResponse locationSettingsResponse) {
								emitter.onSuccess(locationSettingsResponse);
							}
						})
						.addOnFailureListener(new OnFailureListener() {
							@Override
							public void onFailure(@NonNull Exception e) {
								emitter.onError(e);
							}
						});
			}
		});
	}

	@SuppressLint("MissingPermission")
	public Observable<Location> requestLocation() {
		return Observable.create(new ObservableOnSubscribe<Location>() {
			@Override
			public void subscribe(final ObservableEmitter<Location> emitter) {
				final FusedLocationProviderClient fusedLocationClient = LocationServices.getFusedLocationProviderClient(context);
				final LocationCallback locationCallback = new LocationCallback() {
					@Override
					public void onLocationResult(LocationResult locationResult) {
						emitter.onNext(locationResult.getLastLocation());
					}
				};
				fusedLocationClient.requestLocationUpdates(locationRequest, locationCallback, Looper.myLooper())
						.addOnFailureListener(new OnFailureListener() {
							@Override
							public void onFailure(@NonNull Exception e) {
								emitter.onError(e);
							}
						});
				emitter.setCancellable(new Cancellable() {
					@Override
					public void cancel() {
						fusedLocationClient.removeLocationUpdates(locationCallback);
					}
				});
			}
		});
	}
}
