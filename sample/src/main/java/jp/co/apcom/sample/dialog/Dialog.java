package jp.co.apcom.sample.dialog;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.util.SparseArray;

import jp.co.apcom.sample.R;

public class Dialog {
	public static final int CODE_TEST = 0;

	private static final SparseArray<Message> map = new SparseArray<>();
	static {
		map.put(CODE_TEST, new Message(R.string.app_name, android.R.drawable.ic_dialog_info, 0, android.R.string.ok, 0));
	}

	public static void show(FragmentActivity activity, String tag, int code) {
		show(activity.getSupportFragmentManager(), tag, code, null);
	}

	public static void show(FragmentActivity activity, String tag, int code, CharSequence message) {
		show(activity.getSupportFragmentManager(), tag, code, message);
	}

	public static void show(Fragment fragment, String tag, int code) {
		show(fragment.getChildFragmentManager(), tag, code, null);
	}

	public static void show(Fragment fragment, String tag, int code, CharSequence message) {
		show(fragment.getChildFragmentManager(), tag, code, message);
	}

	private static void show(FragmentManager fm, String tag, int code, CharSequence message) {
		Message m = map.get(code, new Message(R.string.app_name, 0, R.string.app_name, android.R.string.ok, 0));
		AlertDialogFragment.Builder builder = new AlertDialogFragment.Builder(new AlertDialogFragment(), fm)
				.cancelable(false)
				.title(m.title);
		if(m.icon != 0) builder.icon(m.icon);
		if(m.message != 0) builder.message(m.message);
		else builder.message(message);
		if(m.positive != 0) builder.positive(m.positive);
		if(m.negative != 0) builder.positive(m.negative);
		builder.show(tag);
	}

	static class Message {
		final int title;
		final int icon;
		final int message;
		final int positive;
		final int negative;

		Message(int title, int icon, int message, int positive, int negative) {
			this.title = title;
			this.icon = icon;
			this.message = message;
			this.positive = positive;
			this.negative = negative;
		}
	}
}
