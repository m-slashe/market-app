package com.example.muril.testeandroid;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBMapper;
import com.example.muril.testeandroid.fragments.CarrinhoFragment;
import com.example.muril.testeandroid.fragments.FornecedorFragment;
import com.example.muril.testeandroid.fragments.InsertFragment;
import com.example.muril.testeandroid.fragments.ListFragment;
import com.example.muril.testeandroid.amazonaws.models.nosql.ProdutoDO;
import com.google.zxing.integration.android.IntentIntegrator;

import java.util.ArrayList;
import java.util.List;

public class TabActivity extends AppCompatActivity {

    private DynamoDBMapper dynamoDBMapper;
    private List<ProdutoDO> produtos = new ArrayList<>();
    private List<ProdutoDO> fornecedores = new ArrayList<>();

    public DynamoDBMapper getDynamoDB(){
        return dynamoDBMapper;
    }

    public List<ProdutoDO> getProdutosList(){
        return produtos;
    }

    public List<ProdutoDO> getFornecedoresList(){
        return fornecedores;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tab);

        Util.InitializeAmazonConfig(this);
        dynamoDBMapper = Util.InitializeDynamoDB(this);

        ActionBar actionBar = getSupportActionBar();
        if(actionBar != null){
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        ViewPager viewPager = findViewById(R.id.viewpager);
        setupViewPager(viewPager);

        TabLayout tabLayout = findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);
    }

    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(new CarrinhoFragment(), "Carrinho");
        adapter.addFragment(new ListFragment(), "Lista");
        adapter.addFragment(new InsertFragment(), "Inserir");
        adapter.addFragment(new FornecedorFragment(), "Fornecedores");
        viewPager.setAdapter(adapter);
    }

    class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        void addFragment(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == IntentIntegrator.REQUEST_CODE) {
            FragmentManager mgr = getSupportFragmentManager();
            Fragment fragment = mgr.getFragments().get(1);
            fragment.onActivityResult(requestCode, resultCode, data);
        }
    }
}