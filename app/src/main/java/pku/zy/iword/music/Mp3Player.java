package pku.zy.iword.music;

import android.content.Context;
import android.media.MediaPlayer;
import android.net.Uri;

import java.io.InputStream;

import pku.zy.iword.Model.WordValue;
import pku.zy.iword.utils.Dict;
import pku.zy.iword.utils.FileUtils;
import pku.zy.iword.utils.HttpUtil;

public class Mp3Player {
    public final static String MUSIC_ENG_RELATIVE_PATH="yueci/sounds/sounds_EN/";
    public final static String MUSIC_USA_RELATIVE_PATH="yueci/sounds/sounds_US/";
    public final static int ENGLISH_ACCENT=0;
    public final static int USA_ACCENT=1;

    public Context context=null;
    public String tableName=null;
    public MediaPlayer mediaPlayer=null;
    FileUtils fileU=null;
    Dict dict=null;
    public  boolean isMusicPermitted=true;     //用于对是否播放音乐进行保护性设置，当该变量为false时，可以阻止一次音乐播放

    public Mp3Player(Context context,String tableName){
        this.context=context;
        this.tableName=tableName;
        fileU=new FileUtils();
        dict=new Dict(context,tableName);
        isMusicPermitted=true;

    }
    /**
     * 首先先看一下SD卡上有没有，若有则播放，没有执行下一步
     * 看一下dict表中有没有单词的记录，若有，看一下发音字段是不是有美式发音或英式发音，若无则退出
     * 若没有字段记录，访问网络下载Mp3然后播放
     * 一个Activity中一般只能有一个Voice成员变量，对应的也就只有一个MediaPlayer对象，这样才能对播放
     * 状态进行有效控制
     * 该方法原则上只能在线程中调用
     * @param word
     * @param accent
     */
    /*
    这个方法的基本逻辑：
根据接受的单词word，先看“词典”（数据库）中有没有该单词记录，若没有，先调用dict的getWordFromInternet()获取单词信息存储进数据库“词典”中，
然后根据对应的口音accent，获得prone或prona的地址，然后访问网络，讲Mp3下载到本地，然后调用MediaPlayer对象播放音乐。

需要注意的几点：
　　形参中isAllowedToUseInternet 是用于控制是否允许访问网络，添加这个控制项是因为，
    我们在播放音乐时有时候不想让该方法访问网络，因为过多的县城访问网络可能会造成同时下载同一个音频，会造成线程阻塞或者下载的文件数据损坏。
    isPlayeRightNow 是用于控制是否立即播放该MP3,因为在之后实现背单词功能时有时只需要把MP3文件下载下来，
    而并不需要立刻播放，例如背单词时给你中文意思和英文选项，此时就不能立即播放音乐，否则就知道选项是什么了。
   注意到还有一个isMusicPermitted 参量这是个静态成员变量，这个参量是用来阻止一次播放的，比如我们背单词时，
   突然不想背了退出，此时可能单词的音频正好要播放，这时需要把isMusicPermitted置为false，否则会出现退出后仍播放单词发音的现象。
     */
    public void playMusicByWord(String word , int accent,boolean isAllowedToUseInternet, boolean isPlayRightNow){
        if(word==null || word.length()<=0)
            return;
        char[] wordArray=word.toCharArray();
        char initialCharacter=wordArray[0];

        String path=null;
        String pronUrl=null;
        WordValue w=null;

        if(accent==ENGLISH_ACCENT){
            path=MUSIC_ENG_RELATIVE_PATH;
        }else{
            path=MUSIC_USA_RELATIVE_PATH;
        }

        if(fileU.isFileExist(path+initialCharacter+"/","-$-"+word+".mp3")==false){
            if(isAllowedToUseInternet==false)
                return;
            //为了避免多次多个线程同时访问网络下载同一个文件，这里加了这么一个控制变量

            if(dict.isWordExist(word)==false){  //数据库中没有单词记录，从网络上进行同步
                if((w=dict.getWordFromInternet(word))==null){
                    return;
                }
                dict.insertWordToDict(w, true);
            }//能走到这一步说明从网上同步成功，数据库中一定存在单词记录

            if(accent==ENGLISH_ACCENT){
                pronUrl=dict.getPronEngUrl(word);
            }else{
                pronUrl=dict.getPronUSAUrl(word);
            }
            if(pronUrl==null ||pronUrl=="null"||pronUrl.length()<=0)
                return;    //这说明网络上也没有对应发音，故退出
            //得到了Mp3地址后下载到文件夹中然后进行播放

            InputStream in=null;
            in = HttpUtil.getInputStreamByUrl(pronUrl);
            if(in==null)
                return;
            if(fileU.saveInputStreamToFile(in, path+initialCharacter+"/","-$-"+word+".mp3")==false)
                return;
        }
        //走到这里说明文件夹里一定有响应的音乐文件，故在这里播放
        if(isPlayRightNow==false)
            return;

        /**
         * 这个方法存在缺点，可能因为同时new 了多个MediaPlayer对象，导致start方法失效，
         * 因此解决的方法是，使用同一个MediaPlayer对象，若一次播放时发现对象非空，那么先
         * 调用release()方法释放资源，再重新create
         */


        if(isMusicPermitted==false){
            return;
        }


        try{
            if(mediaPlayer!=null){
                if(mediaPlayer.isPlaying())
                    mediaPlayer.stop();
                mediaPlayer.release();
                mediaPlayer=null;     //为了防止mediaPlayer多次调用stop release，这里置空还是有必要
            }
            mediaPlayer=MediaPlayer.create(context, Uri.parse("file://"+fileU.getSDRootPath()
                    +path+initialCharacter+"/-$-"+word+".mp3"));
            mediaPlayer.start();

        }catch(Exception e){
            mediaPlayer.release();
            e.printStackTrace();
        }

    }

}

