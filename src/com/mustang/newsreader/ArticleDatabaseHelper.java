package com.mustang.newsreader;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;

public class ArticleDatabaseHelper extends android.database.sqlite.SQLiteOpenHelper {
    /** The name of the database. */
    public static final String DATABASE_NAME = "articledatabase.db";
    
    /** The starting database version. */
    public static final int DATABASE_VERSION = 1;
    
    /**
     * Create a helper object to create, open, and/or manage a database.
     * 
     * @param context
     *                  The application context.
     * @param name
     *                  The name of the database.
     * @param factory
     *                  Factory used to create a cursor. Set to null for default behavior.
     * @param version
     *                  The starting database version.
     */
    public ArticleDatabaseHelper(Context context, String name,
        CursorFactory factory, int version) {
        super(context, name, null, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        ArticleTable.onCreate(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        ArticleTable.onUpgrade(db, oldVersion, newVersion);
    }
}
