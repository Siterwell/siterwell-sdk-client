// IDeviceValueChangeListener.aidl
package uk.co.siterwell.sdk.share;

import uk.co.siterwell.sdk.share.DeviceChangeEvent;

interface IDeviceValueChangeListener {
    void onValueChange(in DeviceChangeEvent e);
}
