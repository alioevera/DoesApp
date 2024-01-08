package com.example.doesapp;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    TextView titlepage, subtitlepage, endpage;
    Button btnAddNew;
    DatabaseReference reference;
    RecyclerView ourdoes;
    ArrayList<MyDoes> list;
    DoesAdapter doesAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Inisialisasi view
        titlepage = findViewById(R.id.titlepage);
        subtitlepage = findViewById(R.id.subtitlepage);
        endpage = findViewById(R.id.endpage);
        btnAddNew = findViewById(R.id.btnAddNew);
        ourdoes = findViewById(R.id.ourdoes);

        // Inisialisasi Typeface
        Typeface MLight = Typeface.createFromAsset(getAssets(), "fonts/ML.ttf");
        Typeface MMedium = Typeface.createFromAsset(getAssets(), "fonts/MM.ttf");

        // Set Typeface untuk TextView dan Button
        titlepage.setTypeface(MMedium);
        subtitlepage.setTypeface(MLight);
        endpage.setTypeface(MLight);
        btnAddNew.setTypeface(MLight);

        // Set OnClickListener untuk Button
        btnAddNew.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent a = new Intent(MainActivity.this, NewTaskActivity.class);
                startActivity(a);
            }
        });

        // working with data
        ourdoes = findViewById(R.id.ourdoes);
        ourdoes.setLayoutManager(new LinearLayoutManager(this));
        list = new ArrayList<MyDoes>();

        // Mengambil data dari Firebase
        reference = FirebaseDatabase.getInstance().getReference().child("DoesApp");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                    MyDoes p = dataSnapshot1.getValue(MyDoes.class);
                    list.add(p);
                }
                Collections.sort(list, new Comparator<MyDoes>() {
                    DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault());

                    @Override
                    public int compare(MyDoes does1, MyDoes does2) {
                        try {
                            Date date1 = dateFormat.parse(does1.getDatedoes()); // Ganti dengan getter tanggal Anda
                            Date date2 = dateFormat.parse(does2.getDatedoes()); // Ganti dengan getter tanggal Anda

                            if (date1 != null && date2 != null) {
                                return date2.compareTo(date1);
                            }
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                        return 0;
                    }
                });
                doesAdapter = new DoesAdapter(MainActivity.this, list);
                ourdoes.setAdapter(doesAdapter);
                doesAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getApplicationContext(), "No Data", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
