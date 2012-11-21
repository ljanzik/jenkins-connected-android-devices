package com.thoughtsonmobile.jenkins.androiddevices.model;

public class AndroidDevice {
	
	private final String ip;
	private final String deviceId;
	private final String version;
	private final int apiLevel;
	private final String name;
	
	public AndroidDevice(final String deviceId, final String name, final String ip, final String version, final int apiLevel)
	{
		this.ip = ip;
		this.deviceId = deviceId;
		this.version = version;
		this.apiLevel = apiLevel;
		this.name = name;
	}
	
	public int getApiLevel() {
		return apiLevel;
	}
	public String getDeviceId() {
		return deviceId;
	}
	public String getIp() {
		return ip;
	}
	public String getName() {
		return name;
	}
	public String getVersion() {
		return version;
	}
	
	@Override
	public String toString() {
		return String.format("Device: %s, id=%s, version=%s, sdk=%d, ip=%s",  name, deviceId, version, apiLevel, ip);
	}
	

}
