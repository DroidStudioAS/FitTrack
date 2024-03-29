package com.aa.fittracker.dialog;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Typeface;
import android.media.Image;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.Group;

import com.aa.fittracker.R;

import org.w3c.dom.Text;

public class InfoDialog extends Dialog {


    //parts of main menu
     TextView trainingInfoTrigger;
     TextView communityInfoTrigger;
     TextView journalInfoTrigger;

     Group journalSubMenu;
     //parts of journalSubMenuGroup
     TextView weightJournalInfoTrigger;
     TextView fitJournalInfoTrigger;

     //JORUNAL ELEMENTS
     ScrollView expContainer;
     TextView expTv;
     TextView iconExpTv;

     Group fitIconsExpGroup;
     Group weighIconsExpGroup;

    //Community Elements
    Group communityIconExpGroup;

    //training elements
    Group trainingExpGroup;



    public InfoDialog(@NonNull Context context) {
        super(context);
    }

    public void setOpenedFrom(String openedFrom) {

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_info);
        getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT,
                WindowManager.LayoutParams.MATCH_PARENT);
        /*******Ui********/
        //MainMenu
        trainingInfoTrigger=(TextView) findViewById(R.id.trainingInformationTrigger);
        communityInfoTrigger=(TextView)findViewById(R.id.communityInformationTrigger);
        journalInfoTrigger=(TextView)findViewById(R.id.JournalInformationTrigger);
        //journalSubMenu
        journalSubMenu=(Group) findViewById(R.id.jorunalSubMenu);
        weightJournalInfoTrigger=(TextView)findViewById(R.id.weightJournalInfoTrigger);
        fitJournalInfoTrigger=(TextView)findViewById(R.id.fitJournalInfoTrigger);

        //journal elements
        expContainer=(ScrollView)findViewById(R.id.expContainer);
        expTv=(TextView)findViewById(R.id.explanaitonTv);
        iconExpTv=(TextView)findViewById(R.id.iconExpTv);

        fitIconsExpGroup=(Group)findViewById(R.id.fitIconsExpGroup);
        weighIconsExpGroup=(Group)findViewById(R.id.weightIconExpGroup);

        //community elements
        communityIconExpGroup=(Group)findViewById(R.id.communityIconExpGroup);

        //training Elements
        trainingExpGroup=(Group)findViewById(R.id.trainingIconGroup);


        boldFocusedTitle(-1);


        /*******OnClickListeners********/
        //Main Menu On Click Listeners
        journalInfoTrigger.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //show the submenu
                journalSubMenu.setVisibility(View.VISIBLE);
                expContainer.setVisibility(View.GONE);
                iconExpTv.setVisibility(View.GONE);
                weighIconsExpGroup.setVisibility(View.GONE);
                communityIconExpGroup.setVisibility(View.GONE);
                trainingExpGroup.setVisibility(View.GONE);
                boldFocusedTitle(2);

            }
        });
        communityInfoTrigger.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                expContainer.setVisibility(View.VISIBLE);
                expTv.setText(R.string.communityExp);
                journalSubMenu.setVisibility(View.INVISIBLE);

                iconExpTv.setVisibility(View.VISIBLE);
                iconExpTv.setText(R.string.communityFurtherExp);
                fitIconsExpGroup.setVisibility(View.GONE);
                weighIconsExpGroup.setVisibility(View.GONE);
                trainingExpGroup.setVisibility(View.GONE);

                communityIconExpGroup.setVisibility(View.VISIBLE);

                boldFocusedTitle(3);
            }
        });
        trainingInfoTrigger.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                expContainer.setVisibility(View.VISIBLE);
                expTv.setText(R.string.trainingPageExp);
                trainingExpGroup.setVisibility(View.VISIBLE);
                journalSubMenu.setVisibility(View.INVISIBLE);

                iconExpTv.setVisibility(View.GONE);
                fitIconsExpGroup.setVisibility(View.GONE);
                weighIconsExpGroup.setVisibility(View.GONE);
                communityIconExpGroup.setVisibility(View.GONE);

                boldFocusedTitle(1);
            }
        });



        //Journal Sub Menu On Click Listeners
        fitJournalInfoTrigger.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*make everything visible
                And set The strings accordingly
                 */
                expContainer.setVisibility(View.VISIBLE);
                expTv.setText(R.string.fitJournalExp);
                iconExpTv.setVisibility(View.VISIBLE);
                iconExpTv.setText(R.string.icon_exp_trainings);
                fitIconsExpGroup.setVisibility(View.VISIBLE);
                weighIconsExpGroup.setVisibility(View.GONE);

            }
        });
        weightJournalInfoTrigger.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*make everything visible
                And set The strings accordingly
                 */
                expContainer.setVisibility(View.VISIBLE);
                expTv.setText(R.string.weightJournalExp);
                iconExpTv.setVisibility(View.VISIBLE);
                iconExpTv.setText(R.string.icon_exp_weight);
                //unneccesary for weight
                fitIconsExpGroup.setVisibility(View.GONE);
                weighIconsExpGroup.setVisibility(View.VISIBLE);
            }
        });

    }
    /********Helpers*********/
    /*
    -1 reset fontFace
    1 trainings
    2 journal
    3 community
    */
    public void boldFocusedTitle(int status){
        switch (status){
            case -1:
                trainingInfoTrigger.setTypeface(Typeface.DEFAULT);
                communityInfoTrigger.setTypeface(Typeface.DEFAULT);
                journalInfoTrigger.setTypeface(Typeface.DEFAULT);
                break;
            case 1:
                trainingInfoTrigger.setTypeface(Typeface.DEFAULT_BOLD);
                communityInfoTrigger.setTypeface(Typeface.DEFAULT);
                journalInfoTrigger.setTypeface(Typeface.DEFAULT);
                break;
            case 2:
                trainingInfoTrigger.setTypeface(Typeface.DEFAULT);
                journalInfoTrigger.setTypeface(Typeface.DEFAULT_BOLD);
                communityInfoTrigger.setTypeface(Typeface.DEFAULT);
                break;
            case 3:
                trainingInfoTrigger.setTypeface(Typeface.DEFAULT);
                journalInfoTrigger.setTypeface(Typeface.DEFAULT);
                communityInfoTrigger.setTypeface(Typeface.DEFAULT_BOLD);
                break;
        }
    }
}


