package jp.co.apcom.cyclicviewpager.viewmodel;

import android.app.Application;
import android.arch.paging.PagedList;
import android.arch.paging.RxPagedListBuilder;
import android.os.Bundle;
import android.support.annotation.NonNull;

import io.reactivex.BackpressureStrategy;
import io.reactivex.Flowable;
import jp.co.apcom.cyclicviewpager.room.EntityA;
import jp.co.apcom.cyclicviewpager.room.MyDatabase;

public class RecyclerViewFragmentViewModel extends ParcelableViewModel {
	public Flowable<PagedList<EntityA>> modelList;

	public RecyclerViewFragmentViewModel(@NonNull Application application) {
		super(application);
	}

	public void init(MyDatabase db) {
		modelList = new RxPagedListBuilder<>(db.entityADao().get3(),/* page size */ 20).buildFlowable(BackpressureStrategy.LATEST);
	}

	@Override
	public void writeTo(@NonNull Bundle bundle) {
	}

	@Override
	public void readFrom(@NonNull Bundle bundle) {
	}
}