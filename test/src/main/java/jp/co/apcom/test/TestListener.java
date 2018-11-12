package jp.co.apcom.test;

import android.view.MotionEvent;
import android.view.View;

public class TestListener implements View.OnTouchListener {
	@Override
	public boolean onTouch(View v, MotionEvent event) {
		android.util.Log.v("test", "onTouch1: " + event);
		return false;
	}
}
