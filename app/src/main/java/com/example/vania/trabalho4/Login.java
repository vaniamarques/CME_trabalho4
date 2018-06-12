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

public class Login extends AppCompatActivity {

    protected EditText editTextEmail;
    protected EditText editTextPassword;
    protected TextView createAccount, recoverPassword;;
    protected Button buttonLogin;
    protected GenerateDatabase sqliteHelper;
    protected GesDatabase gesdatabase;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        gesdatabase = new GesDatabase(this);

        sqliteHelper = new GenerateDatabase(this);

        createAccount = (TextView) findViewById(R.id.createAccount);
        editTextEmail = (EditText) findViewById(R.id.editTextEmail);
        editTextPassword = (EditText) findViewById(R.id.editTextPassword);
        buttonLogin = (Button) findViewById(R.id.buttonLogin);
        recoverPassword = (TextView) findViewById(R.id.recoverPassword);


        //set click event of login button


        createAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Login.this, Register.class);

                startActivity(intent);

            }
        });
        recoverPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Login.this, RecoveryActivity.class);

                startActivity(intent);

            }
        });




        buttonLogin.setOnClickListener(new View.OnClickListener() {
            @Override

            public void onClick(View view) {


                String Email = editTextEmail.getText().toString();
                String Password = editTextPassword.getText().toString();

                if(isValidEmail(Email)) {


                    User currentUser = gesdatabase.Authenticate(new User(null, null, Email, Password));
                    if(currentUser!=null) {
                        Session session = new Session(getApplicationContext());

                        session.setIdUser(Integer.parseInt(currentUser.id));
                        session.setNomeUser(currentUser.userName);

                        Intent intent = new Intent(Login.this, MainActivity.class);
                        startActivity(intent);
                    }
                    else{
                        CharSequence text ;
                        text = "Dados Incorretos!";
                        Context context = getApplicationContext();

                        int duration = Toast.LENGTH_SHORT;

                        Toast toast = Toast.makeText(context, text, duration);
                        toast.show();
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
    }

    private boolean isValidEmail(String email) {
        Pattern pattern = Patterns.EMAIL_ADDRESS;
        return pattern.matcher(email).matches();
    }
}