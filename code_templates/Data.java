package package.name;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Provides high-level access to sqlite db
 */
public class DataClass {
	private static final String TAG = "DataClass";
	
	// database fields
	static final String DATABASE = "db.db";
	static final String TABLE = "table";
	static final int VERSION = 1;

	// columns fields
	public static final String C_ID = "_id";
	public static final String C_COLUMN = "column";
	
	// queries custom variables
	public static final String ORDER_BY_DESC = C_ID + " DESC";

	// database object
	final CustomDbHelper dbHelper;
	
	// main constructor. Create Operation
	public DataClass(Context context) {
		dbHelper = new CustomDbHelper(context);
		Log.i(TAG, "Database Helper created");
	}
	
	// close operation
	public void close() {
		this.dbHelper.close();
	}
	
	// simple insert query
	public void insertOrIgnore(ContentValues values) {
		Log.d(TAG, "insertOrIgnore on " + values);
		SQLiteDatabase db = this.dbHelper.getWritableDatabase();
		try {
			db.insertWithOnConflict(TABLE, null, values, SQLiteDatabase.CONFLICT_IGNORE);
		} finally {
			db.close();
		}
	}
	
	// simple select query
	public Cursor getStatusUpdates() {
		SQLiteDatabase db = this.dbHelper.getReadableDatabase();
		return db.query(TABLE, null, null, null, null, null, ORDER_BY_DESC);
	}
	
	// custom query
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
	
	// database helper class
	private class CustomDbHelper extends SQLiteOpenHelper {

		public CustomDbHelper(Context context) {
			super(context, DATABASE, null, VERSION);
		}

		@Override
		public void onCreate(SQLiteDatabase db) {
			Log.i(TAG, "Creating database: " + DATABASE);
			String sql = String.format("create table %s (%s int primary key, " +
					"%s text)", TABLE, C_ID, C_COLUMN);
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