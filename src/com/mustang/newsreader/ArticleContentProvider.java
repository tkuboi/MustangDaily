package com.mustang.newsreader;

import java.util.Arrays;
import java.util.HashSet;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;

public class ArticleContentProvider extends ContentProvider {
    /** The article database. */
    private ArticleDatabaseHelper database;

    /** Values for the URIMatcher. */
    private static final int ARTICLE_ID = 1;

    /** The authority for this content provider. */
    private static final String AUTHORITY = "com.mustang.newsreader.contentprovider";

    /**
     * The database table to read from and write to, and also the root path for
     * use in the URI matcher. This is essentially a label to a two-dimensional
     * array in the database filled with rows of s whose columns contain
     *  data.
     */
    private static final String BASE_PATH = "article_table";

    /**
     * This provider's content location. Used by accessing applications to
     * interact with this provider.
     */
    public static final Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY
            + "/" + BASE_PATH);

    /**
     * Matches content URIs requested by accessing applications with possible
     * expected content URI formats to take specific actions in this provider.
     */
    private static final UriMatcher sURIMatcher = new UriMatcher(
            UriMatcher.NO_MATCH);
    static {
        sURIMatcher.addURI(AUTHORITY, BASE_PATH + "/articles/#", ARTICLE_ID);
    }

    @Override
    public boolean onCreate() {
        database = new ArticleDatabaseHelper(getContext(),
                ArticleDatabaseHelper.DATABASE_NAME, null,
                ArticleDatabaseHelper.DATABASE_VERSION);

        return true;
    }

    /**
     * Fetches rows from the  table. Given a specified URI that contains a
     * filter, returns a list of s from the  table matching that filter
     * in the form of a Cursor.<br>
     * <br>
     * 
     * Overrides the built-in version of <b>query(...)</b> provided by
     * ContentProvider.<br>
     * <br>
     * 
     * For more information, read the documentation for the built-in version of
     * this method by hovering over the method name in the method signature
     * below this comment block in Eclipse and clicking <b>query(...)</b> in the
     * Overrides details.
     * */
    @Override
    public Cursor query(Uri uri, String[] projection, String selection,
            String[] selectionArgs, String sortOrder) {
        /** Use a helper class to perform a query for us. */
        SQLiteQueryBuilder queryBuilder = new SQLiteQueryBuilder();

        /** Make sure the projection is proper before querying. */
        checkColumns(projection);

        /** Set up helper to query our s table. */
        queryBuilder.setTables(ArticleTable.DATABASE_TABLE_ARTICLE);

        /** Perform the database query. */
        SQLiteDatabase db = this.database.getWritableDatabase();
        Cursor cursor = queryBuilder.query(db, projection, selection, null,
                null, null, null);

        /**
         * Set the cursor to automatically alert listeners for content/view
         * refreshing.
         */
        cursor.setNotificationUri(getContext().getContentResolver(), uri);

        return cursor;
    }

    /** We don't really care about this method for this application. */
    @Override
    public String getType(Uri uri) {
        return null;
    }

    /**
     * Inserts an article into the article table. Given a specific URI that contains an
     * article and the values of that article, writes a new row in the table filled
     * with that article's information and gives the article a new ID, then returns a
     * URI containing the ID of the inserted article.<br>
     * <br>
     * 
     * Overrides the built-in version of <b>insert(...)</b> provided by
     * ContentProvider.<br>
     * <br>
     * 
     * For more information, read the documentation for the built-in version of
     * this method by hovering over the method name in the method signature
     * below this comment block in Eclipse and clicking <b>insert(...)</b> in
     * the Overrides details.
     * */
    @Override
    public Uri insert(Uri uri, ContentValues values) {

        /** Open the database for writing. */
        SQLiteDatabase sqlDB = this.database.getWritableDatabase();

        /** Will contain the ID of the inserted article. */
        long id = 0;

        /** Match the passed-in URI to an expected URI format. */
        int uriType = sURIMatcher.match(uri);

        switch (uriType) {

            /**
             * Expects a article ID, but we will do nothing with the passed-in ID since
             * the database will automatically handle ID assignment and
             * incrementation. IMPORTANT: article ID cannot be set to -1 in passed-in
             * URI; -1 is not interpreted as a numerical value by the URIMatcher.
             */
            case ARTICLE_ID:
    
                /**
                 * Perform the database insert, placing the  at the bottom of
                 * the table.
                 */
                id = sqlDB.insert(ArticleTable.DATABASE_TABLE_ARTICLE, null, values);
                break;
    
            default:
                throw new IllegalArgumentException("Unknown URI: " + uri);
        }

        /**
         * Alert any watchers of an underlying data change for content/view
         * refreshing.
         */
        getContext().getContentResolver().notifyChange(uri, null);

        return Uri.parse(BASE_PATH + "/" + id);
    }

    /**
     * Removes a row from the  table. Given a specific URI containing a 
     * ID, removes rows in the table that match the ID and returns the number of
     * rows removed. Since IDs are automatically incremented on insertion, this
     * will only ever remove a single row from the  table.<br>
     * <br>
     * 
     * Overrides the built-in version of <b>delete(...)</b> provided by
     * ContentProvider.<br>
     * <br>
     * 
     * For more information, read the documentation for the built-in version of
     * this method by hovering over the method name in the method signature
     * below this comment block in Eclipse and clicking <b>delete(...)</b> in
     * the Overrides details.
     * */
    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        /** Open the database for writing. */
        SQLiteDatabase sqlDB = this.database.getWritableDatabase();
        
        /** Number of rows deleted */
        int deletedRowCount = 0;
        
        /** Match the passed-in URI to an expected URI format. */
        int uriType = sURIMatcher.match(uri);
        
        switch (uriType) {
        
            /**
             * Expects a  ID, but we will do nothing with the passed-in ID since
             * the database will automatically handle ID assignment and
             * incrementation. IMPORTANT:  ID cannot be set to -1 in passed-in
             * URI; -1 is not interpreted as a numerical value by the URIMatcher.
             */
            case ARTICLE_ID:
    
                /**
                 * Fetch the last segment of the URI, which should be a filter
                 * number.
                 */
                String articleId = uri.getLastPathSegment();
                
                /**
                 * Perform the database delete
                 */
                deletedRowCount = sqlDB.delete(ArticleTable.DATABASE_TABLE_ARTICLE,
                                               ArticleTable.ARTICLE_KEY_ID + "=" + articleId,
                                               null);
                break;
    
            default:
                throw new IllegalArgumentException("Unknown URI: " + uri);
        }
        
        if(deletedRowCount > 0) {
        
            /**
             * Alert any watchers of an underlying data change for content/view
             * refreshing.
             */
            getContext().getContentResolver().notifyChange(uri, null);
        }
        
        return deletedRowCount;
    }

    /**
     * Updates a row in the  table. Given a specific URI containing a 
     * ID and the new  values, updates the values in the row with the
     * matching ID in the table. Since IDs are automatically incremented on
     * insertion, this will only ever update a single row in the  table.<br>
     * <br>
     * 
     * Overrides the built-in version of <b>update(...)</b> provided by
     * ContentProvider.<br>
     * <br>
     * 
     * For more information, read the documentation for the built-in version of
     * this method by hovering over the method name in the method signature
     * below this comment block in Eclipse and clicking <b>update(...)</b> in
     * the Overrides details.
     * */
    @Override
    public int update(Uri uri, ContentValues values, String selection,
            String[] selectionArgs) {
        /** Open the database for writing. */
        SQLiteDatabase sqlDB = this.database.getWritableDatabase();
        
        /** Number of rows deleted */
        int updatedRowCount = 0;
        
        /** Match the passed-in URI to an expected URI format. */
        int uriType = sURIMatcher.match(uri);
        
        switch (uriType) {
        
            /**
             * Expects a  ID, but we will do nothing with the passed-in ID since
             * the database will automatically handle ID assignment and
             * incrementation. IMPORTANT:  ID cannot be set to -1 in passed-in
             * URI; -1 is not interpreted as a numerical value by the URIMatcher.
             */
            case ARTICLE_ID:
    
                /**
                 * Fetch the last segment of the URI, which should be a filter
                 * number.
                 */
                String articleId = uri.getLastPathSegment();
                
                /**
                 * Perform the database delete
                 */
                updatedRowCount = sqlDB.update(ArticleTable.DATABASE_TABLE_ARTICLE,
                                               values,
                                               ArticleTable.ARTICLE_KEY_ID + "=" + articleId,
                                               null);
                break;
    
            default:
                throw new IllegalArgumentException("Unknown URI: " + uri);
        }
        
        if(updatedRowCount > 0) {
        
            /**
             * Alert any watchers of an underlying data change for content/view
             * refreshing.
             */
            getContext().getContentResolver().notifyChange(uri, null);
        }
        
        return updatedRowCount;
    }

    /**
     * Verifies the correct set of columns to return data from when performing a
     * query.
     * 
     * @param projection
     *            The set of columns about to be queried.
     */
    private void checkColumns(String[] projection) {
        String[] available = { ArticleTable.ARTICLE_KEY_ID, ArticleTable.ARTICLE_KEY_TITLE, 
                ArticleTable.ARTICLE_KEY_DESCRIPTION, ArticleTable.ARTICLE_KEY_CONTENT, 
                ArticleTable.ARTICLE_KEY_PUBDATE, ArticleTable.ARTICLE_KEY_LINK, 
                ArticleTable.ARTICLE_KEY_CATEGORIES, ArticleTable.ARTICLE_KEY_IMGSRCS };

        if (projection != null) {
            HashSet<String> requestedColumns = new HashSet<String>(
                    Arrays.asList(projection));
            HashSet<String> availableColumns = new HashSet<String>(
                    Arrays.asList(available));

            if (!availableColumns.containsAll(requestedColumns)) {
                throw new IllegalArgumentException(
                        "Unknown columns in projection");
            }
        }
    }
}
