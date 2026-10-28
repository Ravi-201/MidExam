package com.example.mid_exam_mad;


import android.app.Activity;
import android.app.AlertDialog;
import android.database.Cursor;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

public class MainActivity extends Activity {

    EditText studentName, studentId;
    Spinner majorSpinner;
    DatabaseHelper database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        studentName = findViewById(R.id.studentName);
        studentId = findViewById(R.id.studentId);
        majorSpinner = findViewById(R.id.majorSpinner);

        Button add = findViewById(R.id.addButton);
        Button edit = findViewById(R.id.editButton);
        Button delete = findViewById(R.id.deleteButton);
        Button view = findViewById(R.id.viewButton);

        database = new DatabaseHelper(this);

        add.setOnClickListener(v -> {
            String id = studentId.getText().toString().trim();
            String name = studentName.getText().toString().trim();
            String major = majorSpinner.getSelectedItem().toString();

            if (id.isEmpty() || name.isEmpty()) {
                message("Enter student name and ID");
            } else if (database.addStudent(id, name, major)) {
                message("Student added successfully");
            } else {
                message("Student ID already exists");
            }
        });

        edit.setOnClickListener(v -> {
            String id = studentId.getText().toString().trim();
            String name = studentName.getText().toString().trim();
            String major = majorSpinner.getSelectedItem().toString();

            if (id.isEmpty() || name.isEmpty()) {
                message("Enter student name and ID");
            } else if (database.updateStudent(id, name, major)) {
                message("Student updated successfully");
            } else {
                message("Student not found");
            }
        });

        delete.setOnClickListener(v -> {
            String id = studentId.getText().toString().trim();

            if (id.isEmpty()) {
                message("Enter student ID");
                return;
            }

            new AlertDialog.Builder(this)
                    .setTitle("Delete Student")
                    .setMessage("Delete student ID " + id + "?")
                    .setPositiveButton("Yes", (dialog, which) -> {
                        if (database.deleteStudent(id)) {
                            studentName.setText("");
                            studentId.setText("");
                            majorSpinner.setSelection(0);
                            message("Student deleted");
                        } else {
                            message("Student not found");
                        }
                    })
                    .setNegativeButton("No", null)
                    .show();
        });

        view.setOnClickListener(v -> {
            String id = studentId.getText().toString().trim();

            if (id.isEmpty()) {
                message("Enter student ID");
                return;
            }

            try (Cursor cursor = database.getStudent(id)) {
                if (cursor.moveToFirst()) {
                    String name = cursor.getString(1);
                    String major = cursor.getString(2);

                    studentName.setText(name);

                    if (major.equals("Information Systems")) {
                        majorSpinner.setSelection(1);
                    } else {
                        majorSpinner.setSelection(0);
                    }

                    new AlertDialog.Builder(this)
                            .setTitle("Student Details")
                            .setMessage("Student Name: " + name +
                                    "\nStudent ID: " + id +
                                    "\nMajor: " + major)
                            .setPositiveButton("OK", null)
                            .show();
                } else {
                    message("Student not found");
                }
            }
        });
    }

    private void message(String text) {
        Toast.makeText(this, text, Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onDestroy() {
        database.close();
        super.onDestroy();
    }
}
