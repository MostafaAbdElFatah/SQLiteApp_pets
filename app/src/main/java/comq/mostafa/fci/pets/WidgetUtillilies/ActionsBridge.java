package comq.mostafa.fci.pets.WidgetUtillilies;

import android.app.IntentService;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.Toast;

import comq.mostafa.fci.pets.EditActivity;
import comq.mostafa.fci.pets.R;
import comq.mostafa.fci.pets.data.PetContract.PetEntry;

public class ActionsBridge extends IntentService {

    public final static int ACTION_EDIT = 3;
    public final static int ACTION_DELETE_ALL = 1;
    public final static String ACTION_CODE = "com.catchingnow.tinyclipboardmanager.actionCode";


    public ActionsBridge() {
        super("ActionsBridge");

    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {

        Intent it = new Intent(Intent.ACTION_CLOSE_SYSTEM_DIALOGS);
        if (intent == null) return;

        Uri uri = intent.getData();
        if(uri != null)
            Log.v("ActionBridge","uri:"+uri.toString());
        int actionCode = intent.getIntExtra(ACTION_CODE, 0);

        switch (actionCode) {
            case ACTION_DELETE_ALL:
                deleteAllPets();
                return;
            case ACTION_EDIT:
                editPet( uri );
                return;
            default:
                return;
        }
    }


    private void deleteAllPets() {
        int count = getContentResolver().delete(PetEntry.CONTENT_URI,null,null);
        if (count == -1) {
            String text = getResources().getString(R.string.editor_delete_pets_failed);
            new Handler(Looper.getMainLooper()).post(new DisplayToast(text));
        }else if (count == 0){
            String text = getResources().getString(R.string.editor_nothing_delete_pets);
            new Handler(Looper.getMainLooper()).post(new DisplayToast(text));
        }else{
            ActionsBridge.updateWidget(getApplicationContext());
            String text = getResources().getString( R.string.editor_delete_pets_successful );
            new Handler(Looper.getMainLooper()).post(new DisplayToast(text));
        }
    }

    private void editPet(Uri uri) {
        Log.v("ActionBridge","editPet function");
        Intent intent = new Intent(this, EditActivity.class)
                .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                .setData(uri);
        startActivity(intent);
    }

    public static void updateWidget(Context context){
        // update the list view items only
        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
        ComponentName thisWidget = new ComponentName(context, PetsAppWidget.class);
        int[] appWidgetIds = appWidgetManager.getAppWidgetIds(thisWidget);
        appWidgetManager.notifyAppWidgetViewDataChanged(appWidgetIds, R.id.widget_main_view);
    }

    private class DisplayToast implements Runnable{
        String mText;

        public DisplayToast(String text){
            mText = text;
        }

        public void run(){
            Toast.makeText(getApplicationContext(), mText , Toast.LENGTH_SHORT).show();
        }
    }

}
