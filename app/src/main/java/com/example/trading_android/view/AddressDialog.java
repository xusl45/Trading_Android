package com.example.trading_android.view;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;

import com.example.trading_android.R;

public class AddressDialog extends Dialog {

    public AddressDialog(Context context) {
        super(context);
        this.show();
    }

    public AddressDialog(Context context, int theme) {
        super(context, theme);
        this.show();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_commodity_buy_dialog);
    }
}
