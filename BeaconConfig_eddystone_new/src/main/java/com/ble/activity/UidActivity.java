package com.ble.activity;
import androidx.annotation.Nullable;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;


import com.ble.MyApplication;
import com.ble.beaconconfig.R;
import com.ble.beaconconfig.databinding.ActivityEidBinding;
import com.ble.beaconconfig.databinding.ActivityUidBinding;
import com.ble.bean.Beacon;
import com.ble.bean.Type;
import com.ble.presenter.ConnectPresenter;
import com.ble.util.StringUtils;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 */
public class UidActivity extends BaseActivity implements View.OnClickListener {
   Beacon beacon=null;
ActivityUidBinding bing;
ConnectPresenter connectPresenter;
    int txpower_at_0m=-10;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        bing=ActivityUidBinding.inflate(getLayoutInflater());
        setContentView(bing.getRoot());
        beacon=MyApplication.Companion.getBeacon();
        assert beacon != null;
        if(beacon.getUid_ID_Instance()!=null){
            bing.edUidNamespace.setText(beacon.getUid_ID_Instance());
            bing.edInstance.setText(beacon.getUid_Namespace());
            bing.spUidTxpower.setSelection(Math.abs(beacon.getTx_power_at_0m())-10);
        }
         connectPresenter=MyApplication.Companion.getConnectPresenter();
          bing.spUidTxpower.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                txpower_at_0m=-10-position;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
            //     ((MyApplication)getApplication()).getConnectPresenter();
    }

    @Override
    public void onClick(View v) {
        String namespace=bing.edUidNamespace.getText().toString();
        String instance=bing.edInstance.getText().toString();
        if(namespace.length()==0||namespace.length()!=20||instance.length()==0||instance.length()!=12){
            System.out.println(namespace.length()+"长度"+instance.length());
            Toast.makeText(this,getResources().getString(R.string.uid_tip),Toast.LENGTH_LONG).show();
        }else{
            try {
                byte txpower= (byte) txpower_at_0m;
                String txpowers=StringUtils.Byte2Hex(txpower);
                byte[] name = StringUtils.HexToByteArr(namespace);
                byte[] stance = StringUtils.HexToByteArr(instance);
                String data="24"+namespace+instance+"0000"+txpowers;
                byte[] datas=StringUtils.HexToByteArr(data);
                connectPresenter.sendValue(new byte[]{0x24, 0x31, 0x31}, Type.change_mode);
                connectPresenter.sendValue(datas, Type.uid);
            }catch (Exception e){
                System.out.println(e.getMessage());
                Toast.makeText(this,e.getMessage(),Toast.LENGTH_LONG).show();

            }
        }
    }

    @Override
    public void initView() {

    }

    @Override
    public void initData() {

    }
}