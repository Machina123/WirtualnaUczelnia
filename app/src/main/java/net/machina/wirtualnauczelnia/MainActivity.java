package net.machina.wirtualnauczelnia;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import net.machina.wirtualnauczelnia.common.PreferencesFields;
import net.machina.wirtualnauczelnia.network.WUDataHelper;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    protected TextView txtLoggedIn;
    protected WUDataHelper dataHelper;
    protected Button btnGrades;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        txtLoggedIn = (TextView) findViewById(R.id.main_txtLoggedIn);
        btnGrades = (Button) findViewById(R.id.main_btnGrades);
        btnGrades.setOnClickListener(this);
        dataHelper = WUDataHelper.getInstance(getApplicationContext());
        if(savedInstanceState == null) {
            dataHelper.getLoggedInUser(((isSuccessful, data) -> {
                if (!isSuccessful) {
                    new AlertDialog.Builder(MainActivity.this)
                            .setTitle(R.string.label_warning)
                            .setIcon(R.drawable.ic_warning_black)
                            .setMessage(R.string.error_not_logged_in)
                            .setPositiveButton("OK", ((dialog, which) -> {
                                Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                                MainActivity.this.startActivity(intent);
                                MainActivity.this.finish();
                            }))
                            .setCancelable(false)
                            .show();
                }
                runOnUiThread(() -> MainActivity.this.txtLoggedIn.setText(data));
            }));
        } else {
            txtLoggedIn.setText(savedInstanceState.getString(PreferencesFields.SHARED_PREFS_KEY_USERNAME));
        }
    }

    @Override
    public void onClick(View v) {
        Intent startIntent;
        switch(v.getId()) {
            case R.id.main_btnGrades:
                startIntent = new Intent(MainActivity.this, GradesActivity.class);
                break;
            default:
                return;
        }
        startActivity(startIntent);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putString(PreferencesFields.SHARED_PREFS_KEY_USERNAME, txtLoggedIn.getText().toString());
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        if(savedInstanceState != null) txtLoggedIn.setText(savedInstanceState.getString(PreferencesFields.SHARED_PREFS_KEY_USERNAME));
        super.onRestoreInstanceState(savedInstanceState);
    }
}
