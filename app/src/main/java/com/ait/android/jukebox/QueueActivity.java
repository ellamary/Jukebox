package com.ait.android.jukebox;

import android.content.Intent;
import android.support.design.widget.Snackbar;
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

public class QueueActivity extends AppCompatActivity {

    public static final String KEY_ITEM_ID = "KEY_ITEM_ID";
    public static final int REQUEST_CODE_EDIT = 1001;

    public static final int REQUEST_CODE_ADD_ITEM = 2001;

    private int positionToEdit = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_queue);

        //Toolbar toolbar = (Toolbar) findViewById(R.id.my_toolbar);
        //setSupportActionBar(toolbar);

        //toolbar.setLogo(R.mipmap.ic_launcher);

        //((ItemApplication)getApplication()).openRealm();

        RecyclerView recyclerViewItem = (RecyclerView) findViewById(R.id.recyclerItem);
        adapter = new SongAdapter(this, ((ItemApplication)getApplication()).getRealmItem());

        recyclerViewItem.setLayoutManager(new LinearLayoutManager(this));
        recyclerViewItem.setHasFixedSize(true);

        ItemTouchHelper.Callback callback = new ItemTouchHelperCallback(adapter);
        ItemTouchHelper touchHelper = new ItemTouchHelper(callback);
        touchHelper.attachToRecyclerView(recyclerViewItem);

        recyclerViewItem.setAdapter(adapter);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return super.onCreateOptionsMenu(menu);

    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.deleteAll:

                adapter.deleteAllItems();
                break;
            case R.id.addNewItem:
                openNewItemActivity();
                break;
            case R.id.shoppingListSummary:
                openSummaryActivity();
                break;
        }

        return super.onOptionsItemSelected(item);
    }


    private void openNewItemActivity() {

        Intent intentNewItem = new Intent();
        intentNewItem.setClass(MainActivity.this, NewItemActivity.class);
        startActivityForResult(intentNewItem, REQUEST_CODE_ADD_ITEM);

    }

    public void openEditActivity(int adapterPosition, String itemID){
        positionToEdit = adapterPosition;
        Intent intentEdit = new Intent(this, EditItemActivity.class);
        intentEdit.putExtra(KEY_ITEM_ID, itemID);
        startActivityForResult(intentEdit, REQUEST_CODE_EDIT);
    }

    private void openSummaryActivity(){

        Intent intentSummary = new Intent();
        intentSummary.setClass(MainActivity.this, SummaryActivity.class);

        intentSummary.putExtra(getString(R.string.total_food), Integer.toString(totalFood));

        intentSummary.putExtra(getString(R.string.total_clothes), Integer.toString(totalClothes));

        intentSummary.putExtra(getString(R.string.total_books), Integer.toString(totalBooks));
        intentSummary.putExtra(getString(R.string.total_price), Double.toString(totalPrice));

        startActivity(intentSummary);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == REQUEST_CODE_ADD_ITEM && data!=null) {
            if(resultCode == RESULT_OK){
                category = data.getStringExtra(getString(R.string.category_key));
                name = data.getStringExtra(getString(R.string.name_key));
                description = data.getStringExtra(getString(R.string.description_key));
                price = data.getStringExtra(getString(R.string.price_key));
                adapter.addItem(category, name, description, price);

                if (category.equals(getString(R.string.food))){
                    totalFood++;
                }
                if (category.equals(getString(R.string.books))){
                    totalBooks++;
                }
                if (category.equals(getString(R.string.clothes))){
                    totalClothes++;
                }
                totalPrice += Double.parseDouble(price);


            }
            if (resultCode == RESULT_CANCELED) {
                //do nothing
            }
        }
        else if (requestCode == REQUEST_CODE_EDIT && resultCode == RESULT_OK){
            String itemIDThatWasEdited = data.getStringExtra(KEY_ITEM_ID);
            adapter.updateItem(itemIDThatWasEdited, positionToEdit);
        }
        else {
            Snackbar.make(findViewById(R.id.layoutContent), getString(R.string.edit_canceled),
                    Snackbar.LENGTH_LONG)
                    .show();
        }
    }

    @Override
    protected void onDestroy() {
        ((ItemApplication)getApplication()).closeRealm();
        super.onDestroy();
    }
}
