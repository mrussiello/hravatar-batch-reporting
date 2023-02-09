/*
 * This code was downloaded from http://weblogs.java.net/blog/jjviana/archive/2010/06/10/threadlocal-thread-pool-bad-idea-or-dealing-apparent-glassfish-memor
 * It is intended to allow you to keep re-deploying your Glassfish App without using up all your PermGen Space.
 *
 */
package com.tm2batch.util;

import com.tm2batch.service.LogService;
import java.io.Serializable;
import java.lang.reflect.Array;
import java.lang.reflect.Field;
import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import jakarta.servlet.annotation.WebListener;

@WebListener
public class ThreadLocalCleanup implements ServletContextListener, Serializable {

    @Override
    public void contextInitialized(ServletContextEvent sce) {
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {


        try
        {
            LogService.logIt("ThreadLocalCleanup.contextDestroyed() START thread locals cleanup..");
            cleanThreadLocals();
            LogService.logIt("ThreadLocalCleanup.contextDestroyed() END thread locals cleanup..");
        }
        catch (Throwable t)
        {
            LogService.logIt(t, "ThreadLocalCleanup.contextDestroyed() ");
        }
    }

    private void cleanThreadLocals() throws NoSuchFieldException, ClassNotFoundException, IllegalArgumentException, IllegalAccessException {

        Thread[] threadgroup = new Thread[256];
        Thread.enumerate(threadgroup);

        for (int i = 0; i < threadgroup.length; i++)
        {
            if (threadgroup[i] != null)
            {
                cleanThreadLocals(threadgroup[i]);
            }
        }
    }

    private void cleanThreadLocals(Thread thread) throws NoSuchFieldException, ClassNotFoundException, IllegalArgumentException, IllegalAccessException {

        Field threadLocalsField = Thread.class.getDeclaredField("threadLocals");
        threadLocalsField.setAccessible(true);

        Class threadLocalMapKlazz = Class.forName("java.lang.ThreadLocal$ThreadLocalMap");
        Field tableField = threadLocalMapKlazz.getDeclaredField("table");
        tableField.setAccessible(true);

        Object fieldLocal = threadLocalsField.get(thread);
        if (fieldLocal == null)
        {
            return;
        }
        Object table = tableField.get(fieldLocal);

        int threadLocalCount = Array.getLength(table);

        for (int i = 0; i < threadLocalCount; i++)
        {
            Object entry = Array.get(table, i);
            if (entry != null)
            {
                Field valueField = entry.getClass().getDeclaredField("value");
                valueField.setAccessible(true);
                Object value = valueField.get(entry);
                if (value != null)
                {
                    if (value.getClass().getName().equals("com.sun.enterprise.security.authorize.HandlerData"))
                    {
                        valueField.set(entry, null);
                    }
                }

            }
        }


    }
}
