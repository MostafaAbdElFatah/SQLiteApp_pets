package comq.mostafa.fci.pets;

import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.content.DialogInterface;
import android.net.Uri;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.app.AppCompatDelegate;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import comq.mostafa.fci.pets.data.ActivityListener;
import comq.mostafa.fci.pets.data.AlterDialog;
import comq.mostafa.fci.pets.data.FragmentListener;
import comq.mostafa.fci.pets.data.OnAPPModeChangedListener;


public class EditActivity extends AppCompatActivity implements FragmentListener.CallBack {

    Uri mCurrentUri = null;
    boolean mPetHasChanged = false;
    private ActivityListener.CallBack callBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        if (AlterDialog.getMode(EditActivity.this) == AppCompatDelegate.MODE_NIGHT_YES)
            setTheme(R.style.DarkTheme);
        else
            setTheme(R.style.AppTheme);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        AlterDialog.setOnAPPModeChangedListener(new OnAPPModeChangedListener() {
            @Override
            public void onModeChanged(int mode) {
                AppCompatDelegate.setDefaultNightMode(mode);
                recreate();
            }
        });

        mCurrentUri = getIntent().getData();
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        EditFragment fragment = new EditFragment();
        fragment.mCurrentUri = mCurrentUri;
        fragmentTransaction.add(R.id.fragContainer,fragment)
                            .commit();
        callBack = fragment;
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        menu.removeItem(R.id.action_delete_all);
        menu.removeItem(R.id.action_dummy_data);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_done) {
            if (mCurrentUri == null) {
               callBack.onSavePet();
            } else{
                callBack.onUpdatePet();
            }
            return true;
        }else if(id == R.id.action_delete){
            callBack.onDeletePet();
            return true;
        }else if(id == android.R.id.home) {
            if (!mPetHasChanged) {
                NavUtils.navigateUpFromSameTask(EditActivity.this);
                return true;
            }
            showUnsavedChangesDialog(true);
            return true;
        }else if (id == R.id.action_setting){
            showSetting();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void showSetting() {
        AlterDialog.setup(EditActivity.this).create().show();
    }


    @Override
    public void onBackPressed() {

        if (!mPetHasChanged) {
            super.onBackPressed();
            return;
        }
        showUnsavedChangesDialog(false);
    }

    private void showUnsavedChangesDialog(final boolean actionHome) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(R.string.unsaved_changes_dialog_msg)
                .setPositiveButton(R.string.discard
                        , new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                if (actionHome)
                                    NavUtils.navigateUpFromSameTask(EditActivity.this);
                                else
                                    finish();
                            }
                        })
                .setNegativeButton(R.string.keep_editing
                        , new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                if (dialog != null) {
                                    dialog.dismiss();
                                }
                            }
                });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    @Override
    public void onDataChange(boolean petHasChanged) {
        mPetHasChanged = petHasChanged;
    }

}
