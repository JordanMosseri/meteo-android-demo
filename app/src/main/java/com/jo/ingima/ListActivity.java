package com.jo.ingima;

import android.content.BroadcastReceiver;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;

public class ListActivity extends AppCompatActivity implements ModelSingleton.InteractionsReceiver, ListCallback {

    private RecyclerView mRecyclerAll;
    private AllAdapter mAdapter;
    private SwipeRefreshLayout mSwipeRefreshLayout;

    private final BroadcastReceiver messengerActivityReceiver = ModelSingleton.buildRootReceiver(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ModelSingleton.getInstance(ListActivity.this).initAtLaunch();

        mRecyclerAll = (RecyclerView) findViewById(R.id.recycler_all);
        mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.allRefreshLayout);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        mAdapter = new AllAdapter(this, this);
        mRecyclerAll.setAdapter(mAdapter);
        mRecyclerAll.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerAll.setItemAnimator(new DefaultItemAnimator());

        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                ModelSingleton.getInstance(ListActivity.this).initAtLaunch();
                mSwipeRefreshLayout.setRefreshing(false);
            }
        });

        registerReceiver(messengerActivityReceiver, ModelSingleton.buildFilterReceiver());

        mSwipeRefreshLayout.setColorSchemeResources(R.color.myAccentColor);

    }

    @Override
    protected void onDestroy() {
        unregisterReceiver(messengerActivityReceiver);
        super.onDestroy();
    }

    @Override
    public void onReceiveMessagesSynchronised() {
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void onItemClicked(String cityCode) {
        openDetailsActivity(cityCode);
    }

    public void openDetailsActivity(final String cityCode) {
        Intent intent = new Intent(this, MyActivity.class);
        intent.putExtra(MyActivity.CITY_CODE, cityCode);
        startActivity(intent);
        //overridePendingTransition(R.anim.slide_from_bottom, R.anim.fade_out_custom);
    }
}
