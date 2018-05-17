package uk.co.siterwell.example.sdkclient

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.design.snackbar
import org.jetbrains.anko.info
import uk.co.siterwell.sdk.client.SwSdk
import uk.co.siterwell.sdk.share.DeviceParcel
import uk.co.siterwell.sdk.share.IDeviceCtrlListener


class SdkSampleActivity : AppCompatActivity(), AnkoLogger {

    lateinit var rootView: View

    private val listener: IDeviceCtrlListener = object : IDeviceCtrlListener.Stub() {
        override fun onDeviceAdded(deviceParcel: DeviceParcel?) {
            runOnUiThread {
                snackbar(rootView, "onDeviceAdded")
            }
        }

        override fun onDeviceQueried(nodeId: String?) {
            runOnUiThread {
                snackbar(window.decorView, "onDeviceQueried")
            }
        }

        override fun onDeviceSlept(nodeId: String?) {
            runOnUiThread {
                snackbar(window.decorView, "onDeviceSlept")
            }
        }

        override fun onOperationFailed() {
            runOnUiThread {
                snackbar(window.decorView, "onOperationFailed")
            }
        }

        override fun onDeviceRemoved(deviceParcel: DeviceParcel?) {
            runOnUiThread {
                snackbar(window.decorView, "onDeviceRemoved")
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sdk_sample)

        rootView = window.decorView

        SwSdk.connect(this) { gateway ->
            info { "gateway bound: ${gateway.isBound()}" }
            info { gateway.listDevice() }
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
//            gateway.stopControlDevice(listener)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        SwSdk.connect(this) { gateway ->
            gateway.stopControlDevice(listener)
        }
    }
}

