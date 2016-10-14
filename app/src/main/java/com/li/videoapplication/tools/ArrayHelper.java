package com.li.videoapplication.tools;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.li.videoapplication.data.model.entity.VideoImage;
import com.li.videoapplication.utils.StringUtil;

public class ArrayHelper {

    /**
     * 功能：String数组转化为集合
     */
    public static String list2Array(List<String> list) {

        StringBuffer sb = new StringBuffer();
        if (list != null && list.size() > 0) {
            for (int i = 0; i < list.size(); i++) {
                if (i < (list.size() - 1)) {
                    sb.append(list.get(i));
                    sb.append(",");
                } else {
                    sb.append(list.get(i));
                }
            }
        }
        return sb.toString().trim();
    }

    /**
     * 功能：集合转化为String数组
     */
    public static List<String> array2List(String arr) {

        List<String> list = new ArrayList<String>();
        if (!StringUtil.isNull(arr)) {
            String[] array = arr.split(",");
            list = Arrays.asList(array);
        }
        return list;
    }

    /**
     * 功能：将集合中的每size个子集合并
     */
    public static List<List<VideoImage>> createList(List<VideoImage> targe, int size) {
        List<List<VideoImage>> listArr = new ArrayList<>();
        // 获取被拆分的数组个数
        int arrSize = targe.size() % size == 0 ? targe.size() / size : targe.size() / size + 1;
        for (int i = 0; i < arrSize; i++) {
            List<VideoImage> sub = new ArrayList<>();
            // 把指定索引数据放入到list中
            for (int j = i * size; j <= size * (i + 1) - 1; j++) {
                if (j <= targe.size() - 1) {
                    sub.add(targe.get(j));
                }
            }
            listArr.add(sub);
        }
        return listArr;
    }

}
