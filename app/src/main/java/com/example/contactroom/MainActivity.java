package com.example.contactroom;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.example.contactroom.adapter.RecyclerViewAdapter;
import com.example.contactroom.model.Contact;
import com.example.contactroom.model.ContactViewModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;
import java.util.Objects;

public class MainActivity extends AppCompatActivity {
    private static final int NEW_CONTACT_ACTIVITY_REQUEST_CODE = 1;
    private ContactViewModel contactViewModel;
    private TextView textView;
    private RecyclerView recyclerView;
    private RecyclerViewAdapter recyclerViewAdapter;
    private LiveData<List<Contact>> contacList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerView = findViewById(R.id.recycler_view);

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));


        contactViewModel = new ViewModelProvider.AndroidViewModelFactory(MainActivity.this
                .getApplication())
                .create(ContactViewModel.class);
        contactViewModel.getAllContacts().observe(this, contacts -> {

            //Khoi tao adapter
            recyclerViewAdapter = new RecyclerViewAdapter(contacts,MainActivity.this);
            recyclerView.setAdapter(recyclerViewAdapter);
//            StringBuilder builder = new StringBuilder();
//            for(Contact contact : contacts){
//                builder.append("-").append(contact.getName()).append(" ").append(contact.getOccupation());
//                Log.d("TAG", "onCreate: "+contact.getName());
//            }
        });


        FloatingActionButton fab = findViewById(R.id.addContact);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, NewContact.class);
                startActivityForResult(intent, NEW_CONTACT_ACTIVITY_REQUEST_CODE);
            }
        });
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == NEW_CONTACT_ACTIVITY_REQUEST_CODE && resultCode == RESULT_OK){
            assert data != null;
            String name = data.getStringExtra(NewContact.NAME_REPLY);
            String occupation = data.getStringExtra(NewContact.OCCUPATION);

            assert name != null;
            Contact contact  = new Contact(name,occupation);

            ContactViewModel.insert(contact);
        }
    }
}