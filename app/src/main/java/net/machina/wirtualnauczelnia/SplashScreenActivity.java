package net.machina.wirtualnauczelnia;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Handler;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import net.machina.wirtualnauczelnia.common.Constants;

public class SplashScreenActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hello);
        if(isOnline()) {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    Intent startIntent = new Intent(SplashScreenActivity.this, LoginActivity.class);
                    SplashScreenActivity.this.startActivity(startIntent);
                    SplashScreenActivity.this.finish();
                }
            }, Constants.SPLASH_DISMISS_TIME_MS);
        } else {
            AlertDialog.Builder alertBuilder = new AlertDialog.Builder(SplashScreenActivity.this);
            alertBuilder.setCancelable(false)
                    .setIcon(R.drawable.ic_signal_wifi_off)
                    .setTitle(R.string.error_no_connection)
                    .setMessage(R.string.error_no_connection_body)
                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            SplashScreenActivity.this.finish();
                        }
                    })
                    .show();
        }
    }

    public boolean isOnline() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnected();
    }
}
