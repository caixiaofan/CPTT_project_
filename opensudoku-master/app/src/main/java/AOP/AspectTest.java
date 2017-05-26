package AOP;

import android.util.Log;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by hupeng on 2017/5/18.
 */

@Aspect

public class AspectTest {

    //private static final String TAG = "AspectTest";

    //@Before("execution(* android.app.Activity.on**(..))")

    private static final String TAG = "AspectJ";
    static Map<String,ArrayList> a = new HashMap<>();
    private static final String InsertPoint = "execution(* android.app.Activity.**(..))";

    @Before(InsertPoint)
    public void onActivityMethodBefore(JoinPoint joinPoint) throws Throwable {
        String key = joinPoint.getSignature().toString();
        ArrayList tmp;
        if (a.containsKey(key)) {
            tmp = a.get(key);
        } else {
            tmp = new ArrayList();
            a.put(key,tmp);
        }
        tmp.add(String.valueOf(System.nanoTime()));
    }

    @After(InsertPoint)
    public void onActivityMethodAfter(JoinPoint joinPoint) throws Throwable {
        String key = joinPoint.getSignature().toString();
        ArrayList tmp = a.get(key);
        String t = String.valueOf(System.nanoTime() -
                Long.parseLong(
                        tmp.get(tmp.size()-1).toString()
                ));
        Log.d(TAG, key + ' ' + t);
        tmp.remove(tmp.size()-1);
    }

}