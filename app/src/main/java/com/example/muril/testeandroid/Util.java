package com.example.muril.testeandroid;

import android.content.Context;
import android.widget.Toast;
import com.amazonaws.mobile.client.AWSMobileClient;
import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBMapper;
import com.amazonaws.mobileconnectors.pinpoint.PinpointConfiguration;
import com.amazonaws.mobileconnectors.pinpoint.PinpointManager;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClient;

public class Util {

    static public void ShowToast(Context context, String message){
        int duration = Toast.LENGTH_SHORT;

        Toast.makeText(context, message, duration).show();
    }

    static public void InitializeAmazonConfig(Context context) {
        AWSMobileClient.getInstance().initialize(context).execute();
    }

    static public DynamoDBMapper InitializeDynamoDB(Context context){
        AmazonDynamoDBClient dynamoDBClient = new AmazonDynamoDBClient(AWSMobileClient.getInstance().getCredentialsProvider());
        return DynamoDBMapper.builder()
                .dynamoDBClient(dynamoDBClient)
                .awsConfiguration(AWSMobileClient.getInstance().getConfiguration())
                .build();
    }

    static public void InitializeAmazonConfig(Context context, DynamoDBMapper dynamoDBMapper, PinpointManager pinpointManager) {
        AWSMobileClient.getInstance().initialize(context).execute();

        if(pinpointManager != null){
            PinpointConfiguration pinpointConfig = new PinpointConfiguration(
                    context,
                    AWSMobileClient.getInstance().getCredentialsProvider(),
                    AWSMobileClient.getInstance().getConfiguration());

            pinpointManager = new PinpointManager(pinpointConfig);

            // Start a session with Pinpoint
            pinpointManager.getSessionClient().startSession();

            // Stop the session and submit the default app started event
            pinpointManager.getSessionClient().stopSession();
            pinpointManager.getAnalyticsClient().submitEvents();
        }
    }

}
