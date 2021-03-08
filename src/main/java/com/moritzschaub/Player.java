package com.moritzschaub;

import java.util.ArrayList;

public class Player {
    private String name;
    private ArrayList<Stone> hand;
    private int id;

    public Player(String name, ArrayList<Stone> hand) {
        this.name = name;
        this.hand = hand;
        this.id = 1;
    }

    public int getId() {
        return this.id;
    }

    public ArrayList<Stone> getHand() {
        return this.hand;
    }

    public String getName() {
        return this.name;
    }
}
