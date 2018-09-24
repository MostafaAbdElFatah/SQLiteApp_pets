package comq.mostafa.fci.pets.WidgetUtillilies;

import android.appwidget.AppWidgetManager;
import android.content.Intent;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

public class AppWidgetService extends RemoteViewsService {
    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        int appWidgetId = intent.getIntExtra(
                AppWidgetManager.EXTRA_APPWIDGET_ID,
                AppWidgetManager.INVALID_APPWIDGET_ID);
        return (new WidgetListProvider(this.getApplicationContext(), intent));
    }
}
