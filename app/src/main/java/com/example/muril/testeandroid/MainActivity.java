package com.example.muril.testeandroid;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ListView listView = (ListView) findViewById(R.id.lista) ;

        List<Produto> listaProdutos = getProdutos();

        ArrayAdapter<Produto> adapter = new ArrayAdapter<Produto>(this, android.R.layout.simple_list_item_1, listaProdutos);

        listView.setAdapter(adapter);
    }

    public List<Produto> getProdutos(){
        List<Produto> list = new ArrayList<Produto>();
        Produto pao = new Produto("Pão", 0.5f);
        Produto queijo = new Produto("Queijo", 0.5f);

        list.add(pao);
        list.add(queijo);

        return list;
    }
}
