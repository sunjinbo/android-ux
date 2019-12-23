package com.android.quack.dp.dagger;

import dagger.Module;
import dagger.Provides;

@Module
public class ComputerFactory {

    @Provides
    public static Cpu provideCpu() {
        return new Cpu();
    }

    @Provides
    public static Computer provideComputer() {
        return new Computer(provideCpu());
    }
}
