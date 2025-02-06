package com.example.onlybeaconconfig

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.ble.beaconcheck.R


class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.target_beacon)
    }
}