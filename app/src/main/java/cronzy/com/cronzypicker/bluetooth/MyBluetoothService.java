package cronzy.com.cronzypicker.bluetooth;


import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.UUID;

import cronzy.com.cronzypicker.constants.ProjectConstants;

public class MyBluetoothService  {

    private Handler handler;

    public MyBluetoothService(Handler handler) {
        this.handler = handler;
    }

    public ConnectedThread getConnectedThread(BluetoothSocket bSocket) {
        return new ConnectedThread(bSocket);
    }

    public class ConnectedThread extends Thread {
        private final BluetoothSocket mmSocket;
        private final InputStream mmInStream;
        private final OutputStream mmOutStream;
        private byte[] mmBuffer; // mmBuffer store for the stream

        public ConnectedThread(BluetoothSocket bSocket) {
            mmSocket = bSocket;
            InputStream tmpIn = null;
            OutputStream tmpOut = null;

            // Get the input and output streams; using temp objects because
            // member streams are final.
            try {
                tmpIn = bSocket.getInputStream();
            } catch (IOException e) {
                Log.e(ProjectConstants.TAG_BT, "Error occurred when creating input stream", e);
            }
            try {
                tmpOut = bSocket.getOutputStream();
            } catch (IOException e) {
                Log.e(ProjectConstants.TAG_BT, "Error occurred when creating output stream", e);
            }

            mmInStream = tmpIn;
            mmOutStream = tmpOut;
        }

        public void run() {
            mmBuffer = new byte[1024];
            int numBytes; // bytes returned from read()

            // Keep listening to the InputStream until an exception occurs.
            while (true) {
                try {
                    // Read from the InputStream.
                    numBytes = mmInStream.read(mmBuffer);
                    // Send the obtained bytes to the UI activity.
                    Message readMsg = handler.obtainMessage(
                            MessageConstants.MESSAGE_READ, numBytes, -1,
                            mmBuffer);
                    readMsg.sendToTarget();

                } catch (IOException e) {
                    Log.d(ProjectConstants.TAG_BT, "Input stream was disconnected", e);
                    break;
                }

            }
        }

        // Call this from the main activity to send data to the remote device.
        public void write(String line) {
            try {
                mmOutStream.write(line.getBytes());

                // Share the sent message with the UI activity.
                Message writtenMsg = handler.obtainMessage(
                        MessageConstants.MESSAGE_WRITE, -1, -1, mmBuffer);
                writtenMsg.sendToTarget();
            } catch (IOException e) {
                Log.e(ProjectConstants.TAG_BT, "Error occurred when sending data", e);

                // Send a failure message back to the activity.
                Message writeErrorMsg =
                        handler.obtainMessage(MessageConstants.MESSAGE_TOAST);
                Bundle bundle = new Bundle();
                bundle.putString("toast",
                        "Couldn't send data to the other device");
                writeErrorMsg.setData(bundle);
                handler.sendMessage(writeErrorMsg);
            }
        }

        // Call this method from the main activity to shut down the connection.
        public void cancel() {
            try {
                mmSocket.close();
                mmInStream.close();
                mmOutStream.close();
            } catch (IOException e) {
                Log.e(ProjectConstants.TAG_BT, "Could not close the connect socket", e);
            }
        }
    }

    private interface MessageConstants {
        public static final int MESSAGE_READ = 0;
        public static final int MESSAGE_WRITE = 1;
        public static final int MESSAGE_TOAST = 2;

        // ... (Add other message types here as needed.)
    }

    public BluetoothSocket connectionBT(BluetoothAdapter btAdapter, String bluetoothAdress) {
        BluetoothSocket btSocket = null;

        Log.d(ProjectConstants.TAG_BT, "...onResume - попытка соединения...");
        // Set up a pointer to the remote node using it's address.
        BluetoothDevice device = btAdapter.getRemoteDevice(bluetoothAdress);
        // Two things are needed to make a connection:
        //   A MAC address, which we got above.
        //   A Service ID or UUID.  In this case we are using the
        //     UUID for SPP.

        try {
            btSocket = createBluetoothSocket(device);
        } catch (IOException e) {
            Log.d(ProjectConstants.TAG_BT, "Fatal Error In onResume() and socket create failed: "
                    + e.getMessage() + ".");
        }
        // Discovery is resource intensive.  Make sure it isn't going on
        // when you attempt to connect and pass your message.
        btAdapter.cancelDiscovery();

        // Establish the connection.  This will block until it connects.
        Log.d(ProjectConstants.TAG_BT, "...Соединяемся...");

        if (btSocket != null) {
            try {
                btSocket.connect();
                Log.d(ProjectConstants.TAG_BT, "...Соединение установлено и готово к передачи данных...");
            } catch (IOException e) {
                Log.d(ProjectConstants.TAG_BT, "IOException: "+e.getMessage());
                try {
                    Log.e("", "trying fallback...");
                    btSocket = (BluetoothSocket) device.getClass().getMethod("createRfcommSocket", new Class[]{int.class}).invoke(device, 1);
                    btSocket.connect();
                    Log.e("", "Connected");
                } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException | IOException e1) {
                    e1.printStackTrace();
                }
            }
        }

        return btSocket;
        // Create a data stream so we can talk to server.
        //mConnectedThread = new ConnectedThread(btSocket);
        //mConnectedThread.start();
    }

    private BluetoothSocket createBluetoothSocket(BluetoothDevice device)
            throws IOException {
        if (Build.VERSION.SDK_INT >= 10) {
            try {
                final Method m = device.getClass().getMethod("createInsecureRfcommSocketToServiceRecord", new Class[]{UUID.class});
                return (BluetoothSocket) m.invoke(device, ProjectConstants.MY_UUID);
            } catch (Exception e) {
                Log.e(ProjectConstants.TAG_BT, "Could not create Insecure RFComm Connection", e);
            }
        }
        return device.createRfcommSocketToServiceRecord(ProjectConstants.MY_UUID);
    }


}
