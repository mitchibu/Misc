package jp.gr.java_conf.mitchibu.permission.permission;

import android.Manifest;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;

public class PermissionManager {
	private static final String TAG = MyFragment.class.getName();
	public void requestPermissions(FragmentManager fm, String... permissions) {
		MyFragment fragment = (MyFragment)fm.findFragmentByTag(TAG);
		if(fragment == null) {
			fragment = new MyFragment();
			fm.beginTransaction().add(fragment, TAG).commitNow();
		}
		fragment.requestPermissions(permissions, 0);
	}

	public static class MyFragment extends Fragment {
		@Override
		public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
		}
	}
}
