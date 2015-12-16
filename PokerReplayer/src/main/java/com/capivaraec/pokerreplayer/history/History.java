package com.capivaraec.pokerreplayer.history;

import com.capivaraec.pokerreplayer.enums.Session;

import java.io.Serializable;
import java.util.ArrayList;

public class History implements Serializable {

	private String buyIn;
	private String room;
	private Session session;
	private ArrayList<Hand> hands;
	private long tourney;
	private String currency;

	public String getBuyIn() {
		return buyIn;
	}

	public void setBuyIn(String buyIn) {
		this.buyIn = buyIn;
	}

	public String getRoom() {
		return room;
	}

	public void setRoom(String room) {
		this.room = room;
	}

	public Session getSession() {
		return session;
	}

	public void setSession(Session session) {
		this.session = session;
	}

	public ArrayList<Hand> getHands() {
		return hands;
	}

	public void setHands(ArrayList<Hand> hands) {
		this.hands = hands;
	}

	public long getTourney() {
		return tourney;
	}

	public void setTourney(long tourney) {
		this.tourney = tourney;
	}

	public void addHand(Hand hand) {
		if (hands == null) {
			hands = new ArrayList<>();
		}
		hands.add(hand);
	}

	public Hand getHand(int index) {
		return hands.get(index);
	}

	public String getCurrency() {
		return currency;
	}

	public void setCurrency(String currency) {
		this.currency = currency;
	}
}
