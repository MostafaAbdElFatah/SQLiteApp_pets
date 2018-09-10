package comq.mostafa.fci.pets.data;

import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatDelegate;
import android.support.v7.view.ContextThemeWrapper;

import java.util.ArrayList;

import comq.mostafa.fci.pets.R;

import static android.content.Context.MODE_PRIVATE;

public class AlterDialog {
    static AlertDialog.Builder  builder = null;
    static String[] setting = {"Night Mode"};
    private static ArrayList<OnAPPModeChangedListener> listeners = new ArrayList<>();

    public static AlertDialog.Builder setup(final Context context){

        boolean[] mode = {getMode(context) == AppCompatDelegate.MODE_NIGHT_YES? true:false };

        builder = new AlertDialog.Builder(context);
        builder.setTitle(R.string.action_setting)
                .setMultiChoiceItems(setting, mode, new DialogInterface.OnMultiChoiceClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i, boolean isChecked) {
                        if (isChecked)
                            saveData(context,AppCompatDelegate.MODE_NIGHT_YES);
                        else
                            saveData(context,AppCompatDelegate.MODE_NIGHT_NO);
                    }
                })
                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        for (OnAPPModeChangedListener listener: listeners) {
                            listener.onModeChanged(getMode(context));
                        }
                    }
                });
        return builder;
    }

    private static void saveData(Context context,int mode) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("Setting", MODE_PRIVATE);
        SharedPreferences.Editor editor=sharedPreferences.edit();
        editor.putInt("mode", mode);
        editor.commit();
    }

    public static int getMode(Context context){
        SharedPreferences sharedPreferences = context.getSharedPreferences("Setting", context.MODE_PRIVATE);
        int modeNightYes =sharedPreferences.getInt("mode", AppCompatDelegate.MODE_NIGHT_NO);
        return modeNightYes;
    }

    public static void setOnAPPModeChangedListener(OnAPPModeChangedListener listener){
        listeners.add(listener);
    }
}
