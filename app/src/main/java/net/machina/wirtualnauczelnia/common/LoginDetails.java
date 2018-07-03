package net.machina.wirtualnauczelnia.common;

import android.content.Context;
import android.content.SharedPreferences;

import com.securepreferences.SecurePreferences;

public class LoginDetails {

    private String identifier;
    private String password;

    public String getIdentifier() {
        return identifier;
    }

    public String getPassword() {
        return password;
    }

    public LoginDetails(String identifier, String password) {
        this.identifier = identifier;
        this.password = password;
    }

    public static LoginDetails fromSavedData(Context ctx) {
        SharedPreferences prefs = ctx.getSharedPreferences(PreferencesFields.SHARED_PREFS_FILENAME, Context.MODE_PRIVATE);
        return new LoginDetails(prefs.getString(PreferencesFields.SHARED_PREFS_KEY_USERNAME, null), prefs.getString(PreferencesFields.SHARED_PREFS_KEY_PASSWORD, null));
    }

    @Override
    public String toString() {
        return "LoginDetails{" +
                "identifier='" + identifier + '\'' +
                ", password='" + password + '\'' +
                '}';
    }
}
