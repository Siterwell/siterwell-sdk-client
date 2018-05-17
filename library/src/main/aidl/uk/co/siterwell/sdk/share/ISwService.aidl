// ISwService.aidl
package uk.co.siterwell.sdk.share;

import uk.co.siterwell.sdk.share.DeviceParcel;
import uk.co.siterwell.sdk.share.IDeviceCtrlListener;

interface ISwService {
    List<DeviceParcel> listDevices();
    void controlDevice(int command);

    void registerDeviceCtrlLisener(IDeviceCtrlListener lisenter);
    void unregisterDeviceCtrlLisener(IDeviceCtrlListener lisenter);
}
