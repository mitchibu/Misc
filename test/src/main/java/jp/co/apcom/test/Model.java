package jp.co.apcom.test;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.MediatorLiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.Observer;
import android.databinding.BindingAdapter;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.SparseIntArray;
import android.widget.RadioGroup;

import jp.co.apcom.test.sketch.SketchView;

public class Model extends AndroidViewModel {
	private static final SparseIntArray map = new SparseIntArray();
	static {
		map.put(Color.BLACK, R.id.black);
		map.put(Color.RED, R.id.red);
	}

	public final MutableLiveData<Integer> color = new MutableLiveData<>();
	public MediatorLiveData<Integer> idByColor = new MediatorLiveData<>();
	public final MutableLiveData<Integer> sketchColor = new MutableLiveData<>();

	public Model(@NonNull Application application) {
		super(application);
		idByColor.addSource(sketchColor, new Observer<Integer>() {
			@Override
			public void onChanged(@Nullable Integer color) {
				if(color == null) return;
				Integer id = map.get(color);
				if(id.equals(idByColor.getValue())) return;
				idByColor.setValue(id);
			}
		});
		sketchColor.setValue(Color.BLACK);
	}

	public void onSplitTypeChanged(RadioGroup view, int id) {
		int index = map.indexOfValue(id);
		if(index < 0) return;
		sketchColor.setValue(map.keyAt(index));
	}

	public void onCheckedChanged(boolean checked, int color) {
		if(!checked) return;
		sketchColor.setValue(color);
	}

	@BindingAdapter("sketchColor")
	public static void setSketchColor(SketchView view, int color) {
		android.util.Log.v("test", "setSketchColor: " + color);
		view.setColor(color);
	}
}
