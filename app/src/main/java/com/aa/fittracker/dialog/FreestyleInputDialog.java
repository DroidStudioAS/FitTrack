package com.aa.fittracker.dialog;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import androidx.annotation.NonNull;

import com.aa.fittracker.R;



public class FreestyleInputDialog extends Dialog {

    ImageView closeFid;
    public FreestyleInputDialog(@NonNull Context context) {
        super(context);
        setContentView(R.layout.freestyle_input_layout);

        /*********Ui Initializations**********/
        closeFid=(ImageView) findViewById(R.id.closeFid);

        /*********End Of Ui Initializations**********/


        /*********OnClickListeners**********/
        closeFid.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });


    }
}
