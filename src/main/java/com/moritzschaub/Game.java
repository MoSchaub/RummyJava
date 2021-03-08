package com.moritzschaub;

import java.util.ArrayList;
import java.util.Stack;
import java.util.Random;

public class Game {
    private ArrayList<Reihe> tisch;
    private ArrayList<Player> players;
    private Stack<Stone> beutel;
    private boolean gameRunning;
    private Random random;

    public Game(ArrayList<Player> players) {
        this.players = players;
        this.tisch = new ArrayList<Reihe>();
        this.beutel = new Stack<Stone>();
        this.gameRunning = false;
        this.random = new Random();
    }

    public Game() {
        this.players = new ArrayList<Player>();
        this.tisch = new ArrayList<Reihe>();
        this.beutel = new Stack<Stone>();
        this.gameRunning = false;
        this.random = new Random();
    }

    private void fillBeutel() {
        // den beutel mit allen möglichen Steinen füllen.
        ArrayList<Stone> allStones = Stone.allStones();
        while (!allStones.isEmpty()) {
            int index = random.nextInt(allStones.size());
            beutel.add(allStones.remove(index));
        }
    }

    public int addNewPlayer(String name) {
        if (!gameRunning) {
            gameRunning = true;
            fillBeutel();
        }
        ArrayList<Stone> hand = new ArrayList<Stone>();
        while (hand.size() < 15) {
            hand.add(beutel.pop());
        }
        Player newPlayer = new Player(name, hand);
        this.players.add(newPlayer);
        return newPlayer.getId();
    }

    public Stone zieheStein(Player player) {
        Stone stone = beutel.pop();
        player.getHand().add(stone);
        return stone;
    }

    public void legeStein(Stone stone, Player player, Reihe reihe) {
        if (tisch.contains(reihe)) {

            // remove the stone
            player.getHand().remove(stone);

            // add the stone
            int index = tisch.indexOf(reihe);
            tisch.get(index).getStones().add(stone);
        }
    }

    public void startGame() {
        this.gameRunning = true;
    }

    public void neueReiheMitSteinen(ArrayList<Stone> steine) {
        Reihe reihe = new Reihe();
        reihe.getStones().addAll(steine);
        this.tisch.add(reihe);
    }

    public Player getPlayer(int id) {
        for (int i = 0; i < players.size(); i++) {
            if (players.get(i).getId() == id) {
                return players.get(i);
            }
        }
        return null;
    }

}
