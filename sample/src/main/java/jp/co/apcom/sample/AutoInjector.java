package jp.co.apcom.sample;

import android.app.Activity;
import android.app.Application;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;

import dagger.android.AndroidInjection;
import dagger.android.support.AndroidSupportInjection;
import dagger.android.support.HasSupportFragmentInjector;

// https://qiita.com/satorufujiwara/items/0f95ccfc3820d3ee1370
public class AutoInjector {
	public static void register(Application app) {
		app.registerActivityLifecycleCallbacks(new Application.ActivityLifecycleCallbacks() {
			@Override
			public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
				if(activity instanceof Injectable || activity instanceof HasSupportFragmentInjector) {
					AndroidInjection.inject(activity);
				}
				if(activity instanceof FragmentActivity) {
					((FragmentActivity)activity).getSupportFragmentManager().registerFragmentLifecycleCallbacks(new FragmentManager.FragmentLifecycleCallbacks() {
						@Override
						public void onFragmentCreated(@NonNull FragmentManager fm, @NonNull Fragment f, @Nullable Bundle savedInstanceState) {
							if(f instanceof Injectable) {
								AndroidSupportInjection.inject(f);
							}
						}
					}, true);
				}
			}

			@Override
			public void onActivityStarted(Activity activity) {
			}

			@Override
			public void onActivityResumed(Activity activity) {
			}

			@Override
			public void onActivityPaused(Activity activity) {
			}

			@Override
			public void onActivityStopped(Activity activity) {
			}

			@Override
			public void onActivitySaveInstanceState(Activity activity, Bundle outState) {
			}

			@Override
			public void onActivityDestroyed(Activity activity) {
			}
		});
	}
}
