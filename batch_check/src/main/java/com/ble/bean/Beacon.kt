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
    var txPower: String = ""
    var companyId: String? = null
    var rssiInOne: Int = 0
    var serviceData: String? = null
    var bt: Int = 1998;
    var interval: Long = 19996;
    var raw: String? = null
    var IsConnect = true
    var temp: String = "";
    var humidity: String = ""
    var isMove: Int =5
    var isSupportTempShidu=false
    var OpenTemp=false
    var temp_shidu_Time=0;

    var isSupportAcc=false
    var isOpenAcc=false
    var AccChangeAdv=0;
    var password="";
    //4:beacon
    //1:url
    //3:eid
    //0:uid
    //2:tlm
    var type:Int=4;
    var url_head:Int=0;
    var url_tail:Int=0
    var url_data:String=""
    var uid_Namespace:String=""
    var uid_ID_Instance:String=""
    var eid_data:String=""
    var tlm_type:Int=0
    var tlm_Advertising_PDU:Int=0;
    var tlm_work_time:String=""
    var tlm_data:String=""
    var tlm_salt:String=""
    var tlm_check:String=""
    var tx_power_at_0m:Int=0;
    var tlm_bt:Int=0
    var tlm_temp:String=""




    //用于信号判断，如果符合当前信号过滤，则一直显示，直到再次修改信号过滤值
    var flag=false;

    var OnlyBeacon=false;

    fun setIsMove(move :Int) {
        isMove=move
    }
    fun isSupportTempShidu(t:Boolean)
    {
        isSupportTempShidu=t;
    }

    fun isOpenAcc(t:Boolean){
        isOpenAcc=t
    }
    fun setAccAdv(t:Int){
        AccChangeAdv=t;
    }
    fun isSupportAcc(t:Boolean){
        isSupportAcc=t;
    }


}