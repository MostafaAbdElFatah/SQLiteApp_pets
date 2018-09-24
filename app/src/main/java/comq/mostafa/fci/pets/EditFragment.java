package comq.mostafa.fci.pets;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import comq.mostafa.fci.pets.WidgetUtillilies.ActionsBridge;
import comq.mostafa.fci.pets.data.MessageEvent;
import comq.mostafa.fci.pets.data.PetContract.PetEntry;


public class EditFragment extends Fragment {

    public Uri mCurrentUri;
    private EditText mNameEditText;
    private EditText mBreedEditText;
    private EditText mWeightEditText;
    private Spinner mGenderSpinner;
    private boolean mPetHasChanged = false;
    private int mGender = PetEntry.GENDER_UNKNOWN;

    //PetDBHelper mDBHelper;

    public EditFragment() {
        // Required empty public constructor
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.edit_fragment, container,false);

        mNameEditText   = view.findViewById(R.id.nameTextView);
        mBreedEditText  = view.findViewById(R.id.breedTextView);
        mWeightEditText = view.findViewById(R.id.weightTextView);
        mGenderSpinner  = view.findViewById(R.id.gender);

        setupSpinner();
        if (mCurrentUri != null)
            setDataToTexts();
        //mDBHelper = new PetDBHelper(this);

        mNameEditText.setOnTouchListener(mTouchListener);
        mBreedEditText.setOnTouchListener(mTouchListener);
        mWeightEditText.setOnTouchListener(mTouchListener);
        mGenderSpinner.setOnTouchListener(mTouchListener);

        EventBus.getDefault().register(this);
        return view;
    }


    private void setDataToTexts() {
        Cursor cursor = getContext().getContentResolver().query(mCurrentUri, null, null
                , null, null);
        int nameColumnIndex = cursor.getColumnIndex(PetEntry.COLUMN_PET_NAME);
        int breedColumnIndex = cursor.getColumnIndex(PetEntry.COLUMN_PET_BREED);
        int weightColumnIndex = cursor.getColumnIndex(PetEntry.COLUMN_PET_WEIGHT);
        int genderColumnIndex = cursor.getColumnIndex(PetEntry.COLUMN_PET_GENDER);

        cursor.moveToFirst();
        String petName = cursor.getString(nameColumnIndex);
        String petBreed = cursor.getString(breedColumnIndex);
        int petWeight = cursor.getInt(weightColumnIndex);
        int petGender = cursor.getInt(genderColumnIndex);

        mNameEditText.setText(petName);
        mBreedEditText.setText(petBreed);
        mWeightEditText.setText(String.valueOf(petWeight));
        mGenderSpinner.setSelection(petGender, true);
    }
    private void setupSpinner() {

        ArrayAdapter genderSpinnerAdapter = ArrayAdapter.createFromResource(getContext() ,
                R.array.array_gender_options, R.layout.spinner_text);
        genderSpinnerAdapter.setDropDownViewResource(R.layout.simple_spinner_dropdown);
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


    private boolean insertPet(){
        String nameString = mNameEditText.getText().toString().trim();
        String breedString = mBreedEditText.getText().toString().trim();
        String weightString = mWeightEditText.getText().toString().trim() ;
        if (nameString.equals("") || weightString.equals("") ) {
            Toast.makeText(getContext(), "Please set Name or Weight...", Toast.LENGTH_SHORT).show();
            return false;
        }
        int weight = Integer.parseInt( weightString );
        /***
         SQLiteDatabase db = mDBHelper.getWritableDatabase();
         ContentValues values = new ContentValues();
         values.put(PetEntry.COLUMN_PET_NAME, nameString);
         values.put(PetEntry.COLUMN_PET_BREED, breedString);
         values.put(PetEntry.COLUMN_PET_GENDER, mGender);
         values.put(PetEntry.COLUMN_PET_WEIGHT, weight);
         long newRowId = db.insert(PetEntry.TABLE_NAME, null, values);
         ***/
        ContentValues values = new ContentValues();
        values.put(PetEntry.COLUMN_PET_NAME, nameString);
        values.put(PetEntry.COLUMN_PET_BREED, breedString);
        values.put(PetEntry.COLUMN_PET_GENDER, mGender);
        values.put(PetEntry.COLUMN_PET_WEIGHT, weight);
        Uri newUri = getContext().getContentResolver().insert(PetEntry.CONTENT_URI, values);
        if (newUri == null) {
            Toast.makeText(getContext(), getString(R.string.editor_insert_pet_failed),
                    Toast.LENGTH_SHORT).show();
            return false;
        } else {
            ActionsBridge.updateWidget(getContext());
            Toast.makeText(getContext(), getString(R.string.editor_insert_pet_successful),
                    Toast.LENGTH_SHORT).show();
            return true;
        }
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
        int count = getContext().getContentResolver().update(mCurrentUri, values,null,null);
        if (count == -1)
            Toast.makeText(getContext(), getString(R.string.editor_update_pet_failed),
                    Toast.LENGTH_SHORT).show();
        else{
            ActionsBridge.updateWidget(getContext());
            Toast.makeText(getContext(), getString(R.string.editor_update_pet_successful),
                    Toast.LENGTH_SHORT).show();
        }

    }

    private void deletePet() {
        /***
         SQLiteDatabase db = mDBHelper.getWritableDatabase();
         String selection = PetEntry._ID + " =?";
         String[] selectionArgs = { String.valueOf( pet.getId() ) };
         int deletedRows = db.delete(PetEntry.TABLE_NAME, selection, selectionArgs);
         **/

        int count = getContext().getContentResolver().delete(mCurrentUri ,null,null);
        if (count == -1)
            Toast.makeText(getContext(), getString(R.string.editor_delete_pet_failed),
                    Toast.LENGTH_SHORT).show();
        else {
            ActionsBridge.updateWidget(getContext());
            Toast.makeText(getContext(), getString(R.string.editor_delete_pet_successful),
                    Toast.LENGTH_SHORT).show();
        }
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        EventBus.getDefault().unregister(this);
    }

    @Subscribe
    public void onEvent(MessageEvent messageEvent){
        switch (messageEvent){
            case SAVE:
                if ( insertPet())
                    getActivity().finish();
                break;
            case UPDATE:
                updatePet();
                getActivity().finish();
                break;
            case DELETE:
                deletePet();
                getActivity().finish();
                break;
        }
    }

    private View.OnTouchListener mTouchListener = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View view, MotionEvent motionEvent) {
            mPetHasChanged = true;
            EventBus.getDefault().post(MessageEvent.DATA_CHANGED);
            return false;
        }
    };

}
