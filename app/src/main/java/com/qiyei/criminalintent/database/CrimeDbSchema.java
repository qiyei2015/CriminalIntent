package com.qiyei.criminalintent.database;

/**
 * Created by qiyei on 2016/6/14.
 */
public class CrimeDbSchema {

    public final class CrimeTable {

        public static final String NAME = "crime";

        public final class Cols{

            public static final String UUID = "uuid";
            public static final String TITLE = "title";
            public static final String DETAIL = "detail";
            public static final String DATE = "date";
            public static final String SOLVED = "solved";
            public static final String SUSPECT = "suspect";
        }
    }
}
