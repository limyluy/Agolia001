package com.example.agolia001;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Canvas;
import android.os.Build;
import android.os.Handler;
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
import android.widget.ProgressBar;
import android.widget.TextView;

import com.algolia.instantsearch.core.events.QueryTextChangeEvent;
import com.algolia.instantsearch.core.events.ResultEvent;
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

    //variables estatica para Algolia
    final static String ALGOLIA_APP_ID = "LXY84IU2VY";
    final static String ALGOLIA_SEARCH_API_KEY = "7edbbd1e0f88f3b1176f3440ad018d3f";
    final static String ALGOLIA_INDEX_NAME = "productos";

    //views
    Hits hits;
    SearchBox box;
    ProgressBar progressBar;
    ConstraintLayout layaut;

    //variable para Algolia
    Searcher searcher;

    //Variables para funcionamiento
     InstantSearch helper;
    private int progressStatus = 0;
    private Handler handler = new Handler();
    SearchView.OnQueryTextListener xd;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final Activity context = this;

        // find para views ancitivty main
        hits = findViewById(R.id.hits);
        box = findViewById(R.id.searchBox);
        progressBar = findViewById(R.id.progressBar);
        layaut = findViewById(R.id.contrain);

        // para hacer funcionar el buscador en algolia
        searcher = Searcher.create(ALGOLIA_APP_ID, ALGOLIA_SEARCH_API_KEY, ALGOLIA_INDEX_NAME);

        //para dar funcionamiento al progresbar y mostar el recycler(hits)
        xd = new SearchView.OnQueryTextListener() {

            @Override
            public boolean onQueryTextSubmit(String s) {
                hits.setVisibility(View.VISIBLE);helper = new InstantSearch(context, searcher);
                helper.search();
                progresbarr();
                return false;
            }
            @Override
            public boolean onQueryTextChange(String s) {
                return false;
            }
        };



        box.setOnQueryTextListener(xd);




        //para cerrar recycler cn cualquier click afuera
        layaut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (hits.getVisibility() == View.VISIBLE) {
                    hits.setVisibility(View.INVISIBLE);
                    progressBar.setVisibility(View.INVISIBLE);
                    box.setOnQueryTextListener(xd);

                }

            }
        });


    }
    //para dar funcion al progresbar durante 3 segundos
    void progresbarr() {
        new Thread(new Runnable() {
            public void run() {
                while (progressStatus < 50) {
                    progressStatus++;
                    // Update the progress bar and display the
                    //current value in the text view
                    handler.post(new Runnable() {
                        public void run() {
                            progressBar.setVisibility(View.VISIBLE);

                        }
                    });
                    try {
                        // Sleep for 200 milliseconds.
                        Thread.sleep(200);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                progressBar.setVisibility(View.INVISIBLE);
            }
        }).start();
    }

    //para cerrar recycler cuado de da tecla atras
    @Override
    public void onBackPressed() {
        if (hits.getVisibility() == View.VISIBLE) {
            hits.setVisibility(View.INVISIBLE);
            progressBar.setVisibility(View.INVISIBLE);

            box.setOnQueryTextListener(xd);
        } else {
            super.onBackPressed();
        }

    }

    //para que el search se estruya
    @Override
    protected void onDestroy() {
        searcher.destroy();
        super.onDestroy();
    }


}

