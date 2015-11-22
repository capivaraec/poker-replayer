package com.capivaraec.pokerreplayer.history;

import java.io.Serializable;
import java.util.ArrayList;

public class History implements Serializable {

	private static final long serialVersionUID = 2019763739104900771L;

	private enum Session {
		TOURNEY, RING
	}

	private double buyIn;
	private double rake;
	private int numPlayers;
	private String room;
	private Session session;
	private ArrayList<Hand> hands;
	private long tourney;

	public double getBuyIn() {
		return buyIn;
	}

	public void setBuyIn(double buyIn) {
		this.buyIn = buyIn;
	}

	public double getRake() {
		return rake;
	}

	public void setRake(double rake) {
		this.rake = rake;
	}

	public int getNumPlayers() {
		return numPlayers;
	}

	public void setNumPlayers(int numPlayers) {
		this.numPlayers = numPlayers;
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
}
