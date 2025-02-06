package com.ble.model

import android.bluetooth.*
import android.content.Context
import android.util.Log
import com.ble.bean.MLog
import com.ble.bean.Type
import com.ble.util.StringUtils
import io.reactivex.rxjava3.annotations.NonNull
import no.nordicsemi.android.ble.BleManager
import no.nordicsemi.android.ble.callback.DataReceivedCallback
import no.nordicsemi.android.ble.callback.FailCallback
import no.nordicsemi.android.ble.callback.SuccessCallback
import java.util.*


/**
 * @package com.ble.model
 * @fileName ConnectModel
 * @date 2020/7/316:55
 * @author Andesen
 * @email 929620555@qq.com
 * @describe 自定义描述
 */
class ConnectModel: BleManager{

    var address:String?=null

    val Service_Eddystone_UUID:UUID=UUID.fromString("0000FFB0-0000-1000-8000-00805F9B34FB")
    val char_Eddystone_UID_UUID:UUID=UUID.fromString("0000FFB2-0000-1000-8000-00805F9B34FB")
    val char_Eddystone_URL_UUID:UUID=UUID.fromString("0000FFB1-0000-1000-8000-00805F9B34FB")
    val char_Eddystone_TLM_UUID:UUID=UUID.fromString("0000FFB4-0000-1000-8000-00805F9B34FB")
    val char_Eddystone_EID_UUID:UUID=UUID.fromString("0000FFB3-0000-1000-8000-00805F9B34FB")
    val Service_Beacon_UUID:UUID=UUID.fromString("0000FFA0-0000-1000-8000-00805F9B34FB")
    val char_minor_UUID:UUID=UUID.fromString("0000FFA3-0000-1000-8000-00805F9B34FB")
    val char_major_UUID:UUID=UUID.fromString("0000FFA2-0000-1000-8000-00805F9B34FB")
    val char_TxPower_UUID:UUID=UUID.fromString("0000FFA7-0000-1000-8000-00805F9B34FB")
    val char_ServiceData_UUID:UUID=UUID.fromString("0000FFA4-0000-1000-8000-00805F9B34FB")
    val char_BeaconUUID_UUID:UUID=UUID.fromString("0000FFA1-0000-1000-8000-00805F9B34FB")
    val char_CompanyId_UUID:UUID=UUID.fromString("0000FFA5-0000-1000-8000-00805F9B34FB")
    val char_RssiInOne_UUID:UUID=UUID.fromString("0000FFA8-0000-1000-8000-00805F9B34FB")
    val char_Interval_UUID:UUID=UUID.fromString("0000FFA6-0000-1000-8000-00805F9B34FB")
    val version_service_UUID:UUID=UUID.fromString("0000180A-0000-1000-8000-00805F9B34FB")
    val version_char:UUID=UUID.fromString("00002A26-0000-1000-8000-00805F9B34FB")

    val Service_Other_UUID:UUID=UUID.fromString("0000FFC0-0000-1000-8000-00805F9B34FB")
    val char_Psw_UUID:UUID=UUID.fromString("0000FFC3-0000-1000-8000-00805F9B34FB")
    val char_DeviceName_UUID:UUID=UUID.fromString("0000FFC2-0000-1000-8000-00805F9B34FB")
    val char_notify_UUID:UUID=UUID.fromString("0000FFC4-0000-1000-8000-00805F9B34FB")
    val char_setMode_UUID:UUID=UUID.fromString("0000FFC1-0000-1000-8000-00805F9B34FB")
    val char_ACC:UUID=UUID.fromString("0000FFC5-0000-1000-8000-00805F9B34FB")
    val char_WenduShidu:UUID=UUID.fromString("0000FFC6-0000-1000-8000-00805F9B34FB")
    val char_read:UUID=UUID.fromString("0000FFC7-0000-1000-8000-00805F9B34FB")
    val char_get:UUID=UUID.fromString("0000FFC8-0000-1000-8000-00805F9B34FB")
    //Beacon的服务
    var BluetoothGattBeacon_service: BluetoothGattService?=null
    var Characteristic_minor:BluetoothGattCharacteristic?=null
    var Characteristic_major:BluetoothGattCharacteristic?=null
    var Characteristic_TxPower:BluetoothGattCharacteristic?=null
    var Characteristic_ServiceData:BluetoothGattCharacteristic?=null
    var Characteristic_BeaconUUID:BluetoothGattCharacteristic?=null
    var Characteristic_CompanyId:BluetoothGattCharacteristic?=null
    var Characteristic_RssiInOne:BluetoothGattCharacteristic?=null
    var Characteristic_Interval:BluetoothGattCharacteristic?=null


