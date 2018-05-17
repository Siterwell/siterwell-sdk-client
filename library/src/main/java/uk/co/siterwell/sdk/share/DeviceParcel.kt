package uk.co.siterwell.sdk.share

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.reflect.TypeToken
import uk.co.siterwell.sdk.client.gson

val stringMapType = object : TypeToken<Map<String, String>>() {
}.type

data class DeviceParcel(val name: String,
                        private val nodeId: String,
                        private val propertiesString: String,
                        val nodeUid: String) : Parcelable {

    val rawPropertiesMap: Map<String, String> by lazy {
        gson.fromJson(propertiesString, stringMapType) as Map<String, String>
    }

    constructor(parcel: Parcel) : this(
            parcel.readString(),
            parcel.readString(),
            parcel.readString(),
            parcel.readString())

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(name)
        parcel.writeString(nodeId)
        parcel.writeString(propertiesString)
        parcel.writeString(nodeUid)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object {
        @JvmField
        val CREATOR = object  : Parcelable.Creator<DeviceParcel> {
            override fun createFromParcel(parcel: Parcel): DeviceParcel {
                return DeviceParcel(parcel)
            }

            override fun newArray(size: Int): Array<DeviceParcel?> {
                return arrayOfNulls(size)
            }
        }
    }


}