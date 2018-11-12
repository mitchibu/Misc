package jp.co.apcom.dagger2;

import android.app.Activity;

import java.lang.reflect.Method;

public class InjectionUtils {
	public static <T extends Activity> void inject(T activity) {
		inject(((MyApplication)activity.getApplication()).appComponent, activity);
	}

	public static <T extends MainFragment> void inject(T fragment) {
		inject(((MyApplication)fragment.getActivity().getApplication()).appComponent, fragment);
	}

	public static void inject(AppComponent appComponent, Object o) {
		try {
			Method m = appComponent.getClass().getMethod("inject", o.getClass());
			m.invoke(appComponent, o);
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
}
