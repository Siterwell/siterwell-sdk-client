package uk.co.siterwell.sdk.client

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.IBinder
import android.os.RemoteException
import com.google.gson.Gson
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.error
import org.jetbrains.anko.info
import uk.co.siterwell.sdk.share.DeviceParcel
import uk.co.siterwell.sdk.share.IDeviceCtrlListener
import uk.co.siterwell.sdk.share.ISwService


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
                        gateway = SwGateway(ISwService.Stub.asInterface(service))
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


/**
 * SwGateway provide an interface to client
 * it should pack by another module
 */
class SwGateway internal constructor(private val service: ISwService, internal var bound: Boolean = true) : AnkoLogger {
    fun isBound() = bound

    /**
     * list all devices [DeviceParcel]
     */
    fun listDevice(): List<DeviceParcel> {
        try {
            return service.listDevices().toList()
        } catch (e: RemoteException) {
            error { e }
        }
        return emptyList()
    }

    /**
     * Start to control device
     *
     * this method will trigger the device controller to scan the device and wait the device respond
     * and respond will invoke the given listener
     * after finished the control flow (add, add more, remove), you should call  [stopControlDevice]
     * to unregister and cancel scan
     */
    fun startControlDevice(listener: IDeviceCtrlListener) {
        try {
            service.registerDeviceCtrlLisener(listener)
            service.startControlDevice()
        } catch (e: RemoteException) {
            error { e }
        }
    }


    /**
     * Stop the controller flow
     *
     * unregister [listener] and cancel scan
     */
    fun stopControlDevice(listener: IDeviceCtrlListener) {
        try {
            service.unregisterDeviceCtrlLisener(listener)
            service.stopControlDevice()
        } catch (e: RemoteException) {
            error { e }
        }
    }
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

