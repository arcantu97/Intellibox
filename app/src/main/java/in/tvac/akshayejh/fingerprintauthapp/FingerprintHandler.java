package in.tvac.akshayejh.fingerprintauthapp;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.hardware.fingerprint.FingerprintManager;
import android.os.Build;
import android.os.CancellationSignal;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import static in.tvac.akshayejh.fingerprintauthapp.DeviceList.EXTRA_ADDRESS;

@TargetApi(Build.VERSION_CODES.M)
public class FingerprintHandler extends FingerprintManager.AuthenticationCallback {
    private String EXTRA_ADDRESS;
    private Context context;
    String address = null;


    public FingerprintHandler(Context context){

        this.context = context;

    }

    public void startAuth(FingerprintManager fingerprintManager, FingerprintManager.CryptoObject cryptoObject){

        CancellationSignal cancellationSignal = new CancellationSignal();
        fingerprintManager.authenticate(cryptoObject, cancellationSignal, 0, this, null);

    }

    @Override
    public void onAuthenticationError(int errorCode, CharSequence errString) {

        this.update("Ocurrió un error de autenticación. " + errString, false);

    }

    @Override
    public void onAuthenticationFailed() {

        this.update("Autenticación fallida. ", false);

    }

    @Override
    public void onAuthenticationHelp(int helpCode, CharSequence helpString) {

        this.update("Error: " + helpString, false);

    }

    @Override
    public void onAuthenticationSucceeded(FingerprintManager.AuthenticationResult result) {
        Button button = ((Activity) context).findViewById(R.id.desbloquear);

        this.update("Caja desbloqueada con éxito.", true);


    }

    @SuppressLint("ClickableViewAccessibility")
    private void update(String s, boolean b) {
        Button button = ((Activity) context).findViewById(R.id.desbloquear);
        TextView paraLabel = (TextView) ((Activity)context).findViewById(R.id.paraLabel);
        ImageView imageView = (ImageView) ((Activity)context).findViewById(R.id.fingerprintImage);
        paraLabel.setText(s);

        if(b == false){

            paraLabel.setTextColor(ContextCompat.getColor(context, R.color.colorAccent));

        } else {

            paraLabel.setTextColor(ContextCompat.getColor(context, R.color.colorPrimary));
            imageView.setImageResource(R.mipmap.action_done);
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    Intent i = new Intent(context, Desbloqueo.class);
                    i.putExtra(EXTRA_ADDRESS, address);
                    //Change the activity.
                    context.startActivity(i);
                }

            });

        }

    }

}
