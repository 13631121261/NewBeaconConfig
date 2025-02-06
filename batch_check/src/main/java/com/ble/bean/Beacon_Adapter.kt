package com.ble.bean

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.ble.beaconcheck.R
import com.ble.view_sw.MyFilterViewCallBack

/**
 * @package com.ble.bean
 * @fileName Beacon_Adapter
 * @date 2020/6/2410:16
 * @author Andesen
 * @email 929620555@qq.com
 * @describe 自定义描述
 */
class Beacon_Adapter(var context: Context): RecyclerView.Adapter<RecyclerView.ViewHolder>() ,MyFilterViewCallBack{
    var refresh:Int=0;
    var nameOrMac_Filter:String?="";
    var rawData_Filter:String?="";
    var rssi_Filter:Int=-100;
    var data : ArrayList<Beacon>?=null
    private var opened :Int= -1
    var flag=false;

    override fun getItemCount(): Int {
       // TODO("Not yet implemented")
      //  Log.e(MLog.NormalE,"数据长度="+data!!.size)
        if(data==null){
          //  System.out.println("步骤555")
            return 0;
        }
      //  System.out.println("步骤777===" + data!!.size)
        return data!!.size;
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        //TODO("Not yet implemented")

        if(viewType==1){

            var view=LayoutInflater.from(parent.context).inflate(R.layout.item_beacon, parent, false);
            return Beacon_ViewHolder(view) ;
        }else {
            var view=LayoutInflater.from(parent.context).inflate(R.layout.nullitem, parent, false);
            return Null_ViewHolder(view);
        }
      // Log.e(MLog.NormalE,"onCreateViewHolder="+data!!.size)

    }

    override fun onScan() {
        //TODO("Not yet implemented")
   //     noDeviceCallback?.onScan1()
    }
    override fun getItemViewType(position: Int): Int {
        return 1;
       /* if(position==0){
            flag=false
        }
        var beacon=data?.get(position)
      //  Log.e(MLog.NormalE, "beacon.flag" + beacon?.flag)
       *//* if(beacon==null){
            return 0;
        }*//*
        //信号符合
        if(beacon?.rssi!! <rssi_Filter&&!beacon?.flag!!){
            if(position+1==(data?.size)){
                if(!flag){
                  //  Log.e(MLog.NormalE, "11该显示背景")
                        noDeviceCallback?.noData();
                }
            }
            return 0;
        }else{
            beacon.flag=true;
            data?.removeAt(position)
            data?.add(position,beacon)
            //原始数据符合
            if(rawData_Filter?.let { beacon.raw?.contains(it) }!!){
                //mac符合
                if(beacon.address.replace(":","").contains(nameOrMac_Filter!!, true)){
                   // Log.e(MLog.NormalE,"Mac="+beacon.address)
                    //Log.e(MLog.NormalE,"Mac="+ StringUtils.replaceAll(beacon.address,":",""))
                    //Log.e(MLog.NormalE,"nameOrMac_Filter="+nameOrMac_Filter)
                    flag=true
                    noDeviceCallback?.hasdata();
                    return 1
                }
                //name符合
               else if(beacon.deviceName.contains(nameOrMac_Filter!!, true)){

                    //Log.e(MLog.NormalE,"Name="+beacon.deviceName)
                    //Log.e(MLog.NormalE,"nameOrMac_Filter="+nameOrMac_Filter)
                    flag=true
                    noDeviceCallback?.hasdata();
                    return 1
                }
                else
                {
                    if(position+1==(data?.size)){
                        if(!flag){
                           // Log.e(MLog.NormalE, "22该显示背景")
                            noDeviceCallback?.noData();
                        }
                    }
                    return 0
                }
            }
            else{
                if(position+1==(data?.size)){
                    if(!flag){
                       // Log.e(MLog.NormalE, "33该显示背景")
                        noDeviceCallback?.noData();
                    }
                }
                return 0
            }
        }*/
    }
    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        //TODO("Not yet implemented")

