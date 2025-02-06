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
import com.ble.aoaconfig.R
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
    var major=-1;
    var minor=-1;
    var rssi_Filter:Int=-100;
    var data : ArrayList<Beacon>?=null
    private var opened :Int= -1
    var  noDeviceCallback:NoDeviceCallback?=null
    var flag=false;
    var ima_interval:ImageView?=null
    override fun getItemCount(): Int {
       // TODO("Not yet implemented")
      //  Log.e(MLog.NormalE,"数据长度="+data!!.size)
        if(data==null){
         //   System.out.println("步骤555")
            return 0;
        }
   //     System.out.println("步骤777===" + data!!.size)
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
        noDeviceCallback?.onScan1()
    }
    override fun getItemViewType(position: Int): Int {
        if(position==0){
            flag=false
        }
        var beacon=data?.get(position)
        Log.e(MLog.NormalE, "beacon.flag" + beacon?.flag)
       /* if(beacon==null){
            return 0;
        }*/
        //信号符合
        if(beacon?.rssi!! <rssi_Filter&&!beacon?.flag!!){
            if(position+1==(data?.size)){
                if(!flag){
                    Log.e(MLog.NormalE, "11该显示背景")
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
                println("满足基本条件"+major)
                if(beacon.address.replace(":","").contains(nameOrMac_Filter!!, true)&&major==-1&&minor==-1){
                    Log.e(MLog.NormalE,"Mac="+beacon.address)
                    //Log.e(MLog.NormalE,"Mac="+ StringUtils.replaceAll(beacon.address,":",""))
                    //Log.e(MLog.NormalE,"nameOrMac_Filter="+nameOrMac_Filter)
                    flag=true
                    noDeviceCallback?.hasdata();
                    return 1
                }
                //name符合
               else if(beacon.deviceName.contains(nameOrMac_Filter!!, true)&&major==-1&&minor==-1){

                    //Log.e(MLog.NormalE,"Name="+beacon.deviceName)
                    //Log.e(MLog.NormalE,"nameOrMac_Filter="+nameOrMac_Filter)
                    flag=true
                    noDeviceCallback?.hasdata();
                    return 1
                }
                else   if(major>-1||minor>-1){
                    if(major>-1&&minor>-1){
                        Log.e(MLog.NormalE, "异常111="+beacon.major)
                        if((beacon.major==major)&&(beacon.minor== minor)){
                            Log.e(MLog.NormalE, "异常222="+beacon.major)
                            flag=true
                            noDeviceCallback?.hasdata();
                            return 1
                        }
                        else {
                            if(position+1==(data?.size)){
                                if(!flag){
                                    Log.e(MLog.NormalE, "33该显示背景")
                                    noDeviceCallback?.noData();
                                }
                            }
                            return 0
                        }
                    }
                  else  if(major>-1&&minor==-1){
                        Log.e(MLog.NormalE, "异常111="+beacon.major)
                        if(beacon.major==major){
                            Log.e(MLog.NormalE, "异常222="+beacon.major)
                            flag=true
                            noDeviceCallback?.hasdata();
                            return 1
                        }else{
                            if(position+1==(data?.size)){
                                if(!flag){
                                    Log.e(MLog.NormalE, "33该显示背景")
                                    noDeviceCallback?.noData();
                                }
                            }
                            return 0
                        }
                    }
                    else  if(minor>-1&&major==-1){
                        Log.e(MLog.NormalE, "异常111="+beacon.major)
                        if(beacon.minor==minor){
                            Log.e(MLog.NormalE, "异常222="+beacon.major)
                            flag=true
                            noDeviceCallback?.hasdata();
                            return 1
                        }else{
                            if(position+1==(data?.size)){
                                if(!flag){
                                    Log.e(MLog.NormalE, "33该显示背景")
                                    noDeviceCallback?.noData();
                                }
                            }
                            return 0
                        }
                    }else{
                        if(position+1==(data?.size)){
                            if(!flag){
                                Log.e(MLog.NormalE, "33该显示背景")
                                noDeviceCallback?.noData();
                            }
                        }
                        return 0
                    }
                }
                else{
                    if(position+1==(data?.size)){
                        if(!flag){
                            Log.e(MLog.NormalE, "33该显示背景")
                            noDeviceCallback?.noData();
                        }
                    }
                    return 0
                }
            }
            else{
                if(position+1==(data?.size)){
                    if(!flag){
                        Log.e(MLog.NormalE, "33该显示背景")
                        noDeviceCallback?.noData();
                    }
                }
                return 0
            }
        }
        return 0
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
       private var logo:ImageView?=null
       private  var mac_address:TextView?=null
       private var btn_connect: ImageView?=null
       private var imageView:ImageView?=null
       private var mode:TextView?=null
       var txt_bbt:TextView?=null
            init {
                view=itemView
                btn_connect=itemView.findViewById(R.id.btn_connect)
                mac_address=itemView.findViewById(R.id.mac_address);
                device_name=itemView.findViewById(R.id.device_name);
                txt_rssi=itemView.findViewById(R.id.txt_rssi);
                txt_intervaal=itemView.findViewById(R.id.txt_intervaal);
                txt_bbt=itemView.findViewById(R.id.bt)

                ima_interval=itemView.findViewById(R.id.ima_interval)
                imageView=itemView.findViewById(R.id.imageViewaa)

                logo=itemView.findViewById(R.id.logo)
                mode=itemView.findViewById(R.id.mode);

              //  Log.e(MLog.NormalE,"Beacon_ViewHolder="+data!!.size)
            }
       @SuppressLint("SuspiciousIndentation")
       fun bind(position: Int, beacon: Beacon){
          // textview!!.text = data
           Log.e(MLog.ScannerE, beacon.address)
           mac_address?.text=beacon.address
           device_name?.text="Chanel: "+beacon.channel
           txt_rssi?.text= beacon.rssi.toString()+" dBm"
           mode?.text="Mode："+beacon.mode
           txt_intervaal?.text=beacon.interval.toString()+" ms"
           //txt_Type?.text=beacon.;
           txt_bbt?.text="Battery: "+ beacon.bt
           view!!.setOnClickListener {
                var status= noDeviceCallback?.cclick();
                    if(status!=true){
                        if (opened == adapterPosition) {
                            //当点击的item已经被展开了, 就关闭.
                            opened = -1;
                            notifyItemChanged(adapterPosition);
                        } else {
                            val  oldOpened = opened;
                            opened = adapterPosition;
                            notifyItemChanged(oldOpened);
                            notifyItemChanged(opened);
                        }
                    }
           }

           if(beacon.isMove==0){
               imageView?.setImageResource(R.mipmap.stopa)
           }
           else if(beacon.isMove==1){
               imageView?.setImageResource(R.mipmap.start)
           }
           if(beacon.IsConnect){
               btn_connect?.visibility=View.VISIBLE
              // ima_interval?.visibility=View.VISIBLE
              // txt_intervaal?.visibility=View.VISIBLE
             //  imageView?.visibility=View.VISIBLE
           }
           else{
             //  btn_connect?.visibility=View.GONE
             //  ima_interval?.visibility=View.GONE
             //  txt_intervaal?.visibility=View.GONE
             //  imageView?.visibility=View.GONE
           }
           if(beacon.interval.toInt() ==19996){
               txt_intervaal?.visibility=View.GONE
           }else{
               txt_intervaal?.visibility=View.VISIBLE
           }




               logo?.setImageResource(R.mipmap.logo1)









          // println("log=== "+ beacon.address+"    "+beacon.isOpenTemp)
           btn_connect?.setOnClickListener(View.OnClickListener {
               //TODO("Not yet implemented")
               noDeviceCallback?.onClickConnect(beacon)
           })
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

    @SuppressLint("NotifyDataSetChanged")
    override fun onMajor_Minor(major1: Int, minor1: Int) {
     //   TODO("Not yet implemented")
    //    rawData_Filter=rawData_Filter+" "+major+" "+ minor
        this.major=major1
        this.minor= minor1
        refresh=1;
        notifyDataSetChanged()
        println("过滤 major"+  this.major+"  "+minor)
    }

    override fun onRssi(rssi: Int) {
        //TODO("Not yet implemented"
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

    interface  NoDeviceCallback{
        fun noData();
        fun cclick():Boolean;
        fun hasdata();
        fun onScan1();
        fun onClickConnect(beacon: Beacon)
    }
}