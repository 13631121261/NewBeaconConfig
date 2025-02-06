package com.ble.presenter

import android.content.Context
import android.util.Log
import android.widget.Toast
import com.ble.activity.IBeaconDetailView
import com.ble.beaconcheck.R
import com.ble.bean.MLog
import com.ble.bean.Type
import com.ble.model.ConnectDataCallBack
import com.ble.model.ConnectModel
import com.ble.util.StringUtils


/**
 * @package com.ble.presenter
 * @fileName ConnectPresenter
 * @date 2020/7/411:48
 * @author Andesen
 * @email 929620555@qq.com
 * @describe 自定义描述
 */
class ConnectPresenter:IConnectPresenter , ConnectDataCallBack {

    var address:String?=null
    var context:Context?=null;
    var beaconDetailView :IBeaconDetailView?=null
    var connectModel :ConnectModel?=null
    constructor(context:Context,beaconDetailView:IBeaconDetailView){
        this.context=context
        this.beaconDetailView=beaconDetailView
        connectModel=ConnectModel(context,this)
    }
    override fun connect(address: String) {
        //TODO("Not yet implemented")
        this.address=address
        Log.e(MLog.ConnectE,"Presener连接="+address)
        connectModel?.connectDevice(address)

    }
     fun reConnect() {
        //TODO("Not yet implemented")

        Log.e(MLog.ConnectE,"Presener连接="+address)
        address?.let { connectModel?.connectDevice(it) }

    }
    override fun disconnect() {
        //TODO("Not yet implemented")
            connectModel?.disconnect()?.enqueue()
    }

    override fun sendPassword(psw: String) {
        //TODO("Not yet implemented")
        var old=psw.toByteArray(Charsets.UTF_8)
        var psw=ByteArray(old.size+2);
        psw[0]=0x24;
        psw[1]=0x00;
        System.arraycopy(old,0,psw,2,old.size);
     //  var array= byteArrayOf(0x24,0x00,old[0],old[1],old[2],old[3]);
        System.out.println("密码="+StringUtils.ByteArrToHex(psw));
        connectModel?.sendPasswrod(psw);

    }
    override fun sendChangePassword(psw: String) {
        //TODO("Not yet implemented")
        var old=psw.toByteArray(Charsets.UTF_8)
        var psw=ByteArray(old.size+2);
        psw[0]=0x24;
        psw[1]=0x01;
        System.arraycopy(old,0,psw,2,old.size);
        //  var array= byteArrayOf(0x24,0x00,old[0],old[1],old[2],old[3]);
        System.out.println("密码="+StringUtils.ByteArrToHex(psw));
        connectModel?.sendPasswrod(psw);

    }
     fun sendValue(value: ByteArray ,type: Type) {
        connectModel?.sendValue(value,type);
    }



    override fun close() {
      //  TODO("Not yet implemented")
        /*val data =ByteArray(3)
        data[0]=0x24;
        data[1]=0x31;
        data[2]=0x30;
        sendValue(data,Type.setMode_beacon);*/
        connectModel?.close()
    }

