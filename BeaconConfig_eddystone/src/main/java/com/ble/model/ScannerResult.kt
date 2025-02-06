package com.ble.model

import com.ble.bean.Beacon

/**
 * @author Andesen
 * @package com.ble.model
 * @fileName ScannerResult
 * @date 2020/6/2217:20
 * @email 929620555@qq.com
 * @describe 自定义描述
 */
interface ScannerResult{
    fun findBeacon(beaconList: ArrayList<Beacon>);
}