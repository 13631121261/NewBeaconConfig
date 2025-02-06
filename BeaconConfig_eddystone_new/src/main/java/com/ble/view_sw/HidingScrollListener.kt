package com.ble.view_sw

import androidx.recyclerview.widget.RecyclerView




/**
 * @package com.ble.view
 * @fileName ss
 * @date 2020/6/2221:04
 * @author Andesen
 * @email 929620555@qq.com
 * @describe 自定义描述
 */
abstract class HidingScrollListener : RecyclerView.OnScrollListener() {
    private var scrolledDistance = 0
    private var controlsVisible = true
    override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
        super.onScrolled(recyclerView!!, dx, dy)
        if (scrolledDistance > HIDE_THRESHOLD && controlsVisible) {
            onHide()
            controlsVisible = false
            scrolledDistance = 0
        } else if (scrolledDistance < -HIDE_THRESHOLD && !controlsVisible) {
            onShow()
            controlsVisible = true
            scrolledDistance = 0
        }
        if (controlsVisible && dy > 0 || !controlsVisible && dy < 0) {
            scrolledDistance += dy
        }
    }

    abstract fun onHide()
    abstract fun onShow()

    companion object {
        private const val HIDE_THRESHOLD = 20
    }
}