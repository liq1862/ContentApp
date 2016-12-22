package com.example.user.contentapp;

import android.Manifest;
import android.content.ContentResolver;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.widget.SimpleCursorAdapter;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;

import static android.Manifest.permission.READ_CONTACTS;
import static android.Manifest.permission.WRITE_CONTACTS;

public class MainActivity extends AppCompatActivity {

    private static final int REQUEST_CONTACTS = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);



        int permission = ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_CONTACTS);

        if(permission != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this, new String[] {READ_CONTACTS, WRITE_CONTACTS},REQUEST_CONTACTS);
        }
        else{
//            readContacts();
        }
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode){
            case REQUEST_CONTACTS:
                if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    readContacts();
                }
                else{
                    AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                    builder.setMessage("使用此應用程式需允許聯絡人權限才可顯示資訊");
                    builder.setPositiveButton("確定", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            ActivityCompat.requestPermissions(MainActivity.this, new String[] {READ_CONTACTS, WRITE_CONTACTS},REQUEST_CONTACTS);
                        }
                    });
                    builder.show();
                }
                return;
        }
    }

    private void readContacts() {
        ListView lv = (ListView) findViewById(R.id.listView);

        ContentResolver resolver = getContentResolver();
        Cursor cursor = resolver.query(ContactsContract.Contacts.CONTENT_URI,
                null,null,null,null);

        SimpleCursorAdapter scadapter = new SimpleCursorAdapter(this,
                android.R.layout.simple_list_item_1,
                cursor,new String[] {ContactsContract.Contacts.DISPLAY_NAME},
                new int[] {android.R.id.text1},0);
        lv.setAdapter(scadapter);

//        while (cursor.moveToNext()){
//            int id = cursor.getInt(cursor.getColumnIndex(ContactsContract.Contacts._ID));
//
//            String name = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
//
//            Log.d("RECORD",id+"/"+name);
//        }
    }


}
