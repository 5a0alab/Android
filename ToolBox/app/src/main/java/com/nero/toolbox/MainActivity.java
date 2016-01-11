package com.nero.toolbox;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity {
    public static String TAG=MainActivity.class.getSimpleName();
    public static boolean D=true;

    private static String msg="";
    private Button run;
    private TextView ans;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        run=(Button)findViewById(R.id.button);
        ans=(TextView)findViewById(R.id.textView);
        run.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                How2makeHttpRequest();
            }
        });
    }
    /**
     * 實現用Android對MySQL執行SQL指令
     */
    public void How2SQLExcute(){
        new SQLExcute().execute();
    }
    /**
     * 實現用Android發起HTTPRequest取的資料
     * 提供POST、GET請求方式
     * JSON、DIRECT直接回傳完整html內容方式
     */
    public void How2makeHttpRequest(){
        new makeHttpRequest().execute();
    }
    /**
     *
     */
    public class makeHttpRequest extends AsyncTask<String, Integer, String> {
        protected String doInBackground(String... params) {
            String var="";
            String RequestUrl="http://opendata.epa.gov.tw/ws/Data/REWXQA/?$select=SiteName,County,PM2.5,PublishTime&$orderby=SiteName&$skip=0&$top=1000&format=json&sort=County";
            msg= HttpUtility.makeHttpRequest(var,RequestUrl, HttpUtility.Request.GET, HttpUtility.Response.DIRECT);
            return "Execute";
        }
        protected void onPostExecute(String result) {
            List<Object> returnList = new ArrayList<Object>();
            try {
                returnList=JSONParser.getListFromJsonStr(msg);
                for (int i = 0; i < returnList.size(); i++) {
                    Map<String, String> temp = new HashMap<String, String>();
                    temp=(Map<String, String>) returnList.get(i);
                    String t="";
                    t="地點:"+temp.get("SiteName")+" 縣市:"+temp.get("County")+" PM2.5:"+temp.get("PM2.5")+" 測量時間:"+temp.get("PublishTime")+" ";
                    if(D)Log.i(TAG, "t:" + t);
                }
            } catch (JSONException e) {e.printStackTrace();}

        }
        public void onPreExecute() {}
        protected void onProgressUpdate(Integer... progress) {}
    }
    /**
     *
     */
    public class SQLExcute extends AsyncTask<String, Integer, String> {
        protected String doInBackground(String... params) {
            String sql="SELECT * FROM `Users`";
            msg=HttpUtility.SQLExcute(Config.host,Config.path,Config.database,sql).toString();
            if(D) Log.i(TAG, "Response:" + msg);
//            String var="db="+Config.database+",sql="+sql+",mode=4";
//            String RequestUrl="http://123.59.68.12/guaTest/GATE.php";
//            msg= HttpUtility.makeHttpRequest(var, RequestUrl, HttpUtility.Request.POST, HttpUtility.Response.JSON);
            return "Execute";
        }
        protected void onPostExecute(String result) {
            ans.setText(msg);}
        public void onPreExecute() {}
        protected void onProgressUpdate(Integer... progress) {}
    }
}