    //用于密码，修改名称的服务
    var BluetoothGattBeacon_service_version: BluetoothGattService?=null
    var BluetoothGattOther_service: BluetoothGattService?=null
    var BluetoothGatt_Eddystone_service: BluetoothGattService?=null
    var Characteristic_psw: BluetoothGattCharacteristic?=null
    var Characteristic_notify: BluetoothGattCharacteristic?=null
    var Characteristic_DeviceName:BluetoothGattCharacteristic?=null
    var Characteristic_setMode:BluetoothGattCharacteristic?=null
    var Characteristic_read:BluetoothGattCharacteristic?=null
    var Characteristic_get:BluetoothGattCharacteristic?=null
    var Characteristic_notify_version:BluetoothGattCharacteristic?=null
    var connectDataCallBack:ConnectDataCallBack?=null
    constructor(context: Context?,connectDataCallBack:ConnectDataCallBack)  : super(context!!){
        this.connectDataCallBack=connectDataCallBack

    }
    fun connectDevice(address:String){
        val  bluetoothManager =context.getSystemService(Context.BLUETOOTH_SERVICE)as BluetoothManager;
        var mBluetoothAdapter = bluetoothManager.adapter;
        Log.e(MLog.ConnectE,"进入连接="+address)
        this.address=address
        connect(mBluetoothAdapter.getRemoteDevice(address))
                .timeout(10000)
                .retry(20, 500)
                .done(SuccessCallback {
                    address?.let { it1 -> connectDataCallBack?.onConnected(it1) }
                    Log.e(MLog.ConnectE, "开始连接咯") }
                ).fail(FailCallback { _, _ ->
                    run {
                        //   address?.let { connectDataCallBack?.onDisConnected(it) }
                        address?.let { connectDataCallBack?.onDisConnected(it) }
                        Log.e(MLog.ConnectE, "连接超时" + address)
                    }
                })
                .enqueue()
    }

    override fun getGattCallback(): BleManagerGattCallback {
        //TODO("Not yet implemented")

        return MyManagerGattCallback();
    }

