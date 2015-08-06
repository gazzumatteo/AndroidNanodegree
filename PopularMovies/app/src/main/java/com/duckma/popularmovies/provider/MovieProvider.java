package com.duckma.popularmovies.provider;

import de.triplet.simpleprovider.AbstractProvider;
import de.triplet.simpleprovider.Column;
import de.triplet.simpleprovider.Table;

/**
 * Copyright © 2015 DuckMa S.r.l. - http://duckma.com
 * <p/>
 * Created by Matteo Gazzurelli on 06/08/15.
 */
public class MovieProvider extends AbstractProvider {
    public static final String MOVIE_AUTHORITY = "com.duckma.popularmovies.movies";

    @Override
    protected String getAuthority() {
        return MOVIE_AUTHORITY;
    }


    @Table
    public class Movie {

        @Column(value = Column.FieldType.INTEGER, primaryKey = true)
        public static final String KEY_ID = "_id";

        @Column(Column.FieldType.TEXT)
        public static final String KEY_OVERVIEW = "overview";

        @Column(Column.FieldType.TEXT)
        public static final String KEY_RELEASE_DATE = "release_date";

        @Column(Column.FieldType.TEXT)
        public static final String KEY_POSTER_PATH = "poster_path";

        @Column(Column.FieldType.TEXT)
        public static final String KEY_TITLE = "title";

        @Column(Column.FieldType.FLOAT)
        public static final String KEY_VOTE_AVERAGE = "vote_average";

        @Column(Column.FieldType.INTEGER)
        public static final String KEY_RUNTIME = "runtime";

    }

    @Table
    public class MovieDetail {

        @Column(value = Column.FieldType.INTEGER, primaryKey = true)
        public static final String KEY_ID = "_id";

        @Column(Column.FieldType.INTEGER)
        public static final String KEY_MOVIE_ID = "movie_id";

        @Column(Column.FieldType.INTEGER)
        public static final String KEY_CONTENT_TYPE = "content_type";

        @Column(Column.FieldType.TEXT)
        public static final String KEY_KEY = "key";

        @Column(Column.FieldType.TEXT)
        public static final String KEY_NAME = "name";

        @Column(Column.FieldType.TEXT)
        public static final String KEY_SITE = "site";

        @Column(Column.FieldType.TEXT)
        public static final String KEY_AUTHOR = "author";

        @Column(Column.FieldType.TEXT)
        public static final String KEY_CONTENT = "content";

    }

    @Override
    protected int getSchemaVersion() {
        return 1;
    }
}
