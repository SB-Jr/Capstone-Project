package com.project.sbjr.showledger.ui.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.project.sbjr.showledger.R;

public class TvShowDetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tv_show_detail);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_tv_show_detail,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.done: addShowToWatchedList();
                break;
            case R.id.watch_later: addShowToWatchLaterList();
                break;
            case R.id.incomplete: addShowToIncompleteList();
                break;
        }
        return super.onOptionsItemSelected(item);
    }


    private void addShowToWatchedList(){

    }

    private void addShowToWatchLaterList(){

    }

    private void addShowToIncompleteList(){
        //todo: add a dialog box to ask the the seasons and episodes the user has watched
    }
}
