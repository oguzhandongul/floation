package com.oguzhandongul.floatinglabelformapp;

import com.oguzhandongul.floation.Floation;

/**
 * Created by oguzhandongul on 28.02.2018.
 */

public class ListItem implements Floation.Listable {
    private String id;
    private String name;

    public ListItem(String id, String name) {
        this.id = id;
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    @Override
    public String getLabel() {
        return name;
    }
}
