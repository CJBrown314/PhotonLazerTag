package Entities;

import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;

public class EventList {
    private int listSize;
    private final ObservableList<PlayerTaggedEvent> list = FXCollections.synchronizedObservableList(FXCollections.observableArrayList());

    public EventList (int listSize) {
        this.listSize = listSize;
    }

    public void add (PlayerTaggedEvent toAdd) {
        if(list.size() > listSize) {
            list.remove(0);
        }

        list.add(toAdd);
    }

    public void add(int index, PlayerTaggedEvent toAdd) {
        //if index is greater than the list size, just append to the end
        if(index > listSize) {
            add(toAdd);
        } else if (list.size() > listSize) {
            list.remove(0);
        }

        list.add(index, toAdd);
    }

    public void updateListSize(int listSize) {
        this.listSize = listSize;
    }

    public void addListener(ListChangeListener<PlayerTaggedEvent> listener) {
        list.addListener(listener);
    }

    public ObservableList<PlayerTaggedEvent> getList() {
        return list;
    }
}
