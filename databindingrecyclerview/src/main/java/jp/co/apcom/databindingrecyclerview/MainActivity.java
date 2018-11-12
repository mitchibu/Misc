package jp.co.apcom.databindingrecyclerview;

import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import jp.co.apcom.databindingrecyclerview.databinding.ActivityMainBinding;
import jp.co.apcom.databindingrecyclerview.databinding.ListItemBinding;
import jp.co.apcom.databindingrecyclerview.databinding.ListHeaderItemBinding;

public class MainActivity extends AppCompatActivity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		ActivityMainBinding binding =  DataBindingUtil.setContentView(this, R.layout.activity_main);

		List<Item> items = new ArrayList<>();
		items.add(new ItemData("Item1"));
		items.add(new ItemData("Item2"));
		items.add(new ItemData("Item3"));
		items.add(new ItemData("Item4"));
		items.add(new ItemData("Item5"));
		items.clear();
		for(int i = 0; i < 100; ++ i) {
			if(i % 5 == 0) {
				items.add(new ItemHeader("header: " + i));
			}
			items.add(new ItemData("https://gpad-img.com/201403/android.jpg"));
			items.add(new ItemData("https://2.bp.blogspot.com/-2ZMkSo7CnUs/WvMvSK0u9RI/AAAAAAAAFZA/zJOCZ8LUM8ol3hcHYHwVyOpc3iiYaxquACLcBGAs/s1600/Jetpack_logo.png"));
		}

		BindableAdapter adapter = new BindableAdapter(items);
		adapter.setOnItemClickListener(new OnItemClickListener<Item>() {
			@Override
			public void onItemClick(View view, int position, Item data) {
				android.util.Log.v("test", "click: " + position);
			}
		});
		binding.recycler.setLayoutManager(new LinearLayoutManager(this));
		binding.recycler.setAdapter(adapter);
	}

	private static class BindableAdapter extends RecyclerView.Adapter<BindableViewHolder> {
		private List<Item> items;

		private OnItemClickListener onItemClickListener = null;

		BindableAdapter(List<Item> items) {
			setItems(items);
		}

		void setItems(List<Item> items) {
			this.items = items;
			notifyDataSetChanged();
		}

		void setOnItemClickListener(OnItemClickListener listener) {
			onItemClickListener = listener;
		}

		@Override
		public int getItemViewType(int position) {
			return items.get(position).type;
		}

		@Override
		public BindableViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
			switch(viewType) {
			case 0:
				return new BindableViewHolder<>(ListHeaderItemBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));
			case 1:
				return new BindableViewHolder<>(ListItemBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));
			default:
				throw new RuntimeException();
			}
		}

		@Override
		public void onBindViewHolder(BindableViewHolder holder, int position) {
			if(items == null) return;

			final Item item = items.get(position);
			ViewDataBinding binding = holder.getBinding();
			if(binding instanceof ListItemBinding) {
				((ListItemBinding)holder.getBinding()).setPosition(position);
				((ListItemBinding)holder.getBinding()).setItem((ItemData)item);
				((ListItemBinding)holder.getBinding()).setListener(onItemClickListener);
			} else if(binding instanceof ListHeaderItemBinding) {
				((ListHeaderItemBinding)holder.getBinding()).setItem((ItemHeader)item);
			}
			holder.getBinding().executePendingBindings();
		}

		@Override
		public int getItemCount() {
			return items == null ? 0 : items.size();
		}
	}
}
