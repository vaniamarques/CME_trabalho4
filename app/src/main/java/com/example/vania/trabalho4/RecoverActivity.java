package com.example.vania.trabalho4;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.Random;

public class RecoverActivity extends AppCompatActivity {
    Button buttonRecover;
    EditText editTextEmailRecover;

    GenerateDatabase sqliteHelper;
    GesDatabase gesdatabase;
    int duration = Toast.LENGTH_SHORT;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recover);

        gesdatabase = new GesDatabase(this).open();
        sqliteHelper = new GenerateDatabase(this);

        editTextEmailRecover = (EditText) findViewById(R.id.email);
        buttonRecover = (Button) findViewById(R.id.buttonRecover);

        buttonRecover.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String new_password = getRandomString(10);
                String email = editTextEmailRecover.getText().toString();

                boolean verifica_email = gesdatabase.isEmailExists(email);
                if(verifica_email){

                    if( gesdatabase.updatePassword(new_password, email)){

                        if(SendEmail.main(email, new_password)){
                            Toast.makeText(RecoverActivity.this, "E-mail enviado!", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(RecoverActivity.this, Login.class);
                            startActivity(intent);
                        }

                    }
                    else{
                        CharSequence text = "Email nao encontrado!";
                    }


                }else{
                    Toast.makeText(RecoverActivity.this, "E-mail não está registado!", Toast.LENGTH_SHORT).show();
                }


            }
        });
    }

    private static final String ALLOWED_CHARACTERS ="0123456789qwertyuiopasdfghjklzxcvbnm";

    private static String getRandomString(final int sizeOfRandomString)
    {
        final Random random=new Random();
        final StringBuilder sb=new StringBuilder(sizeOfRandomString);
        for(int i=0;i<sizeOfRandomString;++i)
            sb.append(ALLOWED_CHARACTERS.charAt(random.nextInt(ALLOWED_CHARACTERS.length())));
        return sb.toString();
    }
}
