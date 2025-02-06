package com.ble.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.ble.MyApplication;
import com.ble.batchconfig.R;
import com.ble.batchconfig.databinding.ActivityUrlBinding;
import com.ble.bean.Type;
import com.ble.presenter.ConnectPresenter;
import com.ble.util.StringUtils;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 */
public class UrlActivity extends BaseActivity implements View.OnClickListener {

    String head="00";
    int txpower_at_0m=-10;
    ConnectPresenter connectPresenter=null;
    ActivityUrlBinding bing;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        bing=ActivityUrlBinding.inflate(getLayoutInflater());
        setContentView(bing.getRoot());

         connectPresenter=MyApplication.Companion.getConnectPresenter();
        bing.spUrlHead.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
               switch (position){
                   case 0:
                       head="00";
                       break;
                   case 1:
                       head="01";
                       break;
                   case 2:
                       head="02";
                       break;
                   case 3:
                       head="03";
                       break;
               }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        bing.spUrlTxpower.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
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
        String url=bing.edUrl.getText().toString();

        if(url.length()==0||url.length()>17){
            Toast.makeText(this,getResources().getString(R.string.uid_tip),Toast.LENGTH_LONG).show();
        }else{
            try {
                byte txpower= (byte) txpower_at_0m;
                String txpowers=StringUtils.Byte2Hex(txpower);
                byte[] urld=url.getBytes();
                String d=StringUtils.ByteArrToHex(urld).replaceAll(" ","");
                String data="24"+head+d+txpowers;
                byte[] datas=StringUtils.HexToByteArr(data);
                connectPresenter.sendValue(new byte[]{0x24, 0x31, 0x31}, Type.change_mode);
                connectPresenter.sendValue(datas, Type.url);
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