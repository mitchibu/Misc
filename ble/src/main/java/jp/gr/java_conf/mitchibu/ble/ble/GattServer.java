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
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import jp.gr.java_conf.mitchibu.ble.ble.annotation.GattCharacteristic;
import jp.gr.java_conf.mitchibu.ble.ble.annotation.GattService;

public class GattServer {
	private final BluetoothGattServerCallback callback = new BluetoothGattServerCallback() {
		@Override
		public void onConnectionStateChange(BluetoothDevice device, int status, int newState) {
			if(stateObserver == null) return;
			stateObserver.onStateChange(device, status, newState);
		}

		@Override
		public void onCharacteristicReadRequest(BluetoothDevice device, int requestId, int offset, BluetoothGattCharacteristic characteristic) {
			GattParams params = new GattParams(device, requestId, offset, characteristic);
			GattResult result = invoke(params);
			server.sendResponse(device, requestId, result == null ? BluetoothGatt.GATT_FAILURE : result.status, offset, result == null ? null : result.value);
		}

		@Override
		public void onCharacteristicWriteRequest(BluetoothDevice device, int requestId, BluetoothGattCharacteristic characteristic, boolean preparedWrite, boolean responseNeeded, int offset, byte[] value) {
			GattParams params = new GattParams(device, requestId, characteristic, preparedWrite, responseNeeded, offset, value);
			GattResult result = invoke(params);
			if(responseNeeded) {
				server.sendResponse(device, requestId, result == null ? BluetoothGatt.GATT_FAILURE : result.status, offset, null);
			}
		}

		private GattResult invoke(GattParams params) {
			MethodMap m = services.get(params.characteristic.getService().getUuid().toString());
			if(m == null) return null;

			Method method = m.get(params.characteristic.getUuid().toString());
			if(method == null) return null;

			try {
				return (GattResult)method.invoke(m.observer, params);
			} catch(Exception e) {
				e.printStackTrace();
			}
			return null;
		}
	};

	private final Map<String, MethodMap> services = new HashMap<>();
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

	public void addService(GattObserver observer) {
		Class<?> clazz = observer.getClass();
		GattService gattService = clazz.getAnnotation(GattService.class);
		if(gattService == null) throw new InvalidParameterException();

		String serviceUuid = gattService.uuid();
		BluetoothGattService service = new BluetoothGattService(
				UUID.fromString(serviceUuid),
				gattService.type());

		MethodMap methodMap = new MethodMap(observer);
		Method[] methods = clazz.getMethods();
		for(Method method : methods) {
			if(isValidMethod(method)) {
				GattCharacteristic gattCharacteristic = method.getAnnotation(GattCharacteristic.class);
				if(gattCharacteristic != null) {
					String characteristicUuid = gattCharacteristic.uuid();
					BluetoothGattCharacteristic characteristic = new BluetoothGattCharacteristic(
							UUID.fromString(characteristicUuid),
							gattCharacteristic.properties(),
							gattCharacteristic.permissions());
					service.addCharacteristic(characteristic);
					methodMap.put(characteristicUuid, method);
				}
			}
		}
		if(!methodMap.isEmpty()) services.put(serviceUuid, methodMap);
		if(!services.isEmpty()) server.addService(service);
	}

	private boolean isValidMethod(Method method) {
		if(!method.getReturnType().isAssignableFrom(GattResult.class)) return false;

		Class<?>[] types = method.getParameterTypes();
		for(Class<?> type : types) {
			if(!type.isAssignableFrom(GattParams.class)) return false;
		}
		return true;
	}

	public interface StateObserver {
		void onStateChange(BluetoothDevice device, int status, int newState);
	}

	private static class MethodMap extends HashMap<String, Method> {
		final GattObserver observer;

		MethodMap(GattObserver observer) {
			this.observer = observer;
		}
	}
}
