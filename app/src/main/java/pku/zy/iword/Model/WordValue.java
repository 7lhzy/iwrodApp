package pku.zy.iword.Model;

import java.io.BufferedReader;
import java.io.StringReader;
import java.util.ArrayList;

public class WordValue {
    public String word,psE,pronE,psA,pronA,
            interpret,sentOrig,sentTrans,sent;





    public WordValue(String word, String psE, String pronE, String psA,
                     String pronA, String interpret, String sentOrig, String sentTrans,String sent) {
        super();
        this.word = ""+word;//单词本身
        this.psE = ""+psE;//英式音标
        this.pronE = ""+pronE;//英式发音
        this.psA = ""+psA;//美式音标
        this.pronA = ""+pronA;//美式发音
        this.interpret = ""+interpret;//单词的词性和词义
        this.sentOrig = ""+sentOrig;//例句原句
        this.sentTrans = ""+sentTrans;//例句翻译
        this.sent=""+sent;
    }

    public WordValue() {
        super();
        this.word = "";      //防止空指针异常
        this.psE = "";
        this.pronE = "";
        this.psA = "";
        this.pronA = "";
        this.interpret = "";
        this.sentOrig = "";
        this.sentTrans = "";
        this.sent="";


    }

    public ArrayList<String> getOrigList(){
        ArrayList<String> list=new ArrayList<String>();
        BufferedReader br=new BufferedReader(new StringReader(this.sentOrig));
        String str=null;
        try{
            while((str=br.readLine())!=null){
                list.add(str);
            }
        }catch(Exception e){
            e.printStackTrace();
        }
        return list;
    }

    public ArrayList<String> getTransList(){
        ArrayList<String> list=new ArrayList<String>();
        BufferedReader br=new BufferedReader(new StringReader(this.sentTrans));
        String str=null;
        try{
            while((str=br.readLine())!=null){
                list.add(str);
            }
        }catch(Exception e){
            e.printStackTrace();
        }
        return list;
    }


    public String getWord() {//可能原来的有地方有同名方法
        return word;
    }

    public void setWord(String word) {
        this.word = word;
    }

    public String getPsE() {
        return psE;
    }

    public void setPsE(String psE) {
        this.psE = psE;
    }

   public String getPronE() {
        return pronE;
    }

    public void setPronE(String pronE) {
        this.pronE = pronE;
    }

    public String getPsA() {
        return psA;
    }

    public void setPsA(String psA) {
        this.psA = psA;
    }

    public String getPronA() {
        return pronA;
    }

    public void setPronA(String pronA) {
        this.pronA = pronA;
    }

    public String getInterpret() {
        return interpret;
    }

    public void setInterpret(String interpret) {
        this.interpret = interpret;
    }

    public String getSentOrig() {
        return sentOrig;
    }

    public void setSentOrig(String sentOrig) {
        this.sentOrig = sentOrig;
    }

    public String getSentTrans() {
        return sentTrans;
    }

    public void setSentTrans(String sentTrans) {
        this.sentTrans = sentTrans;
    }

    public String getSent() {return sent;}
    public void setSent(String sent) {this.sent=sent;}

    public void printInfo(){
        System.out.println(this.word);
        System.out.println(this.psE);
        System.out.println(this.pronE);
        System.out.println(this.psA);
        System.out.println(this.pronA);
        System.out.println(this.interpret);
        System.out.println(this.sentOrig);
        System.out.println(this.sentTrans);

    }


}
