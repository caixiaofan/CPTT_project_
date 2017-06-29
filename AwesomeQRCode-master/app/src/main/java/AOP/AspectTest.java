package AOP;

/**
 * Created by 38094 on 2017/6/18.
 */

import android.util.Log;


import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
@Aspect
public class AspectTest {

    //private static final String TAG = "AspectTest";

    //@Before("execution(* android.app.Activity.on**(..))")
    private static final String TAG = "AspectJ";
    static Map<String,ArrayList> a = new HashMap<>();
    static Map<String,ArrayList> b = new HashMap<>();
    static Map<String,ArrayList> b1 = new HashMap<>();
    private static final String InsertPoint = "execution(* com.github.sumimakito.**.**.**(..))";
    @Before(InsertPoint)
    public void onActivityMethodBefore(JoinPoint joinPoint) throws Throwable {
        String key = joinPoint.getSignature().toString();
        ArrayList tmp;
        if (a.containsKey(key)) {
            tmp = a.get(key);
        } else {
            tmp = new ArrayList();
            a.put(key, tmp);
        }
        tmp.add(String.valueOf(System.nanoTime()));

        ArrayList tmp1;

        int tid = android.os.Process.myTid();
        key = key + String.valueOf(tid);
        if(b.containsKey(key)) {
            tmp = b.get(key);
            tmp1 = b1.get(key);
        }
        else {
            tmp = new ArrayList();
            tmp1 = new ArrayList();
            b.put(key, tmp);
            b1.put(key,tmp1);
        }
        float totalCpuTime1 = getTotalCpuTime();
        float processCpuTime1 = getAppCpuTime();
        tmp.add(totalCpuTime1);
        tmp1.add(processCpuTime1);
    }
    @After(InsertPoint)
    public void onActivityMethodAfter(JoinPoint joinPoint) throws Throwable {
        String key = joinPoint.getSignature().toString();
        ArrayList tmp = a.get(key);
        String t = String.valueOf(System.nanoTime() -
                Long.parseLong(
                        tmp.get(tmp.size()-1).toString()
                ));

        tmp.remove(tmp.size()-1);


        ArrayList tmp1;
        int tid = android.os.Process.myTid();
        key = key + String.valueOf(tid);
        tmp = b.get(key);
        tmp1 = b1.get(key);
        float totalCpuTime2 = getTotalCpuTime();
        float processCpuTime2 = getAppCpuTime();
        //在这里使用P
        float totalCpuTime1 = Float.valueOf(tmp.get(tmp.size()-1).toString());
        float processCpuTime1 = Float.valueOf(tmp1.get(tmp1.size()-1).toString());

        float totalCpuTime = totalCpuTime2 - totalCpuTime1;
        float processCpuTime = processCpuTime2 - processCpuTime1;
        Log.d(TAG, key + ' ' + t + ' ' + String.valueOf(totalCpuTime) + ' ' + String.valueOf(processCpuTime));
        tmp.remove(tmp.size()-1);
        tmp1.remove(tmp1.size()-1);
    }


    public static long getTotalCpuTime()
    { // 获取系统总CPU使用时间
        String[] cpuInfos = null;
        try
        {
            BufferedReader reader = new BufferedReader(new InputStreamReader(
                    new FileInputStream("/proc/stat")), 1000);
            String load = reader.readLine();
            reader.close();
            cpuInfos = load.split(" ");
        }
        catch (IOException ex)
        {
            ex.printStackTrace();
        }
        long totalCpu = Long.parseLong(cpuInfos[2])
                + Long.parseLong(cpuInfos[3]) + Long.parseLong(cpuInfos[4])
                + Long.parseLong(cpuInfos[6]) + Long.parseLong(cpuInfos[5])
                + Long.parseLong(cpuInfos[7]) + Long.parseLong(cpuInfos[8]);
        return totalCpu;
    }

    public static long getAppCpuTime()
    { // 获取线程占用的CPU时间
        String[] cpuInfos = null;
        try
        {
            int pid = android.os.Process.myPid();
            int tid = android.os.Process.myTid();
            BufferedReader reader = new BufferedReader(new InputStreamReader(
                    new FileInputStream("/proc/" + pid + "/task/"+tid+"/stat")), 1000);
            String load = reader.readLine();
            reader.close();
            cpuInfos = load.split(" ");
        }
        catch (IOException ex)
        {
            ex.printStackTrace();
        }
        long appCpuTime = Long.parseLong(cpuInfos[13])
                + Long.parseLong(cpuInfos[14]);
        return appCpuTime;
    }
}