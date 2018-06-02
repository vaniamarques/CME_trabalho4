package com.example.vania.trabalho4;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class UserSettingsActivity extends AppCompatActivity {
    Button save;
    EditText username, email, password, passwordrepeat;
    GenerateDatabase sqliteHelper;
    GesDatabase gesdatabase;
    Context context = getApplicationContext();
    int duration = Toast.LENGTH_SHORT;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_settings);
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

                            Toast toast = Toast.makeText(context, text, duration);
                            toast.show();
                        }
                        else{
                            CharSequence text = "Erro ao alterar dados!";
                            Toast toast = Toast.makeText(context, text, duration);
                            toast.show();
                        }


                    }
                }
                else{

                    CharSequence text = "A password que inseriu não coincide";
                    

                    Toast toast = Toast.makeText(context, text, duration);
                    toast.show();
                }

            }
        });


    }
}
