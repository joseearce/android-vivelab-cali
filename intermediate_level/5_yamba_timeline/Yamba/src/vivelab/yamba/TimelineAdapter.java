package vivelab.yamba;

import android.content.Context;
import android.database.Cursor;
import android.support.v4.widget.SimpleCursorAdapter;
import android.text.format.DateUtils;
import android.view.View;
import android.widget.TextView;

public class TimelineAdapter extends SimpleCursorAdapter {
	static final String[] FROM = { StatusData.C_USER, 
		StatusData.C_CREATED_AT, StatusData.C_TEXT,  };
	static final int[] TO = { R.id.TVStatusUser, 
		R.id.TVStatusCreatedAt, R.id.TVStatusText };
	
	
	public TimelineAdapter(Context context, Cursor c) {
		super(context, R.layout.row, c, FROM, TO, 0);
	}

	@Override
	public void bindView(View view, Context context, Cursor cursor) {
		super.bindView(view, context, cursor);
		
		// change date format
		long timestamp = cursor.getLong(cursor.getColumnIndex(StatusData.C_CREATED_AT));
		TextView TVStatusCreatedAt = (TextView) view.findViewById(R.id.TVStatusCreatedAt);
		TVStatusCreatedAt.setText(DateUtils.getRelativeTimeSpanString(timestamp));
	}

}
