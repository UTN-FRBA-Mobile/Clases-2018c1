package ar.edu.utn.frba.dadm.clases2018c1.clases_2018c1.widget;

import android.app.IntentService;
import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.Intent;

public class WidgetService extends IntentService {

    private static final String ACTION_CHANGE = "ar.edu.utn.frba.myapplication.widget.action.CHANGE";

    private static final String EXTRA_WID = "ar.edu.utn.frba.myapplication.widget.extra.WID";
    private static final String EXTRA_DELTA = "ar.edu.utn.frba.myapplication.widget.extra.DELTA";

    public WidgetService() {
        super("WidgetService");
    }

    public static Intent getChangeIntent(Context context, int widgetId, int delta) {
        Intent intent = new Intent(context, WidgetService.class);
        intent.setAction(ACTION_CHANGE);
        intent.putExtra(EXTRA_WID, widgetId);
        intent.putExtra(EXTRA_DELTA, delta);
        return intent;
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        if (intent != null) {
            final String action = intent.getAction();
            if (ACTION_CHANGE.equals(action)) {
                final int widgetId = intent.getIntExtra(EXTRA_WID, 0);
                final int delta = intent.getIntExtra(EXTRA_DELTA, 0);
                handleActionChange(widgetId, delta);
            }
        }
    }

    private void handleActionChange(int widgetId, int delta) {
        int current = HiAppWidgetConfigureActivity.loadPosPref(this, widgetId);
        current = (current + HiAppWidget.texts.length + delta) % HiAppWidget.texts.length;
        HiAppWidgetConfigureActivity.savePosPref(this, widgetId, current);
        HiAppWidget.updateAppWidget(this, AppWidgetManager.getInstance(this), widgetId);
    }
}
