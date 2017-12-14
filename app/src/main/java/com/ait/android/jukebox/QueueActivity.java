package com.ait.android.jukebox;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import com.ait.android.jukebox.adapter.SongAdapter;
import com.ait.android.jukebox.data.Song;
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

    public static final int REQUEST_CODE_QUEUE = 1;
    private Preview.ActionListener mActionListener;
    public static List<Song> songsToPlay;
    public SongAdapter adapter;
    public static String token;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_queue);

        token = getIntent().getStringExtra(MainActivity.EXTRA_TOKEN);

        songsToPlay = new ArrayList<>();

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
        intent.putExtra(MainActivity.EXTRA_TOKEN, token);
        startActivity(intent);
        finish();
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
}