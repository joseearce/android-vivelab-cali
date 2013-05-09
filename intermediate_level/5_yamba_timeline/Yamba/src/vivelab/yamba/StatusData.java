package vivelab.yamba;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class StatusData {
	private static final String TAG = "StatusData";
	
	// database fields
	static final String DATABASE = "timeline.db";
	static final String TABLE = "timeline";
	static final int VERSION = 1;

	// columns fields
	public static final String C_ID = "_id";
	public static final String C_USER = "user";
	public static final String C_TEXT = "txt";
	public static final String C_CREATED_AT = "created_at";
	
	// queries custom variables
	public static final String ORDER_BY_DESC = C_CREATED_AT + " DESC";
	public static final String[] MAX_CREATED_AT_COLUMN = {
		"max(" + C_CREATED_AT + ")"
	};
	public static final String[] DB_TEXT_COLUMN = { C_TEXT };
	
	// database object
	final YambaDbHelper dbHelper;
	
	// main constructor. Create Operation
	public StatusData(Context context) {
		dbHelper = new YambaDbHelper(context);
		Log.i(TAG, "Helper created");
	}
	
	// close operation
	public void close() {
		this.dbHelper.close();
	}
	
	// insert values into database
	public void insertOrIgnore(ContentValues values) {
		Log.d(TAG, "insertOrIgnore on " + values);
		SQLiteDatabase db = this.dbHelper.getWritableDatabase();
		try {
			db.insertWithOnConflict(TABLE, null, values, SQLiteDatabase.CONFLICT_IGNORE);
		} finally {
			db.close();
		}
	}
	
	// gets status order by created_at DESC
	public Cursor getStatusUpdates() {
		SQLiteDatabase db = this.dbHelper.getReadableDatabase();
		return db.query(TABLE, null, null, null, null, null, ORDER_BY_DESC);
	}
	
	// get latest createdAt date
	public long getLatestStatusCreatedAtTime() {
		SQLiteDatabase db = this.dbHelper.getReadableDatabase();
		try {
			Cursor cursor = db.query(TABLE, MAX_CREATED_AT_COLUMN, 
					null, null, null, null, null);
			try {
				return cursor.moveToNext() ? cursor.getLong(0) : Long.MIN_VALUE;
			} finally {
				cursor.close();
			}
		} finally {
			db.close();
		}
	}
	
	// get latest createdAt date
	public String getStatusById(long id) {
		SQLiteDatabase db = this.dbHelper.getReadableDatabase();
		try {
			Cursor cursor = db.query(TABLE, DB_TEXT_COLUMN, C_ID + "=" + id, 
					null, null, null, null);
			try {
				return cursor.moveToNext() ? cursor.getString(0) : null;
			} finally {
				cursor.close();
			}
		} finally {
			db.close();
		}
	}
	
	// database helper class
	private class YambaDbHelper extends SQLiteOpenHelper {

		public YambaDbHelper(Context context) {
			super(context, DATABASE, null, VERSION);
		}

		@Override
		public void onCreate(SQLiteDatabase db) {
			Log.i(TAG, "Creating database: " + DATABASE);
			String sql = String.format("create table %s (%s int primary key, " +
					"%s text, %s text, %s text)", TABLE, C_ID, C_USER, C_TEXT, C_CREATED_AT);
			db.execSQL(sql);
		}

		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
			Log.i(TAG, "Dropping database: " + DATABASE);
			String sql = String.format("drop table %s", TABLE);
			db.execSQL(sql);
			onCreate(db);
		}
		
	}
	
}
