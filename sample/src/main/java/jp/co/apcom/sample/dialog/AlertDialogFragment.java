package jp.co.apcom.sample.dialog;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AlertDialog;

import java.util.Arrays;
import java.util.Iterator;
import java.util.Objects;

@SuppressWarnings("unused")
public class AlertDialogFragment extends DialogFragment implements DialogInterface.OnClickListener {
	private static final String EXTRA_TITLE = AlertDialogFragment.class.getName() + ".extra.TITLE";
	private static final String EXTRA_ICON = AlertDialogFragment.class.getName() + ".extra.ICON";
	private static final String EXTRA_MESSAGE = AlertDialogFragment.class.getName() + ".extra.MESSAGE";
	private static final String EXTRA_VIEW = AlertDialogFragment.class.getName() + ".extra.VIEW";
	private static final String EXTRA_POSITIVE = AlertDialogFragment.class.getName() + ".extra.POSITIVE";
	private static final String EXTRA_NEUTRAL = AlertDialogFragment.class.getName() + ".extra.NEUTRAL";
	private static final String EXTRA_NEGATIVE = AlertDialogFragment.class.getName() + ".extra.NEGATIVE";
	private static final String EXTRA_PARAMS = AlertDialogFragment.class.getName() + ".extra.PARAMS";

	private OnClickListener onClickListener = null;
	private OnCancelListener onCancelListener = null;
	private OnDismissListener onDismissListener = null;

	@Override
	public void onAttach(Context context) {
		super.onAttach(context);
		Object[] objects = {
				getActivity(),
				getParentFragment(),
				getTargetFragment()
		};
		Iterator<Object> ite = Arrays.asList(objects).iterator();
		while(ite.hasNext() && (onClickListener == null || onCancelListener == null || onDismissListener == null)) {
			Object object = ite.next();
			if(onClickListener == null && object instanceof OnClickListener) onClickListener = (OnClickListener)object;
			if(onCancelListener == null && object instanceof OnCancelListener) onCancelListener = (OnCancelListener)object;
			if(onDismissListener == null && object instanceof OnDismissListener) onDismissListener = (OnDismissListener)object;
		}
	}

