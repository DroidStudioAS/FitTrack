package com.aa.fittracker.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.aa.fittracker.R;

import org.w3c.dom.Text;

public class InfoDialog extends Dialog {
    String openedFrom = "";

    TextView infoTitle;
    TextView introTv;
    TextView legendTv;

    public InfoDialog(@NonNull Context context) {
        super(context);
    }

    public void setOpenedFrom(String openedFrom) {
        this.openedFrom = openedFrom;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_info);

        introTv=(TextView) findViewById(R.id.introTv);
        legendTv=(TextView) findViewById(R.id.legendTv);
        setDialog();
}
public void setDialog(){
        legendTv.setText("If Ever In Doubt About How To Use The App, Click This Button: ");
        switch (openedFrom){
            case "index":
                introTv.setText("Explaining Index Activity: ");
        }
    }
}
