package net.machina.wirtualnauczelnia;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import net.machina.wirtualnauczelnia.common.Constants;
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
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()) {
            case R.id.menu_main_about:
                try {
                    AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                    builder.setTitle(R.string.menu_item_about)
                            .setMessage(String.format(getString(R.string.app_info_basic), getString(R.string.app_name), getPackageManager().getPackageInfo(getPackageName(), 0).versionName, Constants.APP_GITHUB_URL))
                            .setPositiveButton("OK", null)
                            .setCancelable(false)
                            .show();
                } catch(PackageManager.NameNotFoundException e) {e.printStackTrace();}
                break;
            case R.id.menu_main_logout:
                askLogout();
                break;
        }

        return super.onOptionsItemSelected(item);
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

    @Override
    public void onBackPressed() {
        askLogout();
    }

    private void askLogout() {
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setTitle(R.string.button_logout)
                .setMessage(R.string.ask_logout)
                .setPositiveButton(R.string.button_yes, (DialogInterface dialog, int which) -> {
                    WUDataHelper.getInstance(getApplicationContext()).logout(((isSuccessful, data) -> {
                        if(isSuccessful) {
                            runOnUiThread(() -> {
                                SharedPreferences preferences = getSharedPreferences(PreferencesFields.SHARED_PREFS_FILENAME, MODE_PRIVATE);
                                preferences.edit()
                                        .remove(PreferencesFields.SHARED_PREFS_KEY_REMEMBER)
                                        .remove(PreferencesFields.SHARED_PREFS_KEY_PASSWORD)
                                        .apply();
                                Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                                startActivity(intent);
                                MainActivity.this.finish();
                            });
                        }
                    }));
                })
                .setNegativeButton(R.string.button_no, null)
                .setCancelable(false)
                .show();
    }
}
