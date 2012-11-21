package com.thoughtsonmobile.jenkins.androiddevices;

import java.lang.String;

public enum AdbProperties {

	PRODUCT_MODEL("ro.product.model"), PRODUCT_VENDOR("ro.product.manufacturer"), DEVICE_IP(
			"dhcp.eth0.ipaddress"), VERSION_SDK("ro.build.version.sdk"), WLAN_IP("dhcp.wlan0.ipaddress"), VERSION("ro.build.version.release");

	private final String property;

	AdbProperties(final String property) {
		this.property = property;
	}

	@Override
	public String toString() {
		return this.property;
	}
}
