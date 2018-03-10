package io.github.gndps.expenseshareapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    List<TransactionParticipant> participantList;
    RecyclerView rvMain;
    CreateTransactionRVAdapter adapter;
    LinearLayoutManager llm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent newActivity = new Intent(getApplicationContext(), CreateTransactionActivity.class);
                startActivity(newActivity);
            }
        });

        loadData();
    }

    private void loadData() {
        participantList = new ArrayList<>();
        rvMain = findViewById(R.id.rv_main);
        adapter = new CreateTransactionRVAdapter(participantList);
        adapter.setMainActivityMode(true);
        llm = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        rvMain.setLayoutManager(llm);
        rvMain.setAdapter(adapter);
    }

    @Override
    protected void onResume() {
        super.onResume();
        MySingleton singleton = MySingleton.getInstance();
        List<Transaction> transactionList = singleton.getTransactionList();
        if(transactionList != null) {
            participantList = createParticipantListFromTL(transactionList);
            adapter.updateParticipantList(participantList);
            adapter.notifyDataSetChanged();
        }
    }

    private List<TransactionParticipant> createParticipantListFromTL(List<Transaction> transactionList) {
        HashMap<String, Integer> finalSettlement = new HashMap<>();
        for(Transaction t : transactionList) {
            List<TransactionParticipant> transactionParticipants = t.getTransaction();
            for(TransactionParticipant tp : transactionParticipants) {
                //update balance
                Integer balance = 0;
                if(finalSettlement.containsKey(tp.getName())) {
                    balance = finalSettlement.get(tp.getName());
                }
                int newBal = balance + tp.getPaid() - tp.getShare();
                finalSettlement.put(tp.getName(), newBal);
            }
        }
        List<TransactionParticipant> list = new ArrayList<>();
        for(String key : finalSettlement.keySet()) {
            String name = key;
            int bal = finalSettlement.get(key);
            TransactionParticipant tp = new TransactionParticipant(name, bal, 0);
            list.add(tp);
        }
        return list;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
