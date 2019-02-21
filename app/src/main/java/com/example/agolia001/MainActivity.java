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
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.text.Editable;
import android.text.Layout;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.algolia.instantsearch.core.events.QueryTextChangeEvent;
import com.algolia.instantsearch.core.events.ResultEvent;
import com.algolia.instantsearch.core.helpers.Searcher;
import com.algolia.instantsearch.ui.helpers.InstantSearch;
import com.algolia.instantsearch.ui.utils.ItemClickSupport;
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

import org.json.JSONArray;
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
    EditText edtBuscar;

    //variable para Algolia
    Searcher searcher;

    //Variables para funcionamiento
    InstantSearch helper;
    private int progressStatus = 0;
    private Handler handler = new Handler();


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final Activity context = this;

        // find para views ancitivty main
        hits = findViewById(R.id.hits);
        box = findViewById(R.id.searchBox);
        progressBar = findViewById(R.id.progressBar);
        layaut = findViewById(R.id.contrain);
        edtBuscar = findViewById(R.id.edt_buscar);

        // para hacer funcionar el buscador en algolia
        searcher = Searcher.create(ALGOLIA_APP_ID, ALGOLIA_SEARCH_API_KEY, ALGOLIA_INDEX_NAME);
        helper = new InstantSearch(context, searcher);

        // para que el seacher trabae por medio del edidtext
        edtBuscar.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (event.getAction() == KeyEvent.ACTION_DOWN) {
                    switch (keyCode) {
                        case KeyEvent.KEYCODE_DPAD_CENTER:
                        case KeyEvent.KEYCODE_ENTER:
                            realizarBusqueda();
                            return true;
                        default:
                            break;
                    }
                }
                return false;
            }
        });


        //para cerrar recycler cn cualquier click afuera
        layaut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (hits.getVisibility() == View.VISIBLE) {
                    hits.setVisibility(View.INVISIBLE);
                    progressBar.setVisibility(View.INVISIBLE);
                }
            }
        });

        //para dar oncliklistener a los pruductos encontrados
        hits.setOnItemClickListener(new ItemClickSupport.OnItemClickListener() {
            @Override
            public void onItemClick(RecyclerView recyclerView, int position, View v) {
                JSONObject hit = hits.get(position);
                String ola = (String) hit.opt("objectID");

                if (ola == null) {
                    Toast.makeText(context, "Producto no encontrado", Toast.LENGTH_SHORT).show();
                    return;
                }
                Toast.makeText(context, "Producto Id " + ola, Toast.LENGTH_SHORT).show();
            }
        });

    }

    //metodo par realizar la bustuqueda, espeficificamente a utilizar el String que nos da el Edittext
    private void realizarBusqueda() {
        if (edtBuscar.getText().toString().isEmpty()) return;
        String palabra = edtBuscar.getText().toString();
        helper.enableProgressBar();
        helper.search(palabra);
        hits.setVisibility(View.VISIBLE);
        progresbarr();


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

    //=====================================Metodos Para el onclick en el recylcerview===================


}

