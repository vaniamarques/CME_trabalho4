package com.example.vania.trabalho4;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class AddExpenseActivity extends AppCompatActivity {

    protected Intent oIntent;
    protected GesDatabase gesDatabase;
    protected Cursor cursor;
    protected Integer indexDespesa;
    protected Button btnAdd;
    protected EditText edtValorDespesa, edtDataDespesa, edtHoraDespesa;
    protected Spinner spinner;
    protected List<String> arrTipoDespesa;
    protected Integer pos;
    protected String tipoDespesa;
    protected Session session;

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
        setContentView(R.layout.activity_add_expense);

        session = new Session(getApplicationContext());
        Log.v("teste", session.getIdUser().toString());


        edtValorDespesa = (EditText)findViewById(R.id.edtValorDespesa);
        edtDataDespesa = (EditText)findViewById(R.id.edtDataDespesa);
        edtHoraDespesa = (EditText)findViewById(R.id.edtHoraDespesa);
        spinner = (Spinner) findViewById(R.id.spinnerTipoDespesa);

        arrTipoDespesa = new ArrayList<String>();
        arrTipoDespesa.add("Combustíveis");
        arrTipoDespesa.add("Refeições");
        arrTipoDespesa.add("Portagens");
        arrTipoDespesa.add("Outra");

        ArrayAdapter<String> oAdaptador = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, arrTipoDespesa);
        oAdaptador.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(oAdaptador);

        gesDatabase = new GesDatabase(this).open();

        btnAdd = (Button)findViewById(R.id.btnAdd);
        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(validaCampos(edtValorDespesa, edtDataDespesa, edtHoraDespesa)){

                    Boolean resultado = gesDatabase.insertDespesa(session.getIdUser(), spinner.getSelectedItem().toString(), Double.parseDouble(edtValorDespesa.getText().toString()), edtDataDespesa.getText().toString(), edtHoraDespesa.getText().toString(), "");

                    String[] arrMensagem = {"Erro ao inserir a despesa!", "Despesa inserida com sucesso!"};
                    if (resultado) {
                        showToast(arrMensagem[1]);
                        edtValorDespesa.setText("");
                        edtDataDespesa.setText("");
                        edtHoraDespesa.setText("");
                        executarActivity(ListExpenseActivity.class);
                    } else {
                        showToast(arrMensagem[0]);
                    }
                }
            }
        });



    }


    @Override
    public void onSaveInstanceState(Bundle outputState) {
        String s = edtValorDespesa.getText().toString();
        if (!s.equals("")) {
            outputState.putString("edtValorDespesa", s);
        }

        String s2 = edtDataDespesa.getText().toString();
        if (!s2.equals("")) {
            outputState.putString("edtDataDespesa", s2);
        }

        String s3 = edtHoraDespesa.getText().toString();
        if (!s3.equals("")) {
            outputState.putString("edtHoraDespesa", s3);
        }

        Integer positionSpinner = spinner.getSelectedItemPosition();
        if (positionSpinner!=null) {
            outputState.putInt("spinnerTipoDespesa", positionSpinner);
        }

        if (indexDespesa!=null) {
            outputState.putInt("indexDespesa", indexDespesa);
        }

        super.onSaveInstanceState(outputState);
    }

    protected void restoreVarsFromBundle(Bundle savedInstanceState) {
        String s = savedInstanceState.getString("edtValorDespesa");
        if (!s.equals(""))
            edtValorDespesa.setText(s);

        String s2 = savedInstanceState.getString("edtDataDespesa");
        if (!s2.equals(""))
            edtDataDespesa.setText(s2);

        String s3 = savedInstanceState.getString("edtHoraDespesa");
        if (!s3.equals(""))
            edtHoraDespesa.setText(s3);

        Integer positionSpinner = savedInstanceState.getInt("spinnerTipoDespesa");
        if (positionSpinner!=null)
            spinner.setSelection(positionSpinner);

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

    protected Boolean validaCampos(EditText edtValorDespesa, EditText edtDataDespesa, EditText edtHoraDespesa) {
        Boolean b = false;
        if (edtValorDespesa.getText().toString().equals("")) {
            edtValorDespesa.requestFocus();
            showToast("Campo Valor obrigatório!");
        }
        else if(edtDataDespesa.getText().toString().equals("")) {
            edtDataDespesa.requestFocus();
            showToast("Campo Data obrigatório!");
        }
        else if(edtHoraDespesa.getText().toString().equals("")) {
            edtHoraDespesa.requestFocus();
            showToast("Campo Valor obrigatório!");
        }
        else
            b = true;

        return b;
    }

    @Override
    public void onBackPressed() {
        executarActivity(ListExpenseActivity.class);
        finish();
        super.onBackPressed();
    }

    protected void executarActivity(Class<?> subAtividade){
        Intent x = new Intent(this, subAtividade);
        startActivity(x);
    }

    protected void executarActivity(Class<?> subAtividade, Integer indexCategoria){
        Intent x = new Intent(this, subAtividade);
        x.putExtra("indexCategoria", indexCategoria);
        startActivity(x);
    }
}
