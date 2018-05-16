package uk.co.siterwell.sdk.data

import android.os.Parcelable
import com.google.gson.reflect.TypeToken
import kotlinx.android.parcel.Parcelize
import uk.co.siterwell.sdk.client.gson

val stringMapType = object : TypeToken<Map<String, String>>() {
}.type

@Parcelize
data class DeviceParcel(val name: String,
                        private val nodeId: String,
                        private val propertiesString: String,
                        val nodeUid: String) : Parcelable {

    val rawPropertiesMap: Map<String, String> by lazy {
        gson.fromJson(propertiesString, stringMapType) as Map<String, String>
    }
}