package foci.bu.outcalt.fociapp.timer.db;


import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class SessionDbHelper extends SQLiteOpenHelper {

    private static final String TAG = "StateChange";

    public SessionDbHelper(Context context) {
        super(context, SessionContract.DB_NAME, null, SessionContract.DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        Log.i(TAG, "Creating the DB Table");

        String createTable = "CREATE TABLE " + SessionContract.SessionEntry.TABLE + " ( " +
                SessionContract.SessionEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                SessionContract.SessionEntry.COL_SESSION_TITLE + " TEXT NOT NULL, " +
                SessionContract.SessionEntry.COL_SESSION_DATE_CREATED + " TEXT NOT NULL);";

        db.execSQL(createTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.i(TAG, "Drop Table for Schema Change");
        db.execSQL("DROP TABLE IF EXISTS " + SessionContract.SessionEntry.TABLE);
        onCreate(db);
    }

}
