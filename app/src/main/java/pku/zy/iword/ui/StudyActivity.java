package pku.zy.iword.ui;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.util.Xml;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.net.HttpURLConnection;
import java.net.URL;


import javax.net.ssl.HttpsURLConnection;

import pku.zy.iword.Model.WordValue;
import pku.zy.iword.R;
import pku.zy.iword.database.DataBaseHelperDict;
import pku.zy.iword.database.NewWordDB;
import pku.zy.iword.database.OldWordDB;
import pku.zy.iword.music.Mp3Player;
import pku.zy.iword.utils.Dict;

public class StudyActivity extends Activity implements View.OnClickListener {
    private static final int SHOW_NOW_WORD = 1;
    private TextView textDictWord = null;
    private TextView textDictPhonSymbolEng = null;
    private TextView textDictPhonSymbolUSA = null;
    private TextView textDictInterpret = null;
    private TextView textViewDictSentence = null;
    private ImageButton imageBtnDictAddToWordList = null;
    private ImageButton imageBtnDictHornEng = null;
    private ImageButton imageBtnDictHornUSA = null;
    private ImageButton imageBtnDictBackToGeneral = null;


    private Button buttonDictDialogConfirm = null;
    private Button buttonDictDialogCancel = null;
    private Button mRemBtn = null;
    private Button mForgetBtn = null;

    private EditText editTextDictSearch = null;

    private Dict dict = null;

    private WordValue w=new WordValue();


    private NewWordDB nwDB=new NewWordDB(this, "NewWord.db", null, 1);
    private OldWordDB owDB=new OldWordDB(this,"OldWord.db",null,1);
    private DataBaseHelperDict dbGlossaryHelper = null;
    private SQLiteDatabase dbGlossaryR = null;
    private SQLiteDatabase dbGlossaryW = null;


    private Mp3Player mp3Box = null;

    private String showWord = null;

