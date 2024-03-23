package com.example.greenrover;


import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import java.util.Objects;

public class msgPopup {

    public void showToast(String msg, Context context) {
        final Dialog d = new Dialog(context);
        d.requestWindowFeature(Window.FEATURE_NO_TITLE);
        Objects.requireNonNull(d.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        d.setContentView(R.layout.msg_box);
        WindowManager.LayoutParams par = new WindowManager.LayoutParams();
        par.copyFrom(d.getWindow().getAttributes());
        par.width = WindowManager.LayoutParams.MATCH_PARENT;
        par.height = WindowManager.LayoutParams.WRAP_CONTENT;
        d.getWindow().setAttributes(par);
        d.setCancelable(false);
        d.setCanceledOnTouchOutside(false);


        TextView Title = (TextView) d.findViewById(R.id.msg_box_msg);
        Title.setText(msg);
        Button btn = (Button) d.findViewById(R.id.msg_box_btn);


        btn.setOnClickListener(v -> {
            d.dismiss();
        });

        d.show();

    }


}
