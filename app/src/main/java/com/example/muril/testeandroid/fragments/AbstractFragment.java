package com.example.muril.testeandroid.fragments;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBMapper;
import com.example.muril.testeandroid.TabActivity;
import com.example.muril.testeandroid.amazonaws.models.nosql.ProdutoDO;

import java.util.List;

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

    public List<ProdutoDO> getProdutosList(){
        return ((TabActivity)context).getProdutosList();
    }

    public List<ProdutoDO> getFornecedoresList(){
        return ((TabActivity)context).getFornecedoresList();
    }
}
