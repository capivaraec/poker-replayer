package com.capivaraec.pokerreplayer.history;

import com.capivaraec.pokerreplayer.enums.Street;

import java.io.Serializable;
import java.util.ArrayList;

public class Player implements Serializable {

	private String name;
	private ArrayList<String> cards;
	private int position;
    private float stack;

	public Player(String name, float stack, int position) {
		this.name = name;
        this.stack = stack;
		this.position = position;
	}

	public Player(String name) {
		this.name = name;
	}

    private Player(String name, ArrayList<String> cards, int position, float stack) {
        this.name = name;
        this.cards = cards;
        this.position = position;
        this.stack = stack;
    }

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

    public float getStack() {
        return stack;
    }

    public void setStack(float stack) {
        this.stack = stack;
    }

	public ArrayList<String> getCards() {
		return cards;
	}

	public void setCards(ArrayList<String> cards) {
		this.cards = cards;
	}

	public void addCard(String card) {
		if (cards == null) {
			cards = new ArrayList<>();
		}

		cards.add(card);
	}

	public int getPosition() {
		return position;
	}

    public void decreaseStack(float value) {
        stack -= value;
    }

    Player clonePlayer() {
        return new Player(name, cards, position, stack);
    }
}
