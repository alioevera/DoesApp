package com.example.doesapp;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class EditTaskDesk extends AppCompatActivity {

    private EditText titleDoes, descDoes, dateDoes;
    private Button btnSaveUpdate, btnDelete;
    private DatabaseReference reference;
    private Calendar calendar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_task_desk);

        titleDoes = findViewById(R.id.titleDoes);
        descDoes = findViewById(R.id.descDoes);
        dateDoes = findViewById(R.id.dateDoes);

        btnSaveUpdate = findViewById(R.id.btnSaveUpdate);
        btnDelete = findViewById(R.id.btnDelete);

        // Get values
        titleDoes.setText(getIntent().getStringExtra("titledoes"));
        descDoes.setText(getIntent().getStringExtra("descdoes"));
        dateDoes.setText(getIntent().getStringExtra("datedoes"));

        calendar = Calendar.getInstance();

        // Set click listener for dateDoes EditText to show DatePickerDialog
        dateDoes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePicker();
            }
        });

        final String keyDoes = getIntent().getStringExtra("keydoes");
        reference = FirebaseDatabase.getInstance().getReference().child("DoesApp").child("Does" + keyDoes);

        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteTask(keyDoes);
            }
        });

        btnSaveUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateTask(keyDoes);
            }
        });
    }

    private void showDatePicker() {
        DatePickerDialog datePickerDialog = new DatePickerDialog(
                EditTaskDesk.this,
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
        dateDoes.setText(simpleDateFormat.format(calendar.getTime()));
    }

    private void deleteTask(String keyDoes) {
        reference.removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    Intent intent = new Intent(EditTaskDesk.this, MainActivity.class);
                    startActivity(intent);
                } else {
                    Toast.makeText(getApplicationContext(), "Failed to delete task", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void updateTask(String keyDoes) {
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                dataSnapshot.getRef().child("titledoes").setValue(titleDoes.getText().toString());
                dataSnapshot.getRef().child("descdoes").setValue(descDoes.getText().toString());
                dataSnapshot.getRef().child("datedoes").setValue(dateDoes.getText().toString());
                dataSnapshot.getRef().child("keydoes").setValue(keyDoes);

                Intent intent = new Intent(EditTaskDesk.this, MainActivity.class);
                startActivity(intent);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getApplicationContext(), "Failed to update task", Toast.LENGTH_SHORT).show();
            }
        });
    }
}