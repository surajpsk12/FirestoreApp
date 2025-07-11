package com.surajvanshsv.firestoreapp;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

public class MainActivity extends AppCompatActivity {
    private Button saveBtn;
    private Button updateBtn;
    private Button deleteBtn;
    private Button readBtn;
    private TextView textView;
    private EditText nameET;
    private EditText emailET;




    // Firebase Firestore:
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private DocumentReference friendsRef = db.collection("Users")
            .document("Y1GdivbmEs4SJxME3q6L");

    private CollectionReference collectionReference = db.collection("Users");



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);


        nameET = findViewById(R.id.nameET);
        emailET = findViewById(R.id.emailET);
        textView = findViewById(R.id.text);
        saveBtn = findViewById(R.id.SaveBTN);
        deleteBtn = findViewById(R.id.deleteBTN);
        updateBtn = findViewById(R.id.updateBTN);
        readBtn = findViewById(R.id.readBTN);

        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SaveDataToNewDocument();
            }
        });

        readBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GetAllDocumentsInCollection();
            }
        });

        updateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UpdateSpecificDocument();
            }
        });

        deleteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DeleteAll();
            }
        });



        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }
    private void SaveDataToNewDocument(){
        String name = nameET.getText().toString();
        String email = emailET.getText().toString();

        Friend friend = new Friend(name, email);

        collectionReference.add(friend).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
            @Override
            public void onSuccess(DocumentReference documentReference) {
                String docId = documentReference.getId();
            }
        });
    }

    private void GetAllDocumentsInCollection(){
        collectionReference.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                String data = "";

                // This code is executed when data retrieval is successful
                // the queryDocumentSnapshot contains the documents in the collection
                // Each queryDocumentSnapshot ---> represents a document in the collection
                for (QueryDocumentSnapshot snapshot : queryDocumentSnapshots){

                    // Transforming snapshots into objects
                    Friend friend = snapshot.toObject(Friend.class);

                    data += "Name: "+friend.getName() + " Email: "+friend.getEmail()+"\n";



                }

                textView.setText(data);

            }
        });
    }

    private void UpdateSpecificDocument(){
        String name = nameET.getText().toString();
        String email = emailET.getText().toString();

        friendsRef.update("name" , name);
        friendsRef.update("email" , email);
    }

    private void DeleteAll(){
        friendsRef.delete();

    }
}