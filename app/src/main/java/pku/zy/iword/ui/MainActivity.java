package pku.zy.iword.ui;



import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Random;

import pku.zy.iword.R;
import pku.zy.iword.utils.NetUtil;

public class MainActivity extends Activity implements View.OnClickListener {
    private String word="happy";
    private TextView WordText;
    private Button ClickBtn;
    private ImageView mHomeBtn;
    private ImageView mSearchBtn;
    private ImageView mMineBtn;

    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        WordText=(TextView)findViewById(R.id.word_show);
        WordText.setOnClickListener(this);
        ClickBtn=(Button)findViewById(R.id.click_btn);
        ClickBtn.setOnClickListener(this);
        mHomeBtn=(ImageView)findViewById(R.id.button_home);
        mHomeBtn.setOnClickListener(this);
        mSearchBtn=(ImageView)findViewById(R.id.button_search);
        mSearchBtn.setOnClickListener(this);
        mMineBtn=(ImageView)findViewById(R.id.button_mine);
        mMineBtn.setOnClickListener(this);
        if (NetUtil.getNetworkState(this) != NetUtil.NETWORN_NONE) {
            Log.d("myWeather", "网络OK");
            Toast.makeText(MainActivity.this, "网络OK", Toast.LENGTH_LONG).show();
            ;
        } else {
            Log.d("myWeather", "网络无法连接");
            Toast.makeText(MainActivity.this, "网络无法连接", Toast.LENGTH_LONG).show();
        }

        //word=getJson();
        word=getString();
        Log.d("rand",word);
        WordText.setText(word);

    }

    public String getString(){
        String[] array = {"abstract","enthusiastic","licence","retail","feed"};
        Random rand=new Random();
        int index=rand.nextInt(5);//包含0，不包含5
        return array[index];

    }
    public String getWord(){
        String str="";
        Random rand=new Random();
        int max=12347;
        int index=rand.nextInt(max)+1;//1~12347
        int flag=1;
        InputStream in=null;

        try{
            in=getResources().getAssets().open("vocabulary.txt");
            InputStreamReader reader=new InputStreamReader(in);
            BufferedReader bf=new BufferedReader(reader);
            String line;
            while((line=bf.readLine())!=null){
                if(flag==index){
                    str=line;
                }
                flag++;
            }

        }catch(IOException e){
            e.printStackTrace();
        }
        return str;
    }
    public void onClick(View view){
        if(view.getId()==R.id.click_btn){
            Intent intent=new Intent();
            intent.putExtra("word",word);
            intent.setClass(MainActivity.this,StudyActivity.class);
            MainActivity.this.startActivity(intent);
        }
        if(view.getId()==R.id.button_home){
            Intent intent=new Intent();
            intent.setClass(MainActivity.this,MainActivity.class);
            MainActivity.this.startActivity(intent);
        }

        if(view.getId()==R.id.button_search){
            Intent intent=new Intent();
            intent.setClass(MainActivity.this,SearchActivity.class);
            MainActivity.this.startActivity(intent);
        }
        if(view.getId()==R.id.button_mine){
            Intent intent=new Intent();
            intent.setClass(MainActivity.this,MineActivity.class);
            MainActivity.this.startActivity(intent);
        }
    }
    protected void onDestroy(){
        super.onDestroy();
        //unbindService();
    }

}
