package org.ozsoft.texasholdem.gui;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.Toolkit;
import java.net.URL;
import javax.swing.JPanel;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class MenuPanel extends JPanel {

	Toolkit tk = Toolkit.getDefaultToolkit();
	URL img1;
	URL img2;
	URL img3;
	Image i1;
	Image i2;
	Image i3;
	int cont = 1;	
	MenuRefresh mr;
	Menu menu;
	
	public MenuPanel(Menu menu) {
		super();
		this.menu=menu;
		this.setVisible(true);
		img1 = this.getClass().getResource("b1.png");
		img2 = this.getClass().getResource("b2.png");
		img3 = this.getClass().getResource("b3.png");
		i1 = tk.getImage(img1);
		i2 = tk.getImage(img2);
		i3 = tk.getImage(img3);
		mr = new MenuRefresh(this);
		mr.start();
	}
	
	public void paintComponent(Graphics g) {
		if(cont == 1) {
			g.drawImage(i1,0,0,this);
		} 
		if(cont == 2) {
			g.drawImage(i2,0,0,this);
		}
		if(cont == 3) {
			g.drawImage(i3,0,0,this);
		}	
	}
}
