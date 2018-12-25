package jp.gr.java_conf.mitchibu.ble.ble;

import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattServer;
import android.bluetooth.BluetoothGattServerCallback;
import android.bluetooth.BluetoothGattService;
import android.bluetooth.BluetoothManager;
import android.content.Context;

import java.lang.reflect.Method;
import java.security.InvalidParameterException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class GattServer {
	private final BluetoothGattServerCallback callback = new BluetoothGattServerCallback() {
		@Override
		public void onConnectionStateChange(BluetoothDevice device, int status, int newState) {
			if(stateObserver == null) return;
			stateObserver.onStateChange(device, status, newState);
		}

		@Override
		public void onCharacteristicReadRequest(BluetoothDevice device, int requestId, int offset, BluetoothGattCharacteristic characteristic) {
			int status = BluetoothGatt.GATT_FAILURE;
			byte[] value = null;
			GattMap m = map.get(characteristic.getService().getUuid().toString());
			if(m != null) {
				Method method = m.map.get(characteristic.getUuid().toString());
				if(method != null) {
					try {
						Class<?>[] types = method.getParameterTypes();
						List<Object> params = new ArrayList<>();
						for(Class<?> type : types) {
							if(type.isAssignableFrom(BluetoothDevice.class)) params.add(device);
							else if(type.isAssignableFrom(int.class)) params.add(requestId);
							else if(type.isAssignableFrom(int.class)) params.add(offset);
							else if(type.isAssignableFrom(BluetoothGattCharacteristic.class))
								params.add(characteristic);
						}
						GattResult result = (GattResult)method.invoke(m.service, params.toArray(new Object[0]));
						value = result.value;
						status = result.status;
					} catch(Exception e) {
						e.printStackTrace();
					}
				}
			}
			server.sendResponse(device, requestId, status, offset, value);
		}

		@Override
		public void onCharacteristicWriteRequest(BluetoothDevice device, int requestId, BluetoothGattCharacteristic characteristic, boolean preparedWrite, boolean responseNeeded, int offset, byte[] value) {
			int status = BluetoothGatt.GATT_FAILURE;
			GattMap m = map.get(characteristic.getService().getUuid().toString());
			if(m != null) {
				Method method = m.map.get(characteristic.getUuid().toString());
				if(method != null) {
					try {
						Class<?>[] types = method.getParameterTypes();
						List<Object> params = new ArrayList<>();
						for(Class<?> type : types) {
							if(type.isAssignableFrom(BluetoothDevice.class)) params.add(device);
							else if(type.isAssignableFrom(int.class)) params.add(requestId);
							else if(type.isAssignableFrom(BluetoothGattCharacteristic.class)) params.add(characteristic);
							else if(type.isAssignableFrom(boolean.class)) params.add(preparedWrite);
							else if(type.isAssignableFrom(boolean.class)) params.add(responseNeeded);
							else if(type.isAssignableFrom(int.class)) params.add(offset);
							else if(type.isAssignableFrom(byte[].class)) params.add(value);
						}
						GattResult result = (GattResult)method.invoke(m.service, params.toArray(new Object[0]));
						status = result.status;
					} catch(Exception e) {
						e.printStackTrace();
					}
				}
			}
			if(responseNeeded) {
				server.sendResponse(device, requestId, status, offset, null);
			}
		}
	};
	private final Map<String, GattMap> map = new HashMap<>();
	private final BluetoothGattServer server;
	private final StateObserver stateObserver;

	public GattServer(Context context, StateObserver stateObserver) {
		BluetoothManager bm = (BluetoothManager)context.getSystemService(Context.BLUETOOTH_SERVICE);
		server = bm.openGattServer(context.getApplicationContext(), callback);
		this.stateObserver = stateObserver;
	}

	public void close() {
		server.close();
	}

	public void addService(Object impl) {
		Class<?> clazz = impl.getClass();
		GattService gattService = clazz.getAnnotation(GattService.class);
		if(gattService == null) throw new InvalidParameterException();

		String serviceUuid = gattService.uuid();
		BluetoothGattService service = new BluetoothGattService(
				UUID.fromString(serviceUuid),
				gattService.type());

		GattMap gattMap = new GattMap(impl);
		Method[] methods = clazz.getMethods();
		for(Method method : methods) {
			if(method.getReturnType().isAssignableFrom(GattResult.class)) {
				GattCharacteristic gattCharacteristic = method.getAnnotation(GattCharacteristic.class);
				if(gattCharacteristic != null) {
					String characteristicUuid = gattCharacteristic.uuid();
					BluetoothGattCharacteristic characteristic = new BluetoothGattCharacteristic(
							UUID.fromString(characteristicUuid),
							gattCharacteristic.properties(),
							gattCharacteristic.permissions());
					service.addCharacteristic(characteristic);
					gattMap.map.put(characteristicUuid, method);
				}
			}
		}
		if(!gattMap.map.isEmpty()) map.put(serviceUuid, gattMap);
		if(!map.isEmpty()) server.addService(service);
	}

	public interface StateObserver {
		void onStateChange(BluetoothDevice device, int status, int newState);
	}

	public static class GattResult {
		public static GattResult success() {
			return success(null);
		}

		public static GattResult success(byte[] value) {
			return new GattResult(BluetoothGatt.GATT_SUCCESS, value);
		}

		public static GattResult failure() {
			return new GattResult(BluetoothGatt.GATT_FAILURE);
		}

		private final int status;
		private final byte[] value;

		public GattResult(int status) {
			this(status, null);
		}

		public GattResult(int status, byte[] value) {
			this.status = status;
			this.value = value;
		}
	}

	private static class GattMap {
		final Object service;
		final Map<String, Method> map = new HashMap<>();

		GattMap(Object service) {
			this.service = service;
		}
	}
}
