package com.ble.presenter

import com.ble.activity.IMainVIew
import com.ble.bean.Beacon
import com.ble.model.Scanner
import com.ble.model.ScannerResult

/**
 * @package com.ble.presenter
 * @fileName MainPresenter
 * @date 2020/6/1611:49
 * @author Andesen
 * @email 929620555@qq.com
 * @describe 自定义描述
 */
class MainPresenter(mainView:IMainVIew):IMainPresenter,ScannerResult {
    var mainView: IMainVIew?=null;
    var scanner: Scanner?=null
    init{
        this.mainView = mainView
        scanner= Scanner(this);
    }

    override fun startScan() {
        //TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
        scanner?.startScan();

    }

    override fun stopScan() {
     //   TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
        scanner?.stopScan()
    }

    override fun startScan(timeout: Int) {
       // TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun findBeacon(beaconList: ArrayList<Beacon>) {
        mainView!!.showBeacon(beaconList);
    }

    override fun findBeacon(beacon: Beacon) {
        mainView!!.showBeacon(beacon);
    }
}