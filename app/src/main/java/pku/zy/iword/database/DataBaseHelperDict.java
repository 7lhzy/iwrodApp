package pku.zy.iword.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DataBaseHelperDict extends SQLiteOpenHelper {
    public Context mContext=null;
    public String tableName=null;
    public static int VERSION=1;
    public DataBaseHelperDict(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
        // TODO Auto-generated constructor stub
        mContext=context;
        tableName=name;
    }

    public DataBaseHelperDict(Context context, String name, SQLiteDatabase.CursorFactory factory){
        this(context,name,factory,VERSION);
        mContext=context;
        tableName=name;
    }

    public DataBaseHelperDict(Context context, String name){
        this(context,name,null);
        mContext=context;
        tableName=name;
    };


    @Override
    public void onCreate(SQLiteDatabase db) {
        // TODO Auto-generated method stub
        db.execSQL("create table dict(word text,pse text,prone text,psa text,prona text," +
                "interpret text, sentorig text, senttrans text)");

    }//其中word 单词,pse 英式音标,prone 英式发音地址,psa 美式音标,prona 美式发音地址，
    // interpret 翻译, sentorig 利用英文, senttrans 例句中文

    @Override
    public void onUpgrade(SQLiteDatabase arg0, int arg1, int arg2) {
        // TODO Auto-generated method stub

    }

}

