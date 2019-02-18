package com.example.agolia001;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Canvas;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.SearchView;
import android.text.Layout;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;

import com.algolia.instantsearch.core.helpers.Searcher;
import com.algolia.instantsearch.ui.helpers.InstantSearch;
import com.algolia.instantsearch.ui.views.Hits;
import com.algolia.instantsearch.ui.views.SearchBox;
import com.algolia.search.saas.AlgoliaException;
import com.algolia.search.saas.Client;
import com.algolia.search.saas.CompletionHandler;
import com.algolia.search.saas.Index;
import com.algolia.search.saas.Query;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class MainActivity extends AppCompatActivity {
    Searcher searcher;


    private static ArrayList<String> lista;

    Hits hits;
    SearchBox box;
    boolean visible;

    final static String ALGOLIA_APP_ID = "LXY84IU2VY";
    final static String ALGOLIA_SEARCH_API_KEY = "7edbbd1e0f88f3b1176f3440ad018d3f";
    final static String ALGOLIA_INDEX_NAME = "productos";




    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);



        final Context context= this;
         hits = findViewById(R.id.hits);
         box = findViewById(R.id.searchBox);

        searcher = Searcher.create(ALGOLIA_APP_ID, ALGOLIA_SEARCH_API_KEY, ALGOLIA_INDEX_NAME);
        final InstantSearch[] helper = new InstantSearch[1];


        box.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                hits.setVisibility(View.VISIBLE);
                visible = true;
                Log.e("onclink","si");
                helper[0] = new InstantSearch((Activity) context, searcher);
                helper[0].search();
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {

                return false;
            }
        });




        ConstraintLayout layat = findViewById(R.id.contrain);

        layat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e("se cierra","si");
                visible =false;
            }
        });





    }
    @Override
    public void onBackPressed() {
        if (visible) {
            Log.e("se cierra","si");
        }else{

        super.onBackPressed();}
    }

    @Override
    protected void onDestroy() {
        searcher.destroy();
        super.onDestroy();
    }
}
