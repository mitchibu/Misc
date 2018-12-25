package jp.gr.java_conf.mitchibu.ble.ble;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothManager;
import android.bluetooth.le.AdvertiseCallback;
import android.bluetooth.le.AdvertiseData;
import android.bluetooth.le.AdvertiseSettings;
import android.bluetooth.le.BluetoothLeAdvertiser;
import android.content.Context;
import android.os.ParcelUuid;

import java.util.UUID;

public class Peripheral {
	private AdvertiseCallback callback = new AdvertiseCallback() {
		@Override
		public void onStartSuccess(AdvertiseSettings settingsInEffect) {
		}

		@Override
		public void onStartFailure(int errorCode) {
		}
	};
	private final BluetoothLeAdvertiser advertiser;
	private final AdvertiseSettings settings;
	private final AdvertiseData data;

	private Peripheral(Context context, AdvertiseSettings settings, AdvertiseData data) {
		BluetoothManager bm = (BluetoothManager)context.getSystemService(Context.BLUETOOTH_SERVICE);
		BluetoothAdapter adapter = bm.getAdapter();
		advertiser = adapter.getBluetoothLeAdvertiser();
		if(advertiser == null) throw new UnsupportedOperationException("This device is not supported advertising.");
		this.settings = settings;
		this.data = data;
	}

	public void start() {
		advertiser.startAdvertising(settings, data, callback);
	}

	public void stop() {
		advertiser.stopAdvertising(callback);
	}

	public static class Builder {
		private final AdvertiseSettings.Builder settingBuilder = new AdvertiseSettings.Builder();
		private final AdvertiseData.Builder dataBuilder = new AdvertiseData.Builder();
		private final Context context;

		public Builder(Context context) {
			this.context = context;
		}

		public Builder setAdvertiseMode(int mode) {
			settingBuilder.setAdvertiseMode(mode);
			return this;
		}

		public Builder setConnectable(boolean isConnectable) {
			settingBuilder.setConnectable(isConnectable);
			return this;
		}

		public Builder setTimeout(int timeout) {
			settingBuilder.setTimeout(timeout);
			return this;
		}

		public Builder setTxPowerLevel(int level) {
			settingBuilder.setTxPowerLevel(level);
			return this;
		}

		public Builder addServiceUuid(UUID uuid) {
			dataBuilder.addServiceUuid(new ParcelUuid(uuid));
			return this;
		}

		public Peripheral build() {
			return new Peripheral(context.getApplicationContext(), settingBuilder.build(), dataBuilder.build());
		}
	}
}
