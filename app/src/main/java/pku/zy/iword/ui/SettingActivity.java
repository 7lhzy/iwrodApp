package pku.zy.iword.ui;

import android.app.ActionBar;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.preference.CheckBoxPreference;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceManager;
import android.preference.SwitchPreference;
import android.provider.Settings;
import android.widget.Toast;

import pku.zy.iword.R;

public class SettingActivity extends PreferenceActivity implements Preference.OnPreferenceChangeListener,
        Preference.OnPreferenceClickListener {

    private SwitchPreference mAddNewWord;
    private SwitchPreference mRemind;
    private Preference mClearCache;
    private Preference mCheckUpdate;
    private Preference mAbout;
    private String add_new="add_newword";
    private String remind="remind";
    private String clear_cache="clear_cache";
    private String check_update="check_update";
    private String about="about";


    protected ActionBar mActionBar;

    private SharedPreferences sharedPreferences;
    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.pref_setting);//报空指针的错
        sharedPreferences= PreferenceManager.getDefaultSharedPreferences(this);
        //initActionBar();
        setTitle("设置");
        mAddNewWord=(SwitchPreference)findPreference(add_new);
        mRemind=(SwitchPreference)findPreference(remind);
        mCheckUpdate=(Preference)findPreference(check_update);
        mClearCache=(Preference)findPreference(clear_cache);
        mAbout=(Preference)findPreference(about);

        boolean add_new=false;
        boolean remind=true;
        mAddNewWord.setOnPreferenceChangeListener(this);
        mAddNewWord.setChecked(add_new);
        mRemind.setOnPreferenceChangeListener(this);
        mRemind.setChecked(remind);
        mClearCache.setOnPreferenceClickListener(this);//空指针异常
        mCheckUpdate.setOnPreferenceClickListener(this);
        mAbout.setOnPreferenceClickListener(this);
        //mHandSwitchCheckPref.setOnPreferenceClickListener(this);
    }

    private void initActionBar() {
        mActionBar = getActionBar();
        mActionBar.setDisplayHomeAsUpEnabled(true);// 给左上角图标的左边加上一个返回的图标"《" 。对应ActionBar.DISPLAY_HOME_AS_UP
        mActionBar.setDisplayShowHomeEnabled(true); //使左上角图标可点击，对应id为android.R.id.home，对应ActionBar.DISPLAY_SHOW_HOME false 则图标无法点击
        mActionBar.setHomeButtonEnabled(true); // false 就无法点击
    }



    @Override
    public boolean onPreferenceChange(Preference preference, Object newValue) {
        if (preference.getKey().equals(mAddNewWord)) {
            SharedPreferences.Editor editor=sharedPreferences.edit();
            editor.putBoolean("add_newword",!mAddNewWord.isChecked());
            editor.commit();
        } else if (preference.getKey().equals(mRemind)) {
            SharedPreferences.Editor editor=sharedPreferences.edit();
            editor.putBoolean("remind",!mRemind.isChecked());
            editor.commit();
            if (!mRemind.isChecked()) {
                Toast.makeText(SettingActivity.this,"建议开启",Toast.LENGTH_LONG).show();
            }

        } else {
            return false;
        }

        return true;
    }

    @Override
    public boolean onPreferenceClick(Preference preference) {
        if (preference.getKey().equals(mClearCache)) {

        } else if(preference.getKey().equals(mCheckUpdate)){
            Uri uri=Uri.parse("http://www.baidu.com");
            Intent i=new Intent(Intent.ACTION_VIEW,uri);
            startActivity(i);

        }else if(preference.getKey().equals(mAbout)){
            Intent intent=new Intent(SettingActivity.this,AboutActivity.class);
            startActivity(intent);
        }else{
            return false;
        }
        return true;
    }
}
