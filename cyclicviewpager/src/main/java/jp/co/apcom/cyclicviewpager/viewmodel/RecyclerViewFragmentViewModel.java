package jp.co.apcom.cyclicviewpager.viewmodel;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.paging.PagedList;
import android.arch.paging.RxPagedListBuilder;
import android.os.Bundle;
import android.support.annotation.NonNull;

import io.reactivex.BackpressureStrategy;
import io.reactivex.Flowable;
import jp.co.apcom.cyclicviewpager.room.EntityA;
import jp.co.apcom.cyclicviewpager.room.EntityA_DAO;

public class RecyclerViewFragmentViewModel extends AndroidViewModel implements SavableInstanceState {
	public Flowable<PagedList<EntityA>> modelList;

	public RecyclerViewFragmentViewModel(@NonNull Application application) {
		super(application);
	}

	public Flowable<PagedList<EntityA>> get(EntityA_DAO dao) {
		modelList = new RxPagedListBuilder<>(dao.get3(),/* page size */ 20).buildFlowable(BackpressureStrategy.LATEST);
		return modelList;
	}

	@Override
	public void writeTo(@NonNull Bundle bundle) {
	}

	@Override
	public void readFrom(@NonNull Bundle bundle) {
	}
}
