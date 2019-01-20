package pku.zy.iword.ui;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import pku.zy.iword.R;

public class LoginActivity extends Activity {
    private SharedPreferences pref;
    private SharedPreferences.Editor editor;
    private EditText accountEdit;
    private EditText passwordEdit;
    private Button login;
    private CheckBox rememberPass;
    String username="chen",pwd="12345";

    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        pref= PreferenceManager.getDefaultSharedPreferences(this);
        accountEdit=(EditText)findViewById(R.id.acount);
        passwordEdit=(EditText)findViewById(R.id.password);
        rememberPass=(CheckBox)findViewById(R.id.remember_pass);
        login=(Button)findViewById(R.id.login);
        boolean isRemember=pref.getBoolean("remember_password",false);
        if(isRemember){
            String account=pref.getString("account","");
            String password=pref.getString("password","");
            accountEdit.setText(account);
            passwordEdit.setText(password);
            rememberPass.setChecked(true);
        }
        login.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                String account=accountEdit.getText().toString();
                String password=passwordEdit.getText().toString();
                if(account.equals(username) && password.equals(pwd)){
                    editor=pref.edit();
                    if(rememberPass.isChecked()){
                        editor.putBoolean("remember_password",true);
                        editor.putString("account",account);
                        editor.putString("password",password);
                    }else{
                        editor.clear();
                    }
                    editor.apply();
                    Intent intent=new Intent();
                    //Intent intent=new Intent(LoginActivity.this,MineActivity.class);
                    intent.putExtra("user_name",account);//将用户的账户信息显示在个人中心界面，传的值也可以在sharepref中取
                    //startActivity(intent);
                    setResult(RESULT_OK,intent);
                    finish();
                }else{
                    Toast.makeText(LoginActivity.this,"account or password is invalid",Toast.LENGTH_SHORT).show();
                }
            }

        });
    }
}
