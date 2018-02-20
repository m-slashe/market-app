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
import android.widget.EditText;
import android.widget.Spinner;
import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBQueryExpression;
import com.example.muril.testeandroid.R;
import com.example.muril.testeandroid.Util;
import com.example.muril.testeandroid.amazonaws.models.nosql.ProdutoDO;

import java.util.List;
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
        final EditText editNome = view.findViewById(R.id.editText);
        final EditText editDescricao = view.findViewById(R.id.editText2);
        final EditText editGtin = view.findViewById(R.id.editText3);
        final Spinner editForn = view.findViewById(R.id.edit_forn2);

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
}
