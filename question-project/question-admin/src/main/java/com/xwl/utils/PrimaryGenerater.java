package com.xwl.utils;

import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import com.baomidou.mybatisplus.extension.service.IService;
import com.xwl.entity.Project;
import com.xwl.service.IAnswerService;
import com.xwl.service.IProjectService;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Date;
import java.util.List;

@Component
public class PrimaryGenerater {

    @Resource
    private IProjectService projectService;


    private static String SERIAL_NUMBER = "0001" ;
    private static PrimaryGenerater primaryGenerater = null;

    @Deprecated
    private PrimaryGenerater(){}

    /**
     * 取得PrimaryGenerater的单例实现
     *
     * @return
     */
    @Deprecated
    public static PrimaryGenerater getInstance() {
        if (primaryGenerater == null) {
            synchronized (PrimaryGenerater.class) {
                if (primaryGenerater == null) {
                    primaryGenerater = new PrimaryGenerater();
                }
            }
        }
        return primaryGenerater;
    }



    /**
     * 生成 日期+随机数的流水号
     * */
    @Deprecated
    public String getNumberForPK(){
        String id="";
        SimpleDateFormat sf = new SimpleDateFormat("yyyyMMdd");
        String temp = sf.format(new Date());
        int random=(int) (Math.random()*10000);
        id=temp+random;
        return id;
    }



    /**
     * HHTG+年月+8+0001
     * 每月从0001开始计数
     * */
    @Deprecated
    public static synchronized String getnumber(String thisCode){

        String id = null;
        Date date = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat("yyMM");
        String thisData = thisCode.substring(4, 8);
        //这个判断就是判断你数据取出来的最后一个业务单号是不是当月的
        if(!formatter.format(date).equals(thisData)){
            System.out.println("新的一月");
            thisData = formatter.format(date);
            //如果是新的一月的就直接变成0001
            id = "HHTG" + thisData + "80001";
        }else{
            System.out.println("当月");
            DecimalFormat df = new DecimalFormat("0000");

            //不是新的一月就累加
            id ="HHTG"+ formatter.format(date)+"8"
                    + df.format(1 + Integer.parseInt(thisCode.substring(9, 13)));
        }
        return id;
    }



//    public static void main(String[] args){
//
//        String currentDate = "20230818";
//        String maxProjectCode = "2022071891";                      //maxProjectCode:2023081808
//
//        String maxYrMonStr = maxProjectCode.substring(0,6);          //maxDateStr:202308
//        String currentYrMonStr =  currentDate.substring(0,6);        //currentYrMonStr:202308
//        String currentDateStr =  currentDate.substring(0,8);         //currentDateStr:20230818
//        String returnProjNoStr = "";
//        if( currentYrMonStr.equals(maxYrMonStr) ){                  //当前月已有合同: 20230818-66
//            Integer returnProjNoInt = Integer.valueOf(maxProjectCode.substring(8,10))+1;
//            returnProjNoStr = String.format("%02d",returnProjNoInt);    //67
//        } else {                                                    //当前月还没有合同: 20230818-01
//            returnProjNoStr = "01";                                                                 //01
//        }
//        String returnProjectCode = currentDateStr+returnProjNoStr;  //20230818 + 67 = 20230818-67
//
//        System.out.println("===returnProjectCode==="+returnProjectCode); //66
//
//    }

    public static void main(String[] args){



    }


}
