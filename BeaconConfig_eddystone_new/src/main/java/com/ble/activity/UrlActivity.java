package com.ble.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.ble.MyApplication;
import com.ble.beaconconfig.R;
import com.ble.beaconconfig.databinding.ActivityUrlBinding;
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
public class UrlActivity extends BaseActivity implements View.OnClickListener {

    String head="00";
    int txpower_at_0m=-10;
    String end="00";
    ConnectPresenter connectPresenter=null;
    ActivityUrlBinding bing;
    Beacon beacon=null;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        bing=ActivityUrlBinding.inflate(getLayoutInflater());
        setContentView(bing.getRoot());
        beacon=MyApplication.Companion.getBeacon();

         connectPresenter=MyApplication.Companion.getConnectPresenter();

         bing.spUrlHead.setSelection(beacon.getUrl_head());
         bing.spUrlEnd.setSelection(beacon.getUrl_tail());
         bing.edUrl.setText(beacon.getUrl_data());
        bing.spUrlTxpower.setSelection(Math.abs(beacon.getTx_power_at_0m())-10);
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
        bing.spUrlEnd.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                position=position-1;
                System.out.println("选组="+position);
                if(position==-1){
                    end="0E";
                }else{
                    if(position<10){
                        end="0"+position;
                    }else{
                        if(position==10){
                            end="0A";
                        }
                        if(position==11){
                            end="0B";
                        }
                        if(position==12){
                            end="0C";
                        }
                        if(position==13){
                            end="0D";
                        }

                    }
                }


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

        if(url.isEmpty() ||url.length()>17){
            Toast.makeText(this,getResources().getString(R.string.uid_tip),Toast.LENGTH_LONG).show();
        }else{
            try {
                byte txpower= (byte) txpower_at_0m;
                String txpowers=StringUtils.Byte2Hex(txpower);
                byte[] urld=url.getBytes();
                String d=StringUtils.ByteArrToHex(urld).replaceAll(" ","");
                String data="24"+head+end+d+txpowers;
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