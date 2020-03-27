package com.android.quack.dp.dagger;

import dagger.Component;

@Component(modules = ComputerFactory.class)
public interface ComputerShop {
    Computer getComputer();
}
