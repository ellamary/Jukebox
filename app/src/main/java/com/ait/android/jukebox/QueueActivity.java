package com.ait.android.jukebox;

import android.app.Activity;
import android.content.Intent;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.ait.android.jukebox.adapter.SearchResultsAdapter;
import com.ait.android.jukebox.adapter.SongAdapter;
import com.ait.android.jukebox.data.Song;
import com.ait.android.jukebox.data.SongList;
import com.ait.android.jukebox.touch.SongTouchHelperCallback;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


import java.util.ArrayList;
import java.util.List;

import kaaes.spotify.webapi.android.models.Track;

import static com.ait.android.jukebox.MainActivity.token;

public class QueueActivity extends AppCompatActivity implements Preview.View {

    public static final String KEY_ITEM_ID = "KEY_ITEM_ID";
    public static final int REQUEST_CODE_QUEUE = 1;

    private int positionToEdit = -1;
    private Preview.ActionListener mActionListener;

    public static List<Song> songsToPlay;
//    String token = CredentialsHandler.getToken(this);


    public SongAdapter adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_queue);

        //Toolbar toolbar = (Toolbar) findViewById(R.id.my_toolbar);
        //setSupportActionBar(toolbar);

        //toolbar.setLogo(R.mipmap.ic_launcher);
        songsToPlay = new ArrayList<Song>();

        mActionListener = new PreviewPresenter(this, this);
        mActionListener.init(token);

        RecyclerView recyclerViewItem = (RecyclerView) findViewById(R.id.recyclerItem);
        adapter = new SongAdapter(this, new SongAdapter.ItemSelectedListener(){
            @Override
            public void onItemSelected(View itemView, Track item) {
                mActionListener.selectTrack(item);
            }
        });

        recyclerViewItem.setAdapter(adapter);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setReverseLayout(true);
        layoutManager.setStackFromEnd(true);
        recyclerViewItem.setLayoutManager(layoutManager);
        recyclerViewItem.setAdapter(adapter);

        initPostsListener();

    }

    @Override
    public void reset() {
//        adapter.clearData();
    }


    public void initPostsListener() {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("posts");

        reference.orderByChild("score").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Song song = dataSnapshot.getValue(Song.class);
                adapter.addPost(song, dataSnapshot.getKey());
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                Log.d("OnChildChanged", "OnChildChanged");
                Song song = dataSnapshot.getValue(Song.class);
                adapter.updatePost(song, dataSnapshot.getKey());
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

    public void openMainActivity(){
        Intent intent = new Intent(QueueActivity.this, MainActivity.class);
//        this.setResult(RESULT_OK,intent);
//        this.startActivityForResult(intent, REQUEST_CODE_QUEUE);
        startActivity(intent);
        finish();

//                ((Activity) mContext).setResult(RESULT_OK,intent);
//                ((Activity) mContext).startActivityForResult(intent, REQUEST_CODE_ADD_TRACK);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_queue, menu);
        return super.onCreateOptionsMenu(menu);

    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.openMainActivity:
                openMainActivity();
                break;
        }

        return super.onOptionsItemSelected(item);
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