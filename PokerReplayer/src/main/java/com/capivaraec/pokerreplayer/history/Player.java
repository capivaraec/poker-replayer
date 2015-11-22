package com.capivaraec.pokerreplayer.history;

import java.io.Serializable;
import java.util.ArrayList;

public class Player implements Serializable, Cloneable {

	private String name;
	private int stack;
	private ArrayList<String> cards;
	private int position;

	public Player(String name, int stack, int position) {
		this.name = name;
		this.stack = stack;
		this.position = position;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getStack() {
		return stack;
	}

	public void setStack(int stack) {
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

	public void setPosition(int position) {
		this.position = position;
	}

	protected Object cloneObject() {
		try {
            super.clone();
        } catch (CloneNotSupportedException cnse) {
            cnse.printStackTrace();
        }
		return this;
	}
}
