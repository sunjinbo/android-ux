package com.android.quack.dp;

import android.graphics.Color;
import android.os.Bundle;

import com.android.quack.BaseActivity;
import com.android.quack.dp.dagger.Computer;
import com.android.quack.dp.dagger.ComputerShop;
import com.android.quack.dp.dagger.DaggerComputerShop;

import javax.inject.Inject;

public class DaggerActivity extends BaseActivity {

    @Inject
    Computer computer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ComputerShop shop = DaggerComputerShop.create();
        computer = shop.getComputer();
        addMessage(computer.getConfigs(), Color.GREEN);
    }
}
