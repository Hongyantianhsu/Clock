package com.clock;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;

public class MainActivity extends AppCompatActivity {
    //时钟样式
    private static final String[] CLOCK_MODES = {"clock01","clock02"};
    private ListView listview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        initUI();
        setContentView(listview);
    }

    private void initUI() {
        listview =new ListView(this);
        listview.setDividerHeight(1);
        listview.setAdapter(new ListViewAdapter());
        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                switch (i){
                    case 0:
                        Clock01Activity.activityStart(MainActivity.this);
                        break;
                    case 1:
                        break;
                }
            }
        });
    }

    class ListViewAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return CLOCK_MODES.length;
        }

        @Override
        public Object getItem(int i) {
            return null;
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            View view1 = View.inflate(MainActivity.this,R.layout.listview_item,null);
            return view1;
        }
    }
}
