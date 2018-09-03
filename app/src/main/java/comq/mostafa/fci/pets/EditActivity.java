package comq.mostafa.fci.pets;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import comq.mostafa.fci.pets.data.Pet;
import comq.mostafa.fci.pets.data.PetContract.PetEntry;
import comq.mostafa.fci.pets.data.PetDBHelper;


public class EditActivity extends AppCompatActivity {

    Uri mCurrentUri;
    private EditText mNameEditText;
    private EditText mBreedEditText;
    private EditText mWeightEditText;
    private Spinner mGenderSpinner;
    private boolean mPetHasChanged = false;
    private int mGender = PetEntry.GENDER_UNKNOWN;

    //PetDBHelper mDBHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);
        mNameEditText   = findViewById(R.id.nameTextViewEditActivity);
        mBreedEditText  = findViewById(R.id.breedTextViewEditActivity);
        mWeightEditText = findViewById(R.id.weightTextViewEditActivity);
        mGenderSpinner  = findViewById(R.id.genderEditActivity);
        mNameEditText.setOnTouchListener(mTouchListener);
        mBreedEditText.setOnTouchListener(mTouchListener);
        mWeightEditText.setOnTouchListener(mTouchListener);
        mGenderSpinner.setOnTouchListener(mTouchListener);

        setupSpinner();

        //mDBHelper = new PetDBHelper(this);

        mCurrentUri = getIntent().getData();
        setDataToTexts();
    }

    private void setDataToTexts(){
        Cursor cursor = getContentResolver().query(mCurrentUri,null,null
                ,null,null);
        int nameColumnIndex   = cursor.getColumnIndex(PetEntry.COLUMN_PET_NAME);
        int breedColumnIndex  = cursor.getColumnIndex(PetEntry.COLUMN_PET_BREED);
        int weightColumnIndex = cursor.getColumnIndex(PetEntry.COLUMN_PET_WEIGHT);
        int genderColumnIndex = cursor.getColumnIndex(PetEntry.COLUMN_PET_GENDER);

        cursor.moveToFirst();
        String petName = cursor.getString(nameColumnIndex);
        String petBreed = cursor.getString(breedColumnIndex);
        int petWeight = cursor.getInt(weightColumnIndex);
        int petGender = cursor.getInt(genderColumnIndex);

        mNameEditText.setText(petName);
        mBreedEditText.setText(petBreed);
        mWeightEditText.setText(String.valueOf( petWeight ));
        mGenderSpinner.setSelection(petGender,true);
    }
    private void setupSpinner() {

        ArrayAdapter genderSpinnerAdapter = ArrayAdapter.createFromResource(this,
                R.array.array_gender_options, android.R.layout.simple_spinner_item);

        genderSpinnerAdapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);

        mGenderSpinner.setAdapter(genderSpinnerAdapter);

        mGenderSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selection = (String) parent.getItemAtPosition(position);
                if (!TextUtils.isEmpty(selection)) {
                    if (selection.equals(getString(R.string.gender_male))) {
                        mGender = PetEntry.GENDER_MALE;
                    } else if (selection.equals(getString(R.string.gender_female))) {
                        mGender = PetEntry.GENDER_FEMALE;
                    } else {
                        mGender = PetEntry.GENDER_UNKNOWN;
                    }
                }
            }

            // Because AdapterView is an abstract class, onNothingSelected must be defined
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                mGender = PetEntry.GENDER_UNKNOWN;
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_edit, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.edit_done) {
            updatePet();
            finish();
            return true;
        }else if(id == R.id.edit_delete){
            deletePet();
            finish();
            return true;
        }else if(id == android.R.id.home) {
            if (!mPetHasChanged) {
                NavUtils.navigateUpFromSameTask(EditActivity.this);
                return true;
            }
            DialogInterface.OnClickListener discardButtonClickListener =
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            NavUtils.navigateUpFromSameTask(EditActivity.this);
                        }
                    };
            showUnsavedChangesDialog(discardButtonClickListener);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void updatePet() {
        String nameString = mNameEditText.getText().toString().trim();
        String breedString = mBreedEditText.getText().toString().trim();
        String weightString = mWeightEditText.getText().toString().trim() ;

        int weight = Integer.parseInt( weightString );
        /***
        SQLiteDatabase db = mDBHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(PetEntry.COLUMN_PET_NAME, nameString);
        values.put(PetEntry.COLUMN_PET_BREED, breedString);
        values.put(PetEntry.COLUMN_PET_GENDER, mGender);
        values.put(PetEntry.COLUMN_PET_WEIGHT, weight);
        String selection = PetEntry._ID + " =?";
        String[] selectionArgs = { String.valueOf(pet.getId()) };
        int count = db.update(
                PetEntry.TABLE_NAME,
                values,
                selection,
                selectionArgs);
         ***/

        ContentValues values = new ContentValues();
        values.put(PetEntry.COLUMN_PET_NAME, nameString);
        values.put(PetEntry.COLUMN_PET_BREED, breedString);
        values.put(PetEntry.COLUMN_PET_GENDER, mGender);
        values.put(PetEntry.COLUMN_PET_WEIGHT, weight);
        int count = getContentResolver().update(mCurrentUri, values,null,null);
        if (count == -1)
            Toast.makeText(this, getString(R.string.editor_update_pet_failed),
                    Toast.LENGTH_SHORT).show();
        else
            Toast.makeText(this, getString(R.string.editor_update_pet_successful),
                    Toast.LENGTH_SHORT).show();

    }

    private void deletePet() {
        /***
        SQLiteDatabase db = mDBHelper.getWritableDatabase();
        String selection = PetEntry._ID + " =?";
        String[] selectionArgs = { String.valueOf( pet.getId() ) };
        int deletedRows = db.delete(PetEntry.TABLE_NAME, selection, selectionArgs);
        **/

        int count = getContentResolver().delete(mCurrentUri ,null,null);
        if (count == -1)
            Toast.makeText(this, getString(R.string.editor_delete_pet_failed),
                    Toast.LENGTH_SHORT).show();
        else
            Toast.makeText(this, getString(R.string.editor_delete_pet_successful),
                    Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onBackPressed() {
        if (!mPetHasChanged) {
            super.onBackPressed();
            return;
        }
        DialogInterface.OnClickListener discardButtonClickListener =
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        finish();
                    }
                };
        showUnsavedChangesDialog(discardButtonClickListener);
    }

    private void showUnsavedChangesDialog(
            DialogInterface.OnClickListener discardButtonClickListener) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(R.string.unsaved_changes_dialog_msg);
        builder.setPositiveButton(R.string.discard, discardButtonClickListener);
        builder.setNegativeButton(R.string.keep_editing, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                if (dialog != null) {
                    dialog.dismiss();
                }
            }
        });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    private View.OnTouchListener mTouchListener = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View view, MotionEvent motionEvent) {
            mPetHasChanged = true;
            return false;
        }
    };

}
