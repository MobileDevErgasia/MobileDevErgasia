package com.example.mobiledevergasia;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceManager;
import android.preference.SwitchPreference;

import java.util.Locale;

/**
 * Κλαση για τις ρυθμισεις
 * Πιθανες ρυθμισεις : γλωσσα (el,en),
 * μεγιστη διαρκεια ηχογραφησης(5,10,15,20)
 * αυτοματη επαναληψη (on,off)
 */
public class Settings extends PreferenceActivity {
    private SharedPreferences sharedPreferences;
    private SwitchPreference autoLoopSwitch;
    private ListPreference languageListPreference,durationListPreference;


    @Override
    public void onCreate( Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.settings);
        Intent intent=getIntent();
        String lang=intent.getStringExtra("language");
        setLocale(this,lang);

        sharedPreferences= PreferenceManager.getDefaultSharedPreferences(this);
        durationListPreference=(ListPreference)findPreference("maxRecordDuration");
        autoLoopSwitch=(SwitchPreference)findPreference("AutoLoop");
        languageListPreference =(ListPreference)findPreference("selectLanguage");
        setTitles();

        languageListPreference.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object newValue) {
                setLocale(Settings.this,(String)newValue);
                setTitles();
                return true;
            }
        });
    }

    /**
     * Αλλαζει την γλωσσα ολων των στοιχειων
     */
    private void setTitles(){
        autoLoopSwitch.setTitle(R.string.SettingAutoLoop);
        autoLoopSwitch.setSummaryOff(R.string.AutoLoopSummaryOff);
        autoLoopSwitch.setSummaryOn(R.string.AutoLoopSummaryOn);
        languageListPreference.setTitle(R.string.select_language);
        languageListPreference.setDialogTitle(R.string.select_language);
        languageListPreference.setNegativeButtonText(R.string.cancel);
        durationListPreference.setTitle(R.string.SettingsDuration);
        durationListPreference.setNegativeButtonText(R.string.cancel);
        durationListPreference.setDialogTitle(R.string.SettingsDuration);
    }

    /**
     * Αλλαζει την γλωσσα των ρυθμισεων,καλειται μετα η setTitles
     * για να εμφανιζονται σωστα
     * @param activity το Settings activity
     * @param languageCode ο κωδικος της γλωσσας, el για ελληνικα,en για αγγλικα
     */
    private static void setLocale(Activity activity, String languageCode) {
        Locale locale = new Locale(languageCode);
        Locale.setDefault(locale);
        Resources resources = activity.getResources();
        Configuration config = resources.getConfiguration();
        config.setLocale(locale);
        resources.updateConfiguration(config, resources.getDisplayMetrics());
    }

}
