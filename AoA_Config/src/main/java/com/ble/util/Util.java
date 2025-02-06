package com.ble.util;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.content.Intent;
import android.location.LocationManager;
import android.provider.Settings;

import com.ble.bean.Beacon;

import java.text.DecimalFormat;

import no.nordicsemi.android.support.v18.scanner.ScanResult;

/**
 * @author Andesen
 * @package com.ble.util
 * @fileName GPSUtil
 * @date 2020/6/2218:03
 * @email 929620555@qq.com
 * @describe 自定义描述
 */
public class Util {

        /**
         * 判断GPS是否开启，GPS或者AGPS开启一个就认为是开启的
         * @param context
         * @return true 表示开启
         */
        public static final boolean GPSisOPen(final Context context) {
            LocationManager locationManager
                    = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
            // 通过GPS卫星定位，定位级别可以精确到街（通过24颗卫星定位，在室外和空旷的地方定位准确、速度快）
            boolean gps = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
            // 通过WLAN或移动网络(3G/2G)确定的位置（也称作AGPS，辅助GPS定位。主要用于在室内或遮盖物（建筑群或茂密的深林等）密集的地方定位）
            boolean network = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
            if (gps || network) {
                return true;
            }
            return false;
        }


    public static final boolean BleIsOPen(final Context context) {
        BluetoothAdapter mBluetoothAdapter=BluetoothAdapter.getDefaultAdapter();
        if (mBluetoothAdapter == null) {
           return false;
        }
        if(!mBluetoothAdapter.isEnabled()){
            return false;
        }
        else{
            return true;
        }
    }

    /**
         * GPS设置界面
         * @param context
         */
        public static final void openGPS(Activity context) {
            Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
            context.startActivityForResult(intent, 0); // 设置完成后返回到原来的界面

        }

    /**
     * 蓝牙设置界面
     * @param context
     */
    public static final void openBle(Activity context) {
        Intent intent = new Intent(
                Settings.ACTION_BLUETOOTH_SETTINGS);
        context.startActivityForResult(intent, 0); // 设置完成后返回到原来的界面

    }
    public static Beacon getDevice(ScanResult result,Beacon beacon){
        if(beacon==null){
            beacon=new Beacon();
        }
        assert result.getScanRecord() != null;
        byte[]  buffer= result.getScanRecord().getBytes();

        beacon.setI1(beacon.getI2());
        beacon.setI2(System.currentTimeMillis());
        if( beacon.getI1()!=0){
            beacon.setInterval(beacon.getI2() -beacon.getI1());
        }

        //1EFF0D0004A98011759F382F61ACCC274567F7DB34C4038E5C0BAA973056E6

        assert buffer != null;
        String data=StringUtils.ByteArrToHex(buffer).replaceAll(" ","");
        System.out.println("数据="+data);
        if(!data.startsWith("1EFF0D0004")){
            return null;
        }
        else{
            if(data.contains("2F61ACCC274567F7DB34C4038E5C0BAA973056E6")){
                beacon.setChannel(37);
            }else if(data.contains("6FD3100F38722DA85EC258994F8ACEEEB7698807")){
                beacon.setChannel(38);
            }else{
                beacon.setChannel(39);
            }
        }
        byte cmd=buffer[5];
        String cmd_bit= StringUtils.binary2(cmd);
        System.out.println("bit="+cmd_bit);
        char c[]=cmd_bit.toCharArray();
        String hbit=cmd_bit.substring(4,8);
        System.out.println("头="+hbit);
        String version="000000"+cmd_bit.substring(2,4);
        byte versions=StringUtils.bit2byte(version);
        byte head=StringUtils.bit2byte("0000"+hbit);
        System.out.println("head="+head);
        if(c[1]=='1'){
            beacon.setOpen(1);
        }else{
            beacon.setOpen(0);
        }

        switch (versions){
            case 0:
                beacon.setMode("Private 3");
                break;
            case 1:
                beacon.setMode("Private 1");
                break;
            case 2:
                beacon.setMode("Standard 2");
                break;
        }
        if(c[0]=='1'){
                beacon.setD_mode("白化-标准SDK");
        }else if(c[0]=='0'){
            beacon.setD_mode("非白化-私有2.4G");
        }
        if(head==9){
          double bt= (double) buffer[8] /255*6.6;
            DecimalFormat df = new DecimalFormat("#.00");
            String formattedNumber = df.format(bt);
            double bts = Double.parseDouble(formattedNumber);
          beacon.setBt(bts+" V");
        }
        beacon.setRaw(data);
        beacon.setRssi(result.getRssi());
        beacon.setAddress(result.getDevice().getAddress());
        System.out.println("信标="+beacon);
        return beacon;
    }
}
