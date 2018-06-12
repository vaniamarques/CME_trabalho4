package com.example.vania.trabalho4;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.view.KeyEvent;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    protected Intent oIntent;
    protected GesDatabase gesDatabase;
    protected Cursor cursor;
    protected ListView listView;
    protected List<Integer> arrIdDespesa;
    protected ArrayList<HashMap<String, String>> lista_despesas;
    protected Button btnNovaDespesa;
    protected View mHeaderView;
    private TextView mDrawerHeaderTitle;

    @Override
    protected void onStart() {
        super.onStart();
        gesDatabase = new GesDatabase(this).open();

    }
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onStop() {
        super.onStop();
        gesDatabase.close();

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Session session = new Session(getApplicationContext());

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        mHeaderView = navigationView.getHeaderView(0);

        mDrawerHeaderTitle = (TextView) mHeaderView.findViewById(R.id.NomeUser);
        mDrawerHeaderTitle.setText(session.getNomeUser());




        listView = (ListView) findViewById(R.id.list);

        gesDatabase = new GesDatabase(this).open();
        cursor = gesDatabase.obterResumoDespesas(session.getIdUser());

        arrIdDespesa = new ArrayList<>();
        lista_despesas = new ArrayList<>();

        if (cursor.moveToFirst() && cursor!=null) {
            do {
                HashMap<String, String> arr = new HashMap<>();
                arr.put("tipoDespesa", cursor.getString(1));
                arr.put("valorDespesa", Double.toString(cursor.getDouble(2)));
                arr.put("dataDespesa", cursor.getString(3));
                lista_despesas.add(arr);

                arrIdDespesa.add(cursor.getInt(0));

            } while (cursor.moveToNext());
        }

        if(arrIdDespesa.size()!=0) {

            ListAdapter adapter = new SimpleAdapter(
                    this, lista_despesas,
                    R.layout.list_item_resumo, new String[]{"tipoDespesa","valorDespesa", "dataDespesa"}, new int[]{R.id.txvTipoDespesa,
                    R.id.txvValor, R.id.txvData });

            listView.setAdapter(adapter);
        }

        // ListView Item Click Listener
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {

                cursor.moveToPosition(position);
                int index = cursor.getInt(0);

                executarActivity(ViewExpense.class, index);

            }

        });


        btnNovaDespesa = (Button)findViewById(R.id.btnNovaDespesa);
        btnNovaDespesa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                executarActivity(AddExpense.class);

            }
        });

    }
/*
    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }*/





    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.despesas) {
            executarActivity(MainActivity.class);
        } else if (id == R.id.enviar_despesas) {
            executarActivity(SendExpense.class);
        } else if (id == R.id.definicoes) {
            executarActivity(UserSettings.class);
        } else if (id == R.id.sair) {
            executarActivity(Login.class);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    protected void showToast(String mensagem){
        Context context = getApplicationContext();
        CharSequence text = mensagem;
        int duration = Toast.LENGTH_LONG;

        Toast toast = Toast.makeText(context, text, duration);
        toast.show();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event)
    {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
            return  false;
        } else {
            switch (keyCode) {
                case KeyEvent.KEYCODE_BACK:
                    moveTaskToBack(true);
                    return true;
            }
            return false;
        }
    }

    protected void executarActivity(Class<?> subAtividade){
        Intent x = new Intent(this, subAtividade);
        startActivity(x);
    }

    protected void executarActivity(Class<?> subAtividade, Integer indexDespesa){
        Intent x = new Intent(this, subAtividade);
        x.putExtra("indexDespesa", indexDespesa);
        startActivity(x);
    }
}
