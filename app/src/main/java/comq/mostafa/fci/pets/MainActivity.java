package comq.mostafa.fci.pets;

import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.AppCompatDelegate;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import comq.mostafa.fci.pets.data.AlterDialog;
import comq.mostafa.fci.pets.data.OnAPPModeChangedListener;
import comq.mostafa.fci.pets.data.PetCursorAdapter;
import comq.mostafa.fci.pets.data.PetContract.PetEntry;

public class MainActivity extends AppCompatActivity {

    //ArrayList<Pet> pets;
    //PetDBHelper mDBHelper;

    ListView listView;
    //PetsAdapter mPetsAdapter;
    PetCursorAdapter mPetCursorAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        if (AlterDialog.getMode(MainActivity.this) == AppCompatDelegate.MODE_NIGHT_YES)
            setTheme(R.style.DarkTheme);
        else
            setTheme(R.style.AppTheme);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        AlterDialog.setOnAPPModeChangedListener(new OnAPPModeChangedListener() {
            @Override
            public void onModeChanged(int mode) {
                AppCompatDelegate.setDefaultNightMode(mode);
                recreate();
            }
        });

        View emptyView = findViewById(R.id.empty_view);
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this,EditActivity.class));
            }
        });

        listView = findViewById(R.id.listview);
        //pets = new ArrayList<>();
        //mPetsAdapter = new PetsAdapter(this,pets);
        //listView.setAdapter(mPetsAdapter);

        listView.setEmptyView(emptyView);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                Intent intent = new Intent(MainActivity.this,EditActivity.class);
                Uri uri = ContentUris.withAppendedId(PetEntry.CONTENT_URI,id);
                intent.setData(uri);
                startActivity(intent);
            }
        });

        //mDBHelper = new PetDBHelper(this);
        displayDatabaseInfo();
    }

    @Override
    protected void onStart() {
        super.onStart();
        displayDatabaseInfo();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        menu.removeItem(R.id.action_done);
        menu.removeItem(R.id.action_delete);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_dummy_data) {
            insertPet();
            displayDatabaseInfo();
            return true;
        }else if(id == R.id.action_delete_all){
            deleteAllPets();
            displayDatabaseInfo();
            return true;
        }else if (id == R.id.action_setting){
            showSetting();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void showSetting() {
        AlterDialog.setup(MainActivity.this).create().show();
    }

    private void deleteAllPets() {
        /***
        SQLiteDatabase db = mDBHelper.getWritableDatabase();
        int deletedRows = db.delete(PetEntry.TABLE_NAME, null, null);
        ***/
        int count = getContentResolver().delete(PetEntry.CONTENT_URI,null,null);
        if (count == -1)
            Toast.makeText(this, getString(R.string.editor_delete_pet_failed),
                    Toast.LENGTH_SHORT).show();
        else
            Toast.makeText(this, getString(R.string.editor_delete_pet_successful),
                    Toast.LENGTH_SHORT).show();
    }

    private void insertPet(){
        /***
        SQLiteDatabase db = mDBHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(PetEntry.COLUMN_PET_NAME, "Tummy");
        values.put(PetEntry.COLUMN_PET_BREED, "Pomeranian");
        values.put(PetEntry.COLUMN_PET_GENDER, PetEntry.GENDER_MALE);
        values.put(PetEntry.COLUMN_PET_WEIGHT, 4);
        db.insert(PetEntry.TABLE_NAME, null, values);
        displayDatabaseInfo();
        **/
        ContentValues values = new ContentValues();
        values.put(PetEntry.COLUMN_PET_NAME, "Tummy");
        values.put(PetEntry.COLUMN_PET_BREED, "Pomeranian");
        values.put(PetEntry.COLUMN_PET_GENDER, PetEntry.GENDER_MALE);
        values.put(PetEntry.COLUMN_PET_WEIGHT, 4);
        Uri newUri = getContentResolver().insert(PetEntry.CONTENT_URI, values);
        displayDatabaseInfo();
    }

    private void displayDatabaseInfo() {

        /***
         pets.clear();
         SQLiteDatabase db = mDBHelper.getReadableDatabase();
         //Cursor cursor = db.rawQuery("SELECT * FROM " + PetEntry.TABLE_NAME, null);
         Cursor cursor = db.query(
                PetEntry.TABLE_NAME,
                null,
                null,
                null,
                null,
                null,
                null );
         ***/
        Cursor cursor = getContentResolver().query(
                PetEntry.CONTENT_URI,
                null,
                null,
                null,
                null);
        try {
                /***
                int idColumnIndex = cursor.getColumnIndex(PetEntry._ID);
                int nameColumnIndex = cursor.getColumnIndex(PetEntry.COLUMN_PET_NAME);
                int breedColumnIndex = cursor.getColumnIndex(PetEntry.COLUMN_PET_BREED);
                int genderColumnIndex = cursor.getColumnIndex(PetEntry.COLUMN_PET_GENDER);
                int weightColumnIndex = cursor.getColumnIndex(PetEntry.COLUMN_PET_WEIGHT);

                while (cursor.moveToNext()) {
                    int currentID = cursor.getInt(idColumnIndex);
                    String currentName = cursor.getString(nameColumnIndex);
                    String currentBreed = cursor.getString(breedColumnIndex);
                    int currentGender = cursor.getInt(genderColumnIndex);
                    int currentWeight = cursor.getInt(weightColumnIndex);
                    Pet pet = new Pet(currentID,currentName,currentBreed,currentGender,currentWeight);
                    pets.add(pet);
                }
                listView.setVisibility(View.VISIBLE);
                displayView.setVisibility(View.GONE);
                mPetsAdapter.notifyDataSetChanged();
                ***/
                mPetCursorAdapter = new PetCursorAdapter(this,cursor);
                listView.setAdapter(mPetCursorAdapter);
        }catch (Exception e){}
        /**
         finally {
            cursor.close();
        }
         **/
    }

}