package uk.co.siterwell.example.sdkclient

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.view.View
import kotlinx.android.synthetic.main.activity_sdk_sample.*
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.design.snackbar
import org.jetbrains.anko.info
import org.jetbrains.anko.sdk25.coroutines.onClick
import uk.co.siterwell.sdk.client.SwSdk
import uk.co.siterwell.sdk.share.DeviceParcel
import uk.co.siterwell.sdk.share.IDeviceCtrlListener


class SdkSampleActivity : AppCompatActivity(), AnkoLogger {

    lateinit var rootView: View
    lateinit var deviceAdapter: DeviceAdapter

    private val listener: IDeviceCtrlListener = object : IDeviceCtrlListener.Stub() {
        override fun onDeviceAdded(deviceParcel: DeviceParcel?) {
            SwSdk.connect(this@SdkSampleActivity) { gateway ->
                deviceAdapter.udpate(gateway.listDevice())
                runOnUiThread {
                    snackbar(rootView, "onDeviceAdded").show()
                }
            }
        }

        override fun onDeviceQueried(nodeId: String?) {
            runOnUiThread {
                snackbar(window.decorView, "onDeviceQueried").show()
            }
        }

        override fun onDeviceSlept(nodeId: String?) {
            runOnUiThread {
                snackbar(window.decorView, "onDeviceSlept").show()
            }
        }

        override fun onOperationFailed() {
            runOnUiThread {
                snackbar(window.decorView, "onOperationFailed").show()
            }
        }

        override fun onDeviceRemoved(deviceParcel: DeviceParcel?) {
            SwSdk.connect(this@SdkSampleActivity) { gateway ->
                deviceAdapter.udpate(gateway.listDevice())
                runOnUiThread {
                    snackbar(window.decorView, "onDeviceRemoved").show()
                }
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sdk_sample)

        rootView = window.decorView
        deviceAdapter = DeviceAdapter()

        with(rvDevices) {
            adapter = deviceAdapter
            layoutManager = LinearLayoutManager(this@SdkSampleActivity)
            setHasFixedSize(true)
        }

        SwSdk.connect(this) { gateway ->
            info { "gateway bound: ${gateway.isBound()}" }
            val listDevice = gateway.listDevice()
            info { listDevice }
            deviceAdapter.udpate(listDevice)

            add.onClick {
                if (gateway.isBound()) {
                    gateway.startAddControlDevice(listener)
                }
            }

            remove.onClick {
                if (gateway.isBound()) {
                    gateway.startRemoveControlDevice(listener)
                }
            }


            stop.onClick {
                if (gateway.isBound()) {
                    gateway.stopControlDevice(listener)
                }
            }
        }

    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        SwSdk.connect(this) { gateway ->
            info { "gateway bound: ${gateway.isBound()}" }
            info { gateway.listDevice() }
        }
    }

    override fun onStart() {
        super.onStart()
        SwSdk.connect(this) { gateway ->
            gateway.startControlDevice(listener)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        SwSdk.connect(this) { gateway ->
            gateway.stopControlDevice(listener)
        }
    }
}

