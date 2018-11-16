package jp.co.apcom.coordinatorlayout;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

public class MultipleAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
	private final List<RecyclerView.Adapter<RecyclerView.ViewHolder>> adapters = new ArrayList<>();

	public void addAdapter(RecyclerView.Adapter<RecyclerView.ViewHolder> adapter) {
		adapters.add(adapter);
	}

	@Override
	public int getItemViewType(int position) {
		AdapterPosition p = findAdapter(position);
		return p.adapter.getItemViewType(p.position);
	}

	@NonNull
	@Override
	public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int type) {
		return null;
	}

	@Override
	public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
		AdapterPosition p = findAdapter(position);
		p.adapter.onBindViewHolder(holder, p.position);
	}

	@Override
	public int getItemCount() {
		int counter = 0;
		for(RecyclerView.Adapter adapter : adapters) {
			counter += adapter.getItemCount();
		}
		return counter;
	}

	private AdapterPosition findAdapter(int position) {
		int counter = 0;
		for(RecyclerView.Adapter<RecyclerView.ViewHolder> adapter : adapters) {
			int startPosition = counter;
			int endPosition = startPosition + adapter.getItemCount();
			if(startPosition <= position && endPosition > position) {
				return new AdapterPosition(adapter, position - startPosition);
			}
			counter = endPosition;
		}
		return null;
	}

	private static class AdapterPosition {
		RecyclerView.Adapter<RecyclerView.ViewHolder> adapter;
		int position;
		AdapterPosition(RecyclerView.Adapter<RecyclerView.ViewHolder> adapter, int position) {
			this.adapter = adapter;
			this.position = position;
		}
	}
}
