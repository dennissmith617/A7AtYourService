package com.example.a7atyourservice;

import android.content.Context;
import android.widget.Toast;

public class Helpers {

    public static void showToast(Context context, String message) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
    }
}
