package com.android.ux.ux.dp;

import android.graphics.Color;
import android.os.Bundle;
import android.os.SystemClock;

import com.android.ux.ux.BaseActivity;

import java.util.Random;

public class ChainActivity extends BaseActivity {

    private boolean mIsDestroy = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        new Thread(new Runnable() {
            @Override
            public void run() {
                DiscountController controller = DiscountMain.createController(ChainActivity.this);
                Customer customer = new Customer(controller);
                Random random = new Random();

                while (!mIsDestroy) {
                    int discount = random.nextInt(10);
                    customer.askDiscount((float) discount / 10.0f);
                    SystemClock.sleep(1000);
                }
            }
        }).start();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mIsDestroy = true;
    }

    public abstract static class DiscountController {
        protected DiscountController controller;
        protected BaseActivity context;

        protected DiscountController(BaseActivity context, DiscountController controller) {
            this.context = context;
            this.controller = controller;
        }

        public abstract void discount(float discount);

        protected void addMessage(String message, int color) {
            context.addMessage(message, color);
        }
    }

    public final static class Salesman extends DiscountController {

        public Salesman(BaseActivity context, DiscountController controller) {
            super(context, controller);
        }

        @Override
        public void discount(float discount) {
            if (discount <= 0.1f) {
                addMessage("销售员授权给予打折" + discount, Color.BLACK);
            } else {
                this.controller.discount(discount);
            }
        }
    }

    public final static class Manager extends DiscountController {

        public Manager(BaseActivity context, DiscountController controller) {
            super(context, controller);
        }

        @Override
        public void discount(float discount) {
            if (discount <= 0.3f) {
                addMessage("经理授权给予打折" + discount, Color.BLUE);
            } else {
                this.controller.discount(discount);
            }
        }
    }

    public final static class Boss extends DiscountController {

        public Boss(BaseActivity context) {
            super(context, null);
        }

        @Override
        public void discount(float discount) {
            if (discount <= 0.5f) {
                addMessage("老板授权给予打折" + discount, Color.GREEN);
            } else {
                addMessage("老板决绝了折扣" + discount, Color.RED);
            }
        }
    }

    public final static class DiscountMain {
        public static DiscountController createController(BaseActivity context) {
            DiscountController boss = new Boss(context);
            DiscountController manager = new Manager(context, boss);
            DiscountController salesman = new Salesman(context, manager);
            return salesman;
        }
    }

    public final static class Customer {

        private DiscountController controller;

        public Customer(DiscountController controller) {
            this.controller = controller;
        }

        public void askDiscount(float discount) {
            controller.discount(discount);
        }
    }
}
