package com.thoughtsonmobile.jenkins.androiddevices.AndroidDeviceList

def l = namespace(lib.LayoutTagLib)
def f = namespace(lib.FormTagLib)

l.layout {
    def title = _("Connected Android Devices");
    l.header(title:title)
    l.main_panel {
        h1 {
            img(src:"${resURL}/plugin/android-devices/icons/48x48/android.png",alt:"[!]",height:48,width:48)
            text " "
            text title
        }
        p _("blurb")
		
		table(id:"devices", class:"sortable pane bigtable") {
			tr {
				th(width:32) {
					// column to show icon
				}
				th(initialSortDir:"down") {
					text _("Device Name")
				}
				th {
					text _("Android Version")
				}
				th {
					text _("API Level")
				}
				th {
					text _("IP Adresse")
				}
			}
			my.devices.each { e ->
				def dev = e;
				tr {
					td {
						a(href:dev.deviceId) {
							img(src:"${resURL}/plugin/android-devices/icons/24x24/android.png")
						}
					}
					td {
						a(href:dev.deviceId, dev.name)
					}
					td {
						text dev.version;
					}
					td {
						text dev.apiLevel;
					}
					td {
						text dev.ip
					}
				}
			}
		}
    }
}