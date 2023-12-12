package com.example.usb_serial_test

import android.content.Context
import android.content.Context.USB_SERVICE
import android.hardware.usb.UsbManager
import com.hoho.android.usbserial.driver.UsbSerialPort
import com.hoho.android.usbserial.driver.UsbSerialProber


enum class DoorState {
    OPEN, CLOSE
}

class SerialCommunication(context: Context) {

    /**  close, open byte리스트 값 지정 */
    private val doorClosingData = byteArrayOf(0xA0.toByte(), 0x01.toByte(), 0x00.toByte(), 0xA1.toByte())
    private val doorOpeningData = byteArrayOf(0xA0.toByte(), 0x01.toByte(), 0x01.toByte(), 0xA2.toByte())

    private var usbRelayPort: UsbSerialPort;

    private val serialTimeOut = 1000

    init {
        val manager = context.getSystemService(USB_SERVICE) as UsbManager
        val availableDrivers = UsbSerialProber.getDefaultProber().findAllDrivers(manager)

        if (availableDrivers.isEmpty()) throw Exception("사용 가능한 드라이버가 없습니다.")

        val usbRelay = availableDrivers.find {
            it.device.productId == 29987
        } ?: throw Exception("사용 가능한 USB Relay가 없습니다.")

        val connection = manager.openDevice(usbRelay.device)
        usbRelayPort = usbRelay.ports[0]

        usbRelayPort.apply {
            open(connection)
            setParameters(9600, 8, UsbSerialPort.STOPBITS_1, UsbSerialPort.PARITY_NONE)
        }
    }


    fun handleDoor(doorState: DoorState) {
        // 데이터를 시리얼로 전송하는 로직
        try {
            when (doorState) {
                DoorState.OPEN -> usbRelayPort.write(doorOpeningData, serialTimeOut)
                DoorState.CLOSE -> usbRelayPort.write(doorClosingData, serialTimeOut)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }


    fun closeConnection() {
        // 시리얼 연결을 닫는 로직
        usbRelayPort.close()
    }
}