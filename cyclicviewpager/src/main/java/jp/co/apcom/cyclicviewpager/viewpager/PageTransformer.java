package jp.co.apcom.cyclicviewpager.viewpager;

import android.support.annotation.NonNull;
import android.support.v4.view.ViewPager;
import android.view.View;

public class PageTransformer implements ViewPager.PageTransformer {
	float minScaleOut = 0.9f;
	float minScaleIn = 0.9f;
	@Override
	public void transformPage(@NonNull View view, float position) {
		if(-1 < position && position <= 0) {
			view.setScaleX(Math.max(minScaleOut, 1 + position));
			view.setScaleY(Math.max(minScaleOut, 1 + position));
		}

		if(0 < position && position < 1) {
			view.setScaleX(Math.max(minScaleIn, 1 - position));
			view.setScaleY(Math.max(minScaleIn, 1 - position));
		}
	}
}
