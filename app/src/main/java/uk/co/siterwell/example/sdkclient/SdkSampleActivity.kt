package uk.co.siterwell.example.sdkclient

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.info
import uk.co.siterwell.sdk.client.SwSdk
import uk.co.siterwell.sdk.event.DeviceListRespond
import uk.co.siterwell.sdk.event.DeviceListRespondHandler


class SdkSampleActivity : AppCompatActivity(), AnkoLogger, DeviceListRespondHandler {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sdk_sample)

        EventBus.getDefault().register(this)

        SwSdk.connect(this) { gateway ->
            info { "gateway bound: ${gateway.isBound()}" }
            gateway.listDevice()
        }
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        SwSdk.connect(this) { gateway ->
            info { "gateway bound: ${gateway.isBound()}" }

            gateway.listDevice()
        }
    }

    @Subscribe
    override fun OnListDevcideRespone(respond: DeviceListRespond) {
        respond.deviceList.forEach {
            Log.d(loggerTag, it.toString())
        }
    }
}
