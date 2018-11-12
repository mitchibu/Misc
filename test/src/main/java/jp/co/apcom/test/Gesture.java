package jp.co.apcom.test;

import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;

public class Gesture implements GestureDetector.OnGestureListener, ScaleGestureDetector.OnScaleGestureListener {
	@Override
	public boolean onDown(MotionEvent e) {
		return false;
	}

	@Override
	public void onShowPress(MotionEvent e) {
	}

	@Override
	public boolean onSingleTapUp(MotionEvent e) {
		return false;
	}

	@Override
	public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
		return false;
	}

	@Override
	public void onLongPress(MotionEvent e) {
	}

	@Override
	public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
		return false;
	}

	@Override
	public boolean onScale(ScaleGestureDetector detector) {
		return false;
	}

	@Override
	public boolean onScaleBegin(ScaleGestureDetector detector) {
		return false;
	}

	@Override
	public void onScaleEnd(ScaleGestureDetector detector) {
	}
}
