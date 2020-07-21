package com.example.viewinjectdemo;

import android.app.Activity;

import java.lang.reflect.Constructor;

public class Binding {

    public static void bind(Activity activity){
        try {
            Class bindingClass = Class.forName(activity.getClass().getCanonicalName() + "$Binding");
            Class activityClass = Class.forName(activity.getClass().getCanonicalName());

            Constructor constructor = bindingClass.getDeclaredConstructor(activityClass);
            constructor.newInstance(activity);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
