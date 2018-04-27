package com.example.vuphu.app.Dialog;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.graphics.drawable.ColorDrawable;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.vuphu.app.R;

public class notyfi {

    final LinearLayout.LayoutParams lp = new
            LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
            LinearLayout.LayoutParams.WRAP_CONTENT);
    private final Dialog dialog;
    private Activity context;
    private Button dialogButton;
    private TextView text;
    private ImageView icon;

    @SuppressLint("ResourceAsColor")
    public notyfi(Activity context) {
        this.context = context;
        dialog = new Dialog(this.context, R.style.translucentdialog);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.R.color.transparent));
        dialog.setContentView(R.layout.dialog_notyfi);
        dialogButton = dialog.findViewById(R.id.btn_ok);
        icon = dialog.findViewById(R.id.icon_notify);
        text = dialog.findViewById(R.id.text_notify);
        dialogButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
    }

    public void setIcon(int icon) {
        this.icon.setImageResource(icon);
    }

    public void setText(String text) {
        this.text.setText(text);
    }

    public void show() {
        dialog.show();
    }
    public void dismiss () {dialog.dismiss();}

}