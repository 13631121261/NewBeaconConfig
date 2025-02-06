package com.ble.bean

/**
 * @package com.ble.bean
 * @fileName BleNotifyResponse
 * @date 2020/7/411:52
 * @author Andesen
 * @email 929620555@qq.com
 * @describe 自定义描述
 */
class BleNotifyResponse {
    var command:String?=null
    lateinit var type:Type;
    var data:String=""
    var status:Boolean=false
    constructor(bytes:ByteArray) {
        this.data= String(bytes)
        if(data!=null&&data.contains("&0")){
            status=true
            //A字母字符串 字节为0x41
            if(bytes[3]==0x41 as Byte){
                when(bytes[4]){
                    //1数字字符串，字节为0x31
                    0x31 as Byte-> type=Type.uuid
                    0x32 as Byte-> type=Type.major
                    0x33 as Byte-> type=Type.minor
                    0x34 as Byte-> type=Type.serviceData
                    0x35 as Byte-> type=Type.companyId
                    0x36 as Byte-> type=Type.interval
                    0x37 as Byte-> type=Type.txpower
                    0x38 as Byte-> type=Type.rssiatone

                }
            }
            else if(bytes[3]==0x43 as Byte){
                when(bytes[4]){
                    //1数字字符串，字节为0x31
                    0x32 as Byte-> type=Type.deviceName
                    0x33 as Byte->{
                        type = if(command!=null&&command.equals("连接")){
                            Type.connect
                        }else{
                            Type.psw
                        }

                    }
                }
            }
        }
    }
}