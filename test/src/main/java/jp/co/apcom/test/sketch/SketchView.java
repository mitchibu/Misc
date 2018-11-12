package jp.co.apcom.test.sketch;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PointF;
import android.graphics.PorterDuff;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;

import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;

@SuppressWarnings("unused")
public class SketchView extends View {
	private final GestureDetector gestureDetector;
	private final ScaleGestureDetector scaleGestureDetector;
	private final List<PathData> data = new ArrayList<>();
	private final DisplayCanvas displayCanvas = new DisplayCanvas();
	private final Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
	private final Matrix gestureMatrix = new Matrix();

	boolean isInGesture = false;
	private PathData current = null;

	public SketchView(Context context, @Nullable AttributeSet attrs) {
		super(context, attrs);
		paint.setStyle(Paint.Style.STROKE);

		gestureDetector = new GestureDetector(context, new GestureDetector.SimpleOnGestureListener() {
			@Override
			public boolean onDown(MotionEvent e) {
				PointF point = displayCanvas.mapPoints(e.getX(), e.getY());
				if(current == null) {
					current = new PathData();
					data.add(current.color(paint.getColor()).width(paint.getStrokeWidth()));
				} else {
					current.points.clear();
					current.color(paint.getColor()).width(paint.getStrokeWidth());
				}
				current.points.add(point);
				return true;
			}

			@Override
			public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
				if(isInGesture) {
					displayCanvas.postTranslate(-distanceX, -distanceY);
					gestureMatrix.postTranslate(-distanceX, -distanceY);
				} else {
					PointF point = displayCanvas.mapPoints(e2.getX(), e2.getY());
					PointF prev = current.points.get(current.points.size() - 1);
					current.points.add(point);
					displayCanvas.drawLine(prev.x, prev.y, point.x, point.y, paint);
				}
				invalidate();
				return true;
			}
		});
		scaleGestureDetector = new ScaleGestureDetector(context, new ScaleGestureDetector.SimpleOnScaleGestureListener() {
			@Override
			public boolean onScale(ScaleGestureDetector detector) {
				float s = detector.getScaleFactor();
				displayCanvas.postScale(s, s, detector.getFocusX(), detector.getFocusY());
				gestureMatrix.postScale(s, s, detector.getFocusX(), detector.getFocusY());
				invalidate();
				return true;
			}
		});
	}

	public void setColor(int color) {
		paint.setColor(color);
	}

	public void setStrokeWidth(float width) {
		paint.setStrokeWidth(width);
	}

	@Override
	protected void onDraw(Canvas canvas) {
		if(isInGesture) {
			canvas.save();
			canvas.concat(gestureMatrix);
			canvas.drawBitmap(displayCanvas.getBitmap(), 0, 0, null);
			canvas.restore();
		} else {
			canvas.drawBitmap(displayCanvas.getBitmap(), 0, 0, null);
		}
	}

	@Override
	protected void onSizeChanged(int w, int h, int oldw, int oldh) {
		displayCanvas.init(w, h);
	}

	@Override
	protected void onDetachedFromWindow() {
		super.onDetachedFromWindow();
		displayCanvas.release();
	}

	@Override
	public boolean performClick() {
		return super.performClick();
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		performClick();
		switch(event.getActionMasked()) {
		case MotionEvent.ACTION_POINTER_DOWN:
			isInGesture = true;
			gestureMatrix.reset();
			current.points.clear();
			break;
		case MotionEvent.ACTION_UP:
		case MotionEvent.ACTION_CANCEL:
			isInGesture = false;
			load(data);
			break;
		}
		boolean b1 = gestureDetector.onTouchEvent(event);
		boolean b2 = scaleGestureDetector.onTouchEvent(event);
		return b1 || b2;
	}

	private void load(List<PathData> data) {
		Path path = new Path();
		Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
		paint.setStyle(Paint.Style.STROKE);
		displayCanvas.drawColor(0, PorterDuff.Mode.CLEAR);
		for(PathData d : data) {
			paint.setColor(d.color);
			paint.setStrokeWidth(d.width);
			ListIterator<PointF> i = d.points.listIterator();
			while(i.hasNext()) {
				PointF point = i.next();
				if(i.nextIndex() == 1) path.moveTo(point.x, point.y);
				else path.lineTo(point.x, point.y);
			}
			displayCanvas.drawPath(path, paint);
			path.rewind();
		}
		current = null;
		postInvalidate();
	}

	static class PathData {
		final List<PointF> points = new ArrayList<>();

		public int color = 0;
		public float width = 0;

		PathData color(int color) {
			this.color = color;
			return this;
		}

		PathData width(float width) {
			this.width = width;
			return this;
		}
	}
}
