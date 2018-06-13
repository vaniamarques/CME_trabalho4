package com.example.vania.trabalho4;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.regex.Pattern;

public class Register extends AppCompatActivity {

    protected Button buttonRegister;
    protected EditText editTextEmail, editTextPassword, editTextUsername;
    protected GenerateDatabase sqliteHelper;
    protected GesDatabase gesdatabase;
    protected TextView txvVoltar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        gesdatabase = new GesDatabase(this);

        sqliteHelper = new GenerateDatabase(this);

        buttonRegister = (Button) findViewById(R.id.button_register);

        editTextEmail = (EditText) findViewById(R.id.register_email);
        editTextPassword = (EditText) findViewById(R.id.register_password);
        editTextUsername = (EditText) findViewById(R.id.register_username);
        txvVoltar = (TextView)findViewById(R.id.txvVoltar);

        buttonRegister.setOnClickListener(new View.OnClickListener() {
            @Override

            public void onClick(View view) {

                String Username = editTextUsername.getText().toString();
                String Email = editTextEmail.getText().toString();
                String Password = editTextPassword.getText().toString();

                if(isValidEmail(Email)) {
                    User newuser = new User(null, Username, Email, Password);
                    boolean verifica_email = gesdatabase.isEmailExists(Email);
                    if(!verifica_email){
                        gesdatabase.addUser(newuser);

                        Context context = getApplicationContext();
                        CharSequence text = "Adicionado com sucesso!";
                        int duration = Toast.LENGTH_SHORT;

                        Toast toast = Toast.makeText(context, text, duration);
                        toast.show();

                        Intent intent = new Intent(Register.this, Login.class);
                        startActivity(intent);

                    }else{
                        Toast.makeText(Register.this, "E-mail já registado", Toast.LENGTH_SHORT).show();
                    }




                }
                else{
                    Context context = getApplicationContext();
                    CharSequence text = "Por favor, insira um email valido!";
                    int duration = Toast.LENGTH_SHORT;

                    Toast toast = Toast.makeText(context, text, duration);
                    toast.show();
                }


            }
        });

        txvVoltar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent x = new Intent(getApplicationContext(), Login.class);
                startActivity(x);
            }
        });
    }

    private boolean isValidEmail(String email) {
        Pattern pattern = Patterns.EMAIL_ADDRESS;
        return pattern.matcher(email).matches();
    }
}