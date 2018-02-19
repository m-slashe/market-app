package com.example.muril.testeandroid2;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;
import com.amazonaws.http.HttpClient;
import com.amazonaws.http.HttpResponse;
import com.amazonaws.mobile.client.AWSMobileClient;
import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBMapper;
import com.amazonaws.mobileconnectors.pinpoint.PinpointConfiguration;
import com.amazonaws.mobileconnectors.pinpoint.PinpointManager;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClient;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import org.apache.http.HttpStatus;
import org.apache.http.StatusLine;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    public static PinpointManager pinpointManager;
    DynamoDBMapper dynamoDBMapper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final Context context = this;

        Button button = findViewById(R.id.scan_button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new GetProdutoInfo().execute();
                //new IntentIntegrator((MainActivity)context).initiateScan();
            }
        });

        /*try {
            PinpointConfiguration pinpointConfig = new PinpointConfiguration(
                    getApplicationContext(),
                    AWSMobileClient.getInstance().getCredentialsProvider(),
                    AWSMobileClient.getInstance().getConfiguration());

            pinpointManager = new PinpointManager(pinpointConfig);

            // Start a session with Pinpoint
            pinpointManager.getSessionClient().startSession();

            // Stop the session and submit the default app started event
            pinpointManager.getSessionClient().stopSession();
            pinpointManager.getAnalyticsClient().submitEvents();

            AmazonDynamoDBClient dynamoDBClient = new AmazonDynamoDBClient(AWSMobileClient.getInstance().getCredentialsProvider());
            this.dynamoDBMapper = DynamoDBMapper.builder()
                    .dynamoDBClient(dynamoDBClient)
                    .awsConfiguration(AWSMobileClient.getInstance().getConfiguration())
                    .build();
        } catch (Exception e) {
            e.printStackTrace();
        }*/

        /*ListView listView = findViewById(R.id.lista);

        List<Produto> listaProdutos = getProdutos();

        ArrayAdapter<Produto> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, listaProdutos);

        listView.setAdapter(adapter);*/
    }

    private List<Produto> getProdutos() {
        List<Produto> list = new ArrayList<>();
        Produto batataDoce = new Produto("Batata Doce", 0.000001f);
        Produto whey = new Produto("Whey", 99999999.9999999999f);
        Produto frango = new Produto("Frango", 10.0f);

        list.add(batataDoce);
        list.add(whey);
        list.add(frango);

        return list;
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if(result != null) {
            if(result.getContents() == null) {
                Log.d("MainActivity", "Cancelled scan");
                Toast.makeText(this, "Cancelled", Toast.LENGTH_LONG).show();
            } else {
                Log.d("MainActivity", "Scanned");
                Toast.makeText(this, "Scanned: " + result.getContents(), Toast.LENGTH_LONG).show();
            }
        } else {
            // This is important, otherwise the result will not be passed to the fragment
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    private class GetProdutoInfo extends AsyncTask<String, String, String>{

        @Override
        protected String doInBackground(String... uri) {
            try{
                URL url = new URL("https://api.cosmos.bluesoft.com.br/gtins/7891203010209");
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("GET");
                connection.setRequestProperty("X-Cosmos-Token", "aKnNVyWGRGr3kDmqvhRyow");
                connection.setRequestProperty("Content-Type", "application/json");
                connection.setDoOutput(true);
                connection.setConnectTimeout(5000);
                connection.setReadTimeout(5000);
                connection.connect();
                BufferedReader rd = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                String content = "", line;
                while ((line = rd.readLine()) != null) {
                    content += line + "\n";
                }
                return content;
            }catch (Exception e){
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            //Do anything with response..
        }
    }
}
