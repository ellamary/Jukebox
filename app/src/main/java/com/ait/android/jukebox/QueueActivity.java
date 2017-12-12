package com.ait.android.jukebox;

import android.app.Activity;
import android.content.Intent;
import android.support.design.widget.Snackbar;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.Menu;
import android.view.MenuItem;

import com.ait.android.jukebox.adapter.SearchResultsAdapter;
import com.ait.android.jukebox.adapter.SongAdapter;
import com.ait.android.jukebox.data.Song;
import com.ait.android.jukebox.touch.SongTouchHelperCallback;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

import kaaes.spotify.webapi.android.models.Track;

public class QueueActivity extends AppCompatActivity {

    public static final String KEY_ITEM_ID = "KEY_ITEM_ID";
    public static final int REQUEST_CODE_QUEUE = 1;

    private int positionToEdit = -1;

    public SongAdapter adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_queue);

        //Toolbar toolbar = (Toolbar) findViewById(R.id.my_toolbar);
        //setSupportActionBar(toolbar);

        //toolbar.setLogo(R.mipmap.ic_launcher);

        RecyclerView recyclerViewItem = (RecyclerView) findViewById(R.id.recyclerItem);
        adapter = new SongAdapter(this, FirebaseAuth.getInstance().getCurrentUser().getUid());

        recyclerViewItem.setAdapter(adapter);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setReverseLayout(true);
        layoutManager.setStackFromEnd(true);
        recyclerViewItem.setLayoutManager(layoutManager);
        recyclerViewItem.setAdapter(adapter);

        initPostsListener();

    }


    private void initPostsListener() {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("posts");
        reference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Track track = dataSnapshot.getValue(Track.class);
                adapter.addPost(track, dataSnapshot.getKey());
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                adapter.removePostByKey(dataSnapshot.getKey());
            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("posts");

        if(requestCode == REQUEST_CODE_QUEUE
                && resultCode == Activity.RESULT_OK){
            ArrayList<Track> tempQueue =  data.getParcelableExtra("queue");
            for(Track track : tempQueue) {
                String key = databaseReference.getKey();
                adapter.addPost(track, key);
            }
        }
    }

    //TODO I commented this out because it said "method does not override method from its superclass"
//    @SuppressWarnings("StatementWithEmptyBody")
//    @Override
//    public boolean onNavigationItemSelected(MenuItem item) {
//        // Handle navigation view item clicks here.
//        int id = item.getItemId();
//
//        if (id == R.id.nav_logout) {
//            FirebaseAuth.getInstance().signOut();
//            finish();
//        }
//
//        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
//        drawer.closeDrawer(GravityCompat.START);
//        return true;
//    }
}