    private Handler dictHandler = null;



    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_study);


        initial();//初始化控件
        Log.d("studyword", showWord);
  
        Log.d("studyword",w.word);
        Log.d("studyword",w.interpret);

        mForgetBtn.setOnClickListener(this);
        mRemBtn.setOnClickListener(this);

        showWord();

        sendRequestWithHttpURLConnection(showWord);


    }


    private void initial() {
        textDictWord = (TextView) findViewById(R.id.text_dict_word);
        textDictInterpret = (TextView) findViewById(R.id.text_dict_interpret);
        textDictPhonSymbolEng = (TextView) findViewById(R.id.text_dict_phosymbol_eng);
        textDictPhonSymbolUSA = (TextView) findViewById(R.id.text_dict_phosymbol_usa);
        textViewDictSentence = (TextView) findViewById(R.id.textview_dict_sentence);

        imageBtnDictAddToWordList = (ImageButton) findViewById(R.id.image_btn_dict_add_to_wordlist);
        imageBtnDictHornEng = (ImageButton) findViewById(R.id.image_btn_dict_horn_accent_eng);
        imageBtnDictHornUSA = (ImageButton) findViewById(R.id.image_btn_dict_horn_accent_usa);

        mForgetBtn = (Button) findViewById(R.id.forget);
        mRemBtn = (Button) findViewById(R.id.rem);

        dict = new Dict(StudyActivity.this, "dict");
        mp3Box = new Mp3Player(StudyActivity.this, "dict");
        dbGlossaryHelper = new DataBaseHelperDict(StudyActivity.this, "glossary");
        dbGlossaryR = dbGlossaryHelper.getReadableDatabase();
        dbGlossaryW = dbGlossaryHelper.getWritableDatabase();


        dictHandler = new Handler(Looper.getMainLooper());

        //对searchedWord进行初始化
        Intent intent = getIntent();
        showWord = intent.getStringExtra("word");
        if (showWord == null)
            showWord = "";//
        //设置点击详情的文本
        textDictWord.setText(showWord);
        textDictInterpret.setText("N/A");
        textDictPhonSymbolEng.setText("N/A");
        textDictPhonSymbolUSA.setText("N/A");
        textViewDictSentence.setText("N/A");
        /*
        ArrayList<String> sentList = new ArrayList<String>();
        sentList.add("abc");
        sentList.add("jui");
        ArrayList<String> sentInChineseList = new ArrayList<String>();
        sentInChineseList.add("你好");
        sentInChineseList.add("加油");
        ArrayList<HashMap<String, Object>> list = new ArrayList<HashMap<String, Object>>();
        for (int i = 0; i < 2; i++) {
            //            sb.append(sentList.get(i)+"\n"+sentInChineseList.get(i)+"\n\n");
            HashMap<String, Object> map = new HashMap<String, Object>();
            map.put("sentence", sentList.get(i) + "\n" + sentInChineseList.get(i));
            list.add(map);
        }
        //        textDictSentence.setText(sb.toString());
        SearchSentenceListAdapter adapter = new SearchSentenceListAdapter(StudyActivity.this, R.layout.dict_sentence_list_item, list, new String[]{"sentence"}, new int[]{R.id.text_dict_sentence_list_item});
        listViewDictSentence.setAdapter(adapter);
*/

    }


    /*public void setOnClickLis() {
        //imageBtnDictBackToGeneral.setOnClickListener(new IBDictBackToGeneralClickLis() );
        imageBtnDictHornEng.setOnClickListener(new IBDictPlayMusicByAccentClickLis(Mp3Player.ENGLISH_ACCENT));
        imageBtnDictHornUSA.setOnClickListener(new IBDictPlayMusicByAccentClickLis(Mp3Player.USA_ACCENT));

    }*/



    public void onClick(View view) {
        if (view.getId() == R.id.rem) {//如果记住了当前单词，则跳转主界面继续下一个单词
            SQLiteDatabase db = owDB.getWritableDatabase();
            ContentValues values = new ContentValues();
            //values.put("word", w.getWord());
            //values.put("interpret", w.getInterpret());
            values.put("word", w.word);
            values.put("interpret", w.interpret);
            db.insert("oldword", null, values);
            Toast.makeText(StudyActivity.this, "已经加入已背单词", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent();
            intent.setClass(StudyActivity.this, MainActivity.class);
            StudyActivity.this.startActivity(intent);
        }
        if (view.getId() == R.id.forget) {//如果没有记住单词，加入生词本
            SQLiteDatabase db = nwDB.getWritableDatabase();
            ContentValues values = new ContentValues();
            //values.put("word", w.getWord());
            //values.put("interpret", w.getInterpret());
            values.put("word", w.word);
            values.put("interpret", w.interpret);
            db.insert("newword", null, values);
            Toast.makeText(StudyActivity.this, "加入生词本成功", Toast.LENGTH_SHORT).show();

        }
    }

    private void sendRequestWithHttpURLConnection(String word) {
        final String url1 = "http://dict-co.iciba.com/api/dictionary.php?w=";
        final String url2 = "&key=666360419CA80D1BBC86083B666E7214";
        final String address = url1 + word + url2;//查询一个单词URL
        new Thread(new Runnable() {//用新进程去执行网络连接操作
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
                    parseXML(response.toString());//解析XML文件的函数
                    if (w.getWord().length() > 0) {//成功解析后，w是一个WorldValue类的对象
                        showWord();
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
            String interpret = "";//保存单词的释义：词性+中文含义
            String sent = "";//保存单词的例句：英文原句+中文翻译
            while (eventType != XmlPullParser.END_DOCUMENT) {
                String startname = xmlPullParser.getName();
                switch (eventType) {
                    case XmlPullParser.START_TAG: {
                        if (startname.equals("dict")) {
                            String id = xmlPullParser.getAttributeValue(0);
                            Log.i("showword", id);
                        } else if (startname.equals("key")) {
                            String word = xmlPullParser.nextText();
                            w.setWord(word);//单词本身
                            Log.d("showword", word);
                        } else if (startname.equals("ps")) {
                            if (w.getPsE().length() <= 0) {
                                String PsE = xmlPullParser.nextText();
                                w.setPsE(PsE);//英音音标
                                Log.d("showword", PsE);
                            } else {
                                String PsA = xmlPullParser.nextText();
                                w.setPsE(PsA);//美音音标
                                Log.d("showword", PsA);
                            }
                        } else if (startname.equals("pron")) {
                            if (w.getPronE().length() <= 0) {
                                String PronE = xmlPullParser.nextText();
                                w.setPronE(PronE);//英音发音
                                Log.d("showword", PronE);
                            } else {
                                String PronA = xmlPullParser.nextText();
                                w.setPronE(PronA);//美音发音
                                Log.d("showword", PronA);
                            }
                        } else if (startname.equals("pos")) {//pos标签包含单词的词性
                            String pos = xmlPullParser.nextText();
                            interpret = interpret + pos;
                        } else if (startname.equals("acceptation")) {//acceptation标签包含单词的含义
                            String acceptation = xmlPullParser.nextText();
                            interpret = interpret + acceptation;
                        } else if (startname.equals("orig")) {//orig标签包含英文例句
                            String orig = xmlPullParser.nextText();
                            sent = sent + orig + '\n';
                        } else if (startname.equals("trans")) {//orig标签包含例句翻译
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





    private void showWord() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {//进行UI操作，将结果显示到界面上
                textDictWord.setText(w.getWord());
                textDictPhonSymbolEng.setText(w.getPsE());
                textDictPhonSymbolUSA.setText(w.getPsA());
                textDictInterpret.setText(w.getInterpret());
                textViewDictSentence.setText(w.getSent());
            }
        });
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
