package cronzy.com.cronzypicker.activities;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import cronzy.com.cronzypicker.R;
import cronzy.com.cronzypicker.constants.ProjectConstants;
import cronzy.com.cronzypicker.utils.MySharedPreferences;


public class InitActivity extends Activity  {
    private Button searchDevesis;
    private BluetoothAdapter BA;
    private Set<BluetoothDevice> pairedDevices;
    private ListView lv;
    private MySharedPreferences preferences;
    private String bluetoothAdress = ProjectConstants.APP_DEFAULT_NAME_VALUE;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.init_bluetooth);
        preferences = new MySharedPreferences(this);
        redirect();
        initViews();
    }


    private void redirect() {
        bluetoothAdress = preferences.getBluetoothAddress();
        if (!bluetoothAdress.equals(ProjectConstants.APP_DEFAULT_NAME_VALUE)) {
            Intent intent = new Intent(InitActivity.this, MainActivity.class);
            intent.putExtra(ProjectConstants.APP_PREFERENCES_NAME, bluetoothAdress);
            startActivity(intent);
        }
    }

    private void initViews() {
        searchDevesis = (Button) findViewById(R.id.btnSearchDevesis);
        BA = BluetoothAdapter.getDefaultAdapter();
        lv = (ListView) findViewById(R.id.listOfPairedDevises);
    }

    public void showListOfPairedDevises(View v) {
        if (!BA.isEnabled()){
            Toast.makeText(getBaseContext(), "Bluetooth is NOT Enabled!",
                    Toast.LENGTH_SHORT).show();
            Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableBtIntent, ProjectConstants.REQUEST_ENABLE_BT);
        }

        pairedDevices = BA.getBondedDevices();
        ArrayList list = new ArrayList();
        final Map<String, BluetoothDevice> bdLMap = new HashMap<>();
        for (BluetoothDevice bt : pairedDevices) {
            list.add(bt.getName());
            bdLMap.put(bt.getName(), bt);
        }

        final ArrayAdapter adapter = new ArrayAdapter(this,
                android.R.layout.simple_list_item_1, list){

            @Override
            public View getView(int position,  View convertView,  ViewGroup parent) {
                View view = super.getView(position, convertView, parent);

                TextView tv = (TextView) view.findViewById(android.R.id.text1);
                tv.setTextColor(Color.BLACK);
                return view;
            }
        };


        lv.setAdapter(adapter);

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent,
                                    View view,
                                    int position, long id) {
                String name = ((TextView) view).getText().toString();
                String selectedAdress = bdLMap.get(name).getAddress();
                preferences.setBluetoothAddress(selectedAdress);
                redirect();
            }
        });
    }
}