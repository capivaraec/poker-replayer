package com.capivaraec.pokerreplayer.history;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import com.capivaraec.pokerreplayer.enums.Limit;

public class Hand implements Serializable {

	private int smallBlind;
	private int bigBlind;
	private int button;
	private Limit limit;
	private int table;
	private String game;
	private int ante;
	private ArrayList<Action> actions;
	private int pot;
	private long hand;
	private Date date;
	private HashMap<String, Player> players;
	private int numPlayers;

	public int getSmallBlind() {
		return smallBlind;
	}

	public void setSmallBlind(int smallBlind) {
		this.smallBlind = smallBlind;
	}

	public int getBigBlind() {
		return bigBlind;
	}

	public void setBigBlind(int bigBlind) {
		this.bigBlind = bigBlind;
	}

	public int getButton() {
		return button;
	}

	public void setButton(int button) {
		this.button = button;
	}

	public Limit getLimit() {
		return limit;
	}

	public void setLimit(Limit limit) {
		this.limit = limit;
	}

	public int getTable() {
		return table;
	}

	public void setTable(int table) {
		this.table = table;
	}

	public String getGame() {
		return game;
	}

	public void setGame(String game) {
		this.game = game;
	}

	public int getAnte() {
		return ante;
	}

	public void setAnte(int ante) {
		this.ante = ante;
	}

	public ArrayList<Action> getActions() {
		return actions;
	}

	public void setActions(ArrayList<Action> actions) {
		this.actions = actions;
	}

	public void addAction(Action action) {
		if (actions == null) {
			actions = new ArrayList<>();
		}

		actions.add(action);
	}

	public void addAction(Action action, int index) {
		actions.add(index, action);
	}

	public Action getAction(int index) {
		return actions.get(index);
	}

	public int getPot() {
		return pot;
	}

	public void setPot(int pot) {
		this.pot = pot;
	}

	public long getHand() {
		return hand;
	}

	public void setHand(long hand) {
		this.hand = hand;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public HashMap<String, Player> getPlayers() {
		return players;
	}

	public void setPlayers(HashMap<String, Player> players) {
		this.players = players;
	}

    public int getNumPlayers() {
        return numPlayers;
    }

    public void setNumPlayers(int numPlayers) {
        this.numPlayers = numPlayers;
    }

}
