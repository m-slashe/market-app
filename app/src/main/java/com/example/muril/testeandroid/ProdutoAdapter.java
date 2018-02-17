package com.example.muril.testeandroid;

import android.content.Context;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;
import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBMapper;
import com.example.muril.testeandroid.amazonaws.models.nosql.ProdutoDO;

import java.util.ArrayList;
import java.util.List;

public class ProdutoAdapter extends ArrayAdapter<ProdutoDO> {

    private DynamoDBMapper dynamoDBMapper;
    List<ProdutoDO> list;

    public ProdutoAdapter(Context context, int resource) {
        super(context, resource);
    }

    public ProdutoAdapter(Context context, int resource, List<ProdutoDO> items) {
        super(context, resource, items);
        list = items;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View v = convertView;

        if (v == null) {
            LayoutInflater vi;
            vi = LayoutInflater.from(getContext());
            v = vi.inflate(R.layout.listadapter, null);
        }

        final ProdutoDO p = getItem(position);

        if (p != null) {
            Button excluir = v.findViewById(R.id.excluir);
            excluir.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    new AsyncTask<String, Void, String>() {

                        @Override
                        protected String doInBackground(String... params) {
                            try {
                                ((TabActivity) getContext()).getDynamoDB().delete(p);
                                return "Ok";
                            } catch (Exception e) {
                                return "Fail";
                            }
                        }

                        @Override
                        protected void onPostExecute(String result) {
                            super.onPostExecute(result);
                            if (result.equals("Ok")){
                                remove(p);
                                Util.ShowToast(getContext(), "Registro excluido com sucesso!!!");
                            }else
                                Util.ShowToast(getContext(), "Falha ao excluir registro!!!");
                        }
                    }.execute();
                }
            });
            TextView tt1 = v.findViewById(R.id.nome_value);
            TextView tt2 = v.findViewById(R.id.description_value);
            TextView tt3 = v.findViewById(R.id.gtin_value);

            if (tt1 != null) {
                tt1.setText(p.getName());
            }

            if (tt2 != null) {
                tt2.setText(p.getDescription());
            }

            if (tt3 != null) {
                tt3.setText(String.valueOf(p.getGtin()));
            }
        }

        return v;
    }
}
