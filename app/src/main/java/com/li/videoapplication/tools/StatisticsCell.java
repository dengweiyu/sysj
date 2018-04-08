package com.li.videoapplication.tools;

import android.util.Log;

import com.li.videoapplication.data.model.entity.StatisticsModel;
import com.li.videoapplication.interfaces.ICell;
import com.li.videoapplication.interfaces.ICellProcess;

import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Created by linhui on 2018/3/31.
 */
public class StatisticsCell implements ICell,ICellProcess {

    private Timer timer;
    private StatisticsModel statisticsModel;
    private long sum = 0;
    private AtomicBoolean isStop = new AtomicBoolean(false);

    private StatisticsCell(StatisticsModel statisticsModel) {
        this.statisticsModel = statisticsModel;
    }

    public static ICell createDefaultParameter(StatisticsModel statisticsModel) {
        return new StatisticsCell(statisticsModel);
    }


    // 停止定时器
    private void stopTimer() {
        if (timer != null) {
            timer.cancel();
            // 一定设置为null，否则定时器不会被回收
            timer = null;
        }
    }

    @Override
    public long getSumCount() {
        return sum;
    }

    @Override
    public int getDefinitionValues() {
        return DEFINITION_VALUES;
    }

    @Override
    public void load() {
        Log.i(StatisticsOpen.TAG, "load: ");
        isStop.set(false);
        if (timer == null) {
            timer = new Timer();
            timer.schedule(new TimerTask() {

                @Override
                public void run() {
                    execute();
                }
            }, 0, getDefinitionValues() * 1000);
//            timer.
        } else {
            synchronized (this) {
                notify();
            }

        }

    }

    private void execute() {
        synchronized (StatisticsCell.this) {
            if (isStop.get()) {
                try {
                    StatisticsCell.this.wait();
                } catch (InterruptedException e) {
                    cancel();
                    return;
                }
            }
            if (sum == 10) {
                cancel();
            }
            sum = sum + 2;
            StatisticsOpen.getStatistics().getHandlerPost().handlerSum(this);
        }
    }

    @Override
    public void stop() {
        Log.i(StatisticsOpen.TAG, "stop: ");
        isStop.set(true);
    }

    @Override
    public void cancel() {
        Log.i(StatisticsOpen.TAG, "cancel: ");
        stopTimer();
    }

    @Override
    public boolean isStop() {
        return isStop.get();
    }

    @Override
    public StatisticsModel getData() {
        return statisticsModel;
    }

    @Override
    public ICellProcess process() {
        return this;
    }
}
