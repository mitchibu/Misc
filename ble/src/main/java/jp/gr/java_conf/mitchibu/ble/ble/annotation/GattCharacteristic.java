package jp.gr.java_conf.mitchibu.ble.ble.annotation;

import android.bluetooth.BluetoothGattCharacteristic;
import android.support.annotation.IntDef;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface GattCharacteristic {
	@IntDef(value = {
			BluetoothGattCharacteristic.PROPERTY_BROADCAST,
			BluetoothGattCharacteristic.PROPERTY_READ,
			BluetoothGattCharacteristic.PROPERTY_WRITE_NO_RESPONSE,
			BluetoothGattCharacteristic.PROPERTY_WRITE,
			BluetoothGattCharacteristic.PROPERTY_NOTIFY,
			BluetoothGattCharacteristic.PROPERTY_INDICATE,
			BluetoothGattCharacteristic.PROPERTY_SIGNED_WRITE,
			BluetoothGattCharacteristic.PROPERTY_EXTENDED_PROPS
	}, flag = true)
	@interface Property {}

	@IntDef(value = {
			BluetoothGattCharacteristic.PERMISSION_READ,
			BluetoothGattCharacteristic.PERMISSION_READ_ENCRYPTED,
			BluetoothGattCharacteristic.PERMISSION_READ_ENCRYPTED_MITM,
			BluetoothGattCharacteristic.PERMISSION_WRITE,
			BluetoothGattCharacteristic.PERMISSION_WRITE_ENCRYPTED,
			BluetoothGattCharacteristic.PERMISSION_WRITE_ENCRYPTED_MITM,
			BluetoothGattCharacteristic.PERMISSION_WRITE_SIGNED,
			BluetoothGattCharacteristic.PERMISSION_WRITE_SIGNED_MITM
	}, flag = true)
	@interface Permission {}

	String uuid();
	@Property int properties() default BluetoothGattCharacteristic.PROPERTY_READ|BluetoothGattCharacteristic.PROPERTY_WRITE|BluetoothGattCharacteristic.PROPERTY_NOTIFY;
	@Permission int permissions() default BluetoothGattCharacteristic.PERMISSION_READ|BluetoothGattCharacteristic.PERMISSION_WRITE;
}
