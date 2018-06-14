package com.example.vania.trabalho4;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.FileNotFoundException;
import java.io.InputStream;

public class ViewExpense extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    protected Intent oIntent;
    protected GesDatabase gesDatabase;
    protected Cursor cursor;
    protected Integer indexDespesa;
    protected Button btnEditar, btnEliminar;
    protected TextView txvTipoDespesa, txvValorDespesa, txvDataDespesa, txvHoraDespesa;
    protected final Context context = this;
    protected ImageView imageView;
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
        setContentView(R.layout.activity_view_expense);
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


        oIntent = getIntent();
        indexDespesa = oIntent.getExtras().getInt("indexDespesa");



        txvTipoDespesa = (TextView)findViewById(R.id.txvTipoDespesa);
        txvValorDespesa = (TextView)findViewById(R.id.txvValorDespesa);
        txvDataDespesa = (TextView)findViewById(R.id.txvDataDespesa);
        txvHoraDespesa = (TextView)findViewById(R.id.txvHoraDespesa);
        imageView = (ImageView)findViewById(R.id.imageView);

        cursor = null;
        gesDatabase = new GesDatabase(this).open();


        cursor = gesDatabase.obterDespesaEspecifica(indexDespesa);
        if (cursor.moveToFirst()) {

            byte[] image = cursor.getBlob(5);

            Bitmap bitmap = BitmapFactory.decodeByteArray(image, 0, image.length);
            imageView.setImageBitmap(bitmap);
            imageView.setVisibility(View.VISIBLE);

            txvTipoDespesa.setText("Tipo Despesa: "+cursor.getString(1));
            txvValorDespesa.setText("Valor: "+cursor.getDouble(2));
            txvDataDespesa.setText("Data: "+cursor.getString(3));
            txvHoraDespesa.setText("Hora: "+cursor.getString(4));
        }

        btnEditar = (Button)findViewById(R.id.btnEditar);
        btnEditar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                executarActivity(EditExpense.class, indexDespesa);
            }
        });

        btnEliminar = (Button)findViewById(R.id.btnEliminar);
        btnEliminar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);

                alertDialogBuilder.setTitle("Deseja mesmo eliminar esta despesa?");

                alertDialogBuilder
                        .setCancelable(false)
                        .setPositiveButton("Sim",new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,int id) {
                                gesDatabase.deleteDespesa(indexDespesa);
                                showToast("Eliminado com sucesso!");
                                executarActivity(MainActivity.class);
                            }
                        })
                        .setNegativeButton("Não",new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,int id) {
                                dialog.cancel();
                            }
                        });

                AlertDialog alertDialog = alertDialogBuilder.create();
                alertDialog.show();
            }
        });

    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            executarActivity(MainActivity.class);
        }
    }





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

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        if(requestCode == 888){
            if(grantResults.length >0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType("image/*");
                startActivityForResult(intent, 888);
            }
            else {
                Toast.makeText(getApplicationContext(), "You don't have permission to access file location!", Toast.LENGTH_SHORT).show();
            }
            return;
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if(requestCode == 888 && resultCode == RESULT_OK && data != null){
            Uri uri = data.getData();
            try {
                InputStream inputStream = getContentResolver().openInputStream(uri);
                Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                //imageView.setImageBitmap(bitmap);

            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }

        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onSaveInstanceState(Bundle outputState) {
        if (indexDespesa!=null) {
            outputState.putInt("indexDespesa", indexDespesa);
        }
        super.onSaveInstanceState(outputState);
    }

    protected void restoreVarsFromBundle(Bundle savedInstanceState) {
        Integer idDespesa = savedInstanceState.getInt("indexDespesa");
        if (indexDespesa!=null)
            indexDespesa = idDespesa;
    }


    protected void showToast(String mensagem){
        Context context = getApplicationContext();
        CharSequence text = mensagem;
        int duration = Toast.LENGTH_LONG;

        Toast toast = Toast.makeText(context, text, duration);
        toast.show();
    }

    protected void executarActivity(Class<?> subAtividade, Integer indexDespesa){
        Intent x = new Intent(this, subAtividade);
        x.putExtra("indexDespesa", indexDespesa);
        startActivity(x);
    }

    protected void executarActivity(Class<?> subAtividade){
        Intent x = new Intent(this, subAtividade);
        startActivity(x);
    }
}
