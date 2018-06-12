package com.example.vania.trabalho4;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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

    Context context = getApplicationContext();
    int duration = Toast.LENGTH_SHORT;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recover);

        gesdatabase = new GesDatabase(this);
        sqliteHelper = new GenerateDatabase(this);

        editTextEmailRecover = (EditText) findViewById(R.id.editTextPassword);
        buttonRecover = (Button) findViewById(R.id.buttonLogin);

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
                            Toast.makeText(RecoverActivity.this, "There are no email clients installed.", Toast.LENGTH_SHORT).show();
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
}
