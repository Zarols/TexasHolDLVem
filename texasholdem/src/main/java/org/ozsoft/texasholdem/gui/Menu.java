package org.ozsoft.texasholdem.gui;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import javax.swing.JFrame;

public class Menu extends JFrame{
	
	MenuPanel panel;
	
	public Menu() {
		super();
		this.panel = new MenuPanel();
		this.setContentPane(panel);
		this.setVisible(true);
		this.setSize(800,600);
		this.addKeyListener(new KeyAdapter(){
			public void keyPressed(KeyEvent e){
				System.out.println(e.getKeyCode());
				if(e.getKeyCode()==38 ||e.getKeyChar()=='w' ){
					if(panel.cont > 1) {
						panel.cont--;
					}
				}
				if(e.getKeyCode()==40 ||e.getKeyChar()=='s' ) {
					if(panel.cont < 3) {
						panel.cont++;
					}
				}
				if(e.getKeyCode()==10) {
					if(panel.cont == 1) {
						new Main();
					}
				}
				}
		});
	}
	/*
	public void initListener() {
		this.addKeyListener(new KeyAdapter(){
			public void keyPressed(KeyEvent e){
				System.out.println("AAAAAA");
				if(e.getKeyCode()==KeyEvent.VK_UP ||e.getKeyChar()=='w' ){
					if(cont > 1) {
						cont--;
					}
				}
				if(e.getKeyCode()==KeyEvent.VK_UP ||e.getKeyChar()=='s' ) {
					System.out.println("	BBBBB");
					if(cont < 3) {
						cont++;
					}
				}
				}
		});
	}
	*/

}
