package jp.gr.java_conf.mitchibu.permission;

import android.Manifest;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import jp.gr.java_conf.mitchibu.permission.permission.PermissionManager;

public class MainActivity extends AppCompatActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		PermissionManager pm = new PermissionManager();
		pm.requestPermissions(getSupportFragmentManager(), Manifest.permission.CAMERA);
	}

	@Override
	public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
		super.onRequestPermissionsResult(requestCode, permissions, grantResults);
		android.util.Log.v("test", "onRequestPermissionsResult: " + grantResults);
	}
}
