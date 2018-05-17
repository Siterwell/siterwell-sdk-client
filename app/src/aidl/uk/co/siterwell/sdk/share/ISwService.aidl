// ISwService.aidl
package uk.co.siterwell.sdk.share;

import uk.co.siterwell.sdk.share.DeviceParcel;

interface ISwService {
    List<DeviceParcel> listDevices();
}
