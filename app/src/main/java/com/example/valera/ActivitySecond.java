package com.example.valera;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import adapter.Adapter;
import domain.User;
import interactor.UserInteractor;

public class ActivitySecond extends AppCompatActivity {

    RecyclerView rv;
    UserInteractor UI;
    Activity act;
    Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);
        rv = findViewById(R.id.recycler_view);
        UI = new UserInteractor(this);
        act = this;
        context = this;

        rv.setLayoutManager(new LinearLayoutManager(this));
        rv.setAdapter(new Adapter(UI.GetFriends((User) getIntent().getSerializableExtra("User")), this, (User) getIntent().getSerializableExtra("User")));
        Button logOutButton = findViewById(R.id.log_out_button);
        TextView tv = findViewById(R.id.user_name);
        tv.setText(((User) getIntent().getSerializableExtra("User")).name);
        logOutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context, "Logged out", Toast.LENGTH_SHORT).show();
                act.finish();
            }
        });
    }

}
