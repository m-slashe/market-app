package com.example.muril.testeandroid;

import android.content.Context;
import android.os.AsyncTask;
import com.amazonaws.auth.CognitoCachingCredentialsProvider;
import com.amazonaws.mobileconnectors.dynamodbv2.document.Table;
import com.amazonaws.mobileconnectors.dynamodbv2.document.datatype.Document;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClient;

public class Amazon extends AsyncTask {

    Context context;

    public Amazon(Context context){
        this.context = context;
    }

    @Override
    protected Object doInBackground(Object[] objects) {
        try{
            CognitoCachingCredentialsProvider credentialsProvider = new CognitoCachingCredentialsProvider(
                    context, "us-east-2:2bb29670-498d-454e-893b-ada1838d169e", Regions.US_EAST_2);

            // Create a connection to DynamoDB
            AmazonDynamoDBClient dbClient = new AmazonDynamoDBClient(credentialsProvider);

            // Create a table reference
            Table dbTable = Table.loadTable(dbClient, "jomuroteste");

            Document memo = new Document();
            memo.put("seila", "Whey");
            memo.put("teste", "Batata Doce");
            memo.put("teste2", "Frango");

            dbTable.putItem(memo);
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }
}