    private inner class MyManagerGattCallback : BleManagerGattCallback() {
        // This method will be called when the device is connected and services are discovered.
        // You need to obtain references to the characteristics and descriptors that you will use.
        // Return true if all required services are found, false otherwise.
        public override fun isRequiredServiceSupported(@NonNull gatt: BluetoothGatt): Boolean {
            Log.e(MLog.ConnectE,"isRequiredServiceSupported请求服务="+bluetoothDevice.address)
            BluetoothGattBeacon_service = gatt.getService(Service_Beacon_UUID)
            BluetoothGattOther_service=gatt.getService(Service_Other_UUID)
            BluetoothGatt_Eddystone_service=gatt.getService(Service_Eddystone_UUID)
            BluetoothGattBeacon_service_version=gatt.getService(version_service_UUID)

            if (BluetoothGattBeacon_service != null) {
                //  firstCharacteristic = service.getCharacteristic(FIRST_CHAR)
                Characteristic_minor = BluetoothGattBeacon_service!!.getCharacteristic(char_minor_UUID)
                Characteristic_major = BluetoothGattBeacon_service!!.getCharacteristic(char_major_UUID)
                Characteristic_TxPower = BluetoothGattBeacon_service!!.getCharacteristic(char_TxPower_UUID)
                Characteristic_ServiceData= BluetoothGattBeacon_service!!.getCharacteristic(char_ServiceData_UUID)
                Characteristic_CompanyId = BluetoothGattBeacon_service!!.getCharacteristic(char_CompanyId_UUID)
                Characteristic_RssiInOne = BluetoothGattBeacon_service!!.getCharacteristic(char_RssiInOne_UUID)
                Characteristic_Interval= BluetoothGattBeacon_service!!.getCharacteristic(char_Interval_UUID)
                Characteristic_BeaconUUID= BluetoothGattBeacon_service!!.getCharacteristic(char_BeaconUUID_UUID)
                Characteristic_notify_version=BluetoothGattBeacon_service_version!!.getCharacteristic(version_char)
            }
            else{
                return false
            }
            if(BluetoothGattOther_service!=null){
                Characteristic_get=BluetoothGattOther_service!!.getCharacteristic(char_get)
                Characteristic_DeviceName = BluetoothGattOther_service!!.getCharacteristic(char_DeviceName_UUID)
                Characteristic_psw = BluetoothGattOther_service!!.getCharacteristic(char_Psw_UUID)
                Characteristic_notify=BluetoothGattOther_service!!.getCharacteristic(char_notify_UUID)
                Characteristic_setMode=BluetoothGattOther_service!!.getCharacteristic(char_setMode_UUID)

                Characteristic_read=BluetoothGattOther_service!!.getCharacteristic(char_read)

                Log.e(MLog.ConnectE,"UUID="+char_get.toString())
              var lengnth=  BluetoothGattOther_service?.characteristics?.size
                var i=0;
                for (index in 1..lengnth!!){
                    Log.e("UUID=", BluetoothGattOther_service?.characteristics?.get(i)?.uuid.toString());
                    Log.e("UUID=",BluetoothGattOther_service?.characteristics?.get(i).toString());
                    i++;
                }
                Log.e(MLog.ConnectE,"原来="+Characteristic_notify)
                Log.e(MLog.ConnectE,"新增="+Characteristic_get)

            }
            else{
                return false
            }
            return true;
       /*     return (Characteristic_read!=null&&Characteristic_notify1!=null&&Characteristic_minor!=null&&Characteristic_major!=null&&Characteristic_TxPower!=null&&Characteristic_ServiceData!=null&&Characteristic_CompanyId!=null
                    &&Characteristic_Interval!=null&&Characteristic_BeaconUUID!=null&&Characteristic_DeviceName!=null&&Characteristic_psw!=null&&Characteristic_notify!=null)
*/
        }

        // If you have any optional services, allocate them here. Return true only if
        // they are found.
        override fun isOptionalServiceSupported(@NonNull gatt: BluetoothGatt): Boolean {
            Log.e(MLog.ConnectE,"isOptionalServiceSupported="+bluetoothDevice.address)

            return super.isOptionalServiceSupported(gatt)
        }

        // Initialize your device here. Often you need to enable notifications and set required
        // MTU or write some initial data. Do it here.
        override fun initialize() {
            Log.e(MLog.ConnectE,"initialize初始化="+bluetoothDevice.address)
            Log.e(MLog.ConnectE,"initialize初始化="+Characteristic_get)
            Log.e(MLog.ConnectE,"initialize初始化="+Characteristic_notify)
            Log.e(MLog.ConnectE,"initialize初始化="+Characteristic_notify_version)
            // You may enqueue multiple operations. A queue ensures that all operations are
            // performed one after another, but it is not required.
            beginAtomicRequestQueue()
                    .add(enableNotifications(Characteristic_notify))
                    .add(enableNotifications(Characteristic_get))

                    .done(SuccessCallback {
                        Log.e(MLog.ConnectE,"使能通知成功"+bluetoothDevice.address)


                         sleep(500).enqueue()
                         writeCharacteristic(Characteristic_read,StringUtils.HexToByteArr("2405"))
                                 .done(SuccessCallback {
                                     Log.e(MLog.ConnectE, "写设备名称发送成功" + bluetoothDevice.address)
                                 })
                                 .enqueue()
                        sleep(500).enqueue()
                        writeCharacteristic(Characteristic_read,StringUtils.HexToByteArr("2404"))
                                .done(SuccessCallback {
                                    Log.e(MLog.ConnectE, "写设备名称发送成功" + bluetoothDevice.address)
                                })
                                .enqueue()
                        sleep(500).enqueue()
                        Log.e(MLog.ConnectE, "进行读取版本号" + bluetoothDevice.address)
                        readCharacteristic(Characteristic_notify_version).with(receivedCallback_version).enqueue();

                    })

                    .fail(FailCallback{ _, _ ->
                        run {
                            //   address?.let { connectDataCallBack?.onDisConnected(it) }
                          //  address?.let { connectDevice(it) }
                            Log.e(MLog.ConnectE, "使能通知失败了" + bluetoothDevice.address)
                            Log.e(MLog.ConnectE, "使能通知失败了" + Characteristic_notify_version)
                            Log.e(MLog.ConnectE, "使能通知失败了" + receivedCallback_version)
                            readCharacteristic(Characteristic_notify_version).with(receivedCallback_version).enqueue();
                        }
                    })
                    .enqueue()

            // Set a callback for your notifications. You may also use waitForNotification(...).
            // Both callbacks will be called when notification is received.
            setNotificationCallback(Characteristic_notify).with(receivedCallback)
            setNotificationCallback(Characteristic_get).with(receivedCallback1)
        }

        override fun onCharacteristicRead(gatt: BluetoothGatt, characteristic: BluetoothGattCharacteristic) {
            super.onCharacteristicRead(gatt, characteristic)
            System.out.println("数据="+characteristic.uuid+"   "+StringUtils.ByteArrToHex(characteristic.value))
        }

        override fun onCharacteristicWrite(gatt: BluetoothGatt, characteristic: BluetoothGattCharacteristic) {
            super.onCharacteristicWrite(gatt, characteristic)
            System.out.println("11数据="+characteristic.uuid+"   "+StringUtils.ByteArrToHex(characteristic.value))
        }
        override fun onDeviceDisconnected() {
            BluetoothGattBeacon_service=null
            Characteristic_minor=null
            Characteristic_major=null
            Characteristic_TxPower=null
            Characteristic_ServiceData=null
            Characteristic_BeaconUUID=null
            Characteristic_CompanyId=null
            Characteristic_RssiInOne=null
            Characteristic_Interval=null
            //用于密码，修改名称的服务
            BluetoothGattOther_service=null
            Characteristic_psw=null
            Characteristic_notify=null
            Characteristic_DeviceName=null
            Log.e(MLog.ConnectE,"onDeviceDisconnected断开连接=")
            address?.let { connectDataCallBack?.onDisConnected(it) }

        }


    }
    fun sendPasswrod(data:ByteArray){
        writeCharacteristic(Characteristic_psw,data)
                .done(SuccessCallback {
                    Log.e(MLog.ConnectE, "发送密码" + StringUtils.ByteArrToHex(data))
                })
                .enqueue()
    }
    fun read(){
        var char1=BluetoothGattOther_service?.getCharacteristic(char_WenduShidu)
        var char2=BluetoothGattOther_service?.getCharacteristic(char_ACC)
        if(char1!=null){
            readCharacteristic(char1).with(receivedCallback_Temp).enqueue();
            readCharacteristic(char2).with(receivedCallback_Acc).enqueue();
        }
        else{
            System.out.println("666666666666666666")
        }
    }

