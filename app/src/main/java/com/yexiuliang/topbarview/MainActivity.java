package com.yexiuliang.topbarview;

import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Toast;

import com.yexiuliang.library.TopBarView;

public class MainActivity extends AppCompatActivity {

    private TopBarView mTopBarView;
    private BaseTopBarCenterTextAdapter mAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_topbar);
        mTopBarView = (TopBarView) findViewById(R.id.topbar);
        View viewById = findViewById(R.id.button);
        setTitle("TopBarActivity");
        viewById.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                mAdapter.setCenterTextStr("点击之后");
            }
        });
        mAdapter = new BaseTopBarCenterTextAdapter(this);
        mTopBarView.setAdapter(mAdapter);
        mTopBarView.setOnCenterClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(MainActivity.this, "center", Toast.LENGTH_SHORT).show();
            }
        });
        mTopBarView.setOnLeftClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(MainActivity.this, "left", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
