package uk.co.siterwell.sdk.event

import org.greenrobot.eventbus.Subscribe
import uk.co.siterwell.sdk.share.DeviceParcel

/**
 * Created by nester on 2018/5/16.
 */
sealed class Event

data class DeviceListRespond(val deviceList: List<DeviceParcel>) : Event()

interface DeviceListRespondHandler {
    @Subscribe
    fun OnListDevcideRespone(respond: DeviceListRespond)
}





///////////////////////////// Listener
