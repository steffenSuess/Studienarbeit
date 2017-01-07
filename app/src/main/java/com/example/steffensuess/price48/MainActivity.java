package com.example.steffensuess.price48;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.test.espresso.core.deps.guava.collect.Lists;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ListView;

import com.google.android.gms.common.api.CommonStatusCodes;
import com.google.android.gms.vision.barcode.Barcode;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    String ean;
    ListView listView;
    Menu optionsMenu;
    DatabaseHandler db;
    List<SearchQuery> searchQueries;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        listView = (ListView)findViewById(R.id.searchquery_list);

        //this.deleteDatabase("searchQueriesManager");
        db = new DatabaseHandler(this);
        searchQueries = Lists.reverse(db.getAllQueries());
        if(searchQueries.size() > 0){
            List<SearchQuery> searchQueryList = new ArrayList<SearchQuery>();
            for(int i = 0; i < searchQueries.size() && i <= 4; i++){
                searchQueryList.add(searchQueries.get(i));
            }
            SearchQueriesAdapter adapter = new SearchQueriesAdapter(MainActivity.this, R.layout.query_list_item, searchQueryList);
            listView.setAdapter(adapter);
        }
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        finish();
        startActivity(getIntent());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.options_menu, menu);
        optionsMenu = menu;

        // Associate searchable configuration with the SearchView
        SearchManager searchManager =
                (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView =
                (SearchView) menu.findItem(R.id.search).getActionView();
        searchView.setSearchableInfo(
                searchManager.getSearchableInfo(getComponentName()));


        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.barcode_search:
                Intent intent = new Intent(this, ScanBarcodeActivity.class);
                startActivityForResult(intent, 0);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }



    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode==0){
            if(resultCode== CommonStatusCodes.SUCCESS){
                if(data!=null){
                    Barcode barcode = data.getParcelableExtra("barcode");
                    ean = barcode.displayValue;
                    SearchView searchView =
                            (SearchView) optionsMenu.findItem(R.id.search).getActionView();
                    searchView.setQuery(ean, false);
                    searchView.setIconified(false);
                    //new GetContacts().execute();
                }else{
                    ean = "";
                }
            }

        }else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }




    }
