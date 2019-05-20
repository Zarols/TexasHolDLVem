// This file is part of the 'texasholdem' project, an open source
// Texas Hold'em poker application written in Java.
//
// Copyright 2009 Oscar Stigter
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
//     http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.

package org.ozsoft.texasholdem.bots;

import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.ozsoft.texasholdem.Card;
import org.ozsoft.texasholdem.Player;
import org.ozsoft.texasholdem.TableType;
import org.ozsoft.texasholdem.actions.Action;
import org.ozsoft.texasholdem.actions.BetAction;
import org.ozsoft.texasholdem.actions.RaiseAction;
import org.ozsoft.texasholdem.util.PokerUtils;

import it.unical.mat.embasp.base.Handler;
import it.unical.mat.embasp.base.InputProgram;
import it.unical.mat.embasp.base.Output;
import it.unical.mat.embasp.languages.asp.ASPInputProgram;
import it.unical.mat.embasp.languages.asp.ASPMapper;
import it.unical.mat.embasp.languages.asp.AnswerSet;
import it.unical.mat.embasp.languages.asp.AnswerSets;
import it.unical.mat.embasp.platforms.desktop.DesktopHandler;
import it.unical.mat.embasp.specializations.dlv.desktop.DLVDesktopService;
import it.unical.mat.embasp.specializations.dlv2.desktop.DLV2DesktopService;

/**
 * Basic Texas Hold'em poker bot. <br />
 * <br />
 * 
 * The current implementation acts purely on the bot's hole cards, based on the
 * Chen formula, combined with a configurable level of tightness (when to play
 * or fold a hand ) and aggression (how much to bet or raise in case of good
 * cards or when bluffing). <br />
 * <br />
 * 
 * TODO:
 * <ul>
 * <li>Improve basic bot AI</li>
 * <li>bluffing</li>
 * <li>consider board cards</li>
 * <li>consider current bet</li>
 * <li>consider pot</li>
 * </ul>
 * 
 * @author Oscar Stigter
 */
public class BasicBot extends Bot {
    
	private static String encodingPreflop="encodings/preflop";
	private static String encodingFlop="encodings/flop";
	private static String encodingTurn="encodings/preflop";
	private static String encodingRiver="encodings/preflop";
	
	private static Handler handler;
	
	
	
    /** Tightness (0 = loose, 100 = tight). */
    private final int tightness;
    
    /** Betting aggression (0 = safe, 100 = aggressive). */
    private final int aggression;
    
    /** Table type. */
    private TableType tableType;
    
    /** The hole cards. */
    private Card[] cards;
    
    /**
     * Constructor.
     * 
     * @param tightness
     *            The bot's tightness (0 = loose, 100 = tight).
     * @param aggression
     *            The bot's aggressiveness in betting (0 = careful, 100 =
     *            aggressive).
     */
    public BasicBot(int tightness, int aggression) {
        if (tightness < 0 || tightness > 100) {
            throw new IllegalArgumentException("Invalid tightness setting");
        }
        if (aggression < 0 || aggression > 100) {
            throw new IllegalArgumentException("Invalid aggression setting");
        }
        this.tightness = tightness;
        this.aggression = aggression;
    }

    /** {@inheritDoc} */
    @Override
    public void joinedTable(TableType type, int bigBlind, List<Player> players) {
        this.tableType = type;
    }

    /** {@inheritDoc} */
    @Override
    public void messageReceived(String message) {
        // Not implemented.
    }

    /** {@inheritDoc} */
    @Override
    public void handStarted(Player dealer) {
        cards = null;
    }

    /** {@inheritDoc} */
    @Override
    public void actorRotated(Player actor) {
        // Not implemented.
    }

    /** {@inheritDoc} */
    @Override
    public void boardUpdated(List<Card> cards, int bet, int pot) {
        // Not implemented.
    }

    /** {@inheritDoc} */
    @Override
    public void playerUpdated(Player player) {
        if (player.getCards().length == NO_OF_HOLE_CARDS) {
            this.cards = player.getCards();
        }
    }

    /** {@inheritDoc} */
    @Override
    public void playerActed(Player player) {
        // Not implemented.
    }