    fun sendValue(data:ByteArray,type: Type){

        var char:BluetoothGattCharacteristic?=null;
        when(type){
            Type.deviceName->
                char= Characteristic_DeviceName!!;
            Type.uuid->
                char= Characteristic_BeaconUUID!!;
            Type.serviceData->
                char=Characteristic_ServiceData
            Type.major->
                char=Characteristic_major

            Type.minor->
                char=Characteristic_minor
            Type.txpower->
                char=Characteristic_TxPower
            Type.rssiatone->
                char=Characteristic_RssiInOne
            Type.interval->
                char=Characteristic_Interval
            Type.setMode_beacon->
                char=Characteristic_setMode
            Type.wendu_Open->{
                char=BluetoothGattOther_service?.getCharacteristic(char_WenduShidu)
            }
            Type.ACC_Open->{
                char=BluetoothGattOther_service?.getCharacteristic(char_ACC)
            }
            Type.change_mode->{
                char=BluetoothGattOther_service?.getCharacteristic(char_setMode_UUID)
            }
            Type.uid->{
                char=BluetoothGatt_Eddystone_service?.getCharacteristic(char_Eddystone_UID_UUID)
            }

            Type.eid->{
                char=BluetoothGatt_Eddystone_service?.getCharacteristic(char_Eddystone_EID_UUID)
            }
            Type.url->{
                char=BluetoothGatt_Eddystone_service?.getCharacteristic(char_Eddystone_URL_UUID)
            }
            Type.tlm->{
                char=BluetoothGatt_Eddystone_service?.getCharacteristic(char_Eddystone_TLM_UUID)
            }
            else -> {}
        }
        System.out.println("开始发送"+type+"   消息="+StringUtils.ByteArrToHex(data))
        writeCharacteristic(char,data)
                .done(SuccessCallback {
                    Log.e(MLog.ConnectE, "成功发送 Type= "+type + StringUtils.ByteArrToHex(data))
                })
                .enqueue()
    }


    var receivedCallback= DataReceivedCallback { _, data -> //   TODO("Not yet implemented")
        Log.e(MLog.ConnectE,"通知数据="+ data.value?.let { String(it) })

        data.value?.let { String(it) }?.let { connectDataCallBack?.onData(it) };
    }

    var receivedCallback1= DataReceivedCallback { _, data -> //   TODO("Not yet implemented")
        data.value?.let { connectDataCallBack?.onData1(it) }
    }
    var receivedCallback_Acc= DataReceivedCallback { _, data -> //   TODO("Not yet implemented")
        data.value?.let { connectDataCallBack?.onDataAcc(it) }
    };
    var receivedCallback_version= DataReceivedCallback { _, data -> //   TODO("Not yet implemented")
        Log.e(MLog.ConnectE,"通知数据66="+ data.value?.let { String(it) })
        data.value?.let { connectDataCallBack?.onDataVersion(it) }
    };
    var receivedCallback_Temp= DataReceivedCallback { _, data -> //   TODO("Not yet implemented")
        data.value?.let { connectDataCallBack?.onDataTemp(it) }
    };

}