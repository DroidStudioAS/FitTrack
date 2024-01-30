package com.aa.fittracker.dialog;

import android.app.Dialog;
import android.content.Context;
import android.media.Image;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.annotation.NonNull;

import com.aa.fittracker.R;



public class FreestyleInputDialog extends Dialog {

    ImageView closeFid;
    ImageView fiConfirmTrigger;

    Button fiEazyBut;
    Button fiMediumBut;
    Button fiHardBut;

    EditText fiTrainingDescriptionEt;

    public FreestyleInputDialog(@NonNull Context context) {
        super(context);
        setContentView(R.layout.freestyle_input_layout);

        /*********Ui Initializations**********/
        closeFid=(ImageView) findViewById(R.id.closeFid);
        fiConfirmTrigger=(ImageView)findViewById(R.id.fInputTrigger);

        fiEazyBut=(Button)findViewById(R.id.fiEazyBut);
        fiMediumBut=(Button)findViewById(R.id.fiMidBut);
        fiHardBut=(Button)findViewById(R.id.fiHardBut);

        fiTrainingDescriptionEt=(EditText)findViewById(R.id.fiTrainingDescEt);





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
