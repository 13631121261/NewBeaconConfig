package com.ble.model

import android.os.Handler
import android.util.Log
import com.ble.bean.Beacon
import com.ble.bean.MLog.ScannerE
import com.ble.util.ParseLeAdvData
import com.ble.util.ParseLeAdvData.BLE_GAP_AD_TYPE_FLAGS
import com.ble.util.StringUtils
import com.ble.util.Util
import no.nordicsemi.android.support.v18.scanner.BluetoothLeScannerCompat
import no.nordicsemi.android.support.v18.scanner.ScanCallback
import no.nordicsemi.android.support.v18.scanner.ScanResult
import no.nordicsemi.android.support.v18.scanner.ScanSettings


/**
 * @package com.ble.model
 * @fileName Scanner
 * @date 2020/6/1612:01
 * @author Andesen
 * @email 929620555@qq.com
 * @describe 自定义描述
 */
class Scanner(scannerResult: ScannerResult) {
    var raw_beacon_list=ArrayList<Beacon>();
    var map_beacon=HashMap<String, Beacon>();
    val fillter:Byte=0x73;
    var scannerResult:ScannerResult?=null;
    var scanner = BluetoothLeScannerCompat.getScanner()
    val settings: ScanSettings = ScanSettings.Builder()
                .setScanMode(ScanSettings.SCAN_MODE_LOW_POWER)
                .setUseHardwareFilteringIfSupported(false)
                .build();
init {
   this.scannerResult=scannerResult;
}


  fun startScan(){

      scanner.startScan(null, settings, scanCallback);
      raw_beacon_list.clear();
      map_beacon.clear();
      Log.e(ScannerE, "开始扫描")
  }

   fun startScan(timeOut: Long){
       startScan();
       var handler: Handler = Handler()
       var runnable: Runnable =object: Runnable {
           override fun run() {
             stopScan();
           }
       }
       handler.postDelayed(runnable, timeOut);
       Log.e(ScannerE, "定时停止扫描")
   }
    fun stopScan(){
        scanner.stopScan(scanCallback);
    }

    private val scanCallback: ScanCallback = object : ScanCallback() {
        override fun onScanResult(callbackType: Int, result: ScanResult) { //	System.out.println("单次结果="+result.getDevice().getAddress());
            val buff = result.scanRecord!!.bytes
          val serviceData=  ParseLeAdvData.adv_report_parse(ParseLeAdvData.BLE_GAP_AD_TYPE_SERVICE_DATA, buff);
          val adv = ParseLeAdvData.adv_report_parse(ParseLeAdvData.BLE_GAP_AD_TYPE_MANUFACTURER_SPECIFIC_DATA,
                  buff)
            val connectTag=ParseLeAdvData.adv_report_parse(BLE_GAP_AD_TYPE_FLAGS,buff);

        /*    if(result.device.address.equals("F0:C8:F1:0C:04:14")){
                System.out.println("MAC=" + result.device.type+"数据="+ StringUtils.ByteArrToHex(buff))

            }*/

            if(connectTag==null){
                return;
            }




               // Log.e(ScannerE,"扫描结果="+ StringUtils.ByteArrToHex(serviceData));
                var beacon:Beacon?=null
               // System.out.println("步骤111")

        //    if(result.device.address.equals("F0:C8:10:14:82:38")){




                if(map_beacon[result.device.address] !=null){
                 //   System.out.println("步骤222")
                    beacon= map_beacon[result.device.address]
                    Util.getDevice(result,beacon);
                    if(connectTag[0]==6.toByte()){
                        beacon?.IsConnect=true;
                    }else{
                        beacon?.IsConnect=false
                    }
                }else{
                 //   System.out.println("步骤888")
                    beacon=Util.getDevice(result,beacon);
                    if(connectTag[0]==6.toByte()){
                        beacon?.IsConnect=true;
                    }else{
                        beacon?.IsConnect=false
                    }
                    if(beacon!=null){
                        raw_beacon_list.add(beacon);
                        map_beacon[beacon.address] = beacon
                    }else{
                        //System.out.println("ccc=MAC=" + result.device.address)
                    }
                }
                scannerResult!!.findBeacon(raw_beacon_list);

                if (beacon != null) {
                    scannerResult!!.findBeacon(beacon)
                };
          //  }
        }

        override fun onBatchScanResults(results: List<ScanResult>) {
            println("多次结果=" + results[0].device.address)
        }

        override fun onScanFailed(errorCode: Int) {}
    }

}