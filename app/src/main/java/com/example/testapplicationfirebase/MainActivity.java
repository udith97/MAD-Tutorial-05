package com.example.testapplicationfirebase;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.dynamic.IFragmentWrapper;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MainActivity extends AppCompatActivity {

    EditText txtID, txtName, txtAdd, txtConNo;
    Button btnSave, btnUpdate, btnShow, btnDelete;
    DatabaseReference dbRef;
    Student std;

    private void clearControls(){
        txtID.setText("");
        txtName.setText("");
        txtAdd.setText("");
        txtConNo.setText("");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        txtID = findViewById(R.id.EtID);
        txtName = findViewById(R.id.EtName);
        txtAdd = findViewById(R.id.EtAddress);
        txtConNo = findViewById(R.id.EtConNo);

        btnSave = findViewById(R.id.BtnSave);
        btnShow = findViewById(R.id.BtnShow);
        btnUpdate = findViewById(R.id.BtnUpdate);
        btnDelete = findViewById(R.id.BtnDelete);

        std = new Student();

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dbRef = FirebaseDatabase.getInstance().getReference().child("Student");
                try {
                    if(TextUtils.isEmpty(txtID.getText().toString()))
                        Toast.makeText(getApplicationContext(), "Please enter an ID", Toast.LENGTH_SHORT).show();
                    else if (TextUtils.isEmpty(txtName.getText().toString()))
                        Toast.makeText(getApplicationContext(), "Please enter a Name", Toast.LENGTH_SHORT).show();
                    else if (TextUtils.isEmpty(txtAdd.getText().toString()))
                        Toast.makeText(getApplicationContext(), "Please enter an Address", Toast.LENGTH_SHORT).show();
                    else{
                        std.setID(txtID.getText().toString().trim());
                        std.setName(txtName.getText().toString().trim());
                        std.setAddress(txtAdd.getText().toString().trim());
                        std.setConNo(Integer.parseInt(txtConNo.getText().toString().trim()));

                        dbRef.child("Std1").setValue(std);

                        Toast.makeText(getApplicationContext(), "Data Saved Successfully", Toast.LENGTH_SHORT).show();
                        clearControls();
                    }
                }
                catch (NumberFormatException e){
                    Toast.makeText(getApplicationContext(), "Invalid Contact Number", Toast.LENGTH_SHORT).show();
                }
            }
        });

        btnShow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatabaseReference readRef = FirebaseDatabase.getInstance().getReference().child("Student").child("Std1");
                readRef.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (dataSnapshot.hasChildren()){
                            txtID.setText(dataSnapshot.child("id").getValue().toString());
                            txtName.setText(dataSnapshot.child("name").getValue().toString());
                            txtAdd.setText(dataSnapshot.child("address").getValue().toString());
                            txtConNo.setText(dataSnapshot.child("conNo").getValue().toString());
                        }
                        else
                            Toast.makeText(getApplicationContext(), "No sourse to display", Toast.LENGTH_SHORT).show();

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }
        });

        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatabaseReference upRef = FirebaseDatabase.getInstance().getReference().child("Student");
                upRef.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (dataSnapshot.hasChild("Std1")){
                            try {
                                std.setID(txtID.getText().toString().trim());
                                std.setName(txtName.getText().toString().trim());
                                std.setAddress(txtAdd.getText().toString().trim());
                                std.setConNo(Integer.parseInt(txtConNo.getText().toString().trim()));

                                dbRef = FirebaseDatabase.getInstance().getReference().child("Student").child("Std1");
                                dbRef.setValue(std);
                                clearControls();

                                Toast.makeText(getApplicationContext(), "Data Updated Successfully", Toast.LENGTH_SHORT).show();
                            }
                            catch (NumberFormatException e){
                                Toast.makeText(getApplicationContext(),"Invalid Contact Number", Toast.LENGTH_SHORT).show();

                            }
                        }
                        else
                            Toast.makeText(getApplicationContext(), "No Source to Update", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }
        });

        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatabaseReference delRef = FirebaseDatabase.getInstance().getReference().child("Student");
                delRef.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (dataSnapshot.hasChild("Std1")){
                            dbRef = FirebaseDatabase.getInstance().getReference().child("Student").child("Std1");
                            dbRef.removeValue();
                            clearControls();
                            Toast.makeText(getApplicationContext(),"Data Deleted Successfully", Toast.LENGTH_SHORT).show();

                        }
                        else
                            Toast.makeText(getApplicationContext(), "No Source to Delete", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }
        });


    }
}
