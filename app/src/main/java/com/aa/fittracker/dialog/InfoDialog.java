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





        /*******OnClickListeners********/
        //Main Menu On Click Listeners
        journalInfoTrigger.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //show the submenu
                journalSubMenu.setVisibility(View.VISIBLE);
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


