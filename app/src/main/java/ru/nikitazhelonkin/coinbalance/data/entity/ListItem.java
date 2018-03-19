package ru.nikitazhelonkin.coinbalance.data.entity;


public interface ListItem  {

    int getId();

    int getPosition();

    void setPosition(int position);

    long getCreatedAt();

}
