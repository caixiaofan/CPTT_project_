import android.util.Log;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by 38094 on 2017/5/17.
 */
@Aspect
public class Aspectj {
    private static final String TAG = "AspectJ";
    static Map<String,ArrayList> a = new HashMap<>();

    @Before("execution(* me.wcy.music.**.**.**(..))")
    public void onActivityMethodBefore(JoinPoint joinPoint) throws Throwable {
        String key = joinPoint.getSignature().toString();
        ArrayList tmp;
        if(a.containsKey(key))
        {
            tmp = a.get(key);
        }
        else
        {
            tmp = new ArrayList();
            a.put(key,tmp);
        }
        tmp.add(String.valueOf(System.nanoTime()));
    }

    @After("execution(* me.wcy.music.**.**.**(..))")
    public void onActivityMethodAfter(JoinPoint joinPoint) throws Throwable {
        String key = joinPoint.getSignature().toString();
        ArrayList tmp;
        tmp = a.get(key);
        Log.d(TAG,key + ' ' +String.valueOf(System.nanoTime()-Long.parseLong(tmp.get(tmp.size()-1).toString())));
        tmp.remove(tmp.get(tmp.size()-1));
    }
}