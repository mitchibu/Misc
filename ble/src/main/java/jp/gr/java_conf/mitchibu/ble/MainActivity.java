package jp.gr.java_conf.mitchibu.ble;

import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.le.AdvertiseSettings;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import java.util.UUID;

import jp.gr.java_conf.mitchibu.ble.ble.GattCharacteristic;
import jp.gr.java_conf.mitchibu.ble.ble.GattObserver;
import jp.gr.java_conf.mitchibu.ble.ble.GattParams;
import jp.gr.java_conf.mitchibu.ble.ble.GattResult;
import jp.gr.java_conf.mitchibu.ble.ble.GattServer;
import jp.gr.java_conf.mitchibu.ble.ble.GattService;
import jp.gr.java_conf.mitchibu.ble.ble.Peripheral;

public class MainActivity extends AppCompatActivity {
	private static final String UUID_SERVICE = "7080ec6e-7598-4c3c-b9ff-965b0b16ad55";
	private static final String UUID_CHARACTERISTIC_READ = "e4987788-74e6-406b-9ee0-37862e2f30d1";
	private static final String UUID_CHARACTERISTIC_WRITE = "bf0240bd-d7e0-4f91-b15d-376b7105488e";

	private Peripheral peripheral;
	private GattServer server;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		server = new GattServer(this, null);
		server.addService(new GattServiceImpl());

		peripheral = new Peripheral.Builder(this)
				.setAdvertiseMode(AdvertiseSettings.ADVERTISE_MODE_LOW_POWER)
				.setConnectable(true)
				.setTimeout(0)
				.setTxPowerLevel(AdvertiseSettings.ADVERTISE_TX_POWER_LOW)
				.addServiceUuid(UUID.fromString(UUID_SERVICE)).build();
		peripheral.start();
//
//		// test
//		BluetoothGattService sv = new BluetoothGattService(UUID.fromString(UUID_SERVICE), 0);
//		BluetoothGattCharacteristic ch = new BluetoothGattCharacteristic(UUID.fromString(UUID_CHARACTERISTIC_READ), 0, 0);
//		sv.addCharacteristic(ch);
//		ch = sv.getCharacteristic(UUID.fromString(UUID_CHARACTERISTIC_READ));
//		server.callback.onCharacteristicReadRequest(null, 100, 100, ch);
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		peripheral.stop();
		server.close();
	}

/*	private void test() {
		BluetoothManager bm = (BluetoothManager)getSystemService(BLUETOOTH_SERVICE);
		BluetoothAdapter adapter = bm.getAdapter();
		BluetoothLeAdvertiser advertiser = adapter.getBluetoothLeAdvertiser();

		// 設定
		AdvertiseSettings.Builder settingBuilder = new AdvertiseSettings.Builder();
		settingBuilder.setAdvertiseMode(AdvertiseSettings.ADVERTISE_MODE_LOW_POWER);
		settingBuilder.setConnectable(false);
		settingBuilder.setTimeout(0);
		settingBuilder.setTxPowerLevel(AdvertiseSettings.ADVERTISE_TX_POWER_LOW);
		AdvertiseSettings settings = settingBuilder.build();

		// アドバタイジングデータ
		AdvertiseData.Builder dataBuilder = new AdvertiseData.Builder();
		dataBuilder.addServiceUuid(new ParcelUuid(UUID.fromString(UUID_SERVICE)));
		AdvertiseData advertiseData = dataBuilder.build();

		//アドバタイズを開始
		advertiser.startAdvertising(settings, advertiseData, new AdvertiseCallback() {
			@Override
			public void onStartSuccess(AdvertiseSettings settingsInEffect) {
				super.onStartSuccess(settingsInEffect);
			}

			@Override
			public void onStartFailure(int errorCode) {
				super.onStartFailure(errorCode);
			}
		});
	}*/

	@GattService(uuid = UUID_SERVICE)
	static class GattServiceImpl implements GattObserver {
		@GattCharacteristic(
				uuid = UUID_CHARACTERISTIC_READ,
				properties = BluetoothGattCharacteristic.PROPERTY_READ,
				permissions = BluetoothGattCharacteristic.PERMISSION_READ)
		public GattResult readTest(GattParams params) {
			return GattResult.success();
		}

		@GattCharacteristic(
				uuid = UUID_CHARACTERISTIC_WRITE,
				properties = BluetoothGattCharacteristic.PROPERTY_WRITE,
				permissions = BluetoothGattCharacteristic.PERMISSION_WRITE)
		public GattResult writeTest(GattParams params) {
			return GattResult.success();
		}
	}
}
