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

import javax.swing.JButton;
import javax.swing.SwingUtilities;

import org.ozsoft.texasholdem.Card;
import org.ozsoft.texasholdem.Player;
import org.ozsoft.texasholdem.TableType;
import org.ozsoft.texasholdem.actions.Action;
import org.ozsoft.texasholdem.actions.BetAction;
import org.ozsoft.texasholdem.actions.RaiseAction;
import org.ozsoft.texasholdem.gui.ControlPanel;
import org.ozsoft.texasholdem.gui.Main;
import org.ozsoft.texasholdem.gui.PlayerPanel;

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
	private static String encodingTurn="encodings/turn";
	private static String encodingRiver="encodings/river";
	
	private static Handler handler;
	
	
	
    /** Tightness (0 = loose, 100 = tight). */
    private final int tightness;
    
    /** Betting aggression (0 = safe, 100 = aggressive). */
    private final int aggression;
    
    /** Table type. */
    private TableType tableType;
    
    /** The hole cards. */
    private Card[] cards;
    
    public Main main;
	private Boolean visible;
	private Boolean boss;
    
	private String lastPhase;
	private int numberOfRaise;
    
    
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
    	main=null;
    	boss=false;
    	lastPhase="";
    	numberOfRaise=0;
        if (tightness < 0 || tightness > 100) {
            throw new IllegalArgumentException("Invalid tightness setting");
        }
        if (aggression < 0 || aggression > 100) {
            throw new IllegalArgumentException("Invalid aggression setting");
        }
        this.tightness = tightness;
        this.aggression = aggression;
    }

    public BasicBot(int tightness, int aggression, Main main,Boolean boss) {
    	 this.main=main;
    	 this.boss=boss;
    	 lastPhase="";
    	 numberOfRaise=0;
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
    	if(main!=null) {
    		for (Player player : players) {
                PlayerPanel playerPanel = main.playerPanels.get(player.getName());
                if (playerPanel != null) {
                    playerPanel.update(player,"");
                }
            }
    	}
    		this.tableType = type;
    }

    /** {@inheritDoc} */
    @Override
    public void messageReceived(String message) {
    	if(main!=null&&boss==true) {
    	 main.boardPanel.setMessage(message);
         main.boardPanel.waitForUserInput();
    	}
    }

    /** {@inheritDoc} */
    @Override
    public void handStarted(Player dealer) {
    	if(main!=null&&boss==true) {
    	 main.setDealer(false);
         main.dealerName = dealer.getName();
         main.setDealer(true);
    	}
    	else
    		cards = null;
    }

    /** {@inheritDoc} */
    @Override
    public void actorRotated(Player actor) {
    	if(main!=null&&boss==true) {
    	main.setActorInTurn(false);
        main.actorName = actor.getName();
        main.setActorInTurn(true);
    	}
    }

    /** {@inheritDoc} */
    @Override
    public void boardUpdated(List<Card> cards, int bet, int pot) {
    	if(main!=null)
    		main.boardPanel.update(cards, bet, pot);
    }

    /** {@inheritDoc} */
    @Override
    public void playerUpdated(Player player) {
    	if(main!=null) {
    		PlayerPanel playerPanel = main.playerPanels.get(player.getName());
            if (playerPanel != null) {
                playerPanel.update(player,"");
            }
    	}
    	else {
	        if (player.getCards().length == NO_OF_HOLE_CARDS) {
	            this.cards = player.getCards();
	        }
    	}
    }

    /** {@inheritDoc} */
    @Override
    public void playerActed(Player player) {
    	if(main!=null&&boss==true) {
    	 String name = player.getName();
         PlayerPanel playerPanel = main.playerPanels.get(name);
         if (playerPanel != null) {
             playerPanel.update(player,"");
             Action action = player.getAction();
             if (action != null) {
            	main.boardPanel.setMessage(String.format("%s %s.", name, action.getVerb()));
                main.boardPanel.waitForUserInput();
             }
         } else {
             throw new IllegalStateException(
                     String.format("No PlayerPanel found for player '%s'", name));
         }
    	}
    }

    /** {@inheritDoc} */
    @Override
    public Action act(int minBet, int currentBet, Set<Action> allowedActions,Player actor,List<Card> board,int fichesToCall) {
    	if(main!=null&&boss==true) {
    		  SwingUtilities.invokeLater(new Runnable() {
                  @Override
                  public void run() {
                	  main.controlPanel.removeAll();
                      JButton continueButton = main.controlPanel.createActionButton(Action.CONTINUE);
                      main.controlPanel.add(continueButton);
                      //repaint();
                      main.controlPanel.paintAll(main.controlPanel.getGraphics());
                  }
                  
              });
              // Wait for the user to select an action.
              synchronized (main.controlPanel.monitor) {
                  try {
                	  main.controlPanel.monitor.wait();
                  } catch (InterruptedException e) {
                      // Ignore.
                  }
              }	
    	}
    	Comand c = null;
    	Action action = null;
    	handler = new DesktopHandler(new DLVDesktopService("lib/dlv.mingw.exe"));
		InputProgram facts= new ASPInputProgram();

		
		//qui vanno aggiunti i fatti ovvero le azioni permesse che sono i command , le carte del player e le carte sul tavolo
		if(numberOfRaise==3) {
			numberOfRaise=0;
			allowedActions.remove(Action.BET);
			allowedActions.remove(Action.RAISE);
			
		}
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
			facts.addObjectInput(new Bluff());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println(board.size());
		if(board.isEmpty()) {
			if(lastPhase.equals("river")) {
				numberOfRaise=0;
			}
			lastPhase="preflop";
			facts.addFilesPath(encodingPreflop);
		}
		else if(board.size()==3) {
			if(lastPhase.equals("preflop")) {
				numberOfRaise=0;
			}
			lastPhase="flop";
			facts.addFilesPath(encodingFlop);
		}
		else if(board.size()==4) {
			if(lastPhase.equals("flop")) {
				numberOfRaise=0;
			}
			lastPhase="turn";
			facts.addFilesPath(encodingTurn);
		}
		else {
			if(lastPhase.equals("turn")) {
				numberOfRaise=0;
			}
			lastPhase="river";
			facts.addFilesPath(encodingRiver);
		}
			
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
		if(allowedActions.size()!=1) {
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
			numberOfRaise++;
			action = new RaiseAction(c.getPuntata());
		} 
		else if (c.getGiocata().equals("\"bet\"")) {
			numberOfRaise++;
			action = new BetAction(c.getPuntata());
		}
		}
		else
			action=allowedActions.iterator().next();
		
        return action;
    }
    
}
