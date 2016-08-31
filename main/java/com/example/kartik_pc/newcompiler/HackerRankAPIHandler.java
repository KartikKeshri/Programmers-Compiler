package com.example.kartik_pc.newcompiler;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;


public class HackerRankAPIHandler extends ActionBarActivity {

    String language_selected = "";
    String source_code = "";
    String test_cases = "";
    TextView status;
    TextView memory;
    TextView time;
    TextView output;
    TextView STATUS;
    TextView output_main;
    TextView memory_main;
    TextView time_main;

    RelativeLayout res_out;
    RelativeLayout back_grnd_progress;
    TextView output_result;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hacker_rank_apihandler);
        //display back button
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        res_out = (RelativeLayout) findViewById(R.id.hackerrankapihandler);
        back_grnd_progress = (RelativeLayout) findViewById(R.id.loadingPanel);

        STATUS = (TextView) findViewById(R.id.status);
        status = (TextView) findViewById(R.id.status_result);
        memory = (TextView) findViewById(R.id.memory_result);
        time =   (TextView) findViewById(R.id.time_result);
        output = (TextView) findViewById(R.id.output_result);
        output = (TextView) findViewById(R.id.output_result);
        status = (TextView) findViewById(R.id.status_result);
        time = (TextView) findViewById(R.id.time_result);
        memory = (TextView) findViewById(R.id.memory_result);
        time_main = (TextView) findViewById(R.id.time);
        memory_main = (TextView) findViewById(R.id.memory);
        output_main = (TextView) findViewById(R.id.output);
        output.setMovementMethod(new ScrollingMovementMethod());
        status.setMovementMethod(new ScrollingMovementMethod());
        time.setMovementMethod(new ScrollingMovementMethod());
        memory.setMovementMethod(new ScrollingMovementMethod());



        try {
            Bundle bundle = getIntent().getExtras();
            language_selected = bundle.getString("language_selected1");
            source_code = bundle.getString("source_code1");
            test_cases = bundle.getString("test_cases1");
            /*Intent getintent = getIntent();
            language_selected = getintent.getStringExtra("language_selected1");
            source_code = getintent.getStringExtra("source_code1");
            test_cases = getintent.getStringExtra("test_cases1");
            //Toast.makeText(this,language_selected+"\n"+source_code+"\n"+test_cases+"\n",Toast.LENGTH_LONG).show();*/
        }catch (Exception e){
            e.printStackTrace();
        }

        try{
            new FetchJSONData().execute();
        }catch (Exception e)
        {
            e.printStackTrace();
           // Log.d("error","Call json!");
        }
    }

    public class FetchJSONData extends AsyncTask<Void, Void, String> {

        @Override
        protected String doInBackground(Void... arg0) {
            String response = null;

            test_cases = test_cases.replace("\n", "\\n");
            test_cases ="[\""+test_cases+"\"]";

            try {
                HttpClient client = new DefaultHttpClient();
                HttpPost request = new HttpPost("http://api.hackerrank.com/checker/submission.json");
                List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(4);
                nameValuePairs.add(new BasicNameValuePair("source",source_code.toString()));
                nameValuePairs.add(new BasicNameValuePair("lang",Integer.toString(Integer.parseInt(language_selected))));
                nameValuePairs.add(new BasicNameValuePair("testcases", test_cases));
                nameValuePairs.add(new BasicNameValuePair("api_key", "hackerrank|109106-239|765cc872284631f437a11c828dee1efe8755112e"));
                request.setEntity(new UrlEncodedFormEntity(nameValuePairs));
                HttpResponse res = client.execute(request);
                response = EntityUtils.toString(res.getEntity());
               // Log.e("language", language_selected);

            } catch (Exception e) {

                e.printStackTrace();
            }
            return response;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
           // System.out.println(result);

            try {
                back_grnd_progress.setVisibility(View.INVISIBLE);
                res_out.setVisibility(View.VISIBLE);
                JSONObject jsonObject = new JSONObject(result);
                JSONObject sys  = jsonObject.getJSONObject("result");
                String compile = sys.getString("compilemessage");
                String mem = sys.getString("memory");
                String message = sys.getString("message");
                String comp_err = sys.getString("censored_stderr");
                String comp_err1 = comp_err.substring(2,comp_err.length()-2 );
                String out = sys.getString("stdout");
                String out1 = out.replace("\\n", "\n").replace("\\b", "\b").replace("\\t", "\t");
                String time1 = sys.getString("time");
                if(message.subSequence(2, message.length()-2).equals("Success")){
                   // STATUS.setText("Status");
                    String tt = time1.substring(1,Math.min(time1.length(),8));
                    if(tt.charAt(tt.length()-1)!=']')
                        time.setText(time1.substring(1, Math.min(time1.length(), 8)) + " S");
                    else
                        time.setText(time1.substring(1, Math.min(time1.length()-1, 8)) + " S");

                    memory.setText(mem.substring(1,mem.length()-1)+" kb");
                    status.setText("Executed Successfully");
                    status.setTypeface(null, Typeface.BOLD_ITALIC);
                    status.setTextColor(Color.BLUE);
                    output.setText(out1.substring(2, out1.length() - 2));
                    time_main.setText("Time");
                    time_main.setTypeface(null, Typeface.BOLD);
                    memory_main.setText("Memory");
                    memory_main.setTypeface(null, Typeface.BOLD);
                    output_main.setText("Output");
                    output_main.setTypeface(null, Typeface.BOLD);
                }
                else if(compile.length()>0 && time1.equalsIgnoreCase("null")){

                    status.setText("\nCompilation error \n"+compile);
                    status.setTextColor(Color.RED);
                    status.setTypeface(null, Typeface.BOLD);
                    time_main.setText("");
                    memory_main.setText("");
                    output_main.setText("");
                    //time.setText("0 sec");
                   // memory.setText("0 Kb");
                }
                else if(compile.length()>0 || comp_err1.length()>0){
                    status.setText("\nRuntime error \n"+compile);
                    status.setTextColor(Color.RED);
                    status.setTypeface(null,Typeface.BOLD);
                    time_main.setText("");
                    memory_main.setText("");
                    output_main.setText("");

//                    time.setText(time1.substring(1,Math.min(time1.length()-1,8))+" s");
//                    memory.setText(mem.substring(1,mem.length()-1)+" kb");
//                    output.setText(out1.substring(2,out1.length()-2));
                }
            } catch (JSONException e) {

                e.printStackTrace();
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_hacker_rank_apihandler, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
       // if (id == R.id.action_settings) {
         //   return true;
        //}
        switch (item.getItemId()) {
            case android.R.id.home:
                // API 5+ solution
                onBackPressed();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }


    }
}
