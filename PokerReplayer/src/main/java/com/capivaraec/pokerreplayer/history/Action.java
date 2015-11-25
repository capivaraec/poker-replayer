package com.capivaraec.pokerreplayer.history;

import java.io.Serializable;

import com.capivaraec.pokerreplayer.enums.ActionID;
import com.capivaraec.pokerreplayer.enums.Street;

public class Action implements Serializable {

	private Player player;
	private float value;
	private ActionID actionID;
	private Street street;
	private float pot;
	private float totalToCall;
    private float valueSpent;

	public Action(Player player, float value, ActionID actionID, Street street, float pot, float totalToCall, float valueSpent) {
		this.player = player;
		this.value = value;
		this.actionID = actionID;
		this.street = street;
		this.pot = pot;
		this.totalToCall = totalToCall;
        this.valueSpent = valueSpent;
	}

    public Action(Player player, ActionID actionID, Street street, float pot, float totalToCall) {
        this.player = player;
        this.actionID = actionID;
        this.street = street;
        this.pot = pot;
        this.totalToCall = totalToCall;
    }

	public Action(Player player, ActionID actionID, Street street, float pot) {
		this.player = player;
		this.actionID = actionID;
		this.street = street;
		this.pot = pot;
	}

	public Player getPlayer() {
		return player;
	}

	public void setPlayer(Player player) {
		this.player = player;
	}

	public float getValue() {
		return value;
	}

	public void setValue(float value) {
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

	public float getPot() {
		return pot;
	}

	public void setPot(float pot) {
		this.pot = pot;
	}

	public float getTotalToCall() {
		return totalToCall;
	}

	public void setTotalToCall(float totalToCall) {
		this.totalToCall = totalToCall;
	}

    public float getValueSpent() {
        return valueSpent;
    }

}
