package com.example.vitamio.model;

import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created with IntelliJ IDEA.
 * User: william-work from mobicloud.com.cn
 * Date: 12-7-12
 * Time: 上午11:28
 */
public class MediaFile {

    public static final String AUTHORITY = "com.example.vitamio.model.provider.Media";

    private MediaFile() {
    }

    public static final class Medias implements BaseColumns {
        //Content Provider
        public static final Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/media");

        public static final String DEFAULT_SORT_ORDER = "modified DESC";

        public static final String TABLE_NAME = "media";
        //MIME
        public static final String CONTENT_TYPE = "vnd.android.cursor.dir/vnd.google.media";
        //MIME
        public static final String CONTENT_ITEM_TYPE = "vnd.android.cursor.item/vnd.google.media";

        //媒体文件的标题
        public static final String TITLE = "title";
        //媒体文件的标题拼音
        public static final String TITLE_PINYIN = "title_pinyin";
        //媒体的路径
        public static final String PATH = "path";
        //最后一次访问时间
        public static final String LAST_ACCESS_TIME = "last_access_time";
        //媒体时长
        public static final String DURATION = "duration";
        //播放进度
        public static final String POSITION = "position";
        //媒体的缩略图
        public static final String THUMB = "thumb";
    }


}
