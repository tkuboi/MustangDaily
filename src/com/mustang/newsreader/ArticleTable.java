package com.mustang.newsreader;

import android.database.sqlite.SQLiteDatabase;

public class ArticleTable {
    /** Article table in the database. */
    public static final String DATABASE_TABLE_ARTICLE = "article_table";
    
    /** Article table column names and IDs for database access. */
    public static final String ARTICLE_KEY_ID = "_id";
    public static final int ARTICLE_COL_ID = 0;
    
    public static final String ARTICLE_KEY_TITLE = "title";
    public static final int ARTICLE_COL_TITLE = ARTICLE_COL_ID + 1;
    
    public static final String ARTICLE_KEY_DESCRIPTION = "description";
    public static final int ARTICLE_COL_DESCRIPTION = ARTICLE_COL_ID + 2;
    
    public static final String ARTICLE_KEY_CONTENT = "content";
    public static final int ARTICLE_COL_CONTENT = ARTICLE_COL_ID + 3;
    
    public static final String ARTICLE_KEY_PUBDATE = "pubdate";
    public static final int ARTICLE_COL_PUBDATE = ARTICLE_COL_ID + 4;
    
    public static final String ARTICLE_KEY_LINK = "link";
    public static final int ARTICLE_COL_LINK = ARTICLE_COL_ID + 5;
    
    public static final String ARTICLE_KEY_CATEGORIES = "categories";
    public static final int ARTICLE_COL_CATEGORIES = ARTICLE_COL_ID + 6;
    
    public static final String ARTICLE_KEY_IMGSRCS = "imgsrcs";
    public static final int ARTICLE_COL_IMGSRCS = ARTICLE_COL_ID + 7;
    
    /** SQLite database creation statement. Auto-increments IDs of inserted articles.
     * Joke IDs are set after insertion into the database. */
    public static final String DATABASE_CREATE = 
            "create table " + DATABASE_TABLE_ARTICLE + 
            " (" + ARTICLE_KEY_ID + " integer primary key autoincrement, " +
            ARTICLE_KEY_TITLE + " text not null, " + 
            ARTICLE_KEY_DESCRIPTION + " text not null, " + 
            ARTICLE_KEY_CONTENT + " text not null, " + 
            ARTICLE_KEY_PUBDATE + " text not null, " + 
            ARTICLE_KEY_LINK + " text not null, " + 
            ARTICLE_KEY_CATEGORIES + " text not null, " + 
            ARTICLE_KEY_IMGSRCS + " text not null);";
    
    /** SQLite database table removal statement. Only used if upgrading database. */
    public static final String DATABASE_DROP = "drop table if exists " + DATABASE_TABLE_ARTICLE;
    
    /**
     * Initializes the database.
     * 
     * @param database
     *              The database to initialize. 
     */
    public static void onCreate(SQLiteDatabase database) {
        database.execSQL(DATABASE_CREATE);
    }
    
    /**
     * Upgrades the database to a new version.
     * 
     * @param database
     *                  The database to upgrade.
     * @param oldVersion
     *                  The old version of the database.
     * @param newVersion
     *                  The new version of the database.
     */
    public static void onUpgrade(SQLiteDatabase database, int oldVersion, int newVersion) {
        database.execSQL(DATABASE_DROP);
        onCreate(database);
    }
}