	@NonNull
	@Override
	public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
		Bundle params = Objects.requireNonNull(getArguments()).getBundle(EXTRA_PARAMS);
		AlertDialog.Builder builder = new AlertDialog.Builder(Objects.requireNonNull(getContext()));
		title(builder, params);
		icon(builder, params);
		message(builder, params);
		view(builder, params);
		positive(builder, params);
		neutral(builder, params);
		negative(builder, params);
		return builder.create();
	}

	@Override
	public void onCancel(DialogInterface dialog) {
		if(onCancelListener != null) {
			onCancelListener.onCancel(this, Objects.requireNonNull(getArguments()).getBundle(EXTRA_PARAMS));
		}
	}

	@Override
	public void onDismiss(DialogInterface dialog) {
		if(onDismissListener != null) {
			onDismissListener.onDismiss(this, Objects.requireNonNull(getArguments()).getBundle(EXTRA_PARAMS));
		}
	}

	@Override
	public void onClick(DialogInterface dialog, int which) {
		dismiss();
		if(onClickListener != null) {
			onClickListener.onClick(AlertDialogFragment.this, which, Objects.requireNonNull(getArguments()).getBundle(EXTRA_PARAMS));
		}
	}

	protected void title(AlertDialog.Builder builder, Bundle params) {
		Object value = Objects.requireNonNull(getArguments()).get(EXTRA_TITLE);
		if(value instanceof CharSequence) builder.setTitle((CharSequence)value);
		if(value instanceof Integer) builder.setTitle((Integer)value);
	}

	protected void icon(AlertDialog.Builder builder, Bundle params) {
		Object value = Objects.requireNonNull(getArguments()).get(EXTRA_ICON);
		if(value instanceof Integer) builder.setIcon((Integer)value);
	}

	protected void message(AlertDialog.Builder builder, Bundle params) {
		Object value = Objects.requireNonNull(getArguments()).get(EXTRA_MESSAGE);
		if(value instanceof CharSequence) builder.setMessage((CharSequence)value);
		if(value instanceof Integer) builder.setMessage((Integer)value);
	}

	protected void view(AlertDialog.Builder builder, Bundle params) {
		Object value = Objects.requireNonNull(getArguments()).get(EXTRA_VIEW);
		if(value instanceof Integer) builder.setView((Integer)value);
	}

	protected void positive(AlertDialog.Builder builder, Bundle params) {
		Object value = Objects.requireNonNull(getArguments()).get(EXTRA_POSITIVE);
		if(value instanceof CharSequence) builder.setPositiveButton((CharSequence)value, this);
		if(value instanceof Integer) builder.setPositiveButton((Integer)value, this);
	}

	protected void neutral(AlertDialog.Builder builder, Bundle params) {
		Object value = Objects.requireNonNull(getArguments()).get(EXTRA_NEUTRAL);
		if(value instanceof CharSequence) builder.setNeutralButton((CharSequence)value, this);
		if(value instanceof Integer) builder.setNeutralButton((Integer)value, this);
	}

	protected void negative(AlertDialog.Builder builder, Bundle params) {
		Object value = Objects.requireNonNull(getArguments()).get(EXTRA_NEGATIVE);
		if(value instanceof CharSequence) builder.setNegativeButton((CharSequence)value, this);
		if(value instanceof Integer) builder.setNegativeButton((Integer)value, this);
	}

	public interface OnClickListener {
		void onClick(Fragment dialog, int which, Bundle params);
	}

	public interface OnCancelListener {
		void onCancel(Fragment dialog, Bundle params);
	}

	public interface OnDismissListener {
		void onDismiss(Fragment dialog, Bundle params);
	}

	@SuppressWarnings("WeakerAccess")
	public static class Builder {
		private final Bundle args = new Bundle();
		private final FragmentManager fm;
		private final DialogFragment dialog;

		public Builder(FragmentActivity activity) {
			this(new AlertDialogFragment(), activity.getSupportFragmentManager());
		}

		public Builder(Fragment fragment) {
			this(new AlertDialogFragment(), fragment.getChildFragmentManager());
		}

		protected Builder(DialogFragment dialog, FragmentManager fm) {
			this.fm = fm;
			this.dialog = dialog;
			dialog.setArguments(args);
		}

		public Builder cancelable(boolean cancelable) {
			dialog.setCancelable(cancelable);
			return this;
		}

		public Builder title(CharSequence title) {
			args.putCharSequence(EXTRA_TITLE, title);
			return this;
		}

		public Builder title(int title) {
			args.putInt(EXTRA_TITLE, title);
			return this;
		}

		public Builder icon(int icon) {
			args.putInt(EXTRA_ICON, icon);
			return this;
		}

		public Builder message(CharSequence message) {
			args.putCharSequence(EXTRA_MESSAGE, message);
			return this;
		}

		public Builder message(int message) {
			args.putInt(EXTRA_MESSAGE, message);
			return this;
		}

		public Builder view(int view) {
			args.putInt(EXTRA_VIEW, view);
			return this;
		}

		public Builder positive(CharSequence positive) {
			args.putCharSequence(EXTRA_POSITIVE, positive);
			return this;
		}

		public Builder positive(int positive) {
			args.putInt(EXTRA_POSITIVE, positive);
			return this;
		}

		public Builder neutral(CharSequence neutral) {
			args.putCharSequence(EXTRA_NEUTRAL, neutral);
			return this;
		}

		public Builder neutral(int neutral) {
			args.putInt(EXTRA_NEUTRAL, neutral);
			return this;
		}

		public Builder negative(CharSequence negative) {
			args.putCharSequence(EXTRA_NEGATIVE, negative);
			return this;
		}

		public Builder negative(int negative) {
			args.putInt(EXTRA_NEGATIVE, negative);
			return this;
		}

		public Builder params(Bundle params) {
			args.putBundle(EXTRA_PARAMS, params);
			return this;
		}

		@SuppressWarnings("UnusedReturnValue")
		public DialogFragment show(final String tag) {
			dialog.show(fm, tag);
			return dialog;
		}
	}
}
