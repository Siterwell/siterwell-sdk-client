package uk.co.siterwell.sdk.client

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.*
import com.google.gson.Gson
import org.greenrobot.eventbus.EventBus
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.error
import org.jetbrains.anko.info
import uk.co.siterwell.sdk.data.DeviceParcel
import uk.co.siterwell.sdk.event.DeviceListRespond


val gson = Gson()

/**
 * SwServciceConnection provide a SwGateway
 */
class SwServiceConnection(context: Context, val onconnect: (SwGateway) -> Unit, ondisconnect: () -> Unit) : AnkoLogger {
    private lateinit var gateway: SwGateway

    init {
        info { "Start Connection" }
        val intent = Intent().apply {
            action = "uk.co.siterwell.SwService.BIND"
            `package` = "uk.co.siterwell.homedashboard"
        }
        context.applicationContext.bindService(intent,
                object : ServiceConnection {
                    override fun onServiceConnected(className: ComponentName, service: IBinder) {
                        info { "SwSdk Connected" }
                        // This is called when the connection with the service has been
                        // established, giving us the object we can use to
                        // interact with the service.  We are communicating with the
                        // service using a Messenger, so here we get a client-side
                        // representation of that from the raw IBinder object.
                        gateway = SwGateway(Messenger(service))
                        onconnect(gateway)
                    }

                    override fun onServiceDisconnected(className: ComponentName) {
                        info { "SwSdk Disconnected" }
                        // This is called when the connection with the service has been
                        // unexpectedly disconnected -- that is, its process crashed.
                        gateway.bound = false
                        ondisconnect()
                    }
                },
                Context.BIND_AUTO_CREATE)
    }
}


const val WHAT_LIST_DEVICE = 1
const val WHAT_LIST_DEVICE_RES = 2

/**
 * SwGateway provide an interface to client
 * it should pack by another module
 */
class SwGateway internal constructor(private val messenger: Messenger, internal var bound: Boolean = true) : AnkoLogger {

    class ClientResponseHandler : Handler(), AnkoLogger {
        override fun handleMessage(msg: Message) {
            info { "receive what: ${msg.what}" }
            try {
                when {
                    msg.what == WHAT_LIST_DEVICE_RES -> {
                        val data = msg.data
                        data.classLoader = DeviceParcel::class.java.classLoader
                        EventBus.getDefault().post(DeviceListRespond(data.getParcelableArrayList<DeviceParcel>("obj")))
                    }
                    else -> {
                    }
                }
            } catch (e: Exception) {
                error("", e)
            }
        }
    }


    /**
     * Request to list devices
     *
     * Use [org.greenrobot.eventbus.Subscribe] to subscribe response [DeviceListRespond]
     *
     */
    fun listDevice() {
        try {
            messenger.send(listDeviceMessage())
        } catch (e: RemoteException) {
            error { e }
        }
    }


    private fun listDeviceMessage(): Message? {
        return Message.obtain(null, WHAT_LIST_DEVICE).apply {
            replyTo = Messenger(ClientResponseHandler())
        }
    }

    fun isBound() = bound
}


object SwSdk {

    /**
     * Connect to remote SwServer and provide a [SwGateway] to operate APIs
     */
    @JvmOverloads
    @JvmStatic
    fun connect(context: Context, ondisconnect: () -> Unit = {}, onconnect: (SwGateway) -> Unit): SwServiceConnection {
        return SwServiceConnection(context, onconnect, ondisconnect)
    }
}

