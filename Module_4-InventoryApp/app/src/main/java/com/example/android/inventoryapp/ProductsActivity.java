package com.example.android.inventoryapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import io.hasura.sdk.Callback;
import io.hasura.sdk.Hasura;
import io.hasura.sdk.HasuraClient;
import io.hasura.sdk.HasuraUser;
import io.hasura.sdk.exception.HasuraException;

public class ProductsActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    LinearLayoutManager linearLayoutManager;
    CategoryListAdapter adapter;

    List<CategoryResponse> categoryList = new ArrayList<>();

    SectionsPagerAdapter mSectionsPagerAdapter;
    ViewPager mViewPager;

    private String tabs[] = {"Mobiles","Shoes","Clothes"};


    HasuraUser user = Hasura.getClient().getUser();
    HasuraClient client = Hasura.getClient();

    List<CategoryResponse> categories = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_products);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("");
        //toolbar.setLogo(R.drawable.digitalocean_hasura_logo_name);
        setSupportActionBar(toolbar);

        Global.currentItemList = new ArrayList<>();

        client.useDataService()
                .setRequestBody(new SelectCategories())
                .expectResponseTypeArrayOf(CategoryResponse.class)
                .enqueue(new Callback<List<CategoryResponse>, HasuraException>() {
                    @Override
                    public void onSuccess(List<CategoryResponse> categoryResponses) {
                        categoryList.addAll(categoryResponses);
                        setUpViewPager(mViewPager);
                    }

                    @Override
                    public void onFailure(HasuraException e) {
                        Toast.makeText(ProductsActivity.this, e.toString(), Toast.LENGTH_SHORT).show();
                    }
                });


        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        mViewPager = (ViewPager) findViewById(R.id.pager);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setTabMode(TabLayout.MODE_SCROLLABLE);
        tabLayout.setupWithViewPager(mViewPager);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.mainmenu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.go_to_cart:
                Intent i = new Intent(ProductsActivity.this, CartActivity.class);
                startActivity(i);
                return true;
        }
        return false;
    }

    private void setUpViewPager(ViewPager viewPager){
        SectionsPagerAdapter adapter = new SectionsPagerAdapter(getSupportFragmentManager());

        int i;
        for (i = 0;i < categoryList.size();i++){
            adapter.addFragment(new ViewAllItems().NewInstance(categoryList.get(i).getCategory()),categoryList.get(i).getCategory());
        }
        viewPager.setAdapter(adapter);
    }

    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        private final List<Fragment> fragmentList = new ArrayList<>();
        private final List<String> fragmentTitleList = new ArrayList<>();

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {

            return fragmentList.get(position);
        }

        @Override
        public int getCount() {
            return fragmentList.size();
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return fragmentTitleList.get(position);
        }

        public void addFragment(Fragment fragment, String title){
            fragmentList.add(fragment);
            fragmentTitleList.add(title);
        }
    }
}
