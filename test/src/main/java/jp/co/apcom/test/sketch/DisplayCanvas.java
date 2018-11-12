package jp.co.apcom.test.sketch;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PointF;
import android.graphics.PorterDuff;
import android.support.annotation.NonNull;

class DisplayCanvas {
	private final Matrix inverseMatrix = new Matrix();
	private final Matrix matrix = new Matrix();
	private Bitmap bitmap;
	private Canvas canvas;

	@NonNull
	Bitmap getBitmap() {
		checkStatus();
		return bitmap;
	}

	void init(int width, int height) {
		if(bitmap != null) bitmap.recycle();
		bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
		canvas = new Canvas(bitmap);
	}

	void release() {
		bitmap.recycle();
		bitmap = null;
		canvas.setBitmap(null);
		canvas = null;
	}

	void drawColor(int color, PorterDuff.Mode mode) {
		checkStatus();
		canvas.drawColor(color, mode);
	}

	void drawPath(Path path, Paint paint) {
		checkStatus();
		canvas.drawPath(path, paint);
	}

	void drawLine(float x1, float y1, float x2, float y2, Paint paint) {
		checkStatus();
		canvas.drawLine(x1, y1, x2, y2, paint);
	}

	void postTranslate(float dx, float dy) {
		checkStatus();
		matrix.postTranslate(dx, dy);
		canvas.setMatrix(matrix);
	}

	void postScale(float sx, float sy, float px, float py) {
		checkStatus();
		matrix.postScale(sx, sy, px, py);
		canvas.setMatrix(matrix);
	}

	@NonNull
	PointF mapPoints(float x, float y) {
		checkStatus();
		float[] p = {x, y};
		matrix.invert(inverseMatrix);
		inverseMatrix.mapPoints(p);
		return new PointF(p[0], p[1]);
	}

	private void checkStatus() {
		if(bitmap == null || canvas == null) throw new IllegalStateException("DisplayCanvas not yet initialized!");
	}
}