    /** {@inheritDoc} */
    @Override
    public Action act(int minBet, int currentBet, Set<Action> allowedActions,Player actor,List<Card> board,int fichesToCall) {
    	Comand c = null;
    	Action action = null;
    	handler = new DesktopHandler(new DLVDesktopService("lib/dlv.mingw.exe"));
		InputProgram facts= new ASPInputProgram();

		
		//qui vanno aggiunti i fatti ovvero le azioni permesse che sono i command , le carte del player e le carte sul tavolo
		
		try {
			for(Action a : allowedActions) {
				facts.addObjectInput(new AllowedComand(a.getName()));
			}
			facts.addObjectInput(new Fiches(actor.getCash()));
			facts.addObjectInput(new CardOfPlayer(actor.getCards()[0].getRank(),actor.getCards()[0].getSuit()));
			facts.addObjectInput(new CardOfPlayer(actor.getCards()[1].getRank(),actor.getCards()[1].getSuit()));
			for(Card cardontable : board) {
				facts.addObjectInput(new CardOnTable(cardontable.getRank(),cardontable.getSuit()));
			}
			facts.addObjectInput(new FichesToCall(fichesToCall));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println(board.size());
		if(board.isEmpty())
			facts.addFilesPath(encodingPreflop);
		else if(board.size()==3)
			facts.addFilesPath(encodingFlop);
		else if(board.size()==4)
			facts.addFilesPath(encodingTurn);
		else
			facts.addFilesPath(encodingRiver);
			
		handler.addProgram(facts);
		try {
			ASPMapper.getInstance().registerClass(Comand.class);	
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		Output o =  handler.startSync();
		AnswerSets answers = (AnswerSets) o;
		
		for(AnswerSet a:answers.getAnswersets()) {
			try {
				for(Object obj:a.getAtoms()){
					if(! (obj instanceof Comand))continue;
					 c= (Comand) obj;
				}
			} catch (Exception e) {
				e.printStackTrace();
			} 
			
		}
		if(c.getGiocata().equals("\"check\"")) {
			action = Action.CHECK;
		}
		else if(c.getGiocata().equals("\"fold\"")) {
			action = Action.FOLD;
		}
		else if(c.getGiocata().equals("\"call\"")) {
			action = Action.CALL;
		}
		else if(c.getGiocata().equals("\"raise\"")) {
			action = new RaiseAction(c.getPuntata());
		} 
		else if (c.getGiocata().equals("\"bet\"")) {
			action = new BetAction(c.getPuntata());
		}
		/*
        if (allowedActions.size() == 1) {
            // No choice, must check.
            action = Action.CHECK;
        } else {
            double chenScore = PokerUtils.getChenScore(cards);
            double chenScoreToPlay = tightness * 0.2;
            if ((chenScore < chenScoreToPlay)) {
                if (allowedActions.contains(Action.CHECK)) {
                    // Always check for free if possible.
                    action = Action.CHECK;
                } else {
                    // Bad hole cards; play tight.
                    action = Action.FOLD;
                }
            } else {
                // Good enough hole cards, play hand.
                if ((chenScore - chenScoreToPlay) >= ((20.0 - chenScoreToPlay) / 2.0)) {
                    // Very good hole cards; bet or raise!
                    if (aggression == 0) {
                        // Never bet.
                        if (allowedActions.contains(Action.CALL)) {
                            action = Action.CALL;
                        } else {
                            action = Action.CHECK;
                        }
                    } else if (aggression == 100) {
                        // Always go all-in!
                        //FIXME: Check and bet/raise player's remaining cash.
                        int amount = (tableType == TableType.FIXED_LIMIT) ? minBet : 100 * minBet;
                        if (allowedActions.contains(Action.BET)) {
                            action = new BetAction(amount);
                        } else if (allowedActions.contains(Action.RAISE)) {
                            action = new RaiseAction(amount);
                        } else if (allowedActions.contains(Action.CALL)) {
                            action = Action.CALL;
                        } else {
                            action = Action.CHECK;
                        }
                    } else {
                        int amount = minBet;
                        if (tableType == TableType.NO_LIMIT) {
                            int betLevel = aggression / 20;
                            for (int i = 0; i < betLevel; i++) {
                                amount *= 2;
                            }
                        }
                        if (currentBet < amount) {
                            if (allowedActions.contains(Action.BET)) {
                                action = new BetAction(amount);
                            } else if (allowedActions.contains(Action.RAISE)) {
                                action = new RaiseAction(amount);
                            } else if (allowedActions.contains(Action.CALL)) {
                                action = Action.CALL;
                            } else {
                                action = Action.CHECK;
                            }
                        } else {
                            if (allowedActions.contains(Action.CALL)) {
                                action = Action.CALL;
                            } else {
                                action = Action.CHECK;
                            }
                        }
                    }
                } else {
                    // Decent hole cards; check or call.
                    if (allowedActions.contains(Action.CHECK)) {
                        action = Action.CHECK;
                    } else {
                        action = Action.CALL;
                    }
                }
            }
        }*/
        return action;
    }
    
}
