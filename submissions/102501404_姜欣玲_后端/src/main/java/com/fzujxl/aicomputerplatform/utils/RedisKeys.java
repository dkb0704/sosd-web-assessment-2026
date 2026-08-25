package com.fzujxl.aicomputerplatform.utils;

public class RedisKeys {

    public static final String SIGN_DAY_KEY = "sign:day:%d:%s";

    public static final String SIGN_STREAK_KEY = "sign:streak:%d";

    public static final String JWT_BLACKLIST_PREFIX = "jwt:blacklist:";

    public static final String HOT_KEY_PREFIX = "hot:";

    public static final String ARTWORK_DETAIL_KEY_PREFIX = "artwork:detail:";

    public static final String ARTWORK_DETAIL_LOCK_KEY_PREFIX = "artwork:detail:lock:";

    public static final String TOKEN_VERSION_KEY = "token:version:%d";

    public static final String GALLERY_LIST_CACHE_KEY = "gallery:list:%s:%s:%s:%d:%d";

    public static final String GALLERY_IS_VIEWED_KEY = "isViewed:%d:%s";

    public static final String GALLERY_VIEW_COUNT_KEY = "viewCount:%d";

}