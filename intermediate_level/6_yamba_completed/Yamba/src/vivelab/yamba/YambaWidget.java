package vivelab.yamba;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
//import android.database.Cursor;
//import android.text.format.DateUtils;
import android.util.Log;
import android.widget.RemoteViews;

public class YambaWidget extends AppWidgetProvider {
	private static final String TAG = "YambaWidget";
	private int newStatusesCount;

	@Override
	public void onReceive(Context context, Intent intent) {
		super.onReceive(context, intent);
		Log.d(TAG, "onReceive");
		if (intent.getAction().equals(UpdaterService.NEW_STATUS_INTENT)) {
			Log.d(TAG, "New Statuses");
			newStatusesCount = intent.getExtras().getInt(UpdaterService.NEW_STATUSES_COUNT, 0);
			AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
			this.onUpdate(context, appWidgetManager, 
				appWidgetManager.getAppWidgetIds(new ComponentName(context, YambaWidget.class)));
		}
	}

	// we define update rate (Example: 10 minutes)
	@Override
	public void onUpdate(Context context, AppWidgetManager appWidgetManager,
			int[] appWidgetIds) {
		super.onUpdate(context, appWidgetManager, appWidgetIds);
		Log.d(TAG, "onUpdate");

		// Display new status. Update all application widgets
		for (int appWidgetId : appWidgetIds) {
			Log.d(TAG, "Updating widget with id " + appWidgetId);
			RemoteViews views = new RemoteViews(context.getPackageName(), 
					R.layout.yamba_widget);
			String countStr = (this.newStatusesCount == 0) ? "" : Integer.toString(this.newStatusesCount);
			views.setTextViewText(R.id.TVNewStatusesCount, countStr);
			
			// set on click to open application
			views.setOnClickPendingIntent(R.id.yamba_icon, PendingIntent.getActivity(
					context, 0, new Intent(context, TimelineActivity.class), 0));
			
			// update widget
			appWidgetManager.updateAppWidget(appWidgetId, views);
			
		}
	}

}
