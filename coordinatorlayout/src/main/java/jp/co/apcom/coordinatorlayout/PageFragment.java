package jp.co.apcom.coordinatorlayout;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class PageFragment extends Fragment {
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.fragment_page, container, false);
		RecyclerView list = v.findViewById(R.id.list);
		list.setLayoutManager(new LinearLayoutManager(getContext()));
		list.setAdapter(new Adapter());
		return v;
	}

	class Adapter extends RecyclerView.Adapter<Adapter.ViewHolder> {
		@NonNull
		@Override
		public Adapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
			return new ViewHolder(new TextView(viewGroup.getContext()));
		}

		@Override
		public void onBindViewHolder(@NonNull Adapter.ViewHolder viewHolder, int i) {
			((TextView)viewHolder.itemView).setText("position: " + i);
		}

		@Override
		public int getItemCount() {
			return 100;
		}

		class ViewHolder extends RecyclerView.ViewHolder {
			public ViewHolder(@NonNull View itemView) {
				super(itemView);
			}
		}
	}
}
