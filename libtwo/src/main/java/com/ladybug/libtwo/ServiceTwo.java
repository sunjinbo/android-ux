package com.ladybug.libtwo;

import com.ladybug.libbase.IModuleService;

public class ServiceTwo implements IModuleService {
    @Override
    public String runService() {
        return "run ServiceTwo..";
    }
}
