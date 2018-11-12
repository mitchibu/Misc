package jp.co.apcom.rx;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;

import io.reactivex.Single;
import io.reactivex.SingleEmitter;
import io.reactivex.SingleOnSubscribe;
import io.reactivex.subjects.PublishSubject;
import io.reactivex.subjects.SingleSubject;
import io.reactivex.subjects.Subject;

public class RxPermission {
	private static final String TAG = "tag";
	private static final String KEY = "permissions";

	public static Single<PermissionResult> request(final FragmentManager fm, final String... permissions) {
		Bundle args = new Bundle();
		args.putStringArray(KEY, permissions);
		final MyFragment fragment = new MyFragment();
		fragment.setArguments(args);
		fm.beginTransaction().add(fragment, TAG).commit();
		return fragment.subject;
//		return Single.create(new SingleOnSubscribe<PermissionResult>() {
//			@Override
//			public void subscribe(SingleEmitter<PermissionResult> e) throws Exception {
//				fm.beginTransaction().add(fragment, TAG).commit();
//			}
//		});
	}

	public static class MyFragment extends Fragment {
		private SingleSubject<PermissionResult> subject = SingleSubject.create();

		@Override
		public void onCreate(@Nullable Bundle savedInstanceState) {
			super.onCreate(savedInstanceState);
			requestPermissions(getArguments().getStringArray(KEY), 0);
		}

		@Override
		public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
			super.onRequestPermissionsResult(requestCode, permissions, grantResults);
			getFragmentManager().beginTransaction().remove(this).commitNow();
		}
	}

	public static class PermissionResult {
	}
}
