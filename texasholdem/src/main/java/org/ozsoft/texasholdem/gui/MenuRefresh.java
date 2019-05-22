package org.ozsoft.texasholdem.gui;

public class MenuRefresh extends Thread{

	MenuPanel panel;
	
	public MenuRefresh(MenuPanel panel) {
		this.panel = panel;
	}
	
	public void run() {
		while(true) {
		panel.repaint();
		}
	}
}
