package net.machina.wirtualnauczelnia.common;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.support.annotation.Nullable;
import android.util.Base64;

import java.util.Map;
import java.util.Set;

public class ObfuscatedPreferences implements SharedPreferences {

    private SharedPreferences preferences;

    public ObfuscatedPreferences(Context mContext, String filename) {
        this.preferences = mContext.getSharedPreferences(filename, Context.MODE_PRIVATE);
    }

    @Override
    public Map<String, ?> getAll() {
        throw new UnsupportedOperationException("Please do not use getAll() when preferences are obfuscated");
    }

    @Nullable
    @Override
    public String getString(String key, @Nullable String defValue) {
        String received = preferences.getString(Base64.encodeToString(key.getBytes(), Base64.URL_SAFE), defValue);
        if(received.equals(defValue)) return defValue;
        else return new String(Base64.decode(received.getBytes(), Base64.URL_SAFE));
    }

    @Nullable
    @Override
    public Set<String> getStringSet(String key, @Nullable Set<String> defValues) {
        Set<String> received = preferences.getStringSet(Base64.encodeToString(key.getBytes(), Base64.URL_SAFE), defValues);
        if(received.equals(defValues)) return defValues;
        else {
            for(String s : received) {
                String temp = s;
                received.remove(s);
                s = new String(Base64.decode(temp, Base64.URL_SAFE));
                received.add(s);
            }

            return received;
        }
    }

    @Override
    public int getInt(String key, int defValue) {
        return preferences.getInt(Base64.encodeToString(key.getBytes(), Base64.URL_SAFE), defValue);
    }

    @Override
    public long getLong(String key, long defValue) {
        return preferences.getLong(Base64.encodeToString(key.getBytes(), Base64.URL_SAFE), defValue);
    }

    @Override
    public float getFloat(String key, float defValue) {
        return preferences.getFloat(Base64.encodeToString(key.getBytes(), Base64.URL_SAFE), defValue);
    }

    @Override
    public boolean getBoolean(String key, boolean defValue) {
        return preferences.getBoolean(Base64.encodeToString(key.getBytes(), Base64.URL_SAFE), defValue);
    }

    @Override
    public boolean contains(String key) {
        return preferences.contains(Base64.encodeToString(key.getBytes(), Base64.URL_SAFE));
    }

    @Override
    public Editor edit() {
        return new Editor();
    }

    @Override
    public void registerOnSharedPreferenceChangeListener(OnSharedPreferenceChangeListener listener) {
        preferences.registerOnSharedPreferenceChangeListener(listener);
    }

    @Override
    public void unregisterOnSharedPreferenceChangeListener(OnSharedPreferenceChangeListener listener) {
        preferences.unregisterOnSharedPreferenceChangeListener(listener);
    }

    public class Editor implements SharedPreferences.Editor {
        private SharedPreferences.Editor mEditor;

        public Editor() {mEditor = preferences.edit();}

        @Override
        public SharedPreferences.Editor putString(String key, @Nullable String value) {
            return mEditor.putString(Base64.encodeToString(key.getBytes(), Base64.URL_SAFE), Base64.encodeToString(value.getBytes(), Base64.URL_SAFE));
        }

        @Override
        public SharedPreferences.Editor putStringSet(String key, @Nullable Set<String> values) {
            for(String s : values) {
                String temp = s;
                values.remove(s);
                s = Base64.encodeToString(temp.getBytes(), Base64.URL_SAFE);
                values.add(s);
            }
            return mEditor.putStringSet(Base64.encodeToString(key.getBytes(), Base64.URL_SAFE), values);
        }

        @Override
        public SharedPreferences.Editor putInt(String key, int value) {
            return mEditor.putInt(Base64.encodeToString(key.getBytes(), Base64.URL_SAFE), value);
        }

        @Override
        public SharedPreferences.Editor putLong(String key, long value) {
            return mEditor.putLong(Base64.encodeToString(key.getBytes(), Base64.URL_SAFE), value);
        }

        @Override
        public SharedPreferences.Editor putFloat(String key, float value) {
            return mEditor.putFloat(Base64.encodeToString(key.getBytes(), Base64.URL_SAFE), value);
        }

        @Override
        public SharedPreferences.Editor putBoolean(String key, boolean value) {
            return mEditor.putBoolean(Base64.encodeToString(key.getBytes(), Base64.URL_SAFE), value);
        }

        @Override
        public SharedPreferences.Editor remove(String key) {
            return mEditor.remove(Base64.encodeToString(key.getBytes(), Base64.URL_SAFE));
        }

        @Override
        public SharedPreferences.Editor clear() {
            return mEditor.clear();
        }

        @Override
        public boolean commit() {
            return mEditor.commit();
        }

        @Override
        public void apply() {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.GINGERBREAD) {
                mEditor.apply();
            } else {
                commit();
            }
        }
    }
}
