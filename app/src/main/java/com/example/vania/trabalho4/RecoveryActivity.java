package com.example.vania.trabalho4;

import android.content.Context;
import android.content.Intent;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Random;

public class RecoveryActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    Button buttonRecover;
    EditText editTextEmailRecover;
    GenerateDatabase sqliteHelper;
    GesDatabase gesdatabase;
    //Aqui supostamente dá erro //
    Context context = getApplicationContext();
    int duration = Toast.LENGTH_SHORT;
    protected View mHeaderView;
    private TextView mDrawerHeaderTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recovery);

        gesdatabase = new GesDatabase(this);
        sqliteHelper = new GenerateDatabase(this);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        editTextEmailRecover = (EditText) findViewById(R.id.editTextPassword);
        buttonRecover = (Button) findViewById(R.id.buttonLogin);

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


        buttonRecover.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String new_password = getRandomString(10);
                String email = editTextEmailRecover.getText().toString();

                boolean verifica_email = gesdatabase.isEmailExists(email);
                if(verifica_email){

                    if( gesdatabase.updatePassword(new_password, email)){
                        Intent i = new Intent(Intent.ACTION_SEND);
                        i.setType("message/rfc822");
                        i.putExtra(Intent.EXTRA_EMAIL  , new String[]{email.toString()});
                        i.putExtra(Intent.EXTRA_SUBJECT, "Recover Password");
                        i.putExtra(Intent.EXTRA_TEXT   , "A sua nova password Ã© "+new_password);
                        try {
                            startActivity(Intent.createChooser(i, "Email enviado com sucesso!!!"));
                        } catch (android.content.ActivityNotFoundException ex) {
                            Toast.makeText(RecoveryActivity.this, "There are no email clients installed.", Toast.LENGTH_SHORT).show();
                        }
                    }
                    else{
                        CharSequence text = "Email nao encontrado!";
                        Toast toast = Toast.makeText(context, text, duration);
                        toast.show();
                    }


                }


            }
        });
    }

    private static final String ALLOWED_CHARACTERS ="0123456789qwertyuiopasdfghjklzxcvbnm";

    private static String getRandomString(final int sizeOfPasswordString){
        final Random random=new Random();
        final StringBuilder sb=new StringBuilder(sizeOfPasswordString);

        for(int i=0;i<sizeOfPasswordString;++i){
            sb.append(ALLOWED_CHARACTERS.charAt(random.nextInt(ALLOWED_CHARACTERS.length())));
            return sb.toString();
        }
        return null;
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