    override fun onData1(data: ByteArray) {
        //TODO("Not yet implemented")
        beaconDetailView?.showData(data);
    }
    override fun onData(data: String) {
      //  TODO("Not yet implemented")
        when(data){

            "\$&0C3\r\n"->{
                    //Toast.makeText(context,"连接失败",Toast.LENGTH_LONG).show()
                      beaconDetailView?.connectFail();
                }
            "\$@0C3\r\n" -> {
                var str=context?.resources?.getString(R.string.connect_ok)
              //  Toast.makeText(context, str, Toast.LENGTH_SHORT).show()
                beaconDetailView?.connectOk()
                connectModel?.read();
            }
            "\$#0C3\r\n"-> {
                var str=context?.resources?.getString(R.string.connect_passwordError)
              //  Toast.makeText(context, str, Toast.LENGTH_SHORT).show()
                beaconDetailView?.connectFail();
            }
            "\$@0C2\r\n"->{
                var str=context?.resources?.getString(R.string.change_name_OK)
              //  Toast.makeText(context, str, Toast.LENGTH_SHORT).show()
                beaconDetailView?.onSetOk("Name")
               // setMode()
            }
            "\$&0C2\r\n"->{
                var str=context?.resources?.getString(R.string.change_name_fail)
             //  Toast.makeText(context, str, Toast.LENGTH_SHORT).show()
            }
            "\$@0A1\r\n"->{
                var str=context?.resources?.getString(R.string.change_uuid_ok)
             //   Toast.makeText(context, str, Toast.LENGTH_SHORT).show()
                beaconDetailView?.onSetOk("UUID")
               // setMode()
            }
            "\$&0A1\r\n"->{
                var str=context?.resources?.getString(R.string.change_uuid_fail)
             //   Toast.makeText(context, str, Toast.LENGTH_SHORT).show()
            }
            "\$@0A4\r\n"->{
                var str=context?.resources?.getString(R.string.change_customer_ok)
               // Toast.makeText(context, str, Toast.LENGTH_SHORT).show()
                beaconDetailView?.onSetOk("Custom Data")
             //   setMode()
            }
            "\$&0A4\r\n"->{
                var str=context?.resources?.getString(R.string.change_customer_fail)
             //   Toast.makeText(context, str, Toast.LENGTH_SHORT).show()
            }
            "\$&0A2\r\n"->{
                var str=context?.resources?.getString(R.string.change_major_fail)
             //   Toast.makeText(context, str, Toast.LENGTH_SHORT).show()
            }
            "\$@0A2\r\n"->{
                var str=context?.resources?.getString(R.string.change_major_ok)
             //   Toast.makeText(context, str, Toast.LENGTH_SHORT).show()
                beaconDetailView?.onSetOk("Major")
              //  setMode()
            }
            "\$&0A3\r\n"->{
                var str=context?.resources?.getString(R.string.change_minor_fail)
              //  Toast.makeText(context, str, Toast.LENGTH_SHORT).show()
            }
            "\$@0A3\r\n"->{
                var str=context?.resources?.getString(R.string.change_minor_ok)
              //  Toast.makeText(context, str, Toast.LENGTH_SHORT).show()
                beaconDetailView?.onSetOk("Minor")
              //  setMode()
            }
            "\$&0A7\r\n"->{
                var str=context?.resources?.getString(R.string.change_txpower_fail)
             //   Toast.makeText(context, str, Toast.LENGTH_SHORT).show()
            }
            "\$@0A7\r\n"->{
                var str=context?.resources?.getString(R.string.change_txpower_ok)
               // Toast.makeText(context, str, Toast.LENGTH_SHORT).show()
                beaconDetailView?.onSetOk("Txpower")
               // setMode()
            }
            "\$&0A6\r\n"->{
                var str=context?.resources?.getString(R.string.change_fail)
               // Toast.makeText(context, str, Toast.LENGTH_SHORT).show()
            }
            "\$@0A6\r\n"->{
                var str=context?.resources?.getString(R.string.change_adv)
             //   Toast.makeText(context, str, Toast.LENGTH_SHORT).show()
                beaconDetailView?.onSetOk("Interval")
               // setMode()
            }
            "\$@0A8\r\n"->{
                var str=context?.resources?.getString(R.string.change_rssi)
               // Toast.makeText(context, str, Toast.LENGTH_SHORT).show()
                beaconDetailView?.onSetOk("Rssi At 1M")
                // setMode()
            }
            "\$@0C5\r\n"->{
                var str=context?.resources?.getString(R.string.change_ok)
              //  Toast.makeText(context, str, Toast.LENGTH_SHORT).show()
                // setMode()
            }
            "\$@0C1\r\n"->{
                var str=context?.resources?.getString(R.string.change_ok)
               // Toast.makeText(context, str, Toast.LENGTH_SHORT).show()
                // setMode()
            }  "\$@0C6\r\n"->{
            var str=context?.resources?.getString(R.string.change_ok)
           // Toast.makeText(context, str, Toast.LENGTH_SHORT).show()
            // setMode()
        }

            "\$@0C2\r\n"->{
                connectModel?.close()
            }

        }
    }

    override fun onDataAcc(data: ByteArray) {
     //   TODO("Not yet implemented")
        System.out.println("111真实数据="+StringUtils.ByteArrToHex(data))
        beaconDetailView?.setAcc(data)
    }

    override fun onDataTemp(data: ByteArray) {
       // TODO("Not yet implemented")
        System.out.println("222真实数据="+StringUtils.ByteArrToHex(data))
         beaconDetailView?.setTemp(data)

    }
    override fun onDataVersion(data: ByteArray){
        for(i in data.indices){
            data[i]= (data[i]+1).toByte();
        }
    System.out.println("版本号="+StringUtils.ByteArrToHex(data))
        beaconDetailView?.showVersion(String(data))
    }
   fun setMode(){
       val data =ByteArray(3)
       data[0]=0x24;
       data[1]=0x31;
       data[2]=0x30;
       sendValue(data,Type.setMode_beacon);
   }
    override fun onDisConnected(address: String) {
       // TODO("Not yet implemented")

        beaconDetailView?.connectDis()
    }

    override fun onConnected(address: String) {
       // TODO("Not yet implemented")
        beaconDetailView?.showPasswordDialog()

    }

}