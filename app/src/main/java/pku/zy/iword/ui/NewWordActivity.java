package pku.zy.iword.ui;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import pku.zy.iword.R;
import pku.zy.iword.adapter.WordListAdapter;
import pku.zy.iword.database.NewWordDB;
//显示全部生词本
public class NewWordActivity extends Activity  {
    private NewWordDB dbHelper;
    //private String[] data = new String[200];
    private List<String> data = new ArrayList<>();
    int size=0;

    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_newwordlist);//这个布局是一个listview，把数据库中查到的值赋值给各个items
        initList();
        ArrayAdapter<String> adapter=new ArrayAdapter<>(NewWordActivity.this,android.R.layout.simple_list_item_1,data);
        //android.R.layout.simple_list_item_1是android内置的布局文件，里面只有一个TextView 可用于简单的显示一段文本
        //WordListAdapter adapter= new WordListAdapter(NewWordActivity.this,android.R.layout.simple_list_item_1,data);
        ListView listView=(ListView)findViewById(R.id.list_view);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //String str=data[position];//不知道这个position是从0开始还是从1开始
                String str=data.get(position);
                Toast.makeText(NewWordActivity.this,str,Toast.LENGTH_SHORT).show();
                Intent i = new Intent();
                i.putExtra("word",str);
                i.setClass(NewWordActivity.this,SearchActivity.class);
                setResult(RESULT_OK);
                startActivityForResult(i,10);
            }
        });//设置子项的单击事件，每点击一个子项，跳转查词界面展示这个单词的详细信息,
        // 查词的activity接收intent传来的数据时，必须统一成word

    }
    private  void initList(){
        dbHelper=new NewWordDB(this, "NewWord.db", null, 1);
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("word", "swollen");
        values.put("interpret", "adj.膨胀的；肿起的；v.增强；肿胀");
        db.insert("newword",null,values);
        values.clear();
        values.put("word", "vaporize");
        values.put("interpret","vt.（使）蒸发；（使）气化；vi.说大话，自吹自擂");
        db.insert("newword",null,values);
        Cursor cursor = db.query("newword", null, null, null, null, null, null);
        if (cursor.moveToFirst()) {
            do {
                String word = cursor.getString(cursor.getColumnIndex("word"));
                String interpret = cursor.getString(cursor.getColumnIndex("interpret"));
                String item=word+" "+interpret;
                data.add(item);
                /*
                if(size<200){
                    data[size++]=item;
                }*/
                Log.d("NewwordActivity", word);
                Log.d("NewwordActivity", interpret);
            }while(cursor.moveToNext());
        }
        cursor.close();

    }
}

