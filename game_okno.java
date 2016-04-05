package gg;

import javax.swing.JFrame;

//public class game extends JFrame implements Runnable{

public class game_okno extends JFrame{

	game_okno(int mod){

		super ("Game");
		setResizable(false);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setSize(900,600);
		setLocationRelativeTo(null);
		game_panel pan = new game_panel(mod);
		add(pan);

		setVisible(true);
	}
}

