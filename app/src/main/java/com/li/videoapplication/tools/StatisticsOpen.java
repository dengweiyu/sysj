package com.li.videoapplication.tools;

import android.content.Context;

import com.li.videoapplication.data.model.entity.StatisticsModel;
import com.li.videoapplication.impl.StatisticsImp;
import com.li.videoapplication.interfaces.ICell;
import com.li.videoapplication.interfaces.ICellProcess;
import com.li.videoapplication.interfaces.IStatistics;

/**
 * Created by linhui on 2018/4/2.
 */
public final class StatisticsOpen {


    public final static String TAG = "StatisticsOpen";


    public static void init(Context context){
        StatisticsImp.getInstance().init(context);
    }

    public static IStatistics getStatistics(){
        return StatisticsImp.getInstance();
    }

    public static ICellProcess executeMain(StatisticsModel statisticsModel){
        ICell iCell = StatisticsImp.getInstance().createDefaultParameter(statisticsModel);
        StatisticsImp.getInstance().insertOne(iCell);
        ICellProcess iCellProcess = iCell.process();
        iCellProcess.load();
        return iCellProcess;
    }

}
