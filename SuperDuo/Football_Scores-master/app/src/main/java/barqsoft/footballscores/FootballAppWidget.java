package barqsoft.footballscores;

import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.database.Cursor;
import android.widget.RemoteViews;

/**
 * Implementation of App Widget functionality.
 */
public class FootballAppWidget extends AppWidgetProvider {
    public static final int COL_HOME = 3;
    public static final int COL_AWAY = 4;
    public static final int COL_HOME_GOALS = 6;
    public static final int COL_AWAY_GOALS = 7;
    public static final int COL_MATCHTIME = 2;
    public static final int COL_MATCHDAY = 9;

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        // There may be multiple widgets active, so update all of them
        final int N = appWidgetIds.length;

        for (int i = 0; i < N; i++) {
            updateAppWidget(context, appWidgetManager, appWidgetIds[i]);
        }
    }


    @Override
    public void onEnabled(Context context) {
        // Enter relevant functionality for when the first widget is created
    }

    @Override
    public void onDisabled(Context context) {
        // Enter relevant functionality for when the last widget is disabled
    }

    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                int appWidgetId) {

        Cursor cursor = context.getContentResolver().query(DatabaseContract.BASE_CONTENT_URI, null, null, null, DatabaseContract.ScoresTable.DATE_COL + " ASC");

        cursor.getCount();
        // get the latest match
        cursor.moveToFirst();
        String home = cursor.getString(COL_HOME);
        String away = cursor.getString(COL_AWAY);
        String score = Utilies.getScores(cursor.getInt(COL_HOME_GOALS), cursor.getInt(COL_AWAY_GOALS));
        String data = cursor.getString(COL_MATCHTIME)+" / " + cursor.getString(COL_MATCHDAY);
        cursor.close();

        // Construct the RemoteViews object
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.football_app_widget);

        views.setTextViewText(R.id.home_name, home);
        views.setTextViewText(R.id.away_name, away);
        views.setTextViewText(R.id.score_textview, score);
        views.setTextViewText(R.id.data_textview, data);

        // Instruct the widget manager to update the widget
        appWidgetManager.updateAppWidget(appWidgetId, views);
    }
}

