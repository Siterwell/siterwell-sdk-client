package uk.co.siterwell.example.sdkclient

import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.rv_item_device.view.*
import org.jetbrains.anko.layoutInflater
import org.jetbrains.anko.sdk25.coroutines.onClick
import uk.co.siterwell.sdk.share.DeviceParcel

class DeviceAdapter(private var deviceList: List<DeviceParcel> = emptyList(), val onToggleActivate: (DeviceParcel) -> Unit = {}) : RecyclerView.Adapter<DeviceAdapter.ViewHolder>() {

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bind(d: DeviceParcel, onToggleActivate: (DeviceParcel) -> Unit ) {
            itemView.tv_device_name.text = d.name
            itemView.btn_activate.isChecked = d.activate
            itemView.btn_activate.onClick {
                onToggleActivate(d)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = parent.context.layoutInflater.inflate(R.layout.rv_item_device, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int = deviceList.count()


    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(deviceList[position], onToggleActivate)
    }

    fun udpate(l: List<DeviceParcel>) {
        deviceList = l
        notifyDataSetChanged()
    }

}
