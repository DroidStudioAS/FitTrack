package com.aa.fittracker.dialog;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Typeface;
import android.media.Image;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.Group;

import com.aa.fittracker.R;

import org.w3c.dom.Text;

public class InfoDialog extends Dialog {

    TextView journalBut;
    TextView comButton;
    TextView trButton;
    TextView fitInfoButton;

    Group journalSubMenu;
    Group fitJournal;



    public InfoDialog(@NonNull Context context) {
        super(context);
    }

    public void setOpenedFrom(String openedFrom) {

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_info);

        journalBut=(TextView) findViewById(R.id.journalInfo);
        fitInfoButton = (TextView)findViewById(R.id.fitInfo);

        journalSubMenu=(Group) findViewById(R.id.journalSubMenu);

        fitJournal = (Group) findViewById(R.id.fitJournal);

        journalBut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                journalSubMenu.setVisibility(View.VISIBLE);

            }
        });
        fitInfoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fitJournal.setVisibility(View.VISIBLE);
            }
        });









    }

    public void setDialog() {

    }

    public void setExp() {

    }

    public void focusOnTitle(){


    }
}

