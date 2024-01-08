package com.example.doesapp;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.app.DatePickerDialog;
import android.widget.DatePicker;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Random;

public class NewTaskActivity extends AppCompatActivity {

    private TextView titlepage, addtitle, adddesc, adddate;
    private EditText titledoes, descdoes, datedoes;
    private Button btnSaveTask, btnCancelTask;
    private DatabaseReference reference;
    private Integer doesNum = new Random().nextInt();

    private Calendar calendar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_task);

        // Initialize views
        titlepage = findViewById(R.id.titlepage);
        addtitle = findViewById(R.id.addtitle);
        adddesc = findViewById(R.id.adddesc);
        adddate = findViewById(R.id.adddate);

        titledoes = findViewById(R.id.titledoes);
        descdoes = findViewById(R.id.descdoes);
        datedoes = findViewById(R.id.datedoes);

        btnSaveTask = findViewById(R.id.btnSaveTask);
        btnCancelTask = findViewById(R.id.btnCancelTask);

        // Initialize Typefaces
        Typeface MLight = Typeface.createFromAsset(getAssets(), "fonts/ML.ttf");
        Typeface MMedium = Typeface.createFromAsset(getAssets(), "fonts/MM.ttf");

        // Set Typeface for TextView and EditText
        titlepage.setTypeface(MMedium);
        addtitle.setTypeface(MLight);
        titledoes.setTypeface(MMedium);

        adddesc.setTypeface(MLight);
        descdoes.setTypeface(MMedium);

        adddate.setTypeface(MLight);
        datedoes.setTypeface(MMedium);

        btnSaveTask.setTypeface(MMedium);
        btnCancelTask.setTypeface(MLight);

        // Initialize calendar instance
        calendar = Calendar.getInstance();

        // Set click listener for datedoes EditText to show DatePickerDialog
        datedoes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePicker();
            }
        });

        // Set onClickListener for the btnSaveTask button
        btnSaveTask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveTaskToDatabase();
            }
        });

        btnCancelTask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Navigasi ke MainActivity
                Intent intent = new Intent(NewTaskActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

    private void showDatePicker() {
        DatePickerDialog datePickerDialog = new DatePickerDialog(
                NewTaskActivity.this,
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        calendar.set(Calendar.YEAR, year);
                        calendar.set(Calendar.MONTH, monthOfYear);
                        calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                        updateDate();
                    }
                },
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)
        );
        datePickerDialog.show();
    }

    private void updateDate() {
        String dateFormat = "dd/MM/yyyy";
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(dateFormat, Locale.getDefault());
        datedoes.setText(simpleDateFormat.format(calendar.getTime()));
    }

    private void saveTaskToDatabase() {
        reference = FirebaseDatabase.getInstance().getReference("DoesApp").child("Does" + doesNum);

        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                dataSnapshot.getRef().child("titledoes").setValue(titledoes.getText().toString());
                dataSnapshot.getRef().child("descdoes").setValue(descdoes.getText().toString());
                dataSnapshot.getRef().child("datedoes").setValue(datedoes.getText().toString());
                dataSnapshot.getRef().child("keydoes").setValue(doesNum.toString());

                Intent intent = new Intent(NewTaskActivity.this, MainActivity.class);
                startActivity(intent);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getApplicationContext(), "Failed to save task", Toast.LENGTH_SHORT).show();
            }
        });
    }
}