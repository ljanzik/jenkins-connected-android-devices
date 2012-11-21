package com.thoughtsonmobile.jenkins.androiddevices;

import hudson.Extension;
import hudson.Launcher.LocalLauncher;
import hudson.model.RootAction;
import hudson.model.TaskListener;
import hudson.remoting.Callable;
import hudson.util.ArgumentListBuilder;
import hudson.util.StreamTaskListener;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import jenkins.model.Jenkins;

import com.thoughtsonmobile.jenkins.androiddevices.command.AdbDevicesCommand;
import com.thoughtsonmobile.jenkins.androiddevices.model.AndroidDevice;

@Extension
public class AndroidDeviceList implements RootAction {

	private static class FetchTask implements
			Callable<List<AndroidDevice>, IOException> {

		private final TaskListener listener;

		private FetchTask(final TaskListener listener) {
			this.listener = listener;
		}

		public List<AndroidDevice> call() throws IOException {
			try {
				final AdbDevicesCommand command = new AdbDevicesCommand();
				final List<AndroidDevice> deviceList = new ArrayList<AndroidDevice>();

				final ByteArrayOutputStream stream = new ByteArrayOutputStream();

				LocalLauncher launcher = new LocalLauncher(listener);

				final int result = launcher.launch()
						.cmds(command.getCommand()).stdout(stream).join();
				if (result == 0) {
					stream.close();
					final String deviceResults = new String(
							stream.toByteArray());
					final Pattern pattern = Pattern.compile(
							"^(\\S*)\\s*device", Pattern.MULTILINE);
					final Matcher matcher = pattern.matcher(deviceResults);
					final List<String> devices = new ArrayList<String>();
					while (matcher.find()) {
						final String match = matcher.group(1);
						devices.add(match);
					}
					System.out.println("Found " + devices.size()
							+ " connected Android devices\n");
					final Pattern propPattern = Pattern.compile(
							"^\\[(.*)\\]:(.*)\\[(.*)\\]$", Pattern.MULTILINE);
					for (final String device : devices) {
						final HashMap<String, String> deviceDetails = new HashMap<String, String>();
						final ByteArrayOutputStream deviceStream = new ByteArrayOutputStream();
						final ByteArrayOutputStream errorStream = new ByteArrayOutputStream();
						final int resultDetails = launcher
								.launch()
								.cmds(new ArgumentListBuilder(
										"/Users/soerenleif/android-sdks/platform-tools/adb",
										"-s", device, "shell", "getprop"))
								.stdout(deviceStream).stderr(errorStream)
								.join();
						deviceStream.close();
						launcher = null;
						if (resultDetails == 0) {

							final String deviceProperties = new String(
									deviceStream.toByteArray());
							final Matcher propMatcher = propPattern
									.matcher(deviceProperties);
							while (propMatcher.find()) {
								deviceDetails.put(propMatcher.group(1),
										propMatcher.group(3));
							}
							final String ip = deviceDetails
									.containsKey(AdbProperties.DEVICE_IP
											.toString()) ? deviceDetails
									.get(AdbProperties.DEVICE_IP.toString())
									: deviceDetails.get(AdbProperties.WLAN_IP
											.toString());
							final String deviceName = deviceDetails.get(
									AdbProperties.PRODUCT_MODEL.toString())
									.equals("ST70104-1") ? "TrekStor SurfTab breeze"
									: deviceDetails
											.get(AdbProperties.PRODUCT_VENDOR
													.toString())
											+ " "
											+ deviceDetails
													.get(AdbProperties.PRODUCT_MODEL
															.toString());
							final AndroidDevice androidDevice = new AndroidDevice(
									device, deviceName, ip,
									deviceDetails.get(AdbProperties.VERSION
											.toString()),
									Integer.valueOf(deviceDetails
											.get(AdbProperties.VERSION_SDK
													.toString())));
							deviceList.add(androidDevice);
							System.out.println(androidDevice.toString());

						} else {
							System.out.println(new String(errorStream
									.toByteArray()));
						}
					}
				}
				return deviceList;
			} catch (final Exception e) {
				System.out.println(e.getMessage());
			}

			return new ArrayList<AndroidDevice>();
		}
	}

	private volatile List<AndroidDevice> devices;

	public AndroidDeviceList() {
		devices = new ArrayList<AndroidDevice>(); // getConnectedDevices();
		devices = getConnectedDevices();
	}

	public List<AndroidDevice> getConnectedDevices() {
		final FetchTask task = new FetchTask(StreamTaskListener.NULL);
		try {
			return task.call();
		} catch (final IOException e) {
			return new ArrayList<AndroidDevice>();
		}

	}

	public String getDisplayName() {
		return "Connected Android devices";
	}

	public String getIconFileName() {
		return "/plugin/android-devices/android.png";
	}

	public String getUrlName() {
		if (Jenkins.getInstance().hasPermission(Jenkins.READ)) {
			return "android-devices";
		} else {
			return null;
		}
	}

}
