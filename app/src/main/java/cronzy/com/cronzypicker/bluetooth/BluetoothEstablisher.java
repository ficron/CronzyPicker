package cronzy.com.cronzypicker.bluetooth;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.widget.Toast;

import java.io.IOException;

import cronzy.com.cronzypicker.constants.ProjectConstants;
import cronzy.com.cronzypicker.views.ColorCirclePicker;
import petrov.kristiyan.colorpicker.ColorPicker;

public class BluetoothEstablisher {

    private ColorCirclePicker mColorCirclePickerView;

    private MyHandler handler;
    private MyBluetoothService bluetoothService;
    private BluetoothAdapter btAdapter;

    private BluetoothSocket btSocket;
    private String bluetoothAdress;

    private Activity activity;

    /**TODO
     * change class ConnectedThread to protected
     */
    private MyBluetoothService.ConnectedThread mConnectedThread;


    public BluetoothEstablisher (Activity activity, ColorCirclePicker mColorCirclePickerView , String bluetoothAdress){
        this.mColorCirclePickerView = mColorCirclePickerView;
        this.bluetoothAdress = bluetoothAdress;
        this.activity = activity;
        init();
    }

    private void init() {
        this.btAdapter = BluetoothAdapter.getDefaultAdapter();
        if (!isBluetoothAdapterSupported())return;

        enableBluetoothAdapter();
        while (handler==null||bluetoothService==null){
            if (!btAdapter.isEnabled())break;
            this.handler = new MyHandler(mColorCirclePickerView);
            this.bluetoothService = new MyBluetoothService(handler);
        }
    }



    public void createConnectionThred(){
        checkBluetoothSocket();
        if (mConnectedThread != null) {
            mConnectedThread.cancel();
            mConnectedThread = null;
        }

        while (mConnectedThread==null){
            if (!btAdapter.isEnabled()) break;

            mConnectedThread = bluetoothService.getConnectedThread(btSocket);
            mConnectedThread.start();
        }

    }

    private void enableBluetoothAdapter(){
        if (!btAdapter.isEnabled()){
            Intent enableBtIntent = new Intent(btAdapter.ACTION_REQUEST_ENABLE);
            activity.startActivityForResult(enableBtIntent, ProjectConstants.REQUEST_ENABLE_BT);
        }
    }


    public boolean isBluetoothAdapterSupported(){
        return btAdapter!=null ? true: false;
    }

    private boolean isConnectionEstablished(){
        return mConnectedThread!=null?true:false;
    }

    private void checkBluetoothSocket() {
        /**
         * уточнить для себя целесообразность .close() и =null
         * https://developer.android.com/guide/topics/connectivity/bluetooth#java
         */
        if (btSocket!=null){
            try {
                btSocket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            btSocket = null;
        }
        btSocket = bluetoothService.connectionBT(btAdapter, bluetoothAdress);
    }
    
    public void closeBluetoothSocket(){
        if (btSocket!=null){
            try {
                btSocket.close();
                btSocket = null;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    
    public void closeConnectionThread(){
            mConnectedThread.cancel();
            mConnectedThread = null;
    }

    public void sendData(String data){
        if (mConnectedThread != null) {
            mConnectedThread.write(data + "\n");
            Toast.makeText(activity.getBaseContext(), "Sent:" + data, Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(activity.getBaseContext(), "Connection isn't established",
                    Toast.LENGTH_SHORT).show();
        }
    }

}
