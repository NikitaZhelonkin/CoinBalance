package ru.nikitazhelonkin.coinbalance.data.entity;


import java.util.Comparator;

public class ListItemComparator implements Comparator<ListItem> {

    @Override
    public int compare(ListItem t1, ListItem t2) {
        if (t2.getPosition() != -1 && t1.getPosition() != -1) {
            return t1.getPosition() - t2.getPosition();
        } else if (t2.getPosition() == -1 || t1.getPosition() == -1) {
            return t1.getPosition() == -1 ? 1 : -1;
        } else {
            return t1.getCreatedAt() > t2.getCreatedAt() ? 1 : t1.getCreatedAt() == t2.getCreatedAt() ? 0 : -1;
        }
    }
}
