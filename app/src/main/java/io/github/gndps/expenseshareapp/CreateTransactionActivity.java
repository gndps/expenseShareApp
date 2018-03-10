package io.github.gndps.expenseshareapp;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class CreateTransactionActivity extends AppCompatActivity {

    EditText etTransactionDetails, etAmount;
    Button buttonAddParticipant;
    Button buttonAddTransaction;
    RecyclerView rvCreateTransaction;
    List<TransactionParticipant> participantList;
    CreateTransactionRVAdapter adapter;
    LinearLayoutManager llm;

    // Declare
    static final int PICK_CONTACT = 1;
    // Request code for READ_CONTACTS. It can be any number > 0.
    private static final int PERMISSIONS_REQUEST_READ_CONTACTS = 100;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_transaction);
        initUi();
        initializeFunctionality();
    }

    private void initUi() {
        etTransactionDetails = findViewById(R.id.et_transaction_detail);
        etAmount = findViewById(R.id.et_transaction_amount);
        buttonAddParticipant = findViewById(R.id.button_add_participant);
        buttonAddTransaction = findViewById(R.id.button_add_transaction);
        rvCreateTransaction = findViewById(R.id.rv_create_transaction);
    }

    private void initializeFunctionality() {

        participantList = new ArrayList<>();
        adapter = new CreateTransactionRVAdapter(participantList);
        llm = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        rvCreateTransaction.setLayoutManager(llm);
        rvCreateTransaction.setAdapter(adapter);

        buttonAddParticipant.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pickContact();
            }
        });

        buttonAddTransaction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addTransaction();
            }
        });
    }

    private void addTransaction() {
        //write a form validate method

        participantList = adapter.getParticipantList();
        Transaction transaction = new Transaction(participantList);

        //save realm transaction or keep in singleton
        MySingleton singleton = MySingleton.getInstance();
        List<Transaction> transactionList = singleton.getTransactionList();
        transactionList.add(transaction);
        singleton.setTransactionList(transactionList);

        //return to the home page
        finish();
    }

    private void showContacts() {
        // Check the SDK version and whether the permission is already granted or not.

    }

    //code
    @Override
    public void onActivityResult(int reqCode, int resultCode, Intent data) {
        super.onActivityResult(reqCode, resultCode, data);
        switch (reqCode) {
            case (PICK_CONTACT):
                if (resultCode == Activity.RESULT_OK) {

                    Uri contactData = data.getData();
                    Cursor c = managedQuery(contactData, null, null, null, null);
                    if (c.moveToFirst()) {


                        String id = c.getString(c.getColumnIndexOrThrow(ContactsContract.Contacts._ID));

                        String hasPhone = c.getString(c.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER));

                        if (hasPhone.equalsIgnoreCase("1")) {
                            Cursor phones = getContentResolver().query(
                                    ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null,
                                    ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = " + id,
                                    null, null);
                            phones.moveToFirst();
                            String cNumber = phones.getString(phones.getColumnIndex("data1"));
                            System.out.println("number is:" + cNumber);
                        }
                        String name = c.getString(c.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));

                        //contact selected
                        //add this contact to participant list
                        TransactionParticipant tp = new TransactionParticipant(name, 0, 0);
                        if(participantList!= null && !participantList.contains(tp)) {
                            participantList.add(tp);
                        }

                        adapter.updateParticipantList(participantList);
                        adapter.notifyDataSetChanged();

                    }
                }
                break;
        }
    }

    private void pickContact() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && checkSelfPermission(Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.READ_CONTACTS}, PERMISSIONS_REQUEST_READ_CONTACTS);
            //After this point you wait for callback in onRequestPermissionsResult(int, String[], int[]) overriden method
        } else {
            // Android version is lesser than 6.0 or the permission is already granted.
            Intent intent = new Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI);
            startActivityForResult(intent, PICK_CONTACT);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions,
                                           int[] grantResults) {
        if (requestCode == PERMISSIONS_REQUEST_READ_CONTACTS) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission is granted
                Intent intent = new Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI);
                startActivityForResult(intent, PICK_CONTACT);
            } else {
                Toast.makeText(this, "Until you grant the permission, we canot display the names", Toast.LENGTH_SHORT).show();
            }
        }
    }

}
