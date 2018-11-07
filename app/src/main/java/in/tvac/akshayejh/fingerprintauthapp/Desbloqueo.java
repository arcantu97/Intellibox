package in.tvac.akshayejh.fingerprintauthapp;

import android.app.ProgressDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.io.IOException;
import java.util.UUID;

import static in.tvac.akshayejh.fingerprintauthapp.MainActivity.myUUID;

public class Desbloqueo extends AppCompatActivity {

    private static boolean ConnectSuccess;
    private ProgressDialog progress;
    BluetoothAdapter myBluetooth = null;
    BluetoothSocket btSocket = null;
    private boolean isBtConnected = false;
    static final UUID myUUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");
    Button botonCerrar, botonAbrir;
    String address = "";
    boolean abierto = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_desbloqueo);
        botonAbrir = (Button) findViewById(R.id.abrir);
        botonCerrar = (Button) findViewById(R.id.cerrar);
        doInBackground();
        onPrexecute();
        onPostExecute();
        botonAbrir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent newint = getIntent();
                //address = newint.getStringExtra(DeviceList.EXTRA_ADDRESS);
                //System.out.println(MainActivity.address);
                abrircaja();
            }
        });
        botonCerrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cerrarcaja();
                //System.out.println("que pex");

            }
        });
    }

    private void cerrarcaja() {
        if (btSocket != null) {
            try {
                btSocket.getOutputStream().write("b".toString().getBytes());
            } catch (IOException e) {
                msg("Cerrando caja");
            }
        }
    }

    private void onPostExecute() {
        if (!ConnectSuccess) {
            msg("Connection Failed. Is it a SPP Bluetooth? Try again.");
        } else {
            isBtConnected = true;

        }
        msg("Connected.");
        progress.dismiss();
    }

    private void abrircaja() {
        if (btSocket != null) {
            try {
                btSocket.getOutputStream().write("A".toString().getBytes());
            } catch (IOException e) {
                msg("Abriendo caja");
            }
        }
    }

    private void onPrexecute() {
        progress = ProgressDialog.show(Desbloqueo.this, "Connecting...", "Please wait!!!");  //show a progress dialog
    }

    protected void doInBackground(Void... devices) //while the progress dialog is shown, the connection is done in background
    {
        try {
            if (btSocket == null || !isBtConnected) {
                myBluetooth = BluetoothAdapter.getDefaultAdapter();//get the mobile bluetooth device
                System.out.println("1");
                BluetoothDevice dispositivo = myBluetooth.getRemoteDevice(MainActivity.address);//connects to the device's address and checks if it's available
                System.out.println("2");
                btSocket = dispositivo.createInsecureRfcommSocketToServiceRecord(myUUID);//create a RFCOMM (SPP) connection
                System.out.println("3");
                BluetoothAdapter.getDefaultAdapter().cancelDiscovery();
                btSocket.connect();//start connection
            }
        } catch (IOException e) {
            System.out.println(e);
            ConnectSuccess = false;//if the try failed, you can check the exception here
        }
        return;
    }

    private void msg(String s) {
        Toast.makeText(getApplicationContext(), s, Toast.LENGTH_LONG).show();
    }
}
