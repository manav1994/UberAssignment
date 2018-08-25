package com.manav.uberassignment.Interfaces;

import com.manav.uberassignment.model.AddToAdapter;

import java.util.ArrayList;

public interface MainInterface {
    interface view{
        void showPrgress();
        void hideProgress();
        void initializeAdapter(ArrayList<AddToAdapter> list);
        void notifydataChanges(ArrayList<AddToAdapter> list);
        void ToastMessage(String message);
    }

    interface Actions{
        void callNextPages(int pageno,String title);

    }
}
