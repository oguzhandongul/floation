package com.oguzhandongul.floatinglabelformapp;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.oguzhandongul.floation.Floation;
import com.oguzhandongul.floation.FormValidator;
import com.oguzhandongul.floation.utils.validator.ValidationStyle;

import java.util.ArrayList;

public class PlusOneFragment extends Fragment {

    public PlusOneFragment() {
        // Required empty public constructor
    }

    Floation flfName, flfMail;
    Button buttonValidate;

    FormValidator formValidator;

    ArrayList<ListItem> items = new ArrayList<>();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_plus_one, container, false);

        buttonValidate = view.findViewById(R.id.buttonValidate);
        flfName = view.findViewById(R.id.flfName);
        flfMail = view.findViewById(R.id.flfMail);

//        flfMail = view.findViewById(R.id.flfMail);
//        flfOption = view.findViewById(R.id.flfOption);
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


        formValidator = new FormValidator(getActivity(), ValidationStyle.TOAST_MESSAGE, flfName, flfMail);

        buttonValidate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                formValidator.validate();

            }
        });

        return view;
    }


}
