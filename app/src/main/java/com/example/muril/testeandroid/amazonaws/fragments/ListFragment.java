package com.example.muril.testeandroid.amazonaws.fragments;


import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBScanExpression;
import com.example.muril.testeandroid.ProdutoAdapter;
import com.example.muril.testeandroid.R;
import com.example.muril.testeandroid.Util;
import com.example.muril.testeandroid.amazonaws.models.nosql.ProdutoDO;

import java.util.ArrayList;
import java.util.List;

public class ListFragment extends AbstractFragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        return inflater.inflate(R.layout.lista, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        final ListView listView = view.findViewById(R.id.lista);

        new AsyncTask<String, Void, List<ProdutoDO>>(){

            @Override
            protected List<ProdutoDO> doInBackground(String... params) {
                try{
                    DynamoDBScanExpression expression = new DynamoDBScanExpression();
                    return getDynamoDB().scan(ProdutoDO.class, expression);
                }catch (Exception e){
                    Util.ShowToast(getContext(),"Não foi possivel ler os dados!!!");
                    return null;
                }
            }

            @Override
            protected void onPostExecute(List<ProdutoDO> list) {
                super.onPostExecute(list);
                if(list != null){
                    List<ProdutoDO> modifiedList = new ArrayList<>();
                    modifiedList.addAll(list);
                    ProdutoAdapter adapter = new ProdutoAdapter(getContext(), R.layout.listadapter, modifiedList);
                    listView.setAdapter(adapter);
                }
            }
        }.execute();
    }
}
