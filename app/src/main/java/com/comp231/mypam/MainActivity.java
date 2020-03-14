package com.comp231.mypam;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.comp231.mypam.database.DataSource;
import com.comp231.mypam.model.Category;
import com.comp231.mypam.sample.SampleDataProvider;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    List<Category> categoryList = SampleDataProvider.categoryItemList;
    DataSource mDataSource;
    private Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //creating the database
        mDataSource = new DataSource(this);
        mDataSource.open();
        mDataSource.seedDataBase(categoryList);

        final List<Category> listFromDb = mDataSource.getAllItems();
        List<String> itemNames = new ArrayList<>();

        for (Category item: listFromDb) {
            itemNames.add(item.getCategoryName());
        }

        Collections.sort(listFromDb, new Comparator<Category>() {
            @Override
            public int compare(Category o1, Category o2) {
                return o1.getCategoryName().compareTo(o2.getCategoryName());
            }});

        //DataItemAdapter adapter = new DataItemAdapter(this,listFromDb);
        DataItemAdapterListView adapter = new DataItemAdapterListView(this,listFromDb);
        //DataItemAdapterListView adapter = new DataItemAdapterListView(this,categoryList);

        //RecyclerView listView = (RecyclerView) findViewById(android.R.id.list);
        ListView listView = findViewById(android.R.id.list);

        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @RequiresApi(api = Build.VERSION_CODES.Q)
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
                                    long arg3) {
                Intent intent = new Intent(getApplicationContext(),CategoryActivity.class);
                String message = listFromDb.get(arg2).getCategoryId();
                intent.putExtra("category", message);
                startActivity(intent);
            }

        });

        FloatingActionButton fabAdd = findViewById(R.id.fabAdd);

        fabAdd.setOnClickListener(new View.OnClickListener(){
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(), CategoryActivity.class);
                i.putExtra("add", "add");
                startActivity(i);
            }
        });
    }
}
