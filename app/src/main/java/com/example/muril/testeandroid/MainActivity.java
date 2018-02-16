package com.example.muril.testeandroid;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ListView listView = findViewById(R.id.lista) ;

        List<Produto> listaProdutos = getProdutos();

        ArrayAdapter<Produto> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, listaProdutos);

        listView.setAdapter(adapter);

        Amazon amazon = new Amazon(this);
        amazon.execute();
    }

    private List<Produto> getProdutos(){
        List<Produto> list = new ArrayList<>();
        Produto batataDoce = new Produto("Batata Doce", 0.000001f);
        Produto whey = new Produto("Whey", 99999999.9999999999f);
        Produto frango = new Produto("Frango", 10.0f);

        list.add(batataDoce);
        list.add(whey);
        list.add(frango);

        return list;
    }
}
