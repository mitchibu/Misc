package jp.co.apcom.cyclicviewpager.viewmodel;

import android.os.Bundle;
import android.support.annotation.NonNull;

public interface SavableInstanceState {
	void writeTo(@NonNull Bundle bundle);
	void readFrom(@NonNull Bundle bundle);
}
