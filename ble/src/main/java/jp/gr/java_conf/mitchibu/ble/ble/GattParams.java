package jp.gr.java_conf.mitchibu.ble.ble;

import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGattCharacteristic;

public class GattParams {
	public final BluetoothDevice device;
	public final int requestId;
	public final int offset;
	public final BluetoothGattCharacteristic characteristic;
	public final boolean preparedWrite;
	public final boolean responseNeeded;
	public final byte[] value;

	public GattParams(BluetoothDevice device, int requestId, int offset, BluetoothGattCharacteristic characteristic) {
		this.device = device;
		this.requestId = requestId;
		this.offset = offset;
		this.characteristic = characteristic;
		this.preparedWrite = false;
		this.responseNeeded = true;
		this.value = null;
	}

	public GattParams(BluetoothDevice device, int requestId, BluetoothGattCharacteristic characteristic, boolean preparedWrite, boolean responseNeeded, int offset, byte[] value) {
		this.device = device;
		this.requestId = requestId;
		this.characteristic = characteristic;
		this.preparedWrite = preparedWrite;
		this.responseNeeded = responseNeeded;
		this.offset = offset;
		this.value = value;
	}
}
