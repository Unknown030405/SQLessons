package com.example.valera;

import androidx.appcompat.app.AppCompatActivity;
import interactor.*;
import domain.*;

import android.content.ClipData;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    AutoCompleteTextView inputLogin;
    EditText inputPassword;
    Button buttonLogin;
    Button buttonRegister;
    Context context;

    UserInteractor usersInteractor;

    ArrayList<User> lastFiveUsersList = new ArrayList<>();
    ArrayList<String> lastFiveNamesList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();
        initListeners();
    }

    private void init() {
        inputLogin = findViewById(R.id.input_login);
        inputPassword = findViewById(R.id.input_pass);
        buttonLogin = findViewById(R.id.loginButton);
        buttonRegister = findViewById(R.id.registerButton);
        context = this;

        usersInteractor = new UserInteractor(this);
        fillInputLogin();
    }

    private void initListeners() {
        inputLogin.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String pass = "";
                for(User user : lastFiveUsersList){
                    if(parent.getItemAtPosition(position).equals(user.name)){
                        pass = user.password;
                    }
                }
                Log.d("Friendship check", lastFiveNamesList.get(position) + " " + lastFiveUsersList.get(position).toString());
                inputPassword.setText(pass);
            }
        });

        buttonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String login = inputLogin.getText().toString();
                final String password = inputPassword.getText().toString();
                User user = usersInteractor.getUser(login, password);
                if (user != null) {
                    Toast.makeText(context, "Login", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(MainActivity.this, ActivitySecond.class);
                    intent.putExtra("User", user);
                    startActivity(intent);
                } else {
                    Toast.makeText(context, "User not found", Toast.LENGTH_SHORT).show();
                }
            }
        });
        buttonRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String login = inputLogin.getText().toString();
                final String password = inputPassword.getText().toString();
                if (usersInteractor.insertUser(login, password)) {
                    Toast.makeText(context, "Registered", Toast.LENGTH_SHORT).show();
                    fillInputLogin();
                } else {
                    Toast.makeText(context, "User is already there", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void fillInputLogin() {
        lastFiveUsersList = usersInteractor.getUsers(5);

        if (lastFiveUsersList != null) {
            lastFiveNamesList.clear();
            for (User user : lastFiveUsersList) {
                lastFiveNamesList.add(user.name);
            }
            inputLogin.setAdapter(new ArrayAdapter<String>(this,
                    android.R.layout.simple_spinner_dropdown_item,
                    lastFiveNamesList));
            Log.d("Friendship check", lastFiveUsersList.toString());
        }
    }
}
