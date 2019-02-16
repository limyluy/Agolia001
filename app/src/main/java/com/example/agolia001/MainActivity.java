package com.example.agolia001;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.algolia.instantsearch.core.helpers.Searcher;
import com.algolia.instantsearch.ui.helpers.InstantSearch;

public class MainActivity extends AppCompatActivity {
    Searcher searcher;

    final static String ALGOLIA_APP_ID = "LXY84IU2VY";
    final static String ALGOLIA_SEARCH_API_KEY ="7edbbd1e0f88f3b1176f3440ad018d3f";
    final static String ALGOLIA_INDEX_NAME = "productos";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


       searcher = Searcher.create(ALGOLIA_APP_ID, ALGOLIA_SEARCH_API_KEY, ALGOLIA_INDEX_NAME);
        InstantSearch helper = new InstantSearch(this, searcher);
        helper.search();

     
    }

    @Override
    protected void onDestroy() {
        searcher.destroy();
        super.onDestroy();
    }
}
