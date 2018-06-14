package com.example.vania.trabalho4;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;

public class EditExpense extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    protected Intent oIntent;
    protected GesDatabase gesDatabase;
    protected Cursor cursor;
    protected Button btnGuardar, btnChoose;
    protected EditText edtValorDespesa, edtDataDespesa, edtHoraDespesa;
    protected Integer indexDespesa;
    protected Spinner spinner;
    protected List<String> arrTipoDespesa;
    protected Integer pos;
    protected String tipoDespesa;
    protected ImageView imageView;
    private Bitmap bp;
    protected View mHeaderView;
    private TextView mDrawerHeaderTitle;

    final int REQUEST_CODE_GALLERY = 999;

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
        setContentView(R.layout.activity_edit_expense);
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

        edtValorDespesa = (EditText) findViewById(R.id.edtValorDespesa);
        edtDataDespesa = (EditText) findViewById(R.id.edtDataDespesa);
        edtHoraDespesa = (EditText) findViewById(R.id.edtHoraDespesa);
        spinner = (Spinner) findViewById(R.id.spinnerTipoDespesa);
        imageView = (ImageView)findViewById(R.id.imageView);

        arrTipoDespesa = new ArrayList<String>();
        arrTipoDespesa.add("Combustíveis");
        arrTipoDespesa.add("Refeições");
        arrTipoDespesa.add("Portagens");
        arrTipoDespesa.add("Outra");

        gesDatabase = new GesDatabase(this).open();

        cursor = gesDatabase.obterDespesaEspecifica(indexDespesa);
        if (cursor.moveToFirst()) {
            tipoDespesa = cursor.getString(1);
            edtValorDespesa.setText(""+cursor.getDouble(2));
            edtDataDespesa.setText(cursor.getString(3));
            edtHoraDespesa.setText(cursor.getString(4));

            byte[] image = cursor.getBlob(5);

            Bitmap bitmap = BitmapFactory.decodeByteArray(image, 0, image.length);
            imageView.setImageBitmap(bitmap);
            imageView.setVisibility(View.VISIBLE);
        }

        pos = arrTipoDespesa.indexOf(tipoDespesa);

        ArrayAdapter<String> oAdaptador = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, arrTipoDespesa);
        oAdaptador.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(oAdaptador);
        spinner.setSelection(pos);




        btnGuardar = (Button) findViewById(R.id.btnGuardar);
        btnGuardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                if (validaCampos(edtValorDespesa, edtDataDespesa, edtHoraDespesa)) {


                    spinner.getSelectedItem();


                    Boolean resultado = gesDatabase.updateDespesa(indexDespesa, spinner.getSelectedItem().toString(), Double.parseDouble(edtValorDespesa.getText().toString()), edtDataDespesa.getText().toString(), edtHoraDespesa.getText().toString(), imageViewToByte(imageView));

                    String[] arrMensagem = {"Erro ao guardar!", "Guardado com sucesso!"};

                    if (resultado) {
                        showToast(arrMensagem[1]);

                        executarActivity(ViewExpense.class, indexDespesa);


                    } else {
                        showToast(arrMensagem[0]);
                    }
                }

            }
        });


        btnChoose = (Button)findViewById(R.id.btnChoose);
        btnChoose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ActivityCompat.requestPermissions(
                        EditExpense.this,
                        new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                        REQUEST_CODE_GALLERY
                );
            }
        });
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            executarActivity(ViewExpense.class, indexDespesa);
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

    //COnvert and resize our image to 400dp for faster uploading our images to DB
    protected Bitmap decodeUri(Uri selectedImage, int REQUIRED_SIZE) {

        try {

            // Decode image size
            BitmapFactory.Options o = new BitmapFactory.Options();
            o.inJustDecodeBounds = true;
            BitmapFactory.decodeStream(getContentResolver().openInputStream(selectedImage), null, o);

            // The new size we want to scale to
            // final int REQUIRED_SIZE =  size;

            // Find the correct scale value. It should be the power of 2.
            int width_tmp = o.outWidth, height_tmp = o.outHeight;
            int scale = 1;
            while (true) {
                if (width_tmp / 2 < REQUIRED_SIZE
                        || height_tmp / 2 < REQUIRED_SIZE) {
                    break;
                }
                width_tmp /= 2;
                height_tmp /= 2;
                scale *= 2;
            }

            // Decode with inSampleSize
            BitmapFactory.Options o2 = new BitmapFactory.Options();
            o2.inSampleSize = scale;
            return BitmapFactory.decodeStream(getContentResolver().openInputStream(selectedImage), null, o2);
        }
        catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    public static byte[] imageViewToByte(ImageView image) {
        Bitmap bitmap = ((BitmapDrawable)image.getDrawable()).getBitmap();
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 0, stream);
        byte[] byteArray = stream.toByteArray();
        return byteArray;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        if(requestCode == REQUEST_CODE_GALLERY){
            if(grantResults.length >0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType("image/*");
                startActivityForResult(intent, REQUEST_CODE_GALLERY);
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

        if(requestCode == REQUEST_CODE_GALLERY && resultCode == RESULT_OK && data != null){
            Uri uri = data.getData();


            bp=decodeUri(uri, 400);

                /*InputStream inputStream = getContentResolver().openInputStream(bp);

                Bitmap bitmap = BitmapFactory.decodeStream(inputStream);*/
            imageView.setImageBitmap(bp);


        }

        super.onActivityResult(requestCode, resultCode, data);
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

    protected void showToast(String mensagem) {
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
}
