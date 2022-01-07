package com.cryptophonecall.cv;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import androidx.core.util.Pair;

import com.google.gson.Gson;
import com.google.gson.JsonArray;

import org.apache.commons.validator.routines.EmailValidator;
import org.json.JSONArray;
import org.json.JSONObject;

import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.TreeMap;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.KeyManager;
import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManagerFactory;

import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class PhonebookActivity extends Activity {
    private Retrofit retrofit = null;
    private String BASE_URL = "https://cryptophonecall.com:8443/SCRUDU/";
    public static ArrayList<PhonebookEntry> ml = new ArrayList<>();
    public CustomArrayAdapterUserPhonebook ma;
    private Context lac;
    //private ICallListener

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_phonebook);
        lac=this;
        ListView lv = (ListView) findViewById(R.id.pblistView1);


        SSLRoutine mr = new SSLRoutine(this);
        OkHttpClient myclient=null;
        SSLContext context=null;
        try {
            Pair<TrustManagerFactory, KeyManagerFactory> km = mr.getTrustManagerFactory();
            if (km == null || km.first == null || km.second == null) {
                throw new Exception("No valid truststore provided");
            }
            context= SSLContext.getInstance("SSL");
            KeyManager[] keymanagers = km.second.getKeyManagers();
            context.init(keymanagers, km.first.getTrustManagers(), new SecureRandom());
            myclient = new OkHttpClient.Builder()
                    .sslSocketFactory(context.getSocketFactory(), mr.systemDefaultTrustManager())
                    .hostnameVerifier(new HostnameVerifier() {
                        @Override
                        public boolean verify(String hostname, SSLSession session) {
                            return true;}})
                    .build();

        }
        catch(Exception e)
        {
            e.printStackTrace();
        }







        // Log.d("usrnmbr","asdf");

       // String savedNumber = getString("number");
       // if (!savedNumber.isEmpty()) {

            if (retrofit == null) {
                retrofit = new Retrofit.Builder()
                        .baseUrl(BASE_URL)
                        .addConverterFactory(GsonConverterFactory.create())
                        .client(myclient)
                        .build();
            }


            try {

                Map<String, String> dev = new LinkedHashMap<>();
                //Log.d("main", user);
                Intent usrstart = getIntent();
                String umail = usrstart.getStringExtra("user");

                dev.put("user",umail);
                Log.d("usr", "get numbers");
                getPhonebook(dev, "getusernumbers");

            } catch (Exception e) {
                e.printStackTrace();
            }

        //Intent i = getIntent();
        //ArrayList<UsrDevice> list = (ArrayList<UsrDevice>) i.getSerializableExtra("list");
         ma = new CustomArrayAdapterUserPhonebook(PhonebookActivity.this,ml);
         lv.setAdapter(ma);

         lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
             @Override
             public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                 Intent intent=new Intent();
                 intent.putExtra("pbnumber",ml.get(position).getNumber());
                 setResult(2,intent);
                 finish();
             }
         });
            ImageButton newnumber = (ImageButton) findViewById(R.id.newnumberbtn);
            newnumber.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    emButton();


                }
            });






    }


        private void getPhonebook(Map<String,String> dev, String action){
        APIInterfacePhoneBook postsService = retrofit.create(APIInterfacePhoneBook.class);

        Call<JsonArray> call = postsService.getPhonebook(dev.get("user"), dev.get("number"), dev.get("fname"), dev.get("lname"),action);

        call.enqueue(new Callback<JsonArray>() {
            @Override
            public void onResponse(Call<JsonArray> call, Response<JsonArray> response) {

                try {
                    Log.d("phonebook" , "respo");
                    JSONArray usrdev = new JSONArray(new Gson().toJson(response.body()));
                    ml.clear();
                    for(int i = 0; i < usrdev.length(); i++){
                        PhonebookEntry mud = new PhonebookEntry();
                        JSONObject object = usrdev.getJSONObject(i);
                        mud.setNumber(object.getString("number"));
                        mud.setFname(object.getString("fname"));
                        mud.setLname(object.getString("lname"));
                        ml.add(mud);
                        ma.notifyDataSetChanged();
                    }
                    Log.d("phonebook", "end of gathering info");
                    //doDevCompl = true;

                }catch(Exception e){e.printStackTrace();}

            }


            @Override
            public void onFailure(Call<JsonArray> call, Throwable t){
                Log.d("phonebook", "Tomcat server connection error.");
            }
        });

    }

    private void doPhonebook(Map<String,String> dev, String action){
        APIInterfacePhoneBook postsService = retrofit.create(APIInterfacePhoneBook.class);

        Call<LoginResponse> call = postsService.doPhonebook(dev.get("user"), dev.get("number"), dev.get("fname"), dev.get("lname"),action);

        call.enqueue(new Callback<LoginResponse>() {
            @Override
            public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {

                Map<String, String> dev = new LinkedHashMap<>();
                //Log.d("main", user);
                Intent usrstart = getIntent();
                String umail = usrstart.getStringExtra("user");

                dev.put("user",umail);
                Log.d("usr", "get numbers");
                getPhonebook(dev, "getusernumbers");

            }


            @Override
            public void onFailure(Call<LoginResponse> call, Throwable t){
                Log.d("phonebook", "Tomcat server connection error.");
            }
        });

    }

    private void  emButton(){



        LinearLayout layout = new LinearLayout(lac);
        layout.setOrientation(LinearLayout.VERTICAL);

// Add a TextView here for the "Title" label, as noted in the comments
        final EditText email = new EditText(lac);
        email.setHint("First name");
        layout.addView(email); // Notice this is an add method



        final EditText email1 = new EditText(lac);
        email1.setHint("Last name");
        layout.addView(email1); // Notice this is an add method

        final EditText email2 = new EditText(lac);
        email2.setHint("Number");
        layout.addView(email2); // Notice this is an add method

// Add another TextView here for the "Description" label
        AlertDialog.Builder alert = new AlertDialog.Builder(lac);

        alert.setMessage("User info:");

        alert.setView(layout);


        alert.setPositiveButton("Add", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                //What ever you want to do with the value
                // Editable YouEditTextValue = edittext.getText();

            }
        });

        alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                // what ever you want to do with No option.
            }});




        final AlertDialog md = alert.create();
        md.show();


        md.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                String fname = ((EditText)layout.getChildAt(0)).getText().toString();
                String lname = ((EditText)layout.getChildAt(1)).getText().toString();
                String number = ((EditText)layout.getChildAt(2)).getText().toString();
                Intent usrstart = getIntent();
                String user = usrstart.getStringExtra("user");

                if(number == null || number == ""  )
                {
                    Toast.makeText(lac, "Enter number",Toast.LENGTH_LONG).show();
                    return;
                }
                if(fname == "" || lname == "" )
                {
                    Toast.makeText(lac, "Enter valid names",Toast.LENGTH_LONG).show();
                    return;
                }

                Map<String,String> pbe = new TreeMap<>();
                pbe.put("fname",fname);
                pbe.put("lname",lname);
                pbe.put("number",number);
                pbe.put("user",user);
                doPhonebook(pbe,"add");

                //Do stuff, possibly set wantToCloseDialog to true then...
                //else dialog stays open. Make sure you have an obvious way to close the dialog especially if you set cancellable to false.
            }
        });

    }
}
