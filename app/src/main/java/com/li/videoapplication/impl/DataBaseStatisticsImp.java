package com.li.videoapplication.impl;

import android.content.Context;

import com.google.gson.Gson;
import com.li.videoapplication.data.model.entity.StatisticsModel;
import com.li.videoapplication.interfaces.IDataBaseStatistics;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.List;

/**
 * Created by linhui on 2018/4/2.
 */
class DataBaseStatisticsImp implements IDataBaseStatistics {

    private Context context;

    DataBaseStatisticsImp(Context context) {
        this.context = context;
    }

    @Override
    public String getDbJsonData(int loadCount) {
        return new Gson().toJson(getResourceData(loadCount));
    }

    @Override
    public String getDbAllJsonData() {
        return new Gson().toJson(getResourceAllData());
    }

    @Override
    public boolean exportDbAllDataToFile(File outfile) {
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(getDbAllJsonData().getBytes());
        try {
            OutputStream fileOutputStream = new FileOutputStream(outfile);
            byte[] b = new byte[1024];
            while (byteArrayInputStream.read(b) != -1) {
                fileOutputStream.write(b, 0, b.length);
            }
            fileOutputStream.close();
            byteArrayInputStream.close();
            return true;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return false;
    }

    @Override
    public List<StatisticsModel> getResourceData(int loadCount) {
        return null;
    }

    @Override
    public List<StatisticsModel> getResourceAllData() {
        return null;
    }
}
