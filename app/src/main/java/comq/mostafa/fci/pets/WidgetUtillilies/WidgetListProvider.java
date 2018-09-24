package comq.mostafa.fci.pets.WidgetUtillilies;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import java.util.ArrayList;

import comq.mostafa.fci.pets.EditActivity;
import comq.mostafa.fci.pets.MainActivity;
import comq.mostafa.fci.pets.R;
import comq.mostafa.fci.pets.data.Pet;
import comq.mostafa.fci.pets.data.PetContract.PetEntry;

public class WidgetListProvider implements RemoteViewsService.RemoteViewsFactory {

    private int appWidgetId;
    private Context context;
    private ArrayList<Pet> petsList;

    public WidgetListProvider(Context context, Intent intent) {
        this.context = context;
        this.petsList = new ArrayList<>();
        appWidgetId = intent.getIntExtra(AppWidgetManager.EXTRA_APPWIDGET_ID,
                AppWidgetManager.INVALID_APPWIDGET_ID);
    }

    @Override
    public void onCreate() {

    }

    @Override
    public void onDataSetChanged() {

        petsList.clear();
        Cursor cursor = context.getContentResolver().query(
                PetEntry.CONTENT_URI,
                null,
                null,
                null,
                null);

        petsList.addAll( Pet.getPetsList(cursor) );
    }

    @Override
    public void onDestroy() {

    }

    @Override
    public int getCount() {
        return petsList.size();
    }

    @Override
    public RemoteViews getViewAt(int index) {
        final RemoteViews remoteView = new RemoteViews(
                context.getPackageName(), R.layout.app_widget_item_list);
        Pet pet = petsList.get(index);
        String gender ;
        switch (pet.getGender()){
            case PetEntry.GENDER_MALE:
                gender = "Male";
                break;
            case PetEntry.GENDER_FEMALE:
                gender = "Female";
                break;
            default:
                gender = "Unknown";
        }

        remoteView.setTextViewText(R.id.name_pet_widget, pet.getName());
        remoteView.setTextViewText(R.id.breed_pet_widget, pet.getBreed());
        remoteView.setTextViewText(R.id.gender_pet_widget, gender);
        remoteView.setTextViewText(R.id.weight_pet_widget, String.valueOf( pet.getWeight() ) );

        //Pass the ID to EditActivity
        Uri uri = ContentUris.withAppendedId(PetEntry.CONTENT_URI,pet.getId());
        Intent fillInIntent = new Intent();
        fillInIntent
                .setData(uri)
                .putExtra(ActionsBridge.ACTION_CODE, ActionsBridge.ACTION_EDIT);
        remoteView.setOnClickFillInIntent(R.id.widget_card_click_edit, fillInIntent);

        return remoteView;
    }

    @Override
    public RemoteViews getLoadingView() {
        return null;
    }

    @Override
    public int getViewTypeCount() {
        return 1;
    }

    @Override
    public long getItemId(int i) {
        return petsList.get(i).getId();
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }
}
