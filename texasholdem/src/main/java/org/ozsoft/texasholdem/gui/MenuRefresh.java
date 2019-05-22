package org.ozsoft.texasholdem.gui;

public class MenuRefresh extends Thread{

	MenuPanel panel;
	
	public MenuRefresh(MenuPanel panel) {
		this.panel = panel;
	}
	
	public void run() {
		while(panel.menu.choiche==false) {
		panel.repaint();
		}
		panel.menu.setVisible(false);
		if(panel.cont==1)
			new Main("PlayerVsDlv");
		else
			new Main("DlvVsDlv");
	}
}
