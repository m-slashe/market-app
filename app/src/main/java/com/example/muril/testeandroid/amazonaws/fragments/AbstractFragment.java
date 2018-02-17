package com.example.muril.testeandroid.amazonaws.fragments;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBMapper;
import com.example.muril.testeandroid.TabActivity;

public class AbstractFragment extends Fragment {

    private Context context;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        context = activity;
    }

    @Nullable
    @Override
    public Context getContext() {
        return context;
    }

    public DynamoDBMapper getDynamoDB(){
        return ((TabActivity)context).getDynamoDB();
    }
}
