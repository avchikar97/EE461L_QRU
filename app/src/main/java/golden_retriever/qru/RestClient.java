package golden_retriever.qru;

import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;

/**
 * Created by daniel on 3/19/18.
 */

public class RestClient {

    public static final String TAG = "RestClient";

    public RestClient(){}

    public void getIT(final JSONObject query){
        AsyncTask.execute(new Runnable() {
            @Override   
            public void run() {
                // All your networking logic
                // should be here
                String myUrl = "http://10.0.2.2:8080/route1";
                try{
                    URL url = new URL(myUrl);
                    HttpURLConnection httpURLConnection=(HttpURLConnection)url.openConnection();
                    httpURLConnection.setRequestMethod("POST");

                    /*httpURLConnection.setDoOutput(true);
                    httpURLConnection.setDoInput(true);*/

                    httpURLConnection.setRequestProperty("User-Agent","Mozilla/5.0 ( compatible ) ");
                    httpURLConnection.setRequestProperty("Accept","*/*");

                    //httpURLConnection.setDoInput(true);
                    //httpURLConnection.setDoOutput(true);

                    DataOutputStream writer = new DataOutputStream(httpURLConnection.getOutputStream());
                    /*JSONObject query = new JSONObject();
                    try {
                        query.put("firstName", "Daniel");
                    } catch(JSONException e) {
                        e.printStackTrace();
                    }*/
                    //String post_data = query.toString();
                    //Document query1 = new Document("address", "Highway 37");
                    //post_data = post_data.replace("\"", "'");
                    //post_data = post_data.replace("{", "");
                    //post_data = post_data.replace("}", "");
                    //post_data = "{ \"address\": \"Highway 37\" }";
                    writer.writeBytes(query.toString());
                    writer.flush();
                    writer.close();

                    InputStream inputStream = httpURLConnection.getInputStream();
                    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream,"iso-8859-1"));
                    String result="";
                    String line="";
                    while((line = bufferedReader.readLine())!= null)
                        result += line;
                    bufferedReader.close();
                    inputStream.close();
                    httpURLConnection.disconnect();
                    Log.d(TAG, result);
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public void postIT(final JSONObject insert){
        AsyncTask.execute(new Runnable() {
            @Override
            public void run() {
                // All your networking logic
                // should be here
                String myUrl = "http://10.0.2.2:8080/route2";
                try{
                    URL url = new URL(myUrl);
                    HttpURLConnection httpURLConnection=(HttpURLConnection)url.openConnection();
                    httpURLConnection.setRequestMethod("POST");

                    /*httpURLConnection.setDoOutput(true);
                    httpURLConnection.setDoInput(true);*/

                    httpURLConnection.setRequestProperty("User-Agent","Mozilla/5.0 ( compatible ) ");
                    httpURLConnection.setRequestProperty("Accept","*/*");

                    //httpURLConnection.setDoInput(true);
                    //httpURLConnection.setDoOutput(true);

                    DataOutputStream writer = new DataOutputStream(httpURLConnection.getOutputStream());
                    //Document query = new Document("address", "Highway 37");
                    writer.writeBytes(insert.toString());
                    writer.flush();
                    writer.close();

                    InputStream inputStream = httpURLConnection.getInputStream();
                    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream,"iso-8859-1"));
                    String result="";
                    String line="";
                    while((line = bufferedReader.readLine())!= null)
                        result += line;
                    bufferedReader.close();
                    inputStream.close();
                    httpURLConnection.disconnect();
                    Log.d(TAG, result);
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        });
    }
}
