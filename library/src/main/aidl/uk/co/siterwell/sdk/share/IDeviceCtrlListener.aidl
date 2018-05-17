// IDeviceCtrlListener.aidl
package uk.co.siterwell.sdk.share;

import uk.co.siterwell.sdk.share.DeviceParcel;

interface IDeviceCtrlListener {
    void onDeviceAdded(in DeviceParcel deviceParcel);
    void onDeviceQueried(in String nodeId);
    void onDeviceSlept(in String nodeId);
    void onOperationFailed();
    void onDeviceRemoved(in DeviceParcel deviceParcel);
}
