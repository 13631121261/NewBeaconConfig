package com.ble.util;


import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.content.Intent;
import android.location.LocationManager;
import android.provider.Settings;

import com.ble.bean.Beacon;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.DriverManager;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;

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
  /*  if (!result.getDevice().getAddress().equals("D3:36:A0:A0:05:66"))
    {
            System.out.println("扫描到");
            return null;
    }*/
        beacon.setI1(beacon.getI2());
        beacon.setI2(System.currentTimeMillis());
        if( beacon.getI1()!=0){
            beacon.setInterval(beacon.getI2() -beacon.getI1());
        }

        beacon.setAddress(result.getDevice().getAddress());
        beacon.setRssi(result.getRssi());
        byte[] buff=result.getScanRecord().getBytes();
        beacon.setRaw(StringUtils.ByteArrToHex(buff).replaceAll(" ",""));
        byte[] adv= ParseLeAdvData.adv_report_parse(ParseLeAdvData.BLE_GAP_AD_TYPE_MANUFACTURER_SPECIFIC_DATA,
                    buff);
        byte[] sname=ParseLeAdvData.adv_report_parse(ParseLeAdvData.BLE_GAP_AD_TYPE_COMPLETE_LOCAL_NAME,
                buff);
            beacon.setIsConnect(result.isConnectable());
          //  System.out.println(StringUtils.ByteArrToHex(adv));

            if(adv!=null&&adv.length>=25&&adv[2]==0x02){
                beacon.setType(4);
               /* if(result.getDevice().getName()==null){

                    beacon.setDeviceName("N/A");
                }else{
                    System.out.println("666=MAC="+result.getDevice().getAddress()+"---------["+result.getDevice().getName());
                    beacon.setDeviceName(result.getDevice().getName());
                }*/
                //公司ID
                byte[] id=new byte[2];
                id[0]=adv[0];
                id[1]=adv[1];
                beacon.setCompanyId(StringUtils.ByteArrToHex(id).replaceAll(" ",""));
                ////UUID
                byte[] uuid=new byte[16];
                int j=0;
                for(int i=4;i<=19;i++){
                    uuid[j]=adv[i];
                    j++;
                }
                beacon.setUuid(StringUtils.ByteArrToHex(uuid).replace(" ",""));
                //Major
                byte[] major=new byte[2];
                major[0]=adv[20];
                major[1]=adv[21];
                beacon.setMajor(Integer.parseInt(StringUtils.ByteArrToHex(major).replace(" ",""),16));
                //Minor
                byte[] minor=new byte[2];
                minor[0]=adv[22];
                minor[1]=adv[23];
                beacon.setMinor(Integer.parseInt(StringUtils.ByteArrToHex(minor).replace(" ",""),16));
                //rssi
                int rssi=(int)adv[24];
                beacon.setRssiInOne(rssi);
                //beacon.setMepower((int)adv[25]+"");
            //    System.out.println("bbMAC="+result.getDevice().getAddress());
                if(adv.length==26){
                    beacon.setBt(adv[25]);
                }
            }
            else {
                adv= ParseLeAdvData.adv_report_parse(ParseLeAdvData.BLE_GAP_AD_TYPE_SERVICE_DATA,
                        buff);
                if (adv != null && adv.length >= 5 && (adv[1]&0xff) == 0xFE && (adv[0]&0xff) == 0xAA) {
                    String dd = StringUtils.ByteArrToHex(adv).replaceAll(" ", "");
                   // System.out.println("DDD+"+dd);
                    switch (adv[2]) {
                        case 0x10:
                            beacon.setType(1);
                            break;
                        case 0x20:
                            beacon.setType(2);
                            break;
                        case 0x00:
                            beacon.setType(0);
                            break;
                        case 0x30:
                         //   System.out.println("输出输出");
                            beacon.setType(3);
                            break;
                    }
                    beacon.setTx_power_at_0m(adv[3]);
                    //
                    if (beacon.getType() == 0) {
                        String a = dd.substring(8, 28);
                        String b = dd.substring(28, 40);
                        beacon.setUid_Namespace(a);
                        beacon.setUid_ID_Instance(b);
                    } else if (beacon.getType() == 1) {
                        beacon.setUrl_head(adv[4]);
                        String urldata = dd.substring(10, dd.length());
                        byte[] urls = StringUtils.HexToByteArr(urldata);
                        beacon.setUrl_data(new String(urls));

                    } else if (beacon.getType() == 2) {
                        beacon.setTlm_type(adv[3]);
                        if (beacon.getTlm_type() == 0) {
                            //System.out.println("运作");
                            beacon.setTlm_bt((adv[4]&0xff) * 256 + adv[5]);
                            String bit=StringUtils.binary2(adv[7]);
                            //System.out.println("666"+bit);
                            char[] bits=bit.toCharArray();
                            double ll=0;
                            if(bits[0]=='1'){
                                ll=0.5;
                            }
                            if(bits[1]=='1'){
                                ll=ll+0.25;
                            }
                            if(bits[2]=='1'){
                                ll=ll+0.125;
                            }
                            if(bits[3]=='1'){
                                ll=ll+0.0625;
                            }
                            if(bits[4]=='1'){
                                ll=ll+0.03125;
                            }
                            if(bits[5]=='1'){
                                ll=ll+0.015625;
                            }
                            if(bits[6]=='1'){
                                ll=ll+0.0078125;
                            }
                            if(bits[7]=='1'){
                                ll=ll+0.00390625;
                            }

                            beacon.setTlm_temp((adv[6]+ll)+  "℃");
                            beacon.setTlm_Advertising_PDU((adv[8]&0xff) * 16777216 +( adv[9]&0xff) * 65536 + (adv[10]&0xff) * 256 + (adv[11]&0xff));
                            long time=(adv[12]&0xff) * 16777216 + (adv[13]&0xff) * 65536 + (adv[14]&0xff) * 256 +(adv[15]&0xff);
                         //   System.out.println("time+"+time+"------"+(adv[14]&0xff) * 256 );
                            long day=time/10/60/60/24;
                            long h=time/10/60/60%24;
                            long m=time/10/60%60;
                            long s=time/10%60;
                            beacon.setTlm_work_time(""+day+":" +h+":" +m+":" +s+".000");
                          //  System.out.println("运作"+beacon.getTlm_bt());
                        } else if (beacon.getTlm_type() == 1) {
                            beacon.setTlm_data(dd.substring(8, 32));
                            beacon.setTlm_salt(dd.substring(32, 36));
                            beacon.setTlm_check(dd.substring(36, 40));
                        }
                    } else if (beacon.getType() == 3) {
                    //    beacon.setTlm_type(adv[3]);
                       // System.out.println("输出类型EID");
                        beacon.setEid_data(dd.substring(8, 24));
                    }
                }
                else{
                //    System.out.println("aaMAC="+result.getDevice().getAddress());
                    return null;
                }
            }/*
                else{

                }*/


        if(sname!=null&&sname.length>0){
            beacon.setDeviceName(new String(sname));
        }
        else{
            beacon.setDeviceName("N/A");
          //  beacon.setOnlyBeacon(true);
           // return beacon;
        }
        beacon.setOnlyBeacon(false);
        byte[] txpower=ParseLeAdvData.adv_report_parse(ParseLeAdvData.BLE_GAP_AD_TYPE_TX_POWER_LEVEL,
                buff);
        if(txpower!=null){
            beacon.setTxPower(""+txpower[0]);
        }
      //  beacon.setIsConnect(true);
         byte[] data=null;
        if(beacon.getType()==4){
            data =ParseLeAdvData.adv_report_parse(ParseLeAdvData.BLE_GAP_AD_TYPE_SERVICE_DATA,buff);
        }else{
            data =ParseLeAdvData.adv_report_parse(ParseLeAdvData.BLE_GAP_AD_TYPE_MANUFACTURER_SPECIFIC_DATA,buff);
        }

            if(data!=null&&data.length>=3&&data[1]==0x73){
                //	System.out.println("用户数据"+StringUtils.ByteArrToHex(data).replace(" ",""));
                //電量
                int bt=data[0]&0xff;
                beacon.setBt(bt);
                //广播间隔
                int interval=data[2];
                beacon.setInterval((interval*100));

            //    System.out.println("广播间隔="+beacon.getInterval());
                if(data.length<11){
                    return beacon;
                }
                //客戶數據
                byte[] sdata=new byte[data.length-7];
                int j=0;
                for(int i=3;i<data.length-4;i++){
                    sdata[j]=data[i];
                    j++;
                }
                beacon.setServiceData(StringUtils.ByteArrToHex(sdata));
               char[] bits= StringUtils.binary2(data[9]).toCharArray();
              //  System.out.println(beacon.getAddress()+"d'd'd'd'd'd'd'd'd'd'd'd'd'd'd="+StringUtils.binary2(data[9]));
                //00001111
               if(bits[6]=='1'&&data.length==13){
                    beacon.isSupportTempShidu(true);
                   beacon.setTemp(""+data[11]);
                   beacon.setHumidity(""+data[12]);
                   if(bits[7]=='1'){
                       beacon.setOpenTemp(true);
                    //   System.out.println("log11==="+beacon.getAddress()+beacon.getOpenTemp());
                   }
                   else{
                       beacon.setOpenTemp(false);
                      // System.out.println("log222==="+beacon.getAddress()+beacon.getOpenTemp());
                   }
               }
               else{

                   beacon.isSupportTempShidu(false);
                   beacon.setOpenTemp(false);
                //   System.out.println("log33==="+beacon.getAddress()+beacon.getOpenTemp());
               }
               // System.out.println(beacon.getAddress()+"  温度="+beacon.getTemp()+"湿度="+beacon.getHumidity()+"移动="+data[10]);

                if(bits[4]=='1'&&data.length==13){
                   beacon.setIsMove(data[10]);
                    beacon.isSupportAcc(true);
                    if(bits[5]=='1'){
                        beacon.isOpenAcc(true);
                    }
                    else{
                        beacon.isOpenAcc(false);
                    }
               }
               else{
                   beacon.isSupportAcc(false);
                   beacon.isOpenAcc(false);
               }
            }

            else  if(data!=null&&data.length==17&&data[1]==0x4B&&data[0]==0x4C){

            //電量
            int bt=((data[02]&0xff)*256)+(data[3]&0xff);
         //   System.out.println("用户数据"+(data[02]&0xff)*256);

            beacon.setBt(bt);
            //广播间隔
            int interval=data[4];
            beacon.setInterval((interval*100));
            //System.out.println("广播间隔="+beacon.getInterval());
            //发射功率
            beacon.setTxPower(""+data[5]);
            //客戶數據
            byte[] sdata=new byte[data.length-11];
            int j=0;
            for(int i=6;i<data.length-5;i++){
                sdata[j]=data[i];
                j++;
            }


            beacon.setServiceData(StringUtils.ByteArrToHex(sdata));


            char[] bits= StringUtils.binary2(data[12]).toCharArray();
          //  System.out.println(beacon.getAddress()+"d'd'd'd'd'd'd'd'd'd'd'd'd'd'd="+StringUtils.binary2(data[9]));
            //00001111
            if(bits[6]=='1'){
                beacon.isSupportTempShidu(true);
                if(data[15]<10){
                    beacon.setTemp(""+data[14]+".0"+data[15]);
                }else {
                    beacon.setTemp("" + data[14] + "." + data[15]);
                }
                beacon.setHumidity(""+data[16]);
                if(bits[7]=='1'){
                    beacon.setOpenTemp(true);
                   // System.out.println("log11==="+beacon.getAddress()+beacon.getOpenTemp());
                }
                else{
                    beacon.setOpenTemp(false);
                   // System.out.println("log222==="+beacon.getAddress()+beacon.getOpenTemp());
                }
            }
            else{

                beacon.isSupportTempShidu(false);
                beacon.setOpenTemp(false);
                //System.out.println("log33==="+beacon.getAddress()+beacon.getOpenTemp());
            }
            if(bits[4]=='1'){
                beacon.setIsMove(data[13]);
                beacon.isSupportAcc(true);
                if(bits[5]=='1'){
                    beacon.isOpenAcc(true);
                }
                else{
                    beacon.isOpenAcc(false);
                }
            }
            else{
                beacon.isSupportAcc(false);
                beacon.isOpenAcc(false);
            }
        }else if(data!=null&&(data[0]&0xff)==0xf1&&(data[1]&0xff)==0xff){
                beacon.setBt(data[2]);
            }
            else{
                beacon.setIsConnect(true);
                beacon.setIsMove(2);
            }




     //   System.out.println("log444==="+beacon.getAddress()+beacon.getOpenTemp()+"            "+beacon.isOpenAcc());
            return beacon;

    }
    public static ArrayList<HashMap<String, String>> writeExcel(String path,String type, String key,HashMap map) throws IOException {

        //String logFilePath = Environment.getExternalStorageDirectory() + File.separator + "Visitor";
        Sheet sheet = null;
        Row row = null;
        ArrayList<HashMap<String, String>> list = null;

        Workbook wb = null;


        InputStream is = null;
        try {
            System.out.println("1111"+type);
            File file=new File(path);
            is =new FileInputStream(file);
            System.out.println("12222"+is.available());
            if (file.getName().contains("xlsx")) {
                System.out.println("类型在此5");
                try {
                    System.out.println("类型在8"+is);
                    wb = new XSSFWorkbook(is);
                    is.close();
                    System.out.println("类型在此66"+wb);
                }catch (IOException e){
                    System.out.println("大大的异常="+e.toString());
                }
                System.out.println("类型在此111");
            }
            else if (file.getName().contains("xls")) {
                System.out.println("类型在此"+is);
                wb = new HSSFWorkbook(is);
                is.close();
                System.out.println("类型在此6666");
                System.out.println(wb);
            } else {
                return null;

            }
           // System.out.println("65656");
            // 用来存放表中数据
            list = new ArrayList<HashMap<String, String>>();
            // 获取第一个sheet
            assert wb != null;
            sheet = wb.getSheetAt(0);

            // 获取最大行数
            int rownum = sheet.getPhysicalNumberOfRows();
            System.out.println("输出行数="+rownum);


            for (int i = 1; i < rownum; i++) {
                row = sheet.getRow(i);
                if (row != null) {
                    //System.out.println("发几个开朗大方好几个");
                    if(type.equals("mac")){
                        if(key.equals(getCellFormatValue(row.getCell(0)))){
                            Cell cell=  row.createCell(3);
                            cell.setCellValue((String)map.get("name"));
                             cell=  row.createCell(4);
                            cell.setCellValue((String)map.get("uuid"));
                             cell=  row.createCell(5);
                            cell.setCellValue((String)map.get("major"));
                             cell=  row.createCell(6);
                            cell.setCellValue((String)map.get("minor"));
                             cell=  row.createCell(7);
                            cell.setCellValue((String)map.get("txPower"));
                             cell=  row.createCell(8);
                            cell.setCellValue((String)map.get("rssi_1"));
                             cell=  row.createCell(9);
                            cell.setCellValue((String)map.get("interval"));
                             cell=  row.createCell(10);
                            cell.setCellValue((String)map.get("bt"));
                            cell=  row.createCell(11);
                            cell.setCellValue("OK");
                            System.out.println("保存111");
                        }
                    }else if(type.equals("major+minor")){
                       // System.out.println("保存222");
                        String s = ((String) getCellFormatValue(row.getCell(1))).replace(".0","") +"_"+ ((String)  getCellFormatValue(row.getCell(2))).replace(".0","");
                     //   System.out.println("s="+s);
                        if(key.equals(s)){
                            Cell cell=  row.createCell(3);
                            cell.setCellValue((String)map.get("name"));
                            cell=  row.createCell(4);
                            cell.setCellValue((String)map.get("uuid"));
                            cell=  row.createCell(5);
                            cell.setCellValue((String)map.get("major"));
                            cell=  row.createCell(6);
                            cell.setCellValue((String)map.get("minor"));
                            cell=  row.createCell(7);
                            cell.setCellValue((String)map.get("txPower"));
                            cell=  row.createCell(8);
                            cell.setCellValue((String)map.get("rssi_1"));
                            cell=  row.createCell(9);
                            cell.setCellValue((String)map.get("interval"));
                            cell=  row.createCell(10);
                            cell.setCellValue((String)map.get("bt"));
                            cell=  row.createCell(11);
                            cell.setCellValue("OK");
                            System.out.println("保存111");
                        }
                    }else{
                        //System.out.println("类型p="+type);
                    }
                } else {
                    System.out.println("第"+i+"行 为空");
                    break;
                }
            }
            FileOutputStream stream = new FileOutputStream(file);

            wb.write(stream);


            stream.close();
          //  wb.close();


        } catch (FileNotFoundException e) {

            wb.close();
            is.close();
            System.out.println("异常"+e.getMessage());
            return null;
        } catch (IOException e) {
            System.out.println("异常22="+e.getMessage());

            wb.close();
            is.close();
            return null;
        }
        return list;
    }

    public static ArrayList<HashMap<String, String>> readExcel(File file, String columns[]) throws IOException {
        //String logFilePath = Environment.getExternalStorageDirectory() + File.separator + "Visitor";
        Sheet sheet = null;
        Row row = null;
        Row rowHeader = null;
        ArrayList<HashMap<String, String>> list = null;
        String cellData = null;
        Workbook wb = null;
        if (file == null) {
            return null;
        }
        System.out.println("地址=" + file.getName());
        InputStream is = null;
        try {
            System.out.println("1111");
            is =new FileInputStream(file);
            System.out.println("12222"+is.available());
            if (file.getName().contains("xlsx")) {
                System.out.println("类型在此5");
                try {
                    System.out.println("类型在8"+is);
                    wb = new XSSFWorkbook(is);
                    System.out.println("类型在此66"+wb);
                }catch (IOException e){
                    System.out.println("大大的异常="+e.toString());
                }
                System.out.println("类型在此111");
            }
           else if (file.getName().contains("xls")) {
              System.out.println("类型在此"+is);
              wb = new HSSFWorkbook(is);
              System.out.println("类型在此6666");
              System.out.println(wb);
          } else {
              return null;

            }
            System.out.println("65656");
            // 用来存放表中数据
            list = new ArrayList<HashMap<String, String>>();
            // 获取第一个sheet
            assert wb != null;
            sheet = wb.getSheetAt(0);

            // 获取最大行数
            int rownum = sheet.getPhysicalNumberOfRows();
            System.out.println("输出行数="+rownum);
            // 获取第一行
            rowHeader = sheet.getRow(0);
            row = sheet.getRow(0);
            //sheet.createRow(5).createCell(0).setCellValue("今年");


            // 获取最大列数
            int colnum = row.getPhysicalNumberOfCells();
            for (int i = 1; i < rownum; i++) {
                HashMap<String, String> map = new LinkedHashMap<String, String>();
                row = sheet.getRow(i);
                if (row != null) {
                    for (int j = 0; j < columns.length; j++) {
                        System.out.println("J=" + j);
                        if (columns[j].equals(getCellFormatValue(rowHeader.getCell(j)))) {
                            cellData = (String) getCellFormatValue(row.getCell(j));

                            System.out.println(columns[j]+"读取=" + cellData.replace(".0","") + "J=" + j);
                            map.put(columns[j], cellData.replace(".0", ""));

                        }
                    }
                } else {
                    break;
                }
                list.add(map);
            }
        } catch (FileNotFoundException e) {
            wb.close();
            System.out.println("异常"+e.getMessage());
            return null;
        } catch (IOException e) {
            System.out.println("异常22="+e.getMessage());
            wb.close();
            return null;
        }
        wb.close();
        return list;
    }
    public static Object getCellFormatValue(Cell cell) {
        Object cellValue = null;
        if (cell != null) {
            // 判断cell类型
            switch (cell.getCellType()) {
                case Cell.CELL_TYPE_NUMERIC: {
                    cellValue = String.valueOf(cell.getNumericCellValue());
                    break;
                }
                case Cell.CELL_TYPE_FORMULA: {
                    // 判断cell是否为日期格式
                    if (DateUtil.isCellDateFormatted(cell)) {
                        // 转换为日期格式YYYY-mm-dd
                        cellValue = cell.getDateCellValue();
                    } else {
                        // 数字
                        cellValue = String.valueOf(cell.getNumericCellValue());
                    }
                    break;
                }
                case Cell.CELL_TYPE_STRING: {
                    cellValue = cell.getRichStringCellValue().getString();
                    break;
                }
                default:
                    cellValue = "";
            }
        } else {
            cellValue = "";
        }
        return cellValue;
    }


}
