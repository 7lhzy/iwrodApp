package pku.zy.iword.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

public class NewWordDB extends SQLiteOpenHelper{
    public static final String CREATE_NewWord="create table newword (word text,interpret text)";
    private Context mContext;
    public NewWordDB(Context context,String name,SQLiteDatabase.CursorFactory factory,int version){
        super(context,name,factory,version);
    }
    public void onCreate(SQLiteDatabase db){
        db.execSQL(CREATE_NewWord);//执行建表的SQL语句

    }
    public void onUpgrade(SQLiteDatabase db,int oldVersion,int newVersion){

    }

}
