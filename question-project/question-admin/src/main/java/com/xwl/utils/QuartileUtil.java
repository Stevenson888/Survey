package com.xwl.utils;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.db.Db;
import cn.hutool.db.Entity;
import cn.hutool.db.ds.simple.SimpleDataSource;
import com.baomidou.mybatisplus.generator.FastAutoGenerator;
import com.baomidou.mybatisplus.generator.config.OutputFile;
import org.apache.commons.math3.stat.descriptive.rank.Percentile;

import javax.sql.DataSource;
import java.sql.SQLException;
import java.util.*;

/**
 * mp代码生成器
 */
public class QuartileUtil {
    private static final String PASSWORD = "1234";

    public static void main(String[] args) {
        double[] data = {1, 2, 3, 4, 5, 6, 7, 8, 9, 10};

        double q1 = calculateQuartile(data, 20);
        double q2 = calculateQuartile(data, 40);
        double q3 = calculateQuartile(data, 60);
        double q4 = calculateQuartile(data, 80);

        System.out.println("Q1: " + q1);
        System.out.println("Q2: " + q2);
        System.out.println("Q3: " + q3);
        System.out.println("Q4: " + q4);
    }

    public static double calculateQuartile(double[] data, int percentile) {
        Percentile percentileCalculator = new Percentile();
        percentileCalculator.setData(data);
        return percentileCalculator.evaluate(percentile);
    }


/**

 * 将一个list均分成n个list,主要通过偏移量来实现的

 * @param

 * @return

 */

//    public static <List> averageAssign(List source,int n){
//
//        List<> result=new ArrayList>();
//
//        int remaider=source.size()%n; //(先计算出余数)
//
//        int number=source.size()/n; //然后是商
//
//        int offset=0;//偏移量
//
//        for(int i=0;i
//
//        List value=null;
//
//        if(remaider>0){
//
//            value=source.subList(i*number+offset, (i+1)*number+offset+1);
//
//            remaider--;
//
//            offset++;
//
//        }else{
//
//            value=source.subList(i*number+offset, (i+1)*number+offset);
//
//        }
//
//        result.add(value);
//
//    }
//
//return result;
//
//}


//@SuppressWarnings("unused")
//
//public static void main(String[] args) {

//        List integers=new ArrayList<>();
//
//        integers.add(1);
//
//        integers.add(2);
//
//        integers.add(3);
//
//        integers.add(4);
//
//        integers.add(5);
//
//        List<> lists=averageAssign(integers, 2);
//
//        System.out.println(lists);

//        }



}