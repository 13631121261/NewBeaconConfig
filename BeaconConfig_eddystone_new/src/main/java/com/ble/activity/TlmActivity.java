package com.ble.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.ble.MyApplication;
import com.ble.beaconconfig.R;
import com.ble.beaconconfig.databinding.ActivityEidBinding;
import com.ble.beaconconfig.databinding.ActivityTlmBinding;
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
public class TlmActivity extends BaseActivity implements View.OnClickListener {
    ActivityTlmBinding bing;
    Beacon beacon=null;
    int type=1;


    ConnectPresenter connectPresenter=null;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        bing= ActivityTlmBinding.inflate(getLayoutInflater());
        setContentView(bing.getRoot());
         connectPresenter=MyApplication.Companion.getConnectPresenter();
        beacon=MyApplication.Companion.getBeacon();

        if(beacon.getTlm_check()!=null){
            bing.edTlmCheck.setText(beacon.getTlm_check());
            bing.edTlmSalt.setText(beacon.getTlm_salt());
            bing.edTlmEncrypedData.setText(beacon.getTlm_data());
        }

        //     ((MyApplication)getApplication()).getConnectPresenter();
        bing.gGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                  final int a=R.id.g_encrypted;
                switch (checkedId){
                    case a:
                        type=1;
                        bing.linearView.setVisibility(View.VISIBLE);
                        break;
                    case R.id.g_unencrypted:
                        type=0;
                        bing.linearView.setVisibility(View.GONE);
                        break;
                }
            }
        });
    }

    @Override
    public void onClick(View v) {
        if(type==1){
            String tlm_check=bing.edTlmCheck.getText().toString();
            String tlm_salt=bing.edTlmSalt.getText().toString();
            String tlm_encryped_data=bing.edTlmEncrypedData.getText().toString();

            if(tlm_check.length()==0||tlm_check.length()!=4||tlm_salt.length()==0||tlm_salt.length()!=4||tlm_encryped_data.length()==0||tlm_encryped_data.length()!=24){
                Toast.makeText(this,getResources().getString(R.string.uid_tip),Toast.LENGTH_LONG).show();
            }else{
                try {
                    StringUtils.HexToByteArr(tlm_check);
                    StringUtils.HexToByteArr(tlm_salt);
                    StringUtils.HexToByteArr(tlm_encryped_data);

                    String data="2401"+tlm_encryped_data+tlm_salt+tlm_check;
                    byte[] datas=StringUtils.HexToByteArr(data);
                    connectPresenter.sendValue(new byte[]{0x24, 0x31, 0x31}, Type.change_mode);
                    connectPresenter.sendValue(datas, Type.tlm);
                }catch (Exception e){
                    Toast.makeText(this,getResources().getString(R.string.uid_tip),Toast.LENGTH_LONG).show();

                }
            }
        }
        if(type==0){
                try {
                    String data="2400";
                    byte[] datas=StringUtils.HexToByteArr(data);
                    connectPresenter.sendValue(new byte[]{0x24, 0x31, 0x31}, Type.change_mode);
                    connectPresenter.sendValue(datas, Type.tlm);
                }catch (Exception e){
                    Toast.makeText(this,getResources().getString(R.string.uid_tip),Toast.LENGTH_LONG).show();
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