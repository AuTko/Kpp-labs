package gg;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.Box;
//import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class exit extends JFrame {
	exit(){
		setResizable(false);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setSize(400,300);
		setLocationRelativeTo(null);
		setVisible(true);

		Cursor cr=new Cursor(Cursor.HAND_CURSOR);

		JPanel panel = new  JPanel();
		panel.setLayout(new GridBagLayout());
		panel.add(Box.createRigidArea(new Dimension(0,50)));

		JLabel name = new JLabel("Выйти из игры?");
		name.setAlignmentX(JComponent.CENTER_ALIGNMENT);
		name.setFont(new Font("Verdana",Font.ITALIC,20));
		panel.add(name,new GridBagConstraints(0,0,2,1,1,1,GridBagConstraints.NORTH,
				GridBagConstraints.HORIZONTAL,new Insets(50,121,5,5),0,0));	  

		JButton but1 = new JButton("Да");
		but1.setMaximumSize(new Dimension(150, 50));
		but1.setBackground(Color.LIGHT_GRAY);	 
		but1.setCursor(cr);
		but1.setAlignmentX(JComponent.LEFT_ALIGNMENT); // выравнивание
		but1.addActionListener(new ActionListener() {  //действие
			public void actionPerformed(ActionEvent event) {
				System.exit(0);
			}
		});

		panel.add(but1,new GridBagConstraints(0,1,1,1,1,1,GridBagConstraints.NORTH,
				GridBagConstraints.HORIZONTAL,new Insets(50,20,20,20),0,0));

		JButton but2 = new JButton("Нет");
		but2.setMaximumSize(new Dimension(150, 50));
		but2.setBackground(Color.LIGHT_GRAY);
		but2.setCursor(cr);
		but2.setAlignmentX(JComponent.RIGHT_ALIGNMENT);
		but2.addActionListener(new ActionListener() {  //действие
			public void actionPerformed(ActionEvent event) {
				setVisible(false); 
			}
		});
		panel.add(but2,new GridBagConstraints(1,1,1,1,1,1,GridBagConstraints.NORTH,
				GridBagConstraints.HORIZONTAL,new Insets(50,20,20,20),0,0));
		add(panel);
	}
}
