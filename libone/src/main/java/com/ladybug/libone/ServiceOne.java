package com.ladybug.libone;

import com.ladybug.libbase.IModuleService;

public class ServiceOne implements IModuleService {
    @Override
    public String runService() {
        return "run ServiceOne..";
    }
}
