package com.example.muril.testeandroid.amazonaws.fragments;


import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import com.example.muril.testeandroid.R;
import com.example.muril.testeandroid.Util;
import com.example.muril.testeandroid.amazonaws.fragments.AbstractFragment;
import com.example.muril.testeandroid.amazonaws.models.nosql.ProdutoDO;

import java.util.UUID;

public class InsertFragment extends AbstractFragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        return inflater.inflate(R.layout.inserir, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Button button1 = view.findViewById(R.id.button1);
        Button button2 = view.findViewById(R.id.button2);
        final EditText editNome = view.findViewById(R.id.editText);
        final EditText editDescricao = view.findViewById(R.id.editText2);
        final EditText editGtin = view.findViewById(R.id.editText3);

        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Util.ShowToast(getContext(),"Teste");
            }
        });

        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    String nome = editNome.getText().toString();
                    String descricao = editDescricao.getText().toString();
                    Double gtin = Double.valueOf(editGtin.getText().toString());

                    final ProdutoDO novoProduto = new ProdutoDO(UUID.randomUUID().toString(),
                            nome,
                            descricao,
                            gtin);

                    new AsyncTask<String, Void, String>(){

                        @Override
                        protected String doInBackground(String... params) {
                            try{
                                getDynamoDB().save(novoProduto);
                                return "Ok";
                            }catch (Exception e){
                                return "Fail";
                            }
                        }

                        @Override
                        protected void onPostExecute(String result) {
                            super.onPostExecute(result);
                            if(result.equals("Ok")){
                                editNome.setText("");
                                editDescricao.setText("");
                                editGtin.setText("");
                                Util.ShowToast(getContext(),"Registro inserido com sucesso!!!");
                            }else
                                Util.ShowToast(getContext(),"Não foi possivel inserir os dados!!!");
                        }
                    }.execute();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }
}
