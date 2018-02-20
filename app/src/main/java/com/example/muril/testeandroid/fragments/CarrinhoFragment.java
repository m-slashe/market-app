package com.example.muril.testeandroid.fragments;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Spinner;
import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBQueryExpression;
import com.example.muril.testeandroid.R;
import com.example.muril.testeandroid.Util;
import com.example.muril.testeandroid.amazonaws.models.nosql.ProdutoDO;

import java.util.ArrayList;
import java.util.List;

public class CarrinhoFragment extends AbstractFragment {

    private Spinner spinner;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.carrinho, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        spinner = view.findViewById(R.id.spinner2);
        Button adicionar = view.findViewById(R.id.btn_adicionar);
        ListView listView = view.findViewById(R.id.list_carrinho);
        final List<ProdutoDO> carrinho = new ArrayList<>();

        //new LoadProdutos().execute();
        adicionar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ProdutoDO produto = (ProdutoDO) spinner.getSelectedItem();
                carrinho.add(produto);
            }
        });

        ArrayAdapter adpater = new ArrayAdapter(getContext(), R.layout.listadapter, carrinho);
        listView.setAdapter(adpater);
    }

    private class LoadProdutos extends AsyncTask<String, Void, List<ProdutoDO>>{

        @Override
        protected List<ProdutoDO> doInBackground(String... strings) {
            DynamoDBQueryExpression<ProdutoDO> expression = new DynamoDBQueryExpression<>();
            return getDynamoDB().query(ProdutoDO.class, expression);
        }

        @Override
        protected void onPostExecute(List<ProdutoDO> list) {
            super.onPostExecute(list);
            if (list != null) {
                ArrayAdapter<ProdutoDO> adapter = new ArrayAdapter<>(getContext(),
                        android.R.layout.simple_dropdown_item_1line, list);
                spinner.setAdapter(adapter);
            } else
                Util.ShowToast(getContext(), "Não foi possivel carregar os produtos!!!");
        }
    }
}
