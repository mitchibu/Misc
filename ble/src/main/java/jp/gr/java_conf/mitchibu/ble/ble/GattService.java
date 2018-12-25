package jp.gr.java_conf.mitchibu.ble.ble;

import android.bluetooth.BluetoothGattService;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface GattService {
	String uuid();
	int type() default BluetoothGattService.SERVICE_TYPE_PRIMARY;
}
