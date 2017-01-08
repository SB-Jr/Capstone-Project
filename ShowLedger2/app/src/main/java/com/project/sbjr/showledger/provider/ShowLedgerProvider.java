package com.project.sbjr.showledger.provider;

import android.content.ContentProvider;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.net.Uri;

import com.project.sbjr.showledger.database.DatabaseContract;
import com.project.sbjr.showledger.database.ShowTableHelper;

public class ShowLedgerProvider extends ContentProvider {

    private static final UriMatcher sUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
    private ShowTableHelper dbHelper;

    static {
        sUriMatcher.addURI(ProviderContract.AUTHORITY,ProviderContract.URI_MATCH_MOVIE_WATCHED,1);
        sUriMatcher.addURI(ProviderContract.AUTHORITY,ProviderContract.URI_MATCH_MOVIE_WISH,2);

        sUriMatcher.addURI(ProviderContract.AUTHORITY,ProviderContract.URI_MATCH_TV_WATCHED,3);
        sUriMatcher.addURI(ProviderContract.AUTHORITY,ProviderContract.URI_MATCH_TV_WISH,4);
        sUriMatcher.addURI(ProviderContract.AUTHORITY,ProviderContract.URI_MATCH_TV_INCOMPLETE,5);
    }


    public ShowLedgerProvider() { }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) throws IllegalArgumentException {
        String tableName = getTableName(uri);
        if(tableName==null){
            throw new IllegalArgumentException();
        }
        int id = Integer.parseInt(uri.getLastPathSegment());
        return dbHelper.deleteEntry(tableName,id);
    }

    @Override
    public String getType(Uri uri) {
        return ContentResolver.CURSOR_ITEM_BASE_TYPE+getVnd(uri);
    }


    @Override
    public Uri insert(Uri uri, ContentValues values) throws IllegalArgumentException{
        String tableName = getTableName(uri);
        if(tableName==null){
            throw new IllegalArgumentException();
            //return null;
        }
        dbHelper.insertEntryusingContentValues(tableName,values);
        getContext().getContentResolver().notifyChange(uri, null);
        Uri uri1 =  Uri.parse(getUriFromTableName(getTableName(uri))+"/"+values.getAsString(DatabaseContract.TABLE_SHOW_ID));
        return uri1;
    }


    @Override
    public boolean onCreate() {
        dbHelper = new ShowTableHelper(getContext());
        return true;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder)throws IllegalArgumentException {
        String tableName = getTableName(uri);
        if(tableName==null){
            throw new IllegalArgumentException();
        }
        Cursor cursor = dbHelper.getShowIds(tableName);
        cursor.setNotificationUri(getContext().getContentResolver(),uri);
        return cursor;
    }

    //method not required in this case
    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        return 0;
    }


    public String getTableName(Uri uri){
        String tableName;
        switch (sUriMatcher.match(uri)){
            case 1:tableName = DatabaseContract.MOVIE_WATCHED_LIST_TABLE;
                break;
            case 2:tableName = DatabaseContract.MOVIE_WISH_LIST_TABLE;
                break;
            case 3:tableName = DatabaseContract.TV_SHOW_WATCHED_LIST_TABLE;
                break;
            case 4:tableName = DatabaseContract.TV_SHOW_WISH_LIST_TABLE;
                break;
            case 5:tableName = DatabaseContract.TV_SHOW_INCOMPLETE_LIST_TABLE;
                break;
            default:tableName = null;
        }
        return tableName;
    }

    private Uri getUriFromTableName(String tableName){
        if(tableName==null){
            return null;
        }
        else{
            Uri uri = null;
            if(tableName.equalsIgnoreCase(DatabaseContract.MOVIE_WATCHED_LIST_TABLE)){
                uri = Uri.parse(ProviderContract.CONTENT_AUTHORITY +ProviderContract.URI_MATCH_MOVIE_WATCHED);
            }
            if(tableName.equalsIgnoreCase(DatabaseContract.MOVIE_WISH_LIST_TABLE)){
                uri = Uri.parse(ProviderContract.CONTENT_AUTHORITY +ProviderContract.URI_MATCH_MOVIE_WISH);
            }
            if(tableName.equalsIgnoreCase(DatabaseContract.TV_SHOW_WATCHED_LIST_TABLE)){
                uri = Uri.parse(ProviderContract.CONTENT_AUTHORITY +ProviderContract.URI_MATCH_TV_WATCHED);
            }
            if(tableName.equalsIgnoreCase(DatabaseContract.TV_SHOW_WISH_LIST_TABLE)){
                uri = Uri.parse(ProviderContract.CONTENT_AUTHORITY +ProviderContract.URI_MATCH_TV_WISH);
            }
            if(tableName.equalsIgnoreCase(DatabaseContract.TV_SHOW_INCOMPLETE_LIST_TABLE)){
                uri = Uri.parse(ProviderContract.CONTENT_AUTHORITY +ProviderContract.URI_MATCH_TV_INCOMPLETE);
            }
            return uri;
        }
    }

    private String getVnd(Uri uri) throws IllegalArgumentException{
        String vnd;
        switch (sUriMatcher.match(uri)){
            case 1:vnd = ProviderContract.VND_STRING+ProviderContract.VND_MOVIE_WATCHED;
                break;
            case 2:vnd = ProviderContract.VND_STRING+ProviderContract.VND_MOVIE_WISH;
                break;
            case 3:vnd = ProviderContract.VND_STRING+ProviderContract.VND_TV_WATCHED;
                break;
            case 4:vnd = ProviderContract.VND_STRING+ProviderContract.VND_TV_WISH;
                break;
            case 5:vnd = ProviderContract.VND_STRING+ProviderContract.VND_TV_INCOMPLETE;
                break;
            default:vnd = null;
                throw new IllegalArgumentException();

        }
        return vnd;
    }
}
