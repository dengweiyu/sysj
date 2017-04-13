package com.li.videoapplication.utils;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.content.FileProvider;
import android.util.Log;
import com.li.videoapplication.data.local.StorageUtil;

import java.io.File;


import static android.app.Activity.RESULT_OK;

/**
 * Created by liuwei on 2016/11/7.
 */

public class ExpandUtil {

    public static final int IMAGE_CHANNEL_CAMERA = 11;
    public static final int IMAGE_CHANNEL_GALLERY = 22;

    public static String mCameraImagePath;
    //open camera app
    public void openImageCapture(Activity activity){
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        mCameraImagePath = StorageUtil.getInner()+"/cache/"+System.currentTimeMillis()+".jpeg";
        File file=new File(mCameraImagePath);
        Uri uri;
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.N){
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);     //Android 6.0下使用 更严格的文件目录管理权限
            uri = FileProvider.getUriForFile(activity,"com.ipp.fileprovider",file);
        }else {
            uri=Uri.fromFile(file);
        }

        intent.putExtra(MediaStore.EXTRA_OUTPUT,uri);
        activity.startActivityForResult(intent,IMAGE_CHANNEL_CAMERA);
    }

    //open gallery
    public void openPhotoPick(Activity activity){
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        activity.startActivityForResult(intent,IMAGE_CHANNEL_GALLERY);
    }

    //return image file path
    public String  getImagePath(Context context,int requestCode, int resultCode, Intent data){
        String imagePath=null;
        if (resultCode == RESULT_OK){
            switch (requestCode){
                case ExpandUtil.IMAGE_CHANNEL_CAMERA:
                        imagePath = mCameraImagePath;
                    break;
                case ExpandUtil.IMAGE_CHANNEL_GALLERY:
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT){
                            imagePath = getImagePathAboveKitkat(context,data);
                        }else {
                            imagePath = getImagePathBelowKitkat(context,data);
                        }
                    break;
            }
        }else {
            Log.e("resultCode",resultCode+"");
        }
        return imagePath;
    }



    //open file by other app
    public void openFile(Context context,String path,@Nullable String name){
        if (path == null){
            return;
        }
       // name = FileUtil.parseSuffix(name);
        open(context,path,name);
    }
    //open file by other app
    public void openFile(Context context,String path){
     //   String name = FileUtil.parseSuffix(path);
        String name = "";
        openFile(context,path,name);
    }
    /**
     *
     * @param context
     *
     * @param path
     *              absolutely path
     * @param suffix
     *              后缀
     */
    private void open(Context context,String path,String suffix) {
        try {
            Intent intent = new Intent();
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.setAction(Intent.ACTION_VIEW);
         //   String mime = FileUtil.FileTypeMap.getFileMime(suffix);
            String mime = "";
            Uri uri;
            File file = new File(path);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N){
                intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);     //Android 6.0下使用
                uri = FileProvider.getUriForFile(context,"com.ipp.fileprovider",file);
            }else {
                uri = Uri.fromFile(file);
            }
            intent.setDataAndType(uri, mime);
            context.startActivity(intent);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    @TargetApi(19)
    private String getImagePathAboveKitkat(Context context,Intent data){
        Uri uri = data.getData();
        String imagePath = null;
        //if(DocumentsContract.isDocumentUri(context,uri))
        if (true){
            if (isExternalStorageDocument(uri)){
                String docId = DocumentsContract.getDocumentId(uri);
                final String[] splitDocId = docId.split(":");
                if (splitDocId[0].equals("primary")){
                    return StorageUtil.getInner()+splitDocId[1];
                }
            }else if (isDownloadProvider(uri)){
                String docId = DocumentsContract.getDocumentId(uri);
                Uri contentUri = ContentUris.withAppendedId(Uri.parse("content://downloads/public_downloads"), Long.valueOf(docId));
                return getPathFormProvider(context,contentUri,null,null);
            }else if (isMediaProvider(uri)){
                String docId = DocumentsContract.getDocumentId(uri);
                final String[] splitDocId = docId.split(":");
                final String selection = "_id=?";
                final String[] selectionArgs = new String[]{
                        splitDocId[1]
                };
                if (splitDocId[0].equals("image")){
                    return getPathFormProvider(context,MediaStore.Images.Media.EXTERNAL_CONTENT_URI,selection,selectionArgs);
                }
            }else if (isMediaStore(uri)){
                if (isGooglePhotoUri(uri)){
                    return uri.getLastPathSegment();
                }else {
                    return getPathFormProvider(context,uri,null,null);
                }
            }else if(isFile(uri)){
                return uri.getPath();
            }
        }

        return imagePath;
    }


    private String getImagePathBelowKitkat(Context context,Intent data){
        Cursor cursor = null;
        String imagePath = null;
        try {
            Uri uri = data.getData();
            String[]  proj= {MediaStore.Images.Media.DATA};
            cursor = context.getContentResolver().query(uri, proj, null, null, null);
            if (cursor == null){
                if (uri != null){
                    return uri.getPath();           //适配MIUI
                }
            }
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
            imagePath = cursor.getString(column_index);
            if (imagePath == null){
                imagePath = uri.getPath();
            }
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        }finally {
            if (cursor != null){
                cursor.close();
            }
        }
        return  imagePath;
    }

    public boolean isExternalStorageDocument(Uri uri){
        return "com.android.externalstorage.documents".equals(uri.getAuthority());
    }

    public boolean isDownloadProvider(Uri uri){
        return  "com.android.providers.downloads.documents".equals(uri.getAuthority());
    }

    public boolean isMediaProvider(Uri uri){
        return "com.android.providers.media.documents".equals(uri.getAuthority());
    }

    public boolean isGooglePhotoUri(Uri uri){
        return "com.google.android.apps.photos.content".equals(uri.getAuthority());
    }

    public boolean isFile(Uri uri){
        return "file".equalsIgnoreCase(uri.getScheme());
    }

    // MediaStore (and general)
    public boolean isMediaStore(Uri uri){
        return "content".equalsIgnoreCase(uri.getScheme());
    }

    private String getPathFormProvider(Context context ,Uri uri, String selection,
                                       String[] selectionArgs){
        String imagePath = null;
        Cursor cursor = null;
        if (uri == null){
            return imagePath;
        }
        try {
            String[]  proj= {MediaStore.Images.Media.DATA};
            cursor = context.getContentResolver().query(uri, proj, selection, selectionArgs, null);
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
            imagePath = cursor.getString(column_index);
            if (imagePath == null){
                imagePath = uri.getPath();
            }
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        }finally {
            if (cursor != null){
                cursor.close();
            }
        }
        return  imagePath;
    }

}


