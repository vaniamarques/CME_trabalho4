package com.example.vania.trabalho4;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Environment;
import android.os.StrictMode;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;

public class SendExpense extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    EditText et_email;
    EditText et_subject;
    EditText et_message;
    Button Send;
    Button Attachment;
    String email;
    String subject;
    String message;
    String attachmentFile;
    Uri URI = null;
    private static final int PICK_FROM_GALLERY = 101;
    int columnIndex;
    protected View mHeaderView;
    private TextView mDrawerHeaderTitle;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_expense);
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


        et_email = (EditText) findViewById(R.id.et_to);
        et_subject = (EditText) findViewById(R.id.et_subject);
        et_message = (EditText) findViewById(R.id.et_message);
        Attachment = (Button) findViewById(R.id.bt_attachment);
        Send = (Button) findViewById(R.id.bt_send);
        //send button listener
        Send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendEmail();
            }
        });

        Attachment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openFolder();
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

    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        if (requestCode == PICK_FROM_GALLERY && resultCode == RESULT_OK) {

            Uri selectedImage = data.getData();

            // Will return "image:x*"
            String wholeID = DocumentsContract.getDocumentId(selectedImage);
            // Split at colon, use second item in the array
            String id = wholeID.split(":")[1];
            // where id is equal to
            String sel = MediaStore.Images.Media._ID + "=?";


            String[] filePathColumn = { MediaStore.Images.Media.DATA };
            Cursor cursor = getContentResolver().query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,filePathColumn, sel, new String [] { id }, null);

            columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            if (cursor.moveToFirst()) {
                URI = Uri.parse("file://" + cursor.getString(columnIndex));
            }

            cursor.close();
        }

    }
    public void sendEmail()
    {
        try {
            email = et_email.getText().toString();
            subject = et_subject.getText().toString();
            message = et_message.getText().toString();
            final Intent emailIntent = new Intent(
                    android.content.Intent.ACTION_SEND);
            emailIntent.setType("plain/text");
            emailIntent.putExtra(android.content.Intent.EXTRA_EMAIL,
            new String[] { email });
            emailIntent.putExtra(android.content.Intent.EXTRA_SUBJECT,
                    subject);
            if (URI != null) {
                emailIntent.putExtra(Intent.EXTRA_STREAM, URI);
            }
            emailIntent
                    .putExtra(android.content.Intent.EXTRA_TEXT, message);
            this.startActivity(Intent.createChooser(emailIntent,
                    "Sending email..."));

        } catch (Throwable t) {
            Toast.makeText(this,
                    "Request failed try again: " + t.toString(),
                    Toast.LENGTH_LONG).show();
        }

    }

    public void openFolder()
    {
        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        intent.putExtra("return-data", true);
        startActivityForResult(
                Intent.createChooser(intent, "Complete action using"),
                PICK_FROM_GALLERY);
    }
}