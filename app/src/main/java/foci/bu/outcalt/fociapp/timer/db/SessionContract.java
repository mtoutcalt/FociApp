package foci.bu.outcalt.fociapp.timer.db;

import android.provider.BaseColumns;

public final class SessionContract {

    private SessionContract() {}

    public static final String DB_NAME = "outcalt.tasklist.db";

    // If you change the database schema, you must increment the database version.
    //adding a new field so incrementing to version 2
    public static final int DB_VERSION = 6;


    public class SessionEntry implements BaseColumns {
        public static final String TABLE = "session";
        public static final String COL_SESSION_TITLE = "interrupts";
        public static final String COL_SESSION_DATE_CREATED = "created_date";
    }

}
