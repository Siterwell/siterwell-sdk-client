// ISwService.aidl
package uk.co.siterwell.sdk.share;

import uk.co.siterwell.sdk.share.DeviceParcel;
import uk.co.siterwell.sdk.share.IDeviceCtrlListener;
import uk.co.siterwell.sdk.share.IDeviceValueChangeListener;

interface ISwService {
    List<DeviceParcel> listDevices();
    void controlDevice(int command);
    void activeDevice(String nodeUid, boolean active);

    void registerDeviceCtrlLisener(IDeviceCtrlListener lisenter);
    void unregisterDeviceCtrlLisener(IDeviceCtrlListener lisenter);

    void registerDeviceValueChangeLisener(IDeviceValueChangeListener lisenter);
    void unregisterDeviceValueChangeLisener(IDeviceValueChangeListener lisenter);
}
