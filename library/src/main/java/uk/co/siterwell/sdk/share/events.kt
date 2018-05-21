package uk.co.siterwell.sdk.share

import android.os.Parcel
import android.os.Parcelable

/**
 * Created by nester on 2018/5/21.
 */
sealed class Event : Parcelable {
}

data class DeviceChangeEvent(val nodeUid: String, val name: String, val value: String) : Event(), Parcelable {
    constructor(parcel: Parcel) : this(
            parcel.readString(),
            parcel.readString(),
            parcel.readString())

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(nodeUid)
        parcel.writeString(name)
        parcel.writeString(value)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object {
        @JvmField
        val CREATOR = object : Parcelable.Creator<DeviceChangeEvent> {
            override fun createFromParcel(parcel: Parcel): DeviceChangeEvent {
                return DeviceChangeEvent(parcel)
            }

            override fun newArray(size: Int): Array<DeviceChangeEvent?> {
                return arrayOfNulls(size)
            }
        }
    }
}