package com.ifeimo.im.common;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

import com.ifeimo.im.R;
import com.ifeimo.im.activity.ChatActivity;
import com.ifeimo.im.activity.GroupChatActivity;

import java.util.Map;

/**
 * Created by lpds on 2017/1/19.
 */
public class IntentManager {


    public static void createIMWindows(Context context, Map<String,String> map){
        Intent intent = new Intent(context, GroupChatActivity.class);
        for(String s : map.keySet()){
            intent.putExtra(s,map.get(s));
        }
        context.startActivity(intent);
        if(context instanceof Activity){
            ((Activity)context).overridePendingTransition(R.anim.left_in,R.anim.left_out);
        }
    }

    public static void createChat(Context context, Map<String,String> map){
        Intent intent = new Intent(context, ChatActivity.class);
        for(String s : map.keySet()){
            intent.putExtra(s,map.get(s));
        }
        context.startActivity(intent);
        if(context instanceof Activity){
            ((Activity)context).overridePendingTransition(R.anim.left_in,R.anim.left_out);
        }
    }

    public static void startSelectPhotoActivity(Activity activity,int requestCode){
        Intent intent = new Intent(Intent.ACTION_PICK,android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        activity.startActivityForResult(intent,requestCode);
    }
}
