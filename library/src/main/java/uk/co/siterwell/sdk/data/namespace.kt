package uk.co.siterwell.sdk.data

/**
 * Created by nester on 2018/5/17.
 */
sealed class DeviceCtrlCommand(private val v: Int) {
    operator fun invoke(): Int = v
}

object Cancel : DeviceCtrlCommand(0)
object Add : DeviceCtrlCommand(1)
object Remove : DeviceCtrlCommand(2)