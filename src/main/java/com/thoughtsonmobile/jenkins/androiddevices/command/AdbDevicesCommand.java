package com.thoughtsonmobile.jenkins.androiddevices.command;

import hudson.util.ArgumentListBuilder;

public class AdbDevicesCommand {
	
	public ArgumentListBuilder getCommand() {
		 final ArgumentListBuilder args = new ArgumentListBuilder("/Users/soerenleif/android-sdks/platform-tools/adb", "devices");
		 return args;
	}

}
