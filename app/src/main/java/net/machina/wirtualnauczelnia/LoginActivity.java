package net.machina.wirtualnauczelnia;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;

import net.machina.wirtualnauczelnia.common.Constants;
import net.machina.wirtualnauczelnia.common.LoginDetails;
import net.machina.wirtualnauczelnia.common.PreferencesFields;
import net.machina.wirtualnauczelnia.network.WUDataHelper;

import java.io.File;
import java.io.FileOutputStream;

import okhttp3.OkHttpClient;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener{

    protected OkHttpClient httpClient;

    protected Button btnLogin;
    protected EditText inpLogin;
    protected EditText inpPassword;
    protected CheckBox chkRemember;
    protected SharedPreferences preferences;

    protected ProgressDialog dialog;

    protected LoginDetails loginDetails;

    protected WUDataHelper dataHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        preferences = getSharedPreferences(PreferencesFields.SHARED_PREFS_FILENAME, MODE_PRIVATE);
        btnLogin = (Button) findViewById(R.id.login_btnLogin);
        inpLogin = (EditText) findViewById(R.id.login_inpLogin);
        inpPassword = (EditText) findViewById(R.id.login_inpPassword);
        chkRemember = (CheckBox) findViewById(R.id.login_chkRemember);
        dataHelper = WUDataHelper.getInstance(getApplicationContext());
        btnLogin.setOnClickListener(this);
        if(preferences.getBoolean(PreferencesFields.SHARED_PREFS_KEY_REMEMBER,false)) {
            doLogin(true);
        }
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()) {
            case R.id.login_btnLogin:
                if(inpLogin.getText().toString().length() < 1 || inpPassword.getText().toString().length() < 1) {
                    AlertDialog.Builder alertBuilder = new AlertDialog.Builder(this);
                    alertBuilder.setIcon(R.drawable.ic_warning_black)
                            .setTitle(R.string.label_warning)
                            .setMessage(R.string.error_missing_login_data)
                            .setPositiveButton("OK", null)
                            .show();
                    return;
                } else {
                    if(chkRemember.isChecked()) {
                        preferences.edit()
                                .putBoolean(PreferencesFields.SHARED_PREFS_KEY_REMEMBER, true)
                                .putString(PreferencesFields.SHARED_PREFS_KEY_USERNAME, inpLogin.getText().toString())
                                .putString(PreferencesFields.SHARED_PREFS_KEY_PASSWORD, inpPassword.getText().toString())
                                .commit();
                    }
                    doLogin(false);
                }
                break;
            default:
                break;
        }
    }

    protected void doLogin(boolean useSavedData) {
        if(!useSavedData) {
            loginDetails = new LoginDetails(inpLogin.getText().toString(), inpPassword.getText().toString());
        } else {
            loginDetails = LoginDetails.fromSavedData(this);
        }
        dialog = ProgressDialog.show(this, getString(R.string.label_please_wait), getString(R.string.message_logging_in), true, false);
        dataHelper.login(loginDetails, (isSuccessful, data) -> {
            dialog.cancel();
            if(!isSuccessful) {
                preferences.edit()
                        .remove(PreferencesFields.SHARED_PREFS_KEY_REMEMBER)
                        .apply();

                new AlertDialog.Builder(LoginActivity.this)
                        .setTitle(R.string.label_warning)
                        .setIcon(R.drawable.ic_warning_black)
                        .setMessage(data)
                        .setCancelable(true)
                        .setPositiveButton("OK", null)
                        .show();
            } else {
                try {
                    File cacheFile = new File(getApplicationContext().getFilesDir(), Constants.CACHE_SESSIONID);
                    FileOutputStream outputStream = new FileOutputStream(cacheFile);
                    outputStream.write(data.getBytes());
                    outputStream.close();
                } catch(Exception ex) {
                    ex.printStackTrace();
                }
                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                LoginActivity.this.startActivity(intent);
                LoginActivity.this.finish();
            }
        });
    }

}
