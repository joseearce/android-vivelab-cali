package package.name;

import android.content.Context;
import android.database.Cursor;
import android.support.v4.widget.SimpleCursorAdapter;
import android.view.View;

public class CursorAdapter extends SimpleCursorAdapter {
	static final String[] FROM = { Data.C_USER };
	static final int[] TO = { R.id.TVStatusUser };
	
	
	public CursorAdapter(Context context, Cursor c) {
		super(context, R.layout.row, c, FROM, TO, 0);
	}

	@Override
	public void bindView(View view, Context context, Cursor cursor) {
		super.bindView(view, context, cursor);
		
		// change view info or style
	}

}
