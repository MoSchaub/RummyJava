package com.moritzschaub;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Stack;
import java.util.Random;

public class Game {
  private Stone[][] tisch;
  private Stone[][] localTisch;
  private ArrayList<Player> players;
  private Integer currentPlayerId;
  private Stack<Stone> beutel;
  private boolean gameRunning;
  private Random random;
  private LinkedList<Stone> removedStones;

  public Game(ArrayList<Player> players) {
    this.players = players;
    this.tisch = new Stone[10][20];
    this.localTisch = new Stone[10][20];
    this.beutel = new Stack<Stone>();
    this.gameRunning = false;
    this.random = new Random();
    this.removedStones = new LinkedList<>();
  }

  public Game() {
    this.players = new ArrayList<Player>();
    this.tisch = new Stone[10][20];
    this.localTisch = new Stone[10][20];
    this.beutel = new Stack<Stone>();
    this.gameRunning = false;
    this.random = new Random();
    this.removedStones = new LinkedList<>();
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

    ArrayList<Stone> hand = new ArrayList<Stone>();
    Player newPlayer = new Player(name, hand);
    this.players.add(newPlayer);
    if (!gameRunning) {
      startGame();
    }
    while (hand.size() < 15) {
      hand.add(beutel.pop());
    }
    return newPlayer.getId();
  }

  private void startGame() {
    gameRunning = true;
    fillBeutel();
    currentPlayerId = players.get(0).getId();
  }

  public Stone zieheStein(Player player) {
    if (player.getId() == currentPlayerId) {
      Stone stone = beutel.pop();
      player.getHand().add(stone);
      moveToNextPlayer();
      localTisch = tisch;
      return stone;
    } else return null;
  }

  public void moveToNextPlayer() {
    int index = players.indexOf(getPlayer(currentPlayerId));
    if (players.size() > index + 1) {
      currentPlayerId = players.get(index + 1).getId();
    } else currentPlayerId = players.get(0).getId();
  }

  public void legeStein(Stone stone, Player player, int row, int column) {
    if (localTisch[row][column] == null) {

      // remove the stone
      player.getHand().remove(stone);
      removedStones.add(stone);

      //check if player has stones left
      if (player.getHand().isEmpty()) {
        //TODO: Notify the view to finish the game and show a winner screen to the player
      }

      // set the stone
      localTisch[row][column] = stone;
    }
  }

  //returns wether the zug was succesfull
  public boolean beendeZug(Player player) {
    //check if player ist am Zug
    if (currentPlayerId == player.getId()) {

      if (isLocalTischValid()) {
        System.out.println("valid");
        tisch = localTisch;
        removedStones = new LinkedList<>();
        moveToNextPlayer();
        return true;
      } else {
        System.out.println("not valid");
        localTisch = tisch;

        while (!removedStones.isEmpty()) {
          player.getHand().add(removedStones.removeFirst());
        }

        zieheStein(player);
        return false;
      }
    } else return false;
  }

  public boolean isLocalTischValid() {
    for(int i=0; i<localTisch.length; i++) {
      for(int j=0; j<localTisch[i].length; j++) {
        if (localTisch[i][j] != null) { // check if there is an stone
          switch (j) {
            case 0:
              //checke die beiden nächsten Felder
              if (!checkNextTwo(i,j)) {
                //not valid
                return false;
              }
            case 19:
              //checke die vorherigen beiden Felder
              if (!checkPrevTwo(i,j)) {
                return false;
              }

            default:
              //checke die umliegenden, die vorherigen, oder die folgenden beiden Felder
              if (!(checkPrevTwo(i,j)|| checkNextTwo(i,j) || checkAroundOne(i,j))) {
                return false;
              }

          }
        }
      }
    }
    return true;
  }

  private boolean checkNextTwo(int i, int j) {
    return localTisch[i][j+1] != null && localTisch[i][j+2] != null;
  }

  private boolean checkPrevTwo(int i, int j) {
    return localTisch[i][j-1] != null && localTisch[i][j-2] != null;
  }

  private boolean checkAroundOne(int i, int j) {
    return localTisch[i][j-1] != null && localTisch[i][j+1] != null;
  }


  public Player getPlayer(int id) {
    for (int i = 0; i < players.size(); i++) {
      if (players.get(i).getId() == id) {
        return players.get(i);
      }
    }
    return null;
  }

  public Integer getCurrentPlayerId() {
    return currentPlayerId;
  }

  public Stone[][] getTisch() {
    return this.localTisch;
  }

}
