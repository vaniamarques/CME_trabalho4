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
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class UserSettings extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    protected Button save;
    protected EditText username, email, password, passwordrepeat;
    protected GenerateDatabase sqliteHelper;
    protected GesDatabase gesdatabase;
    //protected Context context = getApplicationContext();
    protected int duration = Toast.LENGTH_SHORT;
    protected View mHeaderView;
    private TextView mDrawerHeaderTitle;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_settings);
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

        gesdatabase = new GesDatabase(this);

        sqliteHelper = new GenerateDatabase(this);

        save = (Button) findViewById(R.id.saveSettings);
        username = (EditText) findViewById(R.id.editUsername);
        email = (EditText) findViewById(R.id.editEmail);
        password = (EditText) findViewById(R.id.editPassword);
        passwordrepeat = (EditText) findViewById(R.id.editPasswordRepeat);


        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String new_username = username.getText().toString();
                String new_email = email.getText().toString();
                String new_password = password.getText().toString();
                String new_password_repeat = passwordrepeat.getText().toString();
                if(new_password==new_password_repeat){
                    Session session = new Session(getApplicationContext());
                    Integer iduser = session.getIdUser();
                    User newuser = new User(null, new_username, new_email, new_password);
                    boolean verifica_email = gesdatabase.isEmailExists(new_email);
                    if(!verifica_email){

                        if( gesdatabase.updateUser(newuser)){
                            CharSequence text = "Alterado com sucesso!";

                            Toast toast = Toast.makeText(getApplicationContext(), text, duration);
                            toast.show();
                        }
                        else{
                            CharSequence text = "Erro ao alterar dados!";
                            Toast toast = Toast.makeText(getApplicationContext(), text, duration);
                            toast.show();
                        }


                    }
                }
                else{

                    CharSequence text = "A password que inseriu não coincide";


                    Toast toast = Toast.makeText(getApplicationContext(), text, duration);
                    toast.show();
                }
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

    protected void executarActivity(Class<?> subAtividade){
        Intent x = new Intent(this, subAtividade);
        startActivity(x);
    }
}
