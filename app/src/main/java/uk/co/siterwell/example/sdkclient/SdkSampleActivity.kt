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
import uk.co.siterwell.sdk.client.SwServiceConnection
import uk.co.siterwell.sdk.share.DeviceChangeEvent
import uk.co.siterwell.sdk.share.DeviceParcel
import uk.co.siterwell.sdk.share.IDeviceCtrlListener
import uk.co.siterwell.sdk.share.IDeviceValueChangeListener


class SdkSampleActivity : AppCompatActivity(), AnkoLogger {

    lateinit var rootView: View
    lateinit var deviceAdapter: DeviceAdapter
    var connection: SwServiceConnection? = null

    private lateinit var valueChangelistener: IDeviceValueChangeListener;

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

        initViews()
    }

    private fun initViews() {
        connection = SwSdk.connect(this, false) { gateway ->
            info { "gateway bound: ${gateway.isBound()}" }
            val listDevice = gateway.listDevice()
            info { listDevice }

            valueChangelistener = object : IDeviceValueChangeListener.Stub() {
                override fun onValueChange(e: DeviceChangeEvent?) {
                    runOnUiThread {
                        info { "IDeviceValueChangeListener called $e" }
                        deviceAdapter.udpate(gateway.listDevice())
                    }
                }
            }
            gateway.registerDeviceValueChangeListener(valueChangelistener)

            deviceAdapter = DeviceAdapter(deviceList = listDevice, onToggleActivate = { d ->
                if (gateway.isBound()) {
                    gateway.activeDevice(d.nodeUid, !d.activate)
                }
            })

            with(rvDevices) {
                adapter = deviceAdapter
                layoutManager = LinearLayoutManager(this@SdkSampleActivity)
            }

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

            reset.onClick {
                if (gateway.isBound()) {
                    gateway.resetDevice(listener)
                }
            }
        }
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)

        connection?.unbind()
        connection = null
        initViews()

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

        connection?.unbind()
    }
}

