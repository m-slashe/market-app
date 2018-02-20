package com.example.muril.testeandroid.fragments;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBQueryExpression;
import com.example.muril.testeandroid.R;
import com.example.muril.testeandroid.Util;
import com.example.muril.testeandroid.amazonaws.models.nosql.ProdutoDO;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class FornecedorFragment extends AbstractFragment implements OnMapReadyCallback {

    private MapView mapView;
    private GoogleMap map;
    private Spinner spinner;
    private ProdutoDO produtoAtual;
    private AutoCompleteTextView nome;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.fornecedor, container, false);
        mapView = view.findViewById(R.id.mapView);
        mapView.onCreate(savedInstanceState);
        mapView.getMapAsync(this);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        nome = view.findViewById(R.id.edit_forn);
        spinner = view.findViewById(R.id.spinner);
        Button inserir = view.findViewById(R.id.btn_inserir);
        Button atualizar = view.findViewById(R.id.btn_update_forn);

        atualizar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new SaveFornecedor().execute();
            }
        });

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                produtoAtual = (ProdutoDO) spinner.getSelectedItem();
                List<Double> localizacao = produtoAtual.getLocalizacao();
                LatLng latLng;
                if (localizacao != null)
                    latLng = new LatLng(localizacao.get(0), localizacao.get(1));
                else
                    latLng = new LatLng(-23.6927509, -46.6217797);
                produtoAtual.setLocalizacao(getListFromLatLng(latLng));
                map.clear();
                map.addMarker(new MarkerOptions()
                        .position(latLng)
                        .title(produtoAtual.getName()));
                map.moveCamera(CameraUpdateFactory.newLatLng(latLng));
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

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

                    return getDynamoDB().query(ProdutoDO.class, expression);
                } catch (Exception e) {
                    return null;
                }
            }

            @Override
            protected void onPostExecute(List<ProdutoDO> list) {
                super.onPostExecute(list);
                if (list != null) {
                    ArrayAdapter<ProdutoDO> adapter = new ArrayAdapter<>(getContext(),
                            android.R.layout.simple_dropdown_item_1line, list);
                    nome.setAdapter(adapter);
                    spinner.setAdapter(adapter);
                } else
                    Util.ShowToast(getContext(), "Não foi possivel ler os fornecedores!!!");
            }
        }.execute();

        inserir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                produtoAtual = new ProdutoDO();
                produtoAtual.setId(UUID.randomUUID().toString());
                produtoAtual.setName(nome.getText().toString());
                produtoAtual.setType("Fornecedor");
                new SaveFornecedor().execute();
            }
        });
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        map = googleMap;
        map.getUiSettings().setMyLocationButtonEnabled(false);

        map.setOnMapClickListener(new GoogleMap.OnMapClickListener() {

            @Override
            public void onMapClick(LatLng latLng) {
                map.clear();
                produtoAtual.setLocalizacao(getListFromLatLng(latLng));
                map.addMarker(new MarkerOptions()
                        .position(latLng)
                        .title(produtoAtual.getName()));
            }
        });

        LatLng coop = new LatLng(-23.6927509, -46.6217797);
        map.addMarker(new MarkerOptions()
                .position(coop)
                .title("Coop"));
        map.moveCamera(CameraUpdateFactory.newLatLngZoom(coop, 19f));
    }

    @Override
    public void onResume() {
        mapView.onResume();
        super.onResume();
    }


    @Override
    public void onPause() {
        super.onPause();
        mapView.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapView.onLowMemory();
    }

    private List<Double> getListFromLatLng(LatLng latLng) {
        List<Double> list = new ArrayList<>();
        list.add(latLng.latitude);
        list.add(latLng.longitude);
        return list;
    }

    private class SaveFornecedor extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... params) {
            try {
                getDynamoDB().save(produtoAtual);
                return "Ok";
            } catch (Exception e) {
                return "Fail";
            }
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            if (result.equals("Ok")) {
                getFornecedoresList().add(produtoAtual);
                nome.setText("");
                Util.ShowToast(getContext(), "Registro inserido com sucesso!!!");
            } else
                Util.ShowToast(getContext(), "Não foi possivel inserir os dados!!!");
        }
    }
}
