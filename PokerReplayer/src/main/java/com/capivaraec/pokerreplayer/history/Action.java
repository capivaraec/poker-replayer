package com.capivaraec.pokerreplayer.history;

import java.io.Serializable;

import com.capivaraec.pokerreplayer.enums.ActionID;
import com.capivaraec.pokerreplayer.enums.Street;

public class Action implements Serializable {

	private Player player;
	private int value;
	private ActionID actionID;
	private Street street;
	private int pot;
	private int toCall;

	public Action(Player player, int value, ActionID actionID, Street street, int pot, int toCall) {
		this.player = player;
		this.value = value;
		this.actionID = actionID;
		this.street = street;
		this.pot = pot;
		this.toCall = toCall;
	}

	public Player getPlayer() {
		return player;
	}

	public void setPlayer(Player player) {
		this.player = player;
	}

	public int getValue() {
		return value;
	}

	public void setValue(int value) {
		this.value = value;
	}

	public ActionID getActionID() {
		return actionID;
	}

	public void setActionID(ActionID actionID) {
		this.actionID = actionID;
	}

	public Street getStreet() {
		return street;
	}

	public void setStreet(Street street) {
		this.street = street;
	}

	public int getPot() {
		return pot;
	}

	public void setPot(int pot) {
		this.pot = pot;
	}

	public int getToCall() {
		return toCall;
	}

	public void setToCall(int toCall) {
		this.toCall = toCall;
	}

}
