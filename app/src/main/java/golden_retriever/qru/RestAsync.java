package golden_retriever.qru;

import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import golden_retriever.qru.Candidate_Profile;

/**
 * Created by daniel on 4/20/18.
 */

public class RestAsync extends AsyncTask<JSONObject, Void, JSONObject> {
    public static final String TAG = "RestClient";

    private String type;
    private String id;

    public AsyncResponse delegate = null;

    public RestAsync(AsyncResponse delegate){
        this.delegate = delegate;
    }

    public void setType(String a){
        type = a.toUpperCase();
    }

    public void setId(String b){
        id = b;
    }

    protected JSONObject doInBackground(JSONObject... params){
        JSONObject yeah = params[0];
        JSONObject hold = null;
        if(type.equals("GET")){
            hold = getIT(yeah);
        } else if(type.equals("POST")){
            hold = postIT(yeah);
        } else if(type.equals("UPDATE")){
            hold = updateIT(yeah, id);
        }

        return hold;
    }

    @Override
    protected void onPostExecute(JSONObject hold){
        Log.d(TAG, "Entered onPostExecute");
        delegate.processFinish(hold);
    }

    public JSONObject getIT(final JSONObject query){
        // All your networking logic
        // should be here
        String myUrl = "http://35.194.32.181:8080/route1";
        JSONObject theResult = null;
        try{
            URL url = new URL(myUrl);
            HttpURLConnection httpURLConnection=(HttpURLConnection)url.openConnection();
            httpURLConnection.setRequestMethod("POST");

            httpURLConnection.setRequestProperty("User-Agent","Mozilla/5.0 ( compatible ) ");
            httpURLConnection.setRequestProperty("Accept","*/*");

            DataOutputStream writer = new DataOutputStream(httpURLConnection.getOutputStream());

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

            try{
                JSONArray yeah = new JSONArray(result);
                Log.d(TAG, yeah.toString());
                Log.d(TAG, "yeah length: " + yeah.length());
                if(yeah.length() != 0) {
                    theResult = yeah.getJSONObject(0);
                    Log.d(TAG, theResult.toString());
                } else {
                    theResult = new JSONObject();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return theResult;
    }

    public JSONObject postIT(final JSONObject insert){
        // All your networking logic
        // should be here
        String myUrl = "http://35.194.32.181:8080/route2";
        JSONObject theResult = null;
        try{
            URL url = new URL(myUrl);
            HttpURLConnection httpURLConnection=(HttpURLConnection)url.openConnection();
            httpURLConnection.setRequestMethod("POST");

            httpURLConnection.setRequestProperty("User-Agent","Mozilla/5.0 ( compatible ) ");
            httpURLConnection.setRequestProperty("Accept","*/*");

            DataOutputStream writer = new DataOutputStream(httpURLConnection.getOutputStream());
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
            try{
                theResult = new JSONObject();
                theResult.put("Message", result);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }



        return theResult;
    }

    public JSONObject updateIT(final JSONObject insert, final String id){
        // All your networking logic
        // should be here
        String myUrl = "http://35.194.32.181:8080/route3/";
        myUrl = myUrl + id;
        JSONObject theResult = null;
        try{
            URL url = new URL(myUrl);
            HttpURLConnection httpURLConnection=(HttpURLConnection)url.openConnection();
            httpURLConnection.setRequestMethod("POST");

            httpURLConnection.setRequestProperty("User-Agent","Mozilla/5.0 ( compatible ) ");
            httpURLConnection.setRequestProperty("Accept","*/*");

            DataOutputStream writer = new DataOutputStream(httpURLConnection.getOutputStream());
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
            try{
                JSONArray yeah = new JSONArray(result);
                Log.d(TAG, yeah.toString());
                theResult = yeah.getJSONObject(0);
                Log.d(TAG, theResult.toString());
            } catch (JSONException e) {
                e.printStackTrace();
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return theResult;
    }
}
