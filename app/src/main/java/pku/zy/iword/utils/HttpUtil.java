package pku.zy.iword.utils;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class HttpUtil {
    public final static String url1="http://dict-co.iciba.com/api/dictionary.php?w=";
    public final static String url2="&key=666360419CA80D1BBC86083B666E7214";
    public static InputStream getInputStreamByUrl(final String address) {
                InputStream tempInput = null;

                HttpURLConnection connection = null;
                try {
                    URL url = new URL(address);
                    connection = (HttpURLConnection) url.openConnection();
                    connection.setRequestMethod("GET");
                    connection.setConnectTimeout(8000);
                    connection.setReadTimeout(8000);
                    InputStream Input = connection.getInputStream();


                } catch (IOException e) {
                    e.printStackTrace();

                }
                return tempInput;
            }


    }

