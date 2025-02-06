package com.ble.bean

/**
 * @package com.ble.bean
 * @fileName Type
 * @date 2020/7/411:55
 * @author Andesen
 * @email 929620555@qq.com
 * @describe beacon广播的参数类型，比如含有Major,UUID等等的类型
 */
enum class Type {
    uuid,
    major,
    minor,
    txpower,
    rssiatone,
    deviceName,
    companyId,
    serviceData,
    interval,
    psw,
    connect,
    setMode_beacon,
    setMode_Edd,
    ACC_Open,
    wendu_Open,
    change_psw,
    change_mode,
    uid,
    eid,
    url,
    tlm
}