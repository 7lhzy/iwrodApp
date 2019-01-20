package pku.zy.iword.ui;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import org.w3c.dom.Text;

import pku.zy.iword.R;

public class MineActivity  extends Activity implements View.OnClickListener{
    private TextView mNewBtn;
    private TextView mWordBtn;
    private TextView mSetBtn;
    private TextView mExitBtn;
    private TextView mLoginBtn;
    private ImageView mUserPic;
    private String username="";

    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mine);
        mLoginBtn=(TextView)findViewById(R.id.login);
        mLoginBtn.setOnClickListener(this);
        mUserPic=(ImageView)findViewById(R.id.pic_user);
        mNewBtn=(TextView)findViewById(R.id.text_new);
        mNewBtn.setOnClickListener(this);
        mWordBtn=(TextView)findViewById(R.id.text_myword);
        mWordBtn.setOnClickListener(this);
        mSetBtn=(TextView)findViewById(R.id.text_myset);
        mSetBtn.setOnClickListener(this);
        mExitBtn=(TextView)findViewById(R.id.text_exit);
        mExitBtn.setOnClickListener(this);

    }



    public void onClick(View view){
        if(view.getId()==R.id.login&&username==""){//还没登录跳转登录界面
            Intent intent=new Intent(MineActivity.this,LoginActivity.class);
            startActivityForResult(intent,1);
        }
        if(view.getId()==R.id.text_new){
            Intent intent = new Intent(MineActivity.this, NewWordActivity.class);
            startActivity(intent);
        }
        if(view.getId()==R.id.text_myword){
            Intent intent = new Intent(MineActivity.this, OldWordActivity.class);
            startActivity(intent);
        }
        if(view.getId()==R.id.text_myset){
            startActivity(new Intent(MineActivity.this, SettingActivity.class));
        }
        if(view.getId()==R.id.text_exit){
            AlertDialog.Builder builder=new AlertDialog.Builder(MineActivity.this);
            builder.setTitle("提醒");
            builder.setMessage("您确定退出当前账户吗？");
            builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                }
            });
            builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    mLoginBtn.setText("点击登录");//不显示用户名
                }
            });
            AlertDialog dialog=builder.create();
            dialog.show();


            /*回到桌面
            Intent i = new Intent(Intent.ACTION_MAIN);
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            i.addCategory(Intent.CATEGORY_HOME);
            startActivity(i);*/
        }
    }

    protected void onActivityResult(int requestCode,int resultCode,Intent data){
        switch(requestCode){
            case 1:
                if(resultCode==RESULT_OK){
                    String username="";
                    username=data.getStringExtra("user_name");
                    if(username!=""){
                        mLoginBtn.setText(username);//登录完成跳转显示用户姓名,确保有值传入
                        mUserPic.setImageDrawable(getResources().getDrawable(R.drawable.userpic));
                    }
                }
        }
    }
    /*
    @Override
    protected void onMenuItemClicked(int position, Item item) {
        String title = item.mTitle;
        if (title.equals("分享")) {

        } else if (title.equals("关于")) {
            Intent intent = new Intent(MainActivity.this, AboutActivity.class);
            startActivity(intent);
        } else if (title.equals("已背单词")) {
            Intent intent = new Intent(MainActivity.this, WordListActivity.class);
            intent.putExtra("title", "已背单词");
            startActivity(intent);
        } else if (title.equals("我的收藏")) {
            Intent intent = new Intent(MainActivity.this, WordListActivity.class);
            intent.putExtra("title", "我的收藏");
            startActivity(intent);
        } else if (title.equals("设置")) {
            startActivity(new Intent(MainActivity.this, SettingActivity.class));
        } else if (title.equals(mUserName) || title.equals("注册/登录")) {
            updataUserName();
            ToastUtils.showLong("更改之后，侧滑菜单项目将在下一次启动后更新^ ^");
        } else if (title.equals("退出")) {
            //mMenuDrawer.closeMenu();
            //回到桌面
            Intent i = new Intent(Intent.ACTION_MAIN);
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            i.addCategory(Intent.CATEGORY_HOME);
            startActivity(i);
        } else {
            Toast.makeText(getApplicationContext(), item.mTitle + " 敬请期待^ ^", Toast.LENGTH_SHORT).show();
        }
    }*/
    public void onClickShare() {
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("text/plain");
        intent.putExtra(Intent.EXTRA_SUBJECT, "分享");
        //intent.putExtra(Intent.EXTRA_TEXT, getString(R.string.share_text));
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(Intent.createChooser(intent, getTitle()));
    }

}