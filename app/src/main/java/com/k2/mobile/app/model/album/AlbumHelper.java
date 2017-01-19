package com.k2.mobile.app.model.album;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.BaseColumns;
import android.provider.MediaStore.Audio.AlbumColumns;
import android.provider.MediaStore.Audio.Albums;
import android.provider.MediaStore.Images.ImageColumns;
import android.provider.MediaStore.Images.Media;
import android.provider.MediaStore.Images.Thumbnails;
import android.provider.MediaStore.MediaColumns;

/**
 * @ClassName: AlbumHelper
 * @Description: 相册管理类
 * @author force
 * @date 2015年8月3日 上午8:43:00
 *
 */
public class AlbumHelper {
    Context context = null;
    ContentResolver contentResolver = null;

    // 缩略图列表
    HashMap<String, String> thumbnailList = new HashMap<String, String>();
    List<HashMap<String, String>> albumList = new ArrayList<HashMap<String, String>>();
    HashMap<String, ImageBucket> bucketList = new HashMap<String, ImageBucket>();

    private static AlbumHelper instance = null;

    private AlbumHelper(Context cxt) {
        if (null == this.context && null != cxt) {
            this.context = cxt;
            contentResolver = context.getContentResolver();
        }
    }

    public static AlbumHelper getHelper(Context cxt) {
        if (null == instance) {
            instance = new AlbumHelper(cxt);
        }
        return instance;
    }

    /**
     * 得到缩略图
     */
    private void getThumbnail() {
        String[] projection = {
                BaseColumns._ID, Thumbnails.IMAGE_ID,
                Thumbnails.DATA
        };
        Cursor cursor = null;
        try {
            cursor = contentResolver.query(Thumbnails.EXTERNAL_CONTENT_URI,
                    projection, null, null, null);
            getThumbnailColumnData(cursor);
        } catch (Exception e) {
        } finally {
            if (null != cursor) {
                cursor.close();
            }
        }
    }

    /**
     * 从数据库中得到缩略图
     * 
     * @param cur
     */
    private void getThumbnailColumnData(Cursor cur) {
        try {
            if (null == cur)
                return;
            if (cur.moveToFirst()) {
                @SuppressWarnings("unused")
                int cId;
                int image_id;
                String image_path;
                int _idColumn = cur.getColumnIndex(BaseColumns._ID);
                int image_idColumn = cur.getColumnIndex(Thumbnails.IMAGE_ID);
                int dataColumn = cur.getColumnIndex(Thumbnails.DATA);

                do {
                    cId = cur.getInt(_idColumn);
                    image_id = cur.getInt(image_idColumn);
                    image_path = cur.getString(dataColumn);
                    thumbnailList.put("" + image_id, image_path);
                } while (cur.moveToNext());
            }
        } catch (Exception e) {
        }
    }

    /**
     * @Description 得到原图
     */
    @SuppressWarnings("unused")
    private void getAlbum() {
        String[] projection = {
                BaseColumns._ID, AlbumColumns.ALBUM, AlbumColumns.ALBUM_ART,
                AlbumColumns.ALBUM_KEY, AlbumColumns.ARTIST, AlbumColumns.NUMBER_OF_SONGS
        };
        Cursor cursor = null;
        try {
            cursor = contentResolver.query(Albums.EXTERNAL_CONTENT_URI,
                    projection, null, null, null);
            getAlbumColumnData(cursor);
        } catch (Exception e) {
        } finally {
            if (null != cursor) {
                cursor.close();
            }
        }

    }

    /**
     * 从数据库中得到原图
     * 
     * @param cur
     */
    private void getAlbumColumnData(Cursor cur) {
        try {
            if (cur.moveToFirst()) {
                int _id;
                String album;
                String albumArt;
                String albumKey;
                String artist;
                int numOfSongs;

                int _idColumn = cur.getColumnIndex(BaseColumns._ID);
                int albumColumn = cur.getColumnIndex(AlbumColumns.ALBUM);
                int albumArtColumn = cur.getColumnIndex(AlbumColumns.ALBUM_ART);
                int albumKeyColumn = cur.getColumnIndex(AlbumColumns.ALBUM_KEY);
                int artistColumn = cur.getColumnIndex(AlbumColumns.ARTIST);
                int numOfSongsColumn = cur.getColumnIndex(AlbumColumns.NUMBER_OF_SONGS);

                do {
                    _id = cur.getInt(_idColumn);
                    album = cur.getString(albumColumn);
                    albumArt = cur.getString(albumArtColumn);
                    albumKey = cur.getString(albumKeyColumn);
                    artist = cur.getString(artistColumn);
                    numOfSongs = cur.getInt(numOfSongsColumn);
                    HashMap<String, String> hash = new HashMap<String, String>();
                    hash.put("_id", _id + "");
                    hash.put("album", album);
                    hash.put("albumArt", albumArt);
                    hash.put("albumKey", albumKey);
                    hash.put("artist", artist);
                    hash.put("numOfSongs", numOfSongs + "");
                    albumList.add(hash);
                } while (cur.moveToNext());

            }
        } catch (Exception e) {
        }
    }

    // 是否创建图片集
    boolean hasBuildImagesBucketList = false;

    /**
     * @Description 获取图片集
     */
    private void buildImagesBucketList() {
    	bucketList.clear();
        Cursor cur = null;
        // long startTime = System.currentTimeMillis();
        try {
            // 构造缩略图索引
            getThumbnail();
            //相册uri
            Uri imageUri = Media.EXTERNAL_CONTENT_URI;
            // 构造相册索引
            String columns[] = new String[] {
                    BaseColumns._ID, ImageColumns.BUCKET_ID,
                    ImageColumns.PICASA_ID, MediaColumns.DATA, MediaColumns.DISPLAY_NAME, MediaColumns.TITLE,
                    MediaColumns.SIZE, ImageColumns.BUCKET_DISPLAY_NAME
            };
            //条件
            String selection = MediaColumns.MIME_TYPE + " =? or "
                    + MediaColumns.MIME_TYPE + " =? or "
                    + MediaColumns.MIME_TYPE + " =?";
            //jpg,jpeg,png
            String[] selectionArgs = { "image/jpg", "image/jpeg","image/png"};

            // 得到一个游标
            cur = contentResolver.query(imageUri, columns,
                    selection, selectionArgs,null);
            if (null == cur)
                return;

            if (cur.moveToFirst()) {
                // 获取指定列的索引
                int photoIDIndex = cur.getColumnIndexOrThrow(BaseColumns._ID);
                int photoPathIndex = cur.getColumnIndexOrThrow(MediaColumns.DATA);
                // int photoNameIndex =
                // cur.getColumnIndexOrThrow(Media.DISPLAY_NAME);
                // int photoTitleIndex = cur.getColumnIndexOrThrow(Media.TITLE);
                // int photoSizeIndex = cur.getColumnIndexOrThrow(Media.SIZE);
                int bucketDisplayNameIndex = cur
                        .getColumnIndexOrThrow(ImageColumns.BUCKET_DISPLAY_NAME);
                int bucketIdIndex = cur.getColumnIndexOrThrow(ImageColumns.BUCKET_ID);
                // int picasaIdIndex =
                // cur.getColumnIndexOrThrow(Media.PICASA_ID);
                // 获取图片总数
                @SuppressWarnings("unused")
                int totalNum = cur.getCount();

                do {
                    String id = cur.getString(photoIDIndex);
                    // String name = cur.getString(photoNameIndex);
                    String path = cur.getString(photoPathIndex);
                    // String title = cur.getString(photoTitleIndex);
                    // String size = cur.getString(photoSizeIndex);
                    String bucketName = cur.getString(bucketDisplayNameIndex);
                    String bucketId = cur.getString(bucketIdIndex);
                    // String picasaId = cur.getString(picasaIdIndex);

                    ImageBucket bucket = bucketList.get(bucketId);
                    if (bucket == null) {
                        bucket = new ImageBucket();
                        bucketList.put(bucketId, bucket);
                        bucket.imageList = new ArrayList<ImageItem>();
                        bucket.bucketName = bucketName;
                    }
                    bucket.count++;
                    ImageItem imageItem = new ImageItem();
                    imageItem.setImageId(id);
                    imageItem.setImagePath(path);
                    imageItem.setThumbnailPath(thumbnailList.get(id));
                    bucket.imageList.add(0, imageItem);

                } while (cur.moveToNext());
            }
        } catch (Exception e) {
        } finally {
            cur.close();
        }

        try {
            Iterator<Entry<String, ImageBucket>> itr = bucketList.entrySet()
                    .iterator();
            while (itr.hasNext()) {
                Map.Entry<String, ImageBucket> entry = itr
                        .next();
                ImageBucket bucket = entry.getValue();
                for (int i = 0; i < bucket.imageList.size(); ++i) {
                    @SuppressWarnings("unused")
                    ImageItem image = bucket.imageList.get(i);
                }
            }
            hasBuildImagesBucketList = true;
        } catch (Exception e) {
        }
    }

    /**
     * 得到图片集
     * @param refresh
     * @return
     */
    public List<ImageBucket> getImagesBucketList(boolean refresh) {
        try {
            if (refresh || (!refresh && !hasBuildImagesBucketList)) {
                buildImagesBucketList();
            }
            List<ImageBucket> imageList = new ArrayList<ImageBucket>(bucketList.values());
            return imageList;
        } catch (Exception e) {
            return null;
        }
    }

    boolean probablyDefaultCameraPhotoSet(String photoSetName) {
    	//from my test result, different phones use different names to represent 
    	//default photo set
    	if (photoSetName == null || photoSetName.isEmpty()) {
    		return false;
    	}
    	
    	String lowerCaseName = photoSetName.toLowerCase();
    	
    	//todo eric i18n
    	return lowerCaseName.contains("camera") || lowerCaseName.contains("相机");
    }
    
    /**
     * 得到原始图像路径
     * 
     * @param image_id
     * @return
     */
    @SuppressWarnings("unused")
    private String getOriginalImagePath(String image_id) {
        try {
            String path = null;
            String[] projection = {
                    BaseColumns._ID, MediaColumns.DATA
            };
            Cursor cursor = contentResolver.query(Media.EXTERNAL_CONTENT_URI,
                    projection, BaseColumns._ID + "=" + image_id, null, null);
            if (cursor != null) {
                try {
                    cursor.moveToFirst();
                    path = cursor.getString(cursor.getColumnIndex(MediaColumns.DATA));
                } catch (Exception e) {
                } finally {
                    cursor.close();
                }
            }
            return path;
        } catch (Exception e) {
            return null;
        }
    }

}