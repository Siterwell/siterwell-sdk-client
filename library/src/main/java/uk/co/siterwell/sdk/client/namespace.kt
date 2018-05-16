package uk.co.siterwell.sdk.client

import android.content.ComponentName
import android.content.ServiceConnection
import android.os.IBinder
import android.os.Messenger
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.info

/**
 * SwServciceConnection provide a SwGateway
 */
class SwServiceConnection(val onconnect: (SwGateway) -> Unit, ondisconnect: () -> Unit) : AnkoLogger {
    private lateinit var gateway: SwGateway

    /**
     * Class for interacting with the main interface of the service.
     */
    init {
        info { "Start Connection" }
        object : ServiceConnection {
            override fun onServiceConnected(className: ComponentName, service: IBinder) {
                info { "SwDdk Connected" }
                // This is called when the connection with the service has been
                // established, giving us the object we can use to
                // interact with the service.  We are communicating with the
                // service using a Messenger, so here we get a client-side
                // representation of that from the raw IBinder object.
                gateway = SwGateway(Messenger(service))
                onconnect(gateway)
            }

            override fun onServiceDisconnected(className: ComponentName) {
                info { "SwDdk Disconnected" }
                // This is called when the connection with the service has been
                // unexpectedly disconnected -- that is, its process crashed.
                gateway.bound = false
                ondisconnect()
            }
        }
    }
}

/**
 * SwGateway provide an interface to client
 * it should pack by another module
 */
class SwGateway internal constructor(private val messenger: Messenger, internal var bound: Boolean = true) {
    fun addDevice() {

    }
}

object SwSdk {
    @JvmOverloads
    @JvmStatic
    fun conenct(ondisconnect: () -> Unit = {}, onconnect: (SwGateway) -> Unit): SwServiceConnection {
        return SwServiceConnection(onconnect, ondisconnect)
    }
}