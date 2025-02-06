package com.ble.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.Nullable;
import com.ble.MyApplication;
import com.ble.beaconcheck.R;
import com.ble.beaconcheck.databinding.ActivityEidBinding;
import com.ble.bean.Type;
import com.ble.presenter.ConnectPresenter;
import com.ble.util.StringUtils;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 */
public class EidActivity extends BaseActivity implements View.OnClickListener {
    private ActivityEidBinding binding;

    int txpower_at_0m=-10;
    ConnectPresenter connectPresenter=null;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    //    setContentView(R.layout.activity_eid);
        binding= ActivityEidBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
         connectPresenter=MyApplication.Companion.getConnectPresenter();
            //     ((MyApplication)getApplication()).getConnectPresenter();
        binding.spEidTxpower.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                txpower_at_0m=-10-position;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    @Override
    public void onClick(View v) {
        String Encryped=binding.edEid.getText().toString();

        if(Encryped.length()==0||Encryped.length()!=16){
            Toast.makeText(this,getResources().getString(R.string.uid_tip),Toast.LENGTH_LONG).show();
        }else{
            try {
                byte[] Encrypeds = StringUtils.HexToByteArr(Encryped);
                byte txpower= (byte) txpower_at_0m;
                String txpowers=StringUtils.Byte2Hex(txpower);
                String data="24"+Encryped+txpowers;
                byte[] datas=StringUtils.HexToByteArr(data);
                connectPresenter.sendValue(new byte[]{0x24, 0x31, 0x31}, Type.change_mode);
                connectPresenter.sendValue(datas, Type.eid);
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