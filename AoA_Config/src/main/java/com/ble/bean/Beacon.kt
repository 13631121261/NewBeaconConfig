package com.ble.bean

/**
 * @package com.ble.bean
 * @fileName Beacon
 * @date 2020/6/1610:48
 * @author Andesen
 * @email 929620555@qq.com
 * @describe 自定义描述
 */

class Beacon {
    var i1:Long=0;
    var i2:Long=0;
    var deviceName: String = ""  //
    var address: String = ""
    var rssi: Int = 0
    var uuid: String? = null
    var major: Int = 0
    var minor: Int = 0
    var txPower: String = "N/A"
    var companyId: String? = null
    var rssiInOne: Int = 0
    var serviceData: String? = null
    var bt: String = "1998";
    var interval: Long = 19996;
    var raw: String? = null
    var IsConnect = true
    var password="";
    var flag=false;
    var channel=37;
    var mode="";
    var d_mode="";
    var isMove=0;
    var open=0;
    override fun toString(): String {
        return "Beacon(i1=$i1, i2=$i2, open=$open, deviceName='$deviceName', address='$address', rssi=$rssi, uuid=$uuid, major=$major, minor=$minor, txPower='$txPower', companyId=$companyId, rssiInOne=$rssiInOne, serviceData=$serviceData, bt=$bt, interval=$interval, raw=$raw, IsConnect=$IsConnect, password='$password', flag=$flag, channel=$channel, mode='$mode', d_mode='$d_mode', isMove=$isMove)"
    }

}