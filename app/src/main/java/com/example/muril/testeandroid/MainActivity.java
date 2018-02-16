package com.example.muril.testeandroid;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import com.amazonaws.mobile.client.AWSMobileClient;
import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBMapper;
import com.amazonaws.mobileconnectors.pinpoint.PinpointConfiguration;
import com.amazonaws.mobileconnectors.pinpoint.PinpointManager;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClient;
import com.example.muril.testeandroid.amazonaws.models.nosql.ProdutosDO;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    public static PinpointManager pinpointManager;
    DynamoDBMapper dynamoDBMapper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        try {
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

            final ProdutosDO novoProduto = new ProdutosDO();

            novoProduto.setProductId(1d);
            novoProduto.setProductName("Whey");
            novoProduto.setProductName("Ficar monstro");

            new Thread(new Runnable() {
                @Override
                public void run() {
                    dynamoDBMapper.save(novoProduto);
                    // Item saved
                }
            }).start();
        } catch (Exception e) {
            e.printStackTrace();
        }

        ListView listView = findViewById(R.id.lista);

        List<Produto> listaProdutos = getProdutos();

        ArrayAdapter<Produto> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, listaProdutos);

        listView.setAdapter(adapter);
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
}
