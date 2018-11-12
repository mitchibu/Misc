package jp.co.apcom.sample.rx;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;

import io.reactivex.Observable;
import io.reactivex.subjects.PublishSubject;

public class gomi_RxPermission {
	private static final String TAG = gomi_RxPermission.class.getName();

	public static Observable<PermissionResult> request(FragmentActivity activity, String... permissions) {
		return request(activity.getSupportFragmentManager(), permissions);
	}

	private static Observable<PermissionResult> request(FragmentManager fm, String... permissions) {
		PermissionFragment fragment = (PermissionFragment)fm.findFragmentByTag(TAG);
		if(fragment == null) {
			fragment = new PermissionFragment();
			fm.beginTransaction().add(fragment, TAG).commitNow();
		}
		fragment.requestPermissions(permissions, 1);
		return fragment.subject;
	}

	public static class PermissionFragment extends Fragment {
		PublishSubject<PermissionResult> subject = PublishSubject.create();

		@Override
		public void onCreate(@Nullable Bundle savedInstanceState) {
			super.onCreate(savedInstanceState);
		}

		@Override
		public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
			subject.onNext(new PermissionResult(permissions, grantResults));
		}
	}

	public static class PermissionResult {
		PermissionResult(String[] permissions, @NonNull int[] grantResults) {
		}
	}
}
