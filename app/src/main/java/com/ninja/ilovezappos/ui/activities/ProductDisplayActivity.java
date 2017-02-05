package com.ninja.ilovezappos.ui.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.widget.TextView;

import com.ninja.ilovezappos.R;

public class ProductDisplayActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initToolbar();
    }

    private void initToolbar() {
        Toolbar mToolbar = (Toolbar) findViewById(R.id.toolbar_activity_main);
        TextView mToolbarTitle = (TextView) findViewById(R.id.toolbar_activity_main_text_view);

        setSupportActionBar(mToolbar);
        mToolbarTitle.setText(getResources().getString(R.string.app_name));
    }
}
