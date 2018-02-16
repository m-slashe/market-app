package com.example.muril.testeandroid;

import android.content.Context;
import android.os.AsyncTask;
import com.amazonaws.auth.CognitoCachingCredentialsProvider;
import com.amazonaws.mobileconnectors.dynamodbv2.document.ScanFilter;
import com.amazonaws.mobileconnectors.dynamodbv2.document.Search;
import com.amazonaws.mobileconnectors.dynamodbv2.document.Table;
import com.amazonaws.mobileconnectors.dynamodbv2.document.datatype.Document;
import com.amazonaws.mobileconnectors.dynamodbv2.document.datatype.Primitive;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClient;
import com.amazonaws.services.dynamodbv2.model.*;

import java.util.ArrayList;
import java.util.List;

public class Amazon extends AsyncTask {

    Context context;
    AmazonDynamoDBClient dbClient;

    public Amazon(Context context){
        this.context = context;
        CognitoCachingCredentialsProvider credentialsProvider = new CognitoCachingCredentialsProvider(
                context, "us-east-2:2bb29670-498d-454e-893b-ada1838d169e", Regions.US_EAST_2);

        // Create a connection to DynamoDB
        this.dbClient = new AmazonDynamoDBClient(credentialsProvider);
    }

    @Override
    protected Object doInBackground(Object[] objects) {
        try{
            //createTableProductCatalog();

            ListTablesResult result = dbClient.listTables();
            result.getTableNames();

            // Create a table reference
            Table dbTable = Table.loadTable(dbClient, "NovaTabela");

            Primitive primitive = new Primitive(1);
            Primitive primitive1 = new Primitive("Whey");

            Document whey = dbTable.getItem(primitive, primitive1);

            Document memo = new Document();
            memo.put("Id", 1);
            memo.put("Name", "Whey");
            /*memo.put("teste", "Batata Doce");
            memo.put("teste2", "Frango");*/

            dbTable.putItem(memo);
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    private void createTableProductCatalog(){
        String tableName = "NovaTabela";

        //this.dbClient.deleteTable(tableName);

        AttributeDefinition id = new AttributeDefinition("Id", "N");
        AttributeDefinition nome = new AttributeDefinition("Name", "S");
        List<AttributeDefinition> atributes = new ArrayList<>();
        atributes.add(id);
        atributes.add(nome);

        KeySchemaElement KeyId = new KeySchemaElement("Id", "HASH");
        KeySchemaElement KeyNome = new KeySchemaElement("Name", "RANGE");
        List<KeySchemaElement> keys = new ArrayList<>();
        keys.add(KeyId);
        keys.add(KeyNome);

        ProvisionedThroughput provisoned = new ProvisionedThroughput(10L, 5L);

        this.dbClient.createTable(atributes, tableName, keys, provisoned);
    }

}