        if (holder is Beacon_ViewHolder) {
            data?.get(position)?.let { holder.bind(position, it) }
        }
    }


   inner class Beacon_ViewHolder(itemView: View):RecyclerView.ViewHolder(itemView!!){
       private var view:View?=null;
       private var device_name:TextView?=null;
       private var txt_rssi:TextView?=null;
       private var txt_intervaal:TextView?=null;
       private var ima_interval:ImageView?=null
       private var txt_companyId:TextView?=null;
       private var txt_Type:TextView?=null;
       private var txt_UUID:TextView?=null;
       private var txtUUID:TextView?=null
       private var txt_Major:TextView?=null;
       private var txtMajor:TextView?=null;
       private var txt_Minor:TextView?=null;
       private var txtMinor:TextView?=null;
       private var txt_rssiAtOne:TextView?=null;
       private var txtRssiAtOne:TextView?=null;
       private var txt_txpower:TextView?=null;
       private var txtTxpower:TextView?=null;
       private var txt_ServerData:TextView?=null;
       private var logo:ImageView?=null
       private var ralate_detail:RelativeLayout?=null
       private  var mac_address:TextView?=null
       private var btn_connect: TextView?=null
       private var imageView:ImageView?=null
       private  var aminor:TextView?=null
       private  var amajor:TextView?=null
       private  var auuid:TextView?=null
       var txt_temps:TextView?=null
       var txt_shidus:TextView?=null
       var txt_temp:TextView?=null
       var txt_shidu:TextView?=null
       var txtServerData:TextView?=null
       var txt_bbt:TextView?=null
       var txt_bt:TextView?=null
       var txt_uid_n:TextView?=null
       var txt_uid_NameSpace:TextView?=null
       var txt_uid_i:TextView?=null
       var txt_uid_instance:TextView?=null
       var txt_txpower_0:TextView?=null
       var txt_txpower_0m:TextView?=null
       var txt_url:TextView?=null
       var txt_url_data:TextView?=null
       var txt_eid:TextView?=null
       var txt_eid_data:TextView?=null
       var txt_tlm_on:TextView?=null
       var txt_tlm_on_data:TextView?=null
       var txt_tlm_bt:TextView?=null
       var txt_tlm_bt_data:TextView?=null
       var txt_tlm_wendu:TextView?=null
       var txt_tlm_wendu_data:TextView?=null
       var txt_tlm_pdu:TextView?=null
       var txt_tlm_pdu_data:TextView?=null
       var txt_tlm_run:TextView?=null
       var txt_tlm_run_count:TextView?=null
       var txt_tlm_on_salt:TextView?=null
       var txt_tlm_on_salt_data:TextView?=null
       var txt_tlm_on_mic:TextView?=null
       var txt_tlm_on_mic_data:TextView?=null




            init {
                view=itemView
                btn_connect=itemView.findViewById(R.id.btn_connect)
                mac_address=itemView.findViewById(R.id.mac_address);
                device_name=itemView.findViewById(R.id.device_name);
                txt_rssi=itemView.findViewById(R.id.txt_rssi);
                txt_intervaal=itemView.findViewById(R.id.txt_intervaal);
                txt_companyId=itemView.findViewById(R.id.txt_companyId);
                txt_Type=itemView.findViewById(R.id.txt_Type);
                txt_UUID=itemView.findViewById(R.id.txt_UUID);
                txtUUID=itemView.findViewById(R.id.txtUUID);
                txt_Major=itemView.findViewById(R.id.txt_Major);
                txtMinor=itemView.findViewById(R.id.txtMinor);
                txt_Minor=itemView.findViewById(R.id.txt_Minor);
                txtMajor=itemView.findViewById(R.id.txtMajor);
                txt_rssiAtOne=itemView.findViewById(R.id.txt_rssiAtOne);
                txtRssiAtOne=itemView.findViewById(R.id.txtRssiAtOne);
                txt_txpower=itemView.findViewById(R.id.txt_txpower);
                txtTxpower=itemView.findViewById(R.id.txtTxPower);
                txt_ServerData=itemView.findViewById(R.id.txt_ServerData);
                ralate_detail=itemView.findViewById(R.id.ralate_detail);
                ima_interval=itemView.findViewById(R.id.ima_interval)
                imageView=itemView.findViewById(R.id.imageViewaa)
                txt_temps=itemView.findViewById(R.id.txt_temps)
                txt_shidus=itemView.findViewById(R.id.txt_shidus)
                txt_temp=itemView.findViewById(R.id.txt_temp)
                txt_shidu=itemView.findViewById(R.id.txt_shidu)
                txtServerData=itemView.findViewById(R.id.txtServerData)
                txt_bbt=itemView.findViewById(R.id.txt_bbt);
                txt_bt=itemView.findViewById(R.id.txt_bts);
                logo=itemView.findViewById(R.id.logo)

                amajor=itemView.findViewById(R.id.amajor)
                aminor=itemView.findViewById(R.id.aminor)
                auuid=itemView.findViewById(R.id.auuid)







                 txt_uid_n=itemView.findViewById(R.id.txt_uid_n)
                 txt_uid_NameSpace=itemView.findViewById(R.id.txt_uid_NameSpace)
                 txt_uid_i=itemView.findViewById(R.id.txt_uid_i)
                 txt_uid_instance=itemView.findViewById(R.id.txt_uid_instance)
                 txt_txpower_0=itemView.findViewById(R.id.txt_txpower_0)
                 txt_txpower_0m=itemView.findViewById(R.id.txt_txpower_0m)
                 txt_url=itemView.findViewById(R.id.txt_url)
                 txt_url_data=itemView.findViewById(R.id.txt_url_data)
                 txt_eid=itemView.findViewById(R.id.txt_eid)
                 txt_eid_data=itemView.findViewById(R.id.txt_eid_data)
                 txt_tlm_on=itemView.findViewById(R.id.txt_tlm_on)
                 txt_tlm_on_data=itemView.findViewById(R.id.txt_tlm_on_data)
                 txt_tlm_bt=itemView.findViewById(R.id.txt_tlm_bt)
                 txt_tlm_bt_data=itemView.findViewById(R.id.txt_tlm_bt_data)
                 txt_tlm_wendu=itemView.findViewById(R.id.txt_tlm_wendu)
                 txt_tlm_wendu_data=itemView.findViewById(R.id.txt_tlm_wendu_data)
                 txt_tlm_pdu=itemView.findViewById(R.id.txt_tlm_pdu)
                 txt_tlm_pdu_data=itemView.findViewById(R.id.txt_tlm_pdu_data)
                 txt_tlm_run=itemView.findViewById(R.id.txt_tlm_run)
                 txt_tlm_run_count=itemView.findViewById(R.id.txt_tlm_run_count)
                 txt_tlm_on_salt=itemView.findViewById(R.id.txt_tlm_on_salt)
                 txt_tlm_on_salt_data=itemView.findViewById(R.id.txt_tlm_on_salt_data)
                 txt_tlm_on_mic=itemView.findViewById(R.id.txt_tlm_on_mic)
                 txt_tlm_on_mic_data=itemView.findViewById(R.id.txt_tlm_on_mic_data)

              //  Log.e(MLog.NormalE,"Beacon_ViewHolder="+data!!.size)

            }
       @SuppressLint("SuspiciousIndentation")
       fun bind(position: Int, beacon: Beacon){
          // textview!!.text = data
           //Log.e(MLog.ScannerE, beacon.address)
           if (position == opened){
               ralate_detail!!.visibility=View.VISIBLE;
           } else{
               ralate_detail!!.visibility=View.GONE;
           }
           mac_address?.text=beacon.address
           device_name?.text=beacon.deviceName
           /* when(beacon.status){
            -1 ->{
                btn_connect?.setText("待连接")
            }
                10 ->{
                    btn_connect?.setText("完成")
                }
                -3 ->{
                    btn_connect?.setText("超时")
                }
                else->{
                    btn_connect?.setText("进行中")
                }
            }*/

           txt_rssi?.text= beacon.rssi.toString()+" dBm"

           txt_intervaal?.text=beacon.interval.toString()+" ms"
           txt_companyId?.text=beacon.companyId.toString()
           //txt_Type?.text=beacon.;
           txt_UUID?.text=beacon.uuid
           auuid?.text="UUID:"+beacon.uuid
           amajor?.text="Major:"+beacon.major.toString()
           aminor?.text="Minor:"+beacon.minor.toString()
           txt_Major?.text=beacon.major.toString()
           txt_Minor?.text=beacon.minor.toString()
           txt_rssiAtOne?.text=beacon.rssiInOne.toString()+" dBm"
           txt_txpower?.text=beacon.txPower.toString()+" dBm"
           txt_ServerData?.text=beacon.serviceData
           txt_shidus?.text=""+beacon?.humidity+"%"
           txt_temps?.text=""+beacon?.temp+"°C"
           txt_bbt?.text=""+beacon?.bt+"%"
           if(beacon?.bt!! <=100){
               txt_bbt?.text=""+beacon?.bt+"%"
           }else {
               txt_bbt?.text=""+beacon?.bt+"mV"
           }
           view!!.setOnClickListener {



           }
          //  System.out.println("很花心图标="+imageView+"  状态="+beacon.isMove)
           if(beacon.isOpenAcc){
               imageView?.visibility=View.VISIBLE
           }
           else{
               imageView?.visibility=View.GONE
           }
           if(beacon.isMove==0){
               imageView?.setImageResource(R.mipmap.stopa)
           }
           else if(beacon.isMove==1){
               imageView?.setImageResource(R.mipmap.start)
           }

           if(beacon.interval.toInt() ==19996){
               txt_intervaal?.visibility=View.GONE
           }else{
               txt_intervaal?.visibility=View.VISIBLE
           }
           //beacon.interval=19996


           if(beacon.isSupportTempShidu){
               txt_temps?.visibility=View.VISIBLE
               txt_temp?.visibility=View.VISIBLE
               txt_shidus?.visibility=View.VISIBLE
               txt_shidu?.visibility=View.VISIBLE
           }
           else{
               txt_temps?.visibility=View.GONE
               txt_temp?.visibility=View.GONE
               txt_shidus?.visibility=View.GONE
               txt_shidu?.visibility=View.GONE
           }
           if(beacon.OnlyBeacon){
               txt_ServerData?.visibility=View.GONE
               txtServerData?.visibility=View.GONE
            //   txt_bbt?.visibility=View.GONE
             //  txt_bt?.visibility=View.GONE
           }
           else{
               txt_ServerData?.visibility=View.GONE
               txtServerData?.visibility=View.GONE
            //   txt_bbt?.visibility=View.VISIBLE
             //  txt_bt?.visibility=View.VISIBLE
           }
           if(beacon?.bt==1998){
               txt_bbt?.visibility=View.GONE
               txt_bt?.visibility=View.GONE
           }
           else{
               txt_bbt?.visibility=View.VISIBLE
               txt_bt?.visibility=View.VISIBLE
           }

           if(beacon.type==4){
               logo?.setImageResource(R.mipmap.logo1)
           }else{
               logo?.setImageResource(R.mipmap.eddystone)
           }



            txt_uid_NameSpace?.text=""+beacon.uid_Namespace

            txt_uid_instance?.text=""+beacon.uid_ID_Instance

            txt_txpower_0m?.text=""+beacon.tx_power_at_0m+" dBm"


           if(beacon?.url_head==0){
               txt_url_data?.text="http://www."+beacon?.url_data
           }else if(beacon?.url_head==1){
               txt_url_data?.text="https://www."+beacon?.url_data
           }
           else if(beacon?.url_head==2){
               txt_url_data?.text="http://"+beacon?.url_data
           }
           else if(beacon?.url_head==3){
               txt_url_data?.text="https://"+beacon?.url_data
           }

            txt_eid_data?.text=""+beacon.eid_data

            txt_tlm_on_data?.text=""+beacon.tlm_data

            txt_tlm_bt_data?.text=""+beacon.tlm_bt+"mV"

            txt_tlm_wendu_data?.text=""+beacon.tlm_temp

            txt_tlm_pdu_data?.text=""+beacon.tlm_Advertising_PDU

            txt_tlm_run_count?.text=""+beacon.tlm_work_time

            txt_tlm_on_salt_data?.text=""+beacon.tlm_salt

            txt_tlm_on_mic_data?.text=""+beacon.tlm_check

           if(beacon.type==4){
                txt_UUID?.visibility=View.VISIBLE
                txtUUID?.visibility=View.VISIBLE
                txt_Major?.visibility=View.VISIBLE
                txt_Minor?.visibility=View.VISIBLE
                txtMajor?.visibility=View.VISIBLE
                txtMinor?.visibility=View.VISIBLE
                txt_rssiAtOne?.visibility=View.VISIBLE
                txtRssiAtOne?.visibility=View.VISIBLE
                txt_uid_n?.visibility=View.GONE
                txt_uid_NameSpace?.visibility=View.GONE
                txt_uid_i?.visibility=View.GONE
                txt_uid_instance?.visibility=View.GONE
                txt_txpower_0?.visibility=View.GONE
                txt_txpower_0m?.visibility=View.GONE
                txt_url?.visibility=View.GONE
                txt_url_data?.visibility=View.GONE
                txt_eid?.visibility=View.GONE
                txt_eid_data?.visibility=View.GONE
                txt_tlm_on?.visibility=View.GONE
                txt_tlm_on_data?.visibility=View.GONE
                txt_tlm_bt?.visibility=View.GONE
                txt_tlm_bt_data?.visibility=View.GONE
                txt_tlm_wendu?.visibility=View.GONE
                txt_tlm_wendu_data?.visibility=View.GONE
                txt_tlm_pdu?.visibility=View.GONE
                txt_tlm_pdu_data?.visibility=View.GONE
                txt_tlm_run?.visibility=View.GONE
                txt_tlm_run_count?.visibility=View.GONE
                txt_tlm_on_salt?.visibility=View.GONE
                txt_tlm_on_salt_data?.visibility=View.GONE
                txt_tlm_on_mic?.visibility=View.GONE
                txt_tlm_on_mic_data?.visibility=View.GONE
           }else if(beacon?.type==0){
               txt_UUID?.visibility=View.GONE
               txtUUID?.visibility=View.GONE
               txt_Major?.visibility=View.GONE
               txt_Minor?.visibility=View.GONE
               txtMajor?.visibility=View.GONE
               txtMinor?.visibility=View.GONE
               txt_rssiAtOne?.visibility=View.GONE
               txtRssiAtOne?.visibility=View.GONE
               txt_uid_n?.visibility=View.VISIBLE
               txt_uid_NameSpace?.visibility=View.VISIBLE
               txt_uid_i?.visibility=View.VISIBLE
               txt_uid_instance?.visibility=View.VISIBLE
               txt_txpower_0?.visibility=View.VISIBLE
               txt_txpower_0m?.visibility=View.VISIBLE
               txt_url?.visibility=View.GONE
               txt_url_data?.visibility=View.GONE
               txt_eid?.visibility=View.GONE
               txt_eid_data?.visibility=View.GONE
               txt_tlm_on?.visibility=View.GONE
               txt_tlm_on_data?.visibility=View.GONE
               txt_tlm_bt?.visibility=View.GONE
               txt_tlm_bt_data?.visibility=View.GONE
               txt_tlm_wendu?.visibility=View.GONE
               txt_tlm_wendu_data?.visibility=View.GONE
               txt_tlm_pdu?.visibility=View.GONE
               txt_tlm_pdu_data?.visibility=View.GONE
               txt_tlm_run?.visibility=View.GONE
               txt_tlm_run_count?.visibility=View.GONE
               txt_tlm_on_salt?.visibility=View.GONE
               txt_tlm_on_salt_data?.visibility=View.GONE
               txt_tlm_on_mic?.visibility=View.GONE
               txt_tlm_on_mic_data?.visibility=View.GONE
           }

           else if(beacon?.type==1){
               txt_UUID?.visibility=View.GONE
               txtUUID?.visibility=View.GONE
               txt_Major?.visibility=View.GONE
               txt_Minor?.visibility=View.GONE
               txtMajor?.visibility=View.GONE
               txtMinor?.visibility=View.GONE
               txt_rssiAtOne?.visibility=View.GONE
               txtRssiAtOne?.visibility=View.GONE
               txt_uid_n?.visibility=View.GONE
               txt_uid_NameSpace?.visibility=View.GONE
               txt_uid_i?.visibility=View.GONE
               txt_uid_instance?.visibility=View.GONE
               txt_txpower_0?.visibility=View.VISIBLE
               txt_txpower_0m?.visibility=View.VISIBLE
               txt_url?.visibility=View.VISIBLE
               txt_url_data?.visibility=View.VISIBLE
               txt_eid?.visibility=View.GONE
               txt_eid_data?.visibility=View.GONE
               txt_tlm_on?.visibility=View.GONE
               txt_tlm_on_data?.visibility=View.GONE
               txt_tlm_bt?.visibility=View.GONE
               txt_tlm_bt_data?.visibility=View.GONE
               txt_tlm_wendu?.visibility=View.GONE
               txt_tlm_wendu_data?.visibility=View.GONE
               txt_tlm_pdu?.visibility=View.GONE
               txt_tlm_pdu_data?.visibility=View.GONE
               txt_tlm_run?.visibility=View.GONE
               txt_tlm_run_count?.visibility=View.GONE
               txt_tlm_on_salt?.visibility=View.GONE
               txt_tlm_on_salt_data?.visibility=View.GONE
               txt_tlm_on_mic?.visibility=View.GONE
               txt_tlm_on_mic_data?.visibility=View.GONE
           }
           else if(beacon?.type==2){
               txt_UUID?.visibility=View.GONE
               txtUUID?.visibility=View.GONE
               txt_Major?.visibility=View.GONE
               txt_Minor?.visibility=View.GONE
               txtMajor?.visibility=View.GONE
               txtMinor?.visibility=View.GONE
               txt_rssiAtOne?.visibility=View.GONE
               txtRssiAtOne?.visibility=View.GONE
               txt_uid_n?.visibility=View.GONE
               txt_uid_NameSpace?.visibility=View.GONE
               txt_uid_i?.visibility=View.GONE
               txt_uid_instance?.visibility=View.GONE
               txt_txpower_0?.visibility=View.GONE
               txt_txpower_0m?.visibility=View.GONE
               txt_url?.visibility=View.GONE
               txt_url_data?.visibility=View.GONE
               txt_eid?.visibility=View.GONE
               txt_eid_data?.visibility=View.GONE
               txt_tlm_on?.visibility=View.GONE
               txt_tlm_on_data?.visibility=View.GONE
               txt_tlm_bt?.visibility=View.GONE
               txt_tlm_bt_data?.visibility=View.GONE
               txt_tlm_wendu?.visibility=View.GONE
               txt_tlm_wendu_data?.visibility=View.GONE
               txt_tlm_pdu?.visibility=View.GONE
               txt_tlm_pdu_data?.visibility=View.GONE
               txt_tlm_run?.visibility=View.GONE
               txt_tlm_run_count?.visibility=View.GONE
               txt_tlm_on_salt?.visibility=View.GONE
               txt_tlm_on_salt_data?.visibility=View.GONE
               txt_tlm_on_mic?.visibility=View.GONE
               txt_tlm_on_mic_data?.visibility=View.GONE
               if(beacon.tlm_type==0){
                   txt_tlm_on?.visibility=View.GONE
                   txt_tlm_on_data?.visibility=View.GONE
                   txt_tlm_on_salt?.visibility=View.GONE
                   txt_tlm_on_salt_data?.visibility=View.GONE
                   txt_tlm_on_mic?.visibility=View.GONE
                   txt_tlm_on_mic_data?.visibility=View.GONE
                   txt_tlm_bt?.visibility=View.VISIBLE
                   txt_tlm_bt_data?.visibility=View.VISIBLE
                   txt_tlm_wendu?.visibility=View.VISIBLE
                   txt_tlm_wendu_data?.visibility=View.VISIBLE
                   txt_tlm_pdu?.visibility=View.VISIBLE
                   txt_tlm_pdu_data?.visibility=View.VISIBLE
                   txt_tlm_run?.visibility=View.VISIBLE
                   txt_tlm_run_count?.visibility=View.VISIBLE
               }else{
                   txt_tlm_on?.visibility=View.VISIBLE
                   txt_tlm_on_data?.visibility=View.VISIBLE
                   txt_tlm_on_salt?.visibility=View.VISIBLE
                   txt_tlm_on_salt_data?.visibility=View.VISIBLE
                   txt_tlm_on_mic?.visibility=View.VISIBLE
                   txt_tlm_on_mic_data?.visibility=View.VISIBLE
                   txt_tlm_bt?.visibility=View.GONE
                   txt_tlm_bt_data?.visibility=View.GONE
                   txt_tlm_wendu?.visibility=View.GONE
                   txt_tlm_wendu_data?.visibility=View.GONE
                   txt_tlm_pdu?.visibility=View.GONE
                   txt_tlm_pdu_data?.visibility=View.GONE
                   txt_tlm_run?.visibility=View.GONE
                   txt_tlm_run_count?.visibility=View.GONE
               }
           }else if(beacon.type==3){
               txt_UUID?.visibility=View.GONE
               txtUUID?.visibility=View.GONE
               txt_Major?.visibility=View.GONE
               txt_Minor?.visibility=View.GONE
               txtMajor?.visibility=View.GONE
               txtMinor?.visibility=View.GONE
               txt_rssiAtOne?.visibility=View.GONE
               txtRssiAtOne?.visibility=View.GONE
               txt_uid_n?.visibility=View.GONE
               txt_uid_NameSpace?.visibility=View.GONE
               txt_uid_i?.visibility=View.GONE
               txt_uid_instance?.visibility=View.GONE
               txt_txpower_0?.visibility=View.VISIBLE
               txt_txpower_0m?.visibility=View.VISIBLE
               txt_url?.visibility=View.GONE
               txt_url_data?.visibility=View.GONE
               txt_eid?.visibility=View.VISIBLE
               txt_eid_data?.visibility=View.VISIBLE
               txt_tlm_on?.visibility=View.GONE
               txt_tlm_on_data?.visibility=View.GONE
               txt_tlm_bt?.visibility=View.GONE
               txt_tlm_bt_data?.visibility=View.GONE
               txt_tlm_wendu?.visibility=View.GONE
               txt_tlm_wendu_data?.visibility=View.GONE
               txt_tlm_pdu?.visibility=View.GONE
               txt_tlm_pdu_data?.visibility=View.GONE
               txt_tlm_run?.visibility=View.GONE
               txt_tlm_run_count?.visibility=View.GONE
               txt_tlm_on_salt?.visibility=View.GONE
               txt_tlm_on_salt_data?.visibility=View.GONE
               txt_tlm_on_mic?.visibility=View.GONE
               txt_tlm_on_mic_data?.visibility=View.GONE
           }

          // println("log=== "+ beacon.address+"    "+beacon.isOpenTemp)

         //  Log.e(MLog.NormalE,"bind="+data)
       }

    }
    inner class Null_ViewHolder(itemView: View):RecyclerView.ViewHolder(itemView!!){

    }

    override fun onNameOrMac(content: String) {
        //  TODO("Not yet implemented")
        nameOrMac_Filter=content;
        refresh=1;
        Log.e(MLog.ScannerE, "nameOrMac_Filter=" + content)
        notifyDataSetChanged()
    }

    override fun onRawData(content: String) {
        // TODO("Not yet implemented")
        rawData_Filter=content;
        refresh=1;
        Log.e(MLog.ScannerE, "rawData_Filter=" + content)
        notifyDataSetChanged()
    }

    override fun onRssi(rssi: Int) {
        //TODO("Not yet implemented")
        rssi_Filter=-rssi;
        refresh=1;
        Log.e(MLog.ScannerE, "rssi_Filter=" + rssi_Filter)
        if(data==null){
            return
        }
        for (item in data!!){
            item?.flag=false;
        }
        data=null
        notifyDataSetChanged()

    }

    override fun onchange() {
     //   noDeviceCallback?.onchange()
    }
    interface  NoDeviceCallback{
        fun noData();
        fun cclick():Boolean;
        fun hasdata();
        fun onScan1();
        fun onClickConnect(beacon: Beacon)
        fun onchange()
    }
}