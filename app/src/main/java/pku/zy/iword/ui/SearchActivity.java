package pku.zy.iword.ui;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;

import pku.zy.iword.Model.WordValue;
import pku.zy.iword.R;
import pku.zy.iword.adapter.SearchSentenceListAdapter;
import pku.zy.iword.database.DataBaseHelperDict;
import pku.zy.iword.database.NewWordDB;
import pku.zy.iword.database.OldWordDB;
import pku.zy.iword.music.Mp3Player;
import pku.zy.iword.utils.Dict;

public class SearchActivity extends Activity  implements View.OnClickListener {
    private EditText textEditWord=null;
    private TextView textDictWord=null;
    private TextView textDictPhonSymbolEng=null;
    private TextView textDictPhonSymbolUSA=null;
    private TextView textDictInterpret=null;
    private TextView textViewDictSentence=null;
    private ImageButton imageBtnDictAddToWordList=null;
    private ImageButton imageBtnDictHornEng=null;
    private ImageButton imageBtnDictHornUSA=null;
    private ImageButton imageBtnDictSerach=null;
    private ImageButton imageBtnDictBackToGeneral=null;
    private ImageButton imageBtnDictDelteEditText=null;

    private Button buttonDictDialogConfirm=null;
    private Button buttonDictDialogCancel=null;



    private Dict dict;

    private WordValue w = new WordValue();

    private String searchWord;

    private DataBaseHelperDict dbGlossaryHelper=null;
    private SQLiteDatabase dbGlossaryR=null;
    private SQLiteDatabase dbGlossaryW=null;
    private NewWordDB nwDB=new NewWordDB(this, "NewWord.db", null, 1);


    private Mp3Player mp3Box=null;

    private Handler dictHandler=null;


    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        //nwDB = new NewWordDB(this, "NewWord.db", null, 1);
        initial();//初始化控件
        imageBtnDictBackToGeneral.setOnClickListener(this);
        imageBtnDictSerach.setOnClickListener(this);
        imageBtnDictDelteEditText.setOnClickListener(this);
        imageBtnDictAddToWordList.setOnClickListener(this);
        //sendRequestWithHttpURLConnection(showWord);


    }
    private void initial() {
        textEditWord=(EditText)findViewById(R.id.edit_text_dict);//搜索的文本框
        textDictWord = (TextView) findViewById(R.id.text_dict_word);//查询的单词
        textDictInterpret = (TextView) findViewById(R.id.text_dict_interpret);//查询单词的翻译
        textDictPhonSymbolEng = (TextView) findViewById(R.id.text_dict_phosymbol_eng);//查询单词的英音音标
        textDictPhonSymbolUSA = (TextView) findViewById(R.id.text_dict_phosymbol_usa);//查询单词的美音音标
        textViewDictSentence = (TextView) findViewById(R.id.textview_dict_sentence);//查询单词的例句

        imageBtnDictAddToWordList = (ImageButton) findViewById(R.id.image_btn_dict_add_to_wordlist);//将查询单词加入生词本
        imageBtnDictHornEng = (ImageButton) findViewById(R.id.image_btn_dict_horn_accent_eng);//查询单词的英音发音
        imageBtnDictHornUSA = (ImageButton) findViewById(R.id.image_btn_dict_horn_accent_usa);//查询单词的美音发音
        imageBtnDictBackToGeneral = (ImageButton)findViewById(R.id.image_btn_dict_back_to_general); //返回
        imageBtnDictSerach = (ImageButton)findViewById(R.id.image_btn_dict_search);//搜索
        imageBtnDictDelteEditText= (ImageButton)findViewById(R.id.image_btn_dict_delete_all);//删除



        dict = new Dict(SearchActivity.this, "dict");
        mp3Box = new Mp3Player(SearchActivity.this, "dict");
        dbGlossaryHelper = new DataBaseHelperDict(SearchActivity.this, "glossary");
        dbGlossaryR = dbGlossaryHelper.getReadableDatabase();
        dbGlossaryW = dbGlossaryHelper.getWritableDatabase();


        dictHandler = new Handler(Looper.getMainLooper());
        String str="The capitain captain ordered passengers and crew to abandon abandon ship about an hour later.  " +"\n"+
                "船长在一个小时候命令乘客和船员弃船." +"\n"+
                " But exceeding desire is not happy, however ego abandon, abandon oneself to vice. " +"\n"+
                "但过度的欲望不是幸福, 而是自我放纵, 自甘堕落. ";

        //设置点击详情的文本
        textDictWord.setText("abandon");
        textDictInterpret.setText("vt.放弃；丢弃；n.放任");
        textDictPhonSymbolEng.setText("əˈbændən");
        textDictPhonSymbolUSA.setText("əˈbændən");
        textViewDictSentence.setText(str);
    }
    public void onClick(View view) {
       if(view.getId()==R.id.image_btn_dict_back_to_general){//返回主界面
           Intent intent = new Intent();
           intent.setClass(SearchActivity.this,MainActivity.class);
           SearchActivity.this.startActivity(intent);
       }
       if(view.getId()==R.id.edit_text_dict){
           //searchWord=textEditWord.getText().toString();

       }
       if(view.getId()==R.id.image_btn_dict_delete_all){
           textEditWord.setText("");


       }
       if(view.getId()==R.id.image_btn_dict_search){//搜索框搜索进行单词查询
           searchWord=textEditWord.getText().toString();
           Log.d("searchword", searchWord);
           assign();
           Log.d("searchword",w.word);
           Log.d("searchword",w.interpret);
           showWord();

       }
       if(view.getId()==R.id.image_btn_dict_add_to_wordlist){
           SQLiteDatabase db = nwDB.getWritableDatabase();
           ContentValues values = new ContentValues();
           //values.put("word", w.getWord());
           //values.put("interpret", w.getInterpret());
           if(!w.word.equals("")){
               values.put("word", w.word);
               values.put("interpret", w.interpret);
               db.insert("newword", null, values);
               Toast.makeText(SearchActivity.this, "加入生词本成功", Toast.LENGTH_SHORT).show();
           }
       }
    }
    protected void onActivityResult(int requestCode,int resultCode,Intent data){//响应生词本以及已背单词里的listview里的item的单击，显示单词详情
        switch(requestCode){
            case 10:
                if(resultCode==RESULT_OK){
                    searchWord=data.getStringExtra("word");
                    if(searchWord!=""){
                        Log.d("searchword", searchWord);

                        Log.d("searchword",w.word);
                        Log.d("searchword",w.interpret);
                        showWord();
                    }
                }
        }
    }

    /*public void setOnClickLis() {
        //imageBtnDictBackToGeneral.setOnClickListener(new IBDictBackToGeneralClickLis() );
        imageBtnDictHornEng.setOnClickListener(new IBDictPlayMusicByAccentClickLis(Mp3Player.ENGLISH_ACCENT));
        imageBtnDictHornUSA.setOnClickListener(new IBDictPlayMusicByAccentClickLis(Mp3Player.USA_ACCENT));

    }*/

    private void sendRequestWithHttpURLConnection(String word) {
        final String url1 = "http://dict-co.iciba.com/api/dictionary.php?w=";
        final String url2 = "&key=666360419CA80D1BBC86083B666E7214";
        final String address = url1 + word + url2;
        new Thread(new Runnable() {
            @Override
            public void run() {
                HttpURLConnection con = null;
                BufferedReader reader = null;
                try {
                    URL url = new URL(address);
                    con = (HttpURLConnection) url.openConnection();
                    con.setRequestMethod("GET");
                    con.setConnectTimeout(8000);
                    con.setReadTimeout(8000);
                    InputStream in = con.getInputStream();
                    reader = new BufferedReader(new InputStreamReader(in));
                    StringBuilder response = new StringBuilder();
                    String line;
                    while ((line = reader.readLine()) != null) {
                        response.append(line);
                    }
                    parseXML(response.toString());
                    if (w.getWord().length() > 0) {//成功解析后
                        showWord(w);
                    }else{
                        Log.d("xml","解析错误");
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    if (reader != null) {
                        try {
                            reader.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                    if (con != null) {
                        con.disconnect();
                    }
                }
            }
        }).start();
    }

    private void parseXML(String xmlData) {
        try {
            XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
            XmlPullParser xmlPullParser = factory.newPullParser();
            xmlPullParser.setInput(new StringReader(xmlData));
            int eventType = xmlPullParser.getEventType();
            String interpret = "";
            String sent = "";
            while (eventType != XmlPullParser.END_DOCUMENT) {
                String startname = xmlPullParser.getName();
                switch (eventType) {
                    case XmlPullParser.START_TAG: {
                        if (startname.equals("dict")) {
                            String id = xmlPullParser.getAttributeValue(0);
                            Log.i("showword", id);
                        } else if (startname.equals("key")) {
                            String word = xmlPullParser.nextText();
                            w.setWord(word);
                            Log.d("showword", word);
                        } else if (startname.equals("ps")) {
                            if (w.getPsE().length() <= 0) {
                                String PsE = xmlPullParser.nextText();
                                w.setPsE(PsE);
                                Log.d("showword", PsE);
                            } else {
                                String PsA = xmlPullParser.nextText();
                                w.setPsE(PsA);
                                Log.d("showword", PsA);
                            }
                        } else if (startname.equals("pron")) {
                            if (w.getPronE().length() <= 0) {
                                String PronE = xmlPullParser.nextText();
                                w.setPronE(PronE);
                                Log.d("showword", PronE);
                            } else {
                                String PronA = xmlPullParser.nextText();
                                w.setPronE(PronA);
                                Log.d("showword", PronA);
                            }
                        } else if (startname.equals("pos")) {
                            String pos = xmlPullParser.nextText();
                            interpret = interpret + pos;
                        } else if (startname.equals("acceptation")) {
                            String acceptation = xmlPullParser.nextText();
                            interpret = interpret + acceptation;
                        } else if (startname.equals("orig")) {
                            String orig = xmlPullParser.nextText();
                            sent = sent + orig + '\n';
                        } else if (startname.equals("trans")) {
                            String trans = xmlPullParser.nextText();
                            sent = sent + trans + '\n';
                        }
                        w.setInterpret(interpret);
                        w.setSent(sent);
                        Log.d("showword", interpret);
                        Log.d("showword", sent);
                        break;
                    }
                    default:
                        break;

                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }



*/


    private void showWord() {
        /*runOnUiThread(new Runnable() {
            @Override
            public void run() {
                textDictWord.setText(wordValue.getWord());
                textDictPhonSymbolEng.setText(wordValue.getPsE());
                textDictPhonSymbolUSA.setText(wordValue.getPsA());
                textDictInterpret.setText(wordValue.getInterpret());
                textViewDictSentence.setText(wordValue.getSent());
            }
        });*/
        textDictWord.setText(w.word);
        textDictPhonSymbolEng.setText(w.psE);
        textDictPhonSymbolUSA.setText(w.psA);
        textDictInterpret.setText(w.interpret);
        textViewDictSentence.setText(w.sent);

        /*
        if (wordValue.getPsA().equals("") == false && wordValue.getPsE().equals("") == false) {    //只有有音标时才去下载音乐

            mp3Box.playMusicByWord(showWord, Mp3Player.ENGLISH_ACCENT, true, false);
            mp3Box.playMusicByWord(showWord, Mp3Player.USA_ACCENT, true, false);

        }

    }


    @Override
    protected void onStart() {
        // TODO Auto-generated method stub
        super.onStart();
        mp3Box.isMusicPermitted = true;
    }


    @Override
    protected void onPause() {
        // TODO Auto-generated method stub
        mp3Box.isMusicPermitted = false;
        super.onPause();
    }

    class IBDictPlayMusicByAccentClickLis implements View.OnClickListener {

        public int accentTemp = 0;

        public IBDictPlayMusicByAccentClickLis(int accentTemp) {
            super();
            this.accentTemp = accentTemp;
        }

        @Override
        public void onClick(View arg0) {
            // TODO Auto-generated method stub

            mp3Box.playMusicByWord(showWord, accentTemp, false, true);

        }

    }*/
    }

}

