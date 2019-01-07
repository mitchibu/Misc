package jp.gr.java_conf.mitchibu.ble.ble;

import android.bluetooth.BluetoothGatt;

public class GattResult {
	public static GattResult success() {
		return success(null);
	}

	public static GattResult success(byte[] value) {
		return new GattResult(BluetoothGatt.GATT_SUCCESS, value);
	}

	public static GattResult failure() {
		return new GattResult(BluetoothGatt.GATT_FAILURE);
	}

	final int status;
	final byte[] value;

	public GattResult(int status) {
		this(status, null);
	}

	public GattResult(int status, byte[] value) {
		this.status = status;
		this.value = value;
	}
}
