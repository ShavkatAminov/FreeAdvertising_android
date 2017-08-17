package ru.startandroid.freeadvertising.model;

import android.view.View;
import android.widget.LinearLayout;

import java.util.ArrayList;



public class ReklamType {
    private View view;
    private LinearLayout linearLayout;
    private String reklamtypeName;
    private long id;
    private ArrayList<ReklamType> children;
    private boolean firstclick;
    public ReklamType(String reklamtypeName, long id)
    {
        this.reklamtypeName = reklamtypeName;
        this.id = id;
        this.children = new ArrayList<>();
        firstclick = false;
    }
    public boolean haschildren() {
        return children.size() != 0;
    }
    public void click() {
        this.firstclick = !firstclick;
    }
    public ArrayList<ReklamType> getChildren() {
        return children;
    }

    public boolean isFirstclick() {
        return firstclick;
    }

    public LinearLayout getLinearLayout() {
        return linearLayout;
    }

    public String getReklamtypeName() {
        return reklamtypeName;
    }

    public long getId() {
        return id;
    }

    public View getView() {
        return view;
    }

    public void setChildren(ArrayList<ReklamType> children) {
        this.children = children;
    }

    public void setFirstclick(boolean firstclick) {
        this.firstclick = firstclick;
    }

    public void setId(long id) {
        this.id = id;
    }

    public void setLinearLayout(LinearLayout linearLayout) {
        this.linearLayout = linearLayout;
    }

    public void setReklamtypeName(String reklamtypeName) {
        this.reklamtypeName = reklamtypeName;
    }

    public void setView(View view) {
        this.view = view;
    }
}
