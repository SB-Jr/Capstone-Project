package com.project.sbjr.showledger.provider;

/**
 * Created by sbjr on 8/1/17.
 */

public class ProviderContract {


    public static final String CONTENT_AUTHORITY ="content://com.project.sbjr.showledger.provider/";
    public static final String AUTHORITY ="com.project.sbjr.showledger.provider";

    public static final String URI_MOVIE="movie/";
    public static final String URI_TV="tv/";
    public static final String URI_WATCHED="watched";
    public static final String URI_WISH="wish";
    public static final String URI_INCOMPLETE="incomplete";

    public static final String URI_MATCH_MOVIE_WATCHED=URI_MOVIE+URI_WATCHED;
    public static final String URI_MATCH_MOVIE_WISH=URI_MOVIE+URI_WISH;

    public static final String URI_MATCH_TV_WATCHED=URI_TV+URI_WATCHED;
    public static final String URI_MATCH_TV_WISH=URI_TV+URI_WISH;
    public static final String URI_MATCH_TV_INCOMPLETE=URI_TV+URI_INCOMPLETE;


    public static final String VND_STRING="vnd.project.sbjr.showledger.provider/";

    public static final String VND_MOVIE_WATCHED=URI_MOVIE+URI_WATCHED;
    public static final String VND_MOVIE_WISH=URI_MOVIE+URI_WISH;

    public static final String VND_TV_WATCHED=URI_TV+URI_WATCHED;
    public static final String VND_TV_WISH=URI_TV+URI_WISH;
    public static final String VND_TV_INCOMPLETE=URI_TV+URI_INCOMPLETE;

}
