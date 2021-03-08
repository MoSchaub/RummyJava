package com.moritzschaub;

import java.util.ArrayList;

public class Stone {
	public enum Color {
		RED, BLACK, GREEN, BLUE, WHITE
	}

	public static final int MAX_VALUE = 13;
	public static final int MIN_VALUE = 1;

	private int number;
	private Color color;

	public Stone(Color color, int number) {
		this.color = color;
		this.number = number;
	}

	public Color getColor() {
		return this.color;
	}

	public int getNumber() {
		return this.number;
	}

	@Override
	public String toString() {
		return "(Color: " + color + ", " + "Number: " + " " + number + ")";
	}

	// arraylist with complete deck of stones
	public static ArrayList<Stone> allStones() {

		// create normal stones
		ArrayList<Stone> stones = new ArrayList<Stone>();
		for (int x = 0; x < 2; x++) { // repeat it twice to get each stone double
			for (int i = 1; i <= MAX_VALUE; i++) { // for each 13 numbers
				for (int j = 0; j < Color.values().length; j++) { // each color
					Stone stone = new Stone(Color.values()[j], i);
					stones.add(stone);
				}
			}

			// add joker in red or black
			Stone joker = new Stone(Color.values()[x], 666);
			stones.add(joker);
		}

		return stones;
	}

	public String getFilePath() {
		String filePath = "/Spielesteine" + "/" + this.color.toString() + "/" + Integer.toString(this.number) + ".png";
		return filePath;
	}
}
