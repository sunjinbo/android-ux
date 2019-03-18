package com.android.ux.ux.plugins;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Bundle;

import com.android.ux.ux.BaseActivity;
import com.ladybug.libbase.IModuleName;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class ModuleActivity extends BaseActivity {

    private static ApplicationInfo appInfo;
    private static Map<String, List<Object>> serviceCenter = new HashMap<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        discover();
    }

    private void discover() {
        register(this, "module_name", IModuleName.class);
        List<IModuleName> moduleNames = getServices("module_name");
        addMessage("目前注册的模块有：", Color.BLUE);
        for(IModuleName moduleName : moduleNames){
            addMessage(moduleName.getModuleName(), Color.BLACK);
        }
    }

    private static void register(Context context, String metaDataValue, Class clz) {
        if (context == null || metaDataValue == null) {
            throw new IllegalArgumentException("Context or metaDataValue can not be null");
        }
        Bundle metaData = getMetaData(context);
        if (metaData == null) {
            return;
        }
        Set<String> keySet = metaData.keySet();
        List<Object> services = new ArrayList<>();
        for (String metaDataKey : keySet) {
            Object obj = metaData.get(metaDataKey);
            if (!(obj instanceof String)) {
                continue;
            }
            String registerValue = metaData.getString(metaDataKey);
            if(registerValue == null || !registerValue.equals(metaDataValue)){
                continue;
            }
            try {
                Class serviceCls = Class.forName(metaDataKey);
                Object service = serviceCls.newInstance();
                //noinspection unchecked
                if (!clz.isAssignableFrom(service.getClass())) {
                    throw new IllegalArgumentException(service.getClass().getName() + " you registered as \"" + metaDataValue + "\"" +
                            " is not an instance of " + clz.getName());

                }
                services.add(service);
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InstantiationException e) {
                e.printStackTrace();
                throw new IllegalArgumentException("The service should have a public and non-parameter constructor");
            }
        }
        if (services.size() != 0) {
            serviceCenter.put(metaDataValue, services);
        }
    }

    /**
     * 根据meta-data中的android:value的值获取服务实例
     * 注意： 此处获取的服务实例会强转成你需要的类型，请确保类型正确，否则会crash
     * @param metaDataValue AndroidManifest.xml中的meta-data中android:value的值
     * @param <E> 获取的服务类型
     * @return 服务实例列表，非空
     */
    private static <E> List<E> getServices(String metaDataValue) {
        List<Object> res = serviceCenter.get(metaDataValue);
        if (res == null) {
            res = new ArrayList<>();
        }
        //此处忽略警告，靠调用方保证，否则会crash
        //noinspection unchecked
        return (List<E>) res;

    }

    private static Bundle getMetaData(Context context) {
        if (appInfo == null) {
            try {
                appInfo = context.getPackageManager().getApplicationInfo(context.getPackageName(),
                        PackageManager.GET_META_DATA);
            } catch (PackageManager.NameNotFoundException e) {
                e.printStackTrace();
                return null;
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }
        return appInfo.metaData;
    }
}
