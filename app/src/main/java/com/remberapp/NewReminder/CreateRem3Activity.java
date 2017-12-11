package com.remberapp.NewReminder;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Color;
import android.provider.ContactsContract;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import com.remberapp.R;
import java.util.ArrayList;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;



public class CreateRem3Activity extends AppCompatActivity {
    Bundle bundle = new Bundle();
    private String title = "";
    private String description = "";
    private String date;
    private String number = "";
    ListView listView ;
    ArrayList<String> StoreContacts ;
    ArrayAdapter<String> arrayAdapter ;
    Cursor cursor ;
    String name, phonenumber ;
    public static final int RequestPermissionCode  = 1 ;
    EditText inputSearch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_rem3);
        Intent intentExtras = getIntent();
        bundle = intentExtras.getExtras();
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        if (bundle != null) {
            title = bundle.getString("Title");
            description = bundle.getString("Description");
            date = bundle.getString("Date");
        }
        TextView numberView = findViewById(R.id.numberView);
        numberView.setText("Choose a contact.");
        TextView titleView = findViewById(R.id.title);
        titleView.setText(title);
        TextView dateView = findViewById(R.id.date);
        dateView.setText(date);

        listView = (ListView)findViewById(R.id.listview1);

        StoreContacts = new ArrayList<String>();

        EnableRuntimePermission();

        GetContactsIntoArrayList();
        inputSearch = (EditText) findViewById(R.id.inputSearch);
        arrayAdapter = new ArrayAdapter<String>(
                CreateRem3Activity.this,
                android.R.layout.simple_list_item_1,
                android.R.id.text1, StoreContacts
        );

        inputSearch.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence cs, int arg1, int arg2, int arg3) {
                // When user changed the Text
                CreateRem3Activity.this.arrayAdapter.getFilter().filter(cs);
            }

            @Override
            public void beforeTextChanged(CharSequence arg0, int arg1, int arg2,
                                          int arg3) {
                // TODO Auto-generated method stub

            }

            @Override
            public void afterTextChanged(Editable arg0) {
                // TODO Auto-generated method stub
            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position,
                                    long id) {
                Object item = parent.getItemAtPosition(position);
                Button create = findViewById(R.id.button);
                String name = item.toString().split("\n")[0];
                number = item.toString().split("\n")[1];
                numberView.setText(name + " (" + number.replaceAll(" ","") + ")");

                create.setBackgroundResource(R.drawable.rounded_shape);
                create.setTextColor(Color.WHITE);
            }
        });

        listView.setAdapter(arrayAdapter);
    }

    public void GoBack(View view) {
        Intent intent = new Intent(this, CreateRem2Activity.class);
        intent.putExtras(bundle);
        startActivity(intent);
    }

    public void GetContactsIntoArrayList(){

        cursor = getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null,null, null, null);

        while (cursor.moveToNext()) {

            name = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));

            phonenumber = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER)).replaceAll(" ","");;
            if (phonenumber.length() > 8) {
                StoreContacts.add(name + " "  + "\n" + " " + phonenumber);
            }
        }

        cursor.close();

    }

    public void CreateReminder(View view) {
        if (number != "") {
            //TODO: Send info to DB. @number(String), @title(String), @description(String), @dateTime(String)
            //TODO: Check if @number exists in DB.
            //TODO: Check if successfull.
        } else {
            Toast.makeText(CreateRem3Activity.this,"Choose a contact!", Toast.LENGTH_LONG).show();
        }
    }

    public void EnableRuntimePermission(){

        if (ActivityCompat.shouldShowRequestPermissionRationale(
                CreateRem3Activity.this,
                android.Manifest.permission.READ_CONTACTS))
        {

            Toast.makeText(CreateRem3Activity.this,"Contacts permission allows us to Access contacts app", Toast.LENGTH_LONG).show();

        } else {

            ActivityCompat.requestPermissions(CreateRem3Activity.this,new String[]{
                    android.Manifest.permission.READ_CONTACTS}, RequestPermissionCode);

        }
    }

    @Override
    public void onRequestPermissionsResult(int RC, String per[], int[] PResult) {

        switch (RC) {

            case RequestPermissionCode:

                if (PResult.length > 0 && PResult[0] == PackageManager.PERMISSION_GRANTED) {

                    //Toast.makeText(CreateRem3Activity.this,"Access to contacts granted.", Toast.LENGTH_LONG).show();

                } else {

                    Toast.makeText(CreateRem3Activity.this,"Access to contacts declined.", Toast.LENGTH_LONG).show();

                }
                break;
        }
    }
}
