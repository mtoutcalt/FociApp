package foci.bu.outcalt.fociapp.todo.db;

import android.provider.BaseColumns;

public final class TaskContract {

    private TaskContract() {}

    public static final String DB_NAME = "outcalt.tasklist.db";

    // If you change the database schema, you must increment the database version.
    //adding a new field so incrementing to version 2
    public static final int DB_VERSION = 5;


    public class TaskEntry implements BaseColumns {
        public static final String TABLE = "tasks";
        public static final String COL_TASK_TITLE = "title";
        public static final String COL_TASK_DATE_CREATED = "created_date";
    }

}
