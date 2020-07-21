package com.example.viewinjectdemo;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

import com.example.lib_inject_view_annotation.MyBindView;

public class MainActivity extends AppCompatActivity {

    @MyBindView(R.id.text_view)
    public TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Binding.bind(this);

        textView.setText("View Inject Success");
    }
}
