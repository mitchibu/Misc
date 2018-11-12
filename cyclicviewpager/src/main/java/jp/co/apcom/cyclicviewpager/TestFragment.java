package jp.co.apcom.cyclicviewpager;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.Objects;

public class TestFragment extends Fragment {
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
//		android.util.Log.v("test", "onCreateView");
		View v = inflater.inflate(R.layout.fragment_test, container, false);
//		TextView t = v.findViewById(R.id.test);
//		t.setText(Objects.requireNonNull(getArguments()).getString("test"));
		return v;
	}

	@Override
	public void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
//		android.util.Log.v("test", "onCreate");
	}

	@Override
	public void onActivityCreated(@Nullable Bundle savedInstanceState) {
//		android.util.Log.v("test", "onActivityCreated");
		super.onActivityCreated(savedInstanceState);
		TextView t = getView().findViewById(R.id.test);
		t.setText(Objects.requireNonNull(getArguments()).getString("test"));
	}
}
