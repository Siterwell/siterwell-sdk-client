package uk.co.siterwell.sdk.share

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.reflect.TypeToken

import uk.co.siterwell.sdk.client.gson

data class DeviceParcel(val name: String,
                        private val nodeId: String,
                        val propertiesString: String,
                        val nodeUid: String,
                        val activate: Boolean
) : Parcelable {
    constructor(parcel: Parcel) : this(
            parcel.readString(),
            parcel.readString(),
            parcel.readString(),
            parcel.readString(),
            parcel.readInt() == 1)

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(name)
        parcel.writeString(nodeId)
        parcel.writeString(propertiesString)
        parcel.writeString(nodeUid)
        parcel.writeInt(if (activate) 1 else 0)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object {
        @JvmField
        val CREATOR = object : Parcelable.Creator<DeviceParcel> {
            override fun createFromParcel(parcel: Parcel): DeviceParcel {
                return DeviceParcel(parcel)
            }

            override fun newArray(size: Int): Array<DeviceParcel?> {
                return arrayOfNulls(size)
            }
        }
    }
}

val DeviceParcel.stringMapType
    get() = object : TypeToken<Map<String, String>>() {
    }.type


val DeviceParcel.rawPropertiesMap: Map<String, String>
    get() = gson.fromJson(propertiesString, stringMapType) as Map<String, String>