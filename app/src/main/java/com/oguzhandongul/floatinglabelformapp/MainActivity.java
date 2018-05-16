package com.oguzhandongul.floatinglabelformapp;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;

import com.oguzhandongul.floation.Floation;
import com.oguzhandongul.floation.FormValidator;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    Floation flfName, flfOption, flfMail;
    Button buttonValidate;

    FormValidator formValidator;

    ArrayList<ListItem> items = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//        flfName = findViewById(R.id.flfName);
//        flfMail = findViewById(R.id.flfMail);
//        flfOption = findViewById(R.id.flfOption);
//        buttonValidate = findViewById(R.id.buttonValidate);
//
//        items.add(new ListItem("1", "OPT 1"));
//        items.add(new ListItem("2", "OPT 2"));
//        items.add(new ListItem("3", "OPT 3"));
//        items.add(new ListItem("4", "OPT 4"));
//        items.add(new ListItem("5", "OPT 5"));
//
//        for (int i = 0; i < items.size(); i++) {
//            flfOption.addOption(items.get(i));
//        }
//        flfOption.setFirstOptionAsHint(true);
//
//
//        formValidator = new FormValidator(this, ValidationStyle.UNDER_LABEL, flfName, flfMail, flfOption);
//
//        buttonValidate.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                formValidator.validate();
//
//            }
//        });

    }

}
