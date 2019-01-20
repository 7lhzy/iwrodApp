package pku.zy.iword.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class OldWordDB extends SQLiteOpenHelper {
    public static final String CREATE_NewWord="create table oldword (word text,interpret text)";
    private Context mContext;
    public OldWordDB(Context context, String name, SQLiteDatabase.CursorFactory factory, int version){
        super(context,name,factory,version);
    }
    public void onCreate(SQLiteDatabase db){
        db.execSQL(CREATE_NewWord);

    }
    public void onUpgrade(SQLiteDatabase db,int oldVersion,int newVersion){

    }


}