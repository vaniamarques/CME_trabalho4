package com.example.vania.trabalho4;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;


public class ViewExpense extends AppCompatActivity {

    protected Intent oIntent;
    protected GesDatabase gesDatabase;
    protected Cursor cursor;
    protected Integer indexDespesa;
    protected Button btnEditar, btnEliminar;
    protected TextView txvTipoDespesa, txvValorDespesa, txvDataDespesa, txvHoraDespesa;
    protected final Context context = this;

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

        oIntent = getIntent();
        indexDespesa = oIntent.getExtras().getInt("indexDespesa");



        txvTipoDespesa = (TextView)findViewById(R.id.txvTipoDespesa);
        txvValorDespesa = (TextView)findViewById(R.id.txvValorDespesa);
        txvDataDespesa = (TextView)findViewById(R.id.txvDataDespesa);
        txvHoraDespesa = (TextView)findViewById(R.id.txvHoraDespesa);

        cursor = null;
        gesDatabase = new GesDatabase(this).open();


        cursor = gesDatabase.obterDespesaEspecifica(indexDespesa);
        if (cursor.moveToFirst()) {
            txvTipoDespesa.setText("Tipo Despesa: "+cursor.getString(1));
            txvValorDespesa.setText("Valor: "+cursor.getDouble(2));
            txvDataDespesa.setText("Data: "+cursor.getString(3));
            txvHoraDespesa.setText("Hora: "+cursor.getString(4));
        }

        btnEditar = (Button)findViewById(R.id.btnEditar);
        btnEditar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                executarActivity(EditExpenseActivity.class, indexDespesa);
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
                                executarActivity(ListExpenseActivity.class);
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

    @Override
    public void onBackPressed() {
        executarActivity(ListExpenseActivity.class);
        finish();
        super.onBackPressed();
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
