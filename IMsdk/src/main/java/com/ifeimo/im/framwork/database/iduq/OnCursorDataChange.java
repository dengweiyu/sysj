package com.ifeimo.im.framwork.database.iduq;

import android.database.Cursor;

/**
 * Created by lpds on 2017/2/14.
 */
public interface OnCursorDataChange {

    void onCursorDataChange(Cursor cursor);

    void onFirstDataChange(Cursor cursor);

}
