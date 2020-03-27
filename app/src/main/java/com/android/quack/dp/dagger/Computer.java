package com.android.quack.dp.dagger;

import javax.inject.Inject;

public class Computer implements CpuInject {

    // 依赖注入框架注入
    @Inject
    Cpu cpu;

    // 构造函数注入
    @Inject
    public Computer(Cpu cpu) {
        this.cpu = cpu;
    }

    // setter方法注入
    public void setCpu(Cpu cpu){
        this.cpu = cpu;
    }

    // 接口注入
    @Override
    public void injectMenu(Cpu cpu) {
        this.cpu = cpu;
    }

    public String getConfigs() {
        return this.cpu.getModel();
    }
}
