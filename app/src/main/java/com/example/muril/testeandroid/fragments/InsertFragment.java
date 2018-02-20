package com.example.muril.testeandroid.fragments;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBQueryExpression;
import com.example.muril.testeandroid.R;
import com.example.muril.testeandroid.TabActivity;
import com.example.muril.testeandroid.Util;
import com.example.muril.testeandroid.amazonaws.models.nosql.ProdutoDO;
import org.json.JSONException;
import org.json.JSONObject;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;
import java.util.UUID;

public class InsertFragment extends AbstractFragment {

    private EditText editNome;
    private EditText editDescricao;
    private EditText editGtin;

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
        editNome = view.findViewById(R.id.editText);
        editDescricao = view.findViewById(R.id.editText2);
        editGtin = view.findViewById(R.id.editText3);
        final Spinner editForn = view.findViewById(R.id.edit_forn2);

        final Fragment fragment = this;

        Button button = view.findViewById(R.id.scan);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                IntentIntegrator.forFragment(fragment).initiateScan();
            }
        });

        new AsyncTask<String, Void, List<ProdutoDO>>() {

            @Override
            protected List<ProdutoDO> doInBackground(String... params) {
                try {
                    ProdutoDO teste = new ProdutoDO();
                    teste.setType("Fornecedor");

                    DynamoDBQueryExpression<ProdutoDO> expression = new DynamoDBQueryExpression<ProdutoDO>()
                            .withHashKeyValues(teste)
                            .withConsistentRead(false);

                    List<ProdutoDO> list = getDynamoDB().query(ProdutoDO.class, expression);
                    return list;
                } catch (Exception e) {
                    return null;
                }
            }

            @Override
            protected void onPostExecute(List<ProdutoDO> list) {
                super.onPostExecute(list);
                if (list != null) {
                    getFornecedoresList().clear();
                    getFornecedoresList().addAll(list);
                    ArrayAdapter<ProdutoDO> adapter = new ArrayAdapter<>(getContext(),
                            android.R.layout.simple_dropdown_item_1line, getFornecedoresList());
                    editForn.setAdapter(adapter);
                } else
                    Util.ShowToast(getContext(), "Não foi possivel obter os fornecedores!!!");
            }
        }.execute();

        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    String nome = editNome.getText().toString();
                    String descricao = editDescricao.getText().toString();
                    Double gtin = Double.valueOf(editGtin.getText().toString());
                    ProdutoDO fornecedor = (ProdutoDO) editForn.getSelectedItem();

                    final ProdutoDO novoProduto = new ProdutoDO(UUID.randomUUID().toString(),
                            nome,
                            descricao,
                            gtin,
                            "Produto",
                            fornecedor.getName());

                    new AsyncTask<String, Void, String>() {

                        @Override
                        protected String doInBackground(String... params) {
                            try {
                                getDynamoDB().save(novoProduto);
                                return "Ok";
                            } catch (Exception e) {
                                return "Fail";
                            }
                        }

                        @Override
                        protected void onPostExecute(String result) {
                            super.onPostExecute(result);
                            if (result.equals("Ok")) {
                                getProdutosList().add(novoProduto);
                                editNome.setText("");
                                editDescricao.setText("");
                                editGtin.setText("");
                                Util.ShowToast(getContext(), "Registro inserido com sucesso!!!");
                            } else
                                Util.ShowToast(getContext(), "Não foi possivel inserir os dados!!!");
                        }
                    }.execute();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if (result != null) {
            if (result.getContents() == null) {
                Util.ShowToast(getContext(), "Cancelled");
            } else {
                Util.ShowToast(getContext(), "Scanned: " + result.getContents());
                new GetProdutoInfo().execute(result.getContents());
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    private class GetProdutoInfo extends AsyncTask<String, String, JSONObject> {

        @Override
        protected JSONObject doInBackground(String... params) {
            try {
                String gtin = params[0];
                String cosmos = "http://api.cosmos.bluesoft.com.br/gtins/" + gtin;
                URL url = new URL(cosmos);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("GET");
                connection.setRequestProperty("X-Cosmos-Token", "4G2LdsaO4WYzwDuLiHQ50w");
                connection.setRequestProperty("Content-Type", "application/json; charset=utf-8");

                if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                    BufferedReader rd = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                    String content = "", line;
                    while ((line = rd.readLine()) != null) {
                        content += line + "\n";
                    }
                    return new JSONObject(content);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(JSONObject produto) {
            super.onPostExecute(produto);
            if (produto != null) {
                try {
                    JSONObject ncm = produto.getJSONObject("ncm");
                    editNome.setText(produto.getString("description"));
                    editDescricao.setText(ncm.getString("full_description"));
                    editGtin.setText(produto.getString("gtin"));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}