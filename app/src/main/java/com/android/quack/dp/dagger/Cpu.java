package com.android.quack.dp.dagger;

import javax.inject.Inject;

public class Cpu {
    String model;

    @Inject
    public Cpu() {
        this.model = "intel";
    }

    public String getModel() {
        return this.model;
    }
}
