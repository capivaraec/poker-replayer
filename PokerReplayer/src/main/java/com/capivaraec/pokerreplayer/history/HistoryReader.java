package com.capivaraec.pokerreplayer.history;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.TimeZone;
import android.util.Log;
import com.capivaraec.pokerreplayer.enums.ActionID;
import com.capivaraec.pokerreplayer.enums.Limit;
import com.capivaraec.pokerreplayer.enums.Street;

public class HistoryReader {

	private static final String BOARD = "Board";
	private static final String ALL_IN = " and is all-in";
	private static int pot = 0;
	private static int toCall = 0;

	public static History readFile(File file) {

		History history = new History();
		BufferedReader br = null;

		try {
			br = new BufferedReader(new FileReader(file));

			String line;

			Hand hand = null;
			HashMap<String, Player> players = null;
			Street street = Street.PRE_FLOP;
			while ((line = br.readLine()) != null) {
				if (line.contains("Level ")) {

					if (hand != null) {
						history.addHand(hand);
					}

					hand = new Hand();
					players = new HashMap<>();
					street = Street.PRE_FLOP;
					pot = 0;
					newHand(hand, players, line, history);
				} else if (line.startsWith("Table")) {
					setTable(hand, line, history);
				} else if (line.contains(" in chips)")) { //TODO: verificar se cash game também tem esse texto para mostrar stack
					setChips(hand, players, line);
				} else if (line.contains(" posts")) {
					setBlinds(hand, players, line);
				} else if (line.startsWith("Dealt to ")) {
					setCards(hand, players, line);
				} else if (line.contains("calls")) {
					if (history.getHands() != null && history.getHands().size() >= 4) {
						Log.e("", "");
					}
					setCall(hand, players, line, street);
				} else if (line.contains("bets") || line.contains("raises")) {
					setBetOrRaise(hand, players, line, street);
				} else if (line.contains("checks") || line.contains("folds")) {
					setCheckOrFold(hand, players, line, street);
				} else if (line.contains("Uncalled bet")) {
					setUncalledBet(hand, players, line, street);
				} else if (line.contains("*** FLOP ***")) {
					street = Street.FLOP;

                    assert players != null;
                    Player board = (Player) players.get(BOARD).cloneObject();

					String[] card = getCards(line);

					board.addCard(card[0]);
					board.addCard(card[1]);
					board.addCard(card[2]);

					toCall = 0;
					Action action = new Action(board, 0, ActionID.FLOP, street, pot, toCall);
                    assert hand != null;
                    hand.addAction(action);
				} else if (line.contains("*** TURN ***")) {
					street = Street.TURN;

                    assert players != null;
                    Player board = (Player) players.get(BOARD).cloneObject();

					String[] card = getCards(line);

					board.addCard(card[0]);

					toCall = 0;
					Action action = new Action(board, 0, ActionID.TURN, street, pot, toCall);
                    assert hand != null;
                    hand.addAction(action);
				} else if (line.contains("*** RIVER ***")) {
					street = Street.RIVER;

                    assert players != null;
                    Player board = (Player) players.get(BOARD).cloneObject();
					String[] card = getCards(line);

					board.addCard(card[0]);

					toCall = 0;
					Action action = new Action(board, 0, ActionID.RIVER, street, pot, toCall);
                    assert hand != null;
                    hand.addAction(action);
				} else if (line.contains("shows")) {
					showDown(hand, players, line, street);
				} else if (line.contains("collected") && !line.contains("collected (")) {
					getPot(hand, players, line, street);
				}
				// TODO: sitting out e return
			}

			if (hand != null) {
				history.addHand(hand);
			}
		} catch (IOException ioe) {
			ioe.printStackTrace();

			return null;
		} finally {
			try {
                assert br != null;
                br.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		return history;
	}

	private static void newHand(Hand hand, HashMap<String, Player> players, String line, History history) {
		//TODO: colocar este método na classe PokerStarsReader
		Player player = new Player(BOARD, 0, 0);
		players.put(player.getName(), player);

		int leftParenthesis = line.indexOf('(');
		int rightParenthesis = line.indexOf(')');
		int slash = line.indexOf('/');

		int smallBlind = Integer.parseInt(line.substring(leftParenthesis + 1, slash));
		int bigBlind = Integer.parseInt(line.substring(slash + 1, rightParenthesis));

		hand.setSmallBlind(smallBlind);
		hand.setBigBlind(bigBlind);

		int roomIndex = line.indexOf(" Hand");
		String room = line.substring(0, roomIndex);
		//TODO: verificar a sala só na primeira vez
		history.setRoom(room);

		int startHandIndex = line.indexOf('#') + 1;
		int endHandIndex = line.indexOf(':');
		long numHand = Long.parseLong(line.substring(startHandIndex, endHandIndex));
		hand.setHand(numHand);
		//TODO: achar outra forma de pegar o torneio (a princípio se não tiver escrito Hold'em, lançar exceção pq histórico não poderá ser lido
		if (line.contains("Hold'em")) {
			hand.setGame("Texas Hold'em");
		}
		//TODO: abrir diferentes torneios para ver como aparece (Pot Limit, Fixed Limit)
		if (line.contains("No Limit")) {
			hand.setLimit(Limit.NO_LIMIT);
		}
		//TODO: pegar número do torneio só na primeira vez
		int startIndexTourney = line.lastIndexOf('#') + 1;
		int endIndexTourney = line.indexOf(',');
		long tourney = Long.parseLong(line.substring(startIndexTourney, endIndexTourney));
		history.setTourney(tourney);

		int startIndexDate = line.indexOf('[') + 1;
		int endIndexDate = line.trim().lastIndexOf(' ');
		String data = line.substring(startIndexDate, endIndexDate);

		Date date = formatDate(data);
		hand.setDate(date);

		// TODO: setar buy in e rake
	}

	private static Date formatDate(String data) {
		Date date;
		try {
			Locale locale = new Locale("en", "US");
			DateFormat formatter = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss", locale);
			formatter.setTimeZone(TimeZone.getTimeZone("GMT-5"));
			date = formatter.parse(data);
		} catch (ParseException e) {
			return null;
		}
		return date;
	}

	private static void setTable(Hand hand, String line, History history) {

		int initialIndexButton = line.indexOf("Seat #") + 6;
		int finalIndexButton = line.indexOf(" is the button");
		int button = Integer.parseInt(line.substring(initialIndexButton, finalIndexButton));
		hand.setButton(button);

		int finalIndexPlayers = line.indexOf("-max");
		int initialIndexPlayers = line.substring(0, finalIndexPlayers).lastIndexOf(' ') + 1;

		int numPlayers = Integer.parseInt(line.substring(initialIndexPlayers, finalIndexPlayers));

		history.setNumPlayers(numPlayers);

		int initialIndexTable = line.indexOf('\'') + 1;
		initialIndexTable += line.substring(initialIndexTable).indexOf(' ') + 1;
		int finalIndexTable = line.lastIndexOf('\'');
		String table = line.substring(initialIndexTable, finalIndexTable);

		hand.setTable(Integer.parseInt(table));
	}

	private static void setChips(Hand hand, HashMap<String, Player> players, String line) {
		int indexColon = line.indexOf(':');

		int position = Integer.parseInt(line.substring(5, indexColon));

		int finalIndex = line.lastIndexOf(" in chips)");
		int leftParenthesis = line.substring(0, finalIndex).lastIndexOf('(');

		int stack = Integer.parseInt(line.substring(leftParenthesis + 1, finalIndex));

		String name = line.substring(indexColon + 2, leftParenthesis - 1);

		Player player = new Player(name, stack, position);
		players.put(name, player);

		Player initialPlayer = new Player(name, stack, position);
		HashMap<String, Player> initialPlayers = hand.getPlayers();
		if (initialPlayers == null) {
			initialPlayers = new HashMap<>();
		}
		initialPlayers.put(name, initialPlayer);

		hand.setPlayers(initialPlayers);
	}

	private static void setBlinds(Hand hand, HashMap<String, Player> players, String line) {
		int value = getValue(line);

		Player player = getPlayer(line, players, value);

		ActionID actionID;

		Street street = Street.PRE_FLOP;

		if (line.contains(" small blind ")) {
			actionID = ActionID.SMALL_BLIND;
		} else if (line.contains(" big blind ")) {
			actionID = ActionID.BIG_BLIND;
		} else {
			street = Street.ANTE;
			actionID = ActionID.ANTE;
			hand.setAnte(value);
		}

		pot += value;

		if (line.endsWith(ALL_IN)) {
			actionID = ActionID.ALL_IN;
		}

		toCall = value;
		Action action = new Action((Player) player.cloneObject(), value, actionID, street, pot, toCall);
		hand.addAction(action);

	}

	private static void setCards(Hand hand, HashMap<String, Player> players, String line) {
		int finalIndex = line.indexOf('[') - 1;

		String[] cards = getCards(line);

		String name = line.substring(9, finalIndex);

		Player player = players.get(name);
        for (String card : cards) {
            player.addCard(card);
        }

		Action action = new Action((Player) player.cloneObject(), 0, ActionID.HOLE_CARDS, Street.PRE_FLOP, pot, toCall);
		hand.addAction(action);
	}

	private static void setBetOrRaise(Hand hand, HashMap<String, Player> players, String line, Street street) {
		int totalValue = getValue(line);
		String name = getPlayerName(line);
		ArrayList<Action> actions = hand.getActions();
		int streetValue = getValueFromStreet(name, street, actions);
		int value = totalValue - streetValue;
		toCall = totalValue;
		setBetCallOrRaise(hand, players, line, street, value, totalValue);
	}

	private static void setBetCallOrRaise(Hand hand, HashMap<String, Player> players, String line, Street street, int value, int totalValue) {
		Player player = getPlayer(line, players, value);
		ActionID actionID = ActionID.CALL;
		if (line.contains(ALL_IN)) {
			actionID = ActionID.ALL_IN;
		} else if (line.contains("bets")) {
			actionID = ActionID.BET;
		} else if (line.contains("raises")) {
			actionID = ActionID.RAISE;
		}
		pot += value;
		Action action = new Action((Player) player.cloneObject(), totalValue, actionID, street, pot, toCall);
		hand.addAction(action);
	}

	private static void setCall(Hand hand, HashMap<String, Player> players, String line, Street street) {
		int value = getValue(line);
		String name = getPlayerName(line);
		ArrayList<Action> actions = hand.getActions();
		int streetValue = getValueFromStreet(name, street, actions);
		int totalValue = value + streetValue;
		setBetCallOrRaise(hand, players, line, street, value, totalValue);
	}

	private static void setCheckOrFold(Hand hand, HashMap<String, Player> players, String line, Street street) {
		Player player = getPlayer(line, players, 0);
		ActionID actionID = ActionID.CHECK;

		if (line.contains("folds")) {
			actionID = ActionID.FOLD;
		}

		Action action = new Action((Player) player.cloneObject(), 0, actionID, street, pot, toCall);
		hand.addAction(action);
	}

	private static void setUncalledBet(Hand hand, HashMap<String, Player> players, String line, Street street) {
		int leftParenthesis = line.indexOf('(') + 1;
		int rightParenthesis = line.indexOf(')');
		int value = Integer.parseInt(line.substring(leftParenthesis, rightParenthesis));

		int indexPlayer = line.indexOf("returned to") + 11;
		String name = line.substring(indexPlayer).trim();
		Player player = players.get(name);
		player.setStack(player.getStack() + value);

		ActionID actionID = ActionID.UNCALLED_BET;
		pot -= value;
		toCall = 0;
		Action action = new Action((Player) player.cloneObject(), value, actionID, street, pot, toCall);
		hand.addAction(action);
	}

	private static void getPot(Hand hand, HashMap<String, Player> players, String line, Street street) {
		int index = line.lastIndexOf(" collected");

		String name = line.substring(0, index);
		Player player = players.get(name);

		int finalIndex = line.lastIndexOf(" from pot");

		if (finalIndex == -1) {
			finalIndex = line.lastIndexOf(" from side pot");
			if (finalIndex == -1) {
				finalIndex = line.lastIndexOf(" from main pot");
			}
		}

		int value = Integer.parseInt(line.substring(index + 11, finalIndex));
		player.setStack(player.getStack() + value);

		ActionID actionID = ActionID.COLLECTED;

		toCall = 0;
		Action action = new Action((Player) player.cloneObject(), value, actionID, street, value, toCall);
		hand.addAction(action);
	}

	private static void showDown(Hand hand, HashMap<String, Player> players, String line, Street street) {
		Player player = getPlayer(line, players, 0);
		String[] card = getCards(line);
		try {
			player.addCard(card[0]);
			player.addCard(card[1]);//TODO: verificar o porquê do showdown não estar sendo adicionado à lista
		} catch (Exception e) {
			Log.e("Erro", "Erro " + line);
		}
		ActionID actionID = ActionID.SHOW_DOWN;

		toCall = 0;
		Action action = new Action((Player) player.cloneObject(), 0, actionID, street, pot, toCall);

		for (int x = hand.getActions().size() - 1; x >= 0; x--) {
			Action act = hand.getAction(x);
			if (act.getActionID() == ActionID.ALL_IN || act.getActionID() == ActionID.CALL) {
				hand.addAction(action, x + 1);
				break;
			}
		}

	}

	private static int getValue(String line) {
		line = line.trim();

		if (line.endsWith(ALL_IN)) {
			line = line.substring(0, line.length() - ALL_IN.length());
		}

		int index = line.lastIndexOf(' ') + 1;

		return Integer.parseInt(line.substring(index));
	}

	private static Player getPlayer(String line, HashMap<String, Player> players, int value) {
		String name = getPlayerName(line);

		Player player = null;
		try {
			player = players.get(name);
		} catch (Exception e) {
			e.printStackTrace();
		}

        assert player != null;
        player.setStack(player.getStack() - value);

		return player;
	}

	private static String getPlayerName(String line) {
		int indexColon = line.lastIndexOf(':');

		return line.substring(0, indexColon);
	}

	private static String[] getCards(String line) {
		int index = line.lastIndexOf('[') + 1;
		int finalIndex = line.lastIndexOf(']');

		String cards = line.trim().substring(index, finalIndex);

		return cards.split(" ");
	}

	private static int getValueFromStreet(String name, Street street, ArrayList<Action> actions) {
		int totalValue = 0;

		int actionSize = actions.size();
		for (int x = actionSize - 1; x >= 0; x--) {
			if (actions.get(x).getStreet() != street) {
				break;
			}
			if (actions.get(x).getPlayer().getName().equals(name)) {
				totalValue = actions.get(x).getValue();
				break;
			}
		}

		return totalValue;
	}
}
