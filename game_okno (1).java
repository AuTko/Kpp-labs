package gg;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JRadioButton;


public class game_okno extends JFrame implements Consta {

  game_okno(int mod) {

    super("Game");
    setResizable(false);
    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    setSize(GameWinX, GameWinY);
    setLocationRelativeTo(null);

    final game_panel pan = new game_panel(mod);
    add(pan);

    JMenuBar Menu = new JMenuBar();

    JMenu BotMenu = new JMenu("Bot");
    final JCheckBox butb = new JCheckBox("Activate");
    BotMenu.setMaximumSize(new Dimension(oknoMenuButX, oknoMenuButY));
    butb.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent event) {
        if (butb.isSelected()) {
          pan.setBot(true);
        } else {
          pan.setBot(false);
        }
      }
    });
    BotMenu.add(butb);
    Menu.add(BotMenu);
    JMenu DifficultMenu = new JMenu("Difficult");
    final JCheckBox butdm = new JCheckBox("Medium");
    final JCheckBox butde = new JCheckBox("Easy");
    if (pan.getBot()) {
      butb.setSelected(true);
    }

    if (pan.getDiv() == 1) {
      butdm.setSelected(true);
      butdm.setEnabled(false);
    } else {
      butde.setSelected(true);
      butde.setEnabled(false);
    }
    DifficultMenu.setMaximumSize(new Dimension(oknoMenuButX, oknoMenuButY));
    butde.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent event) {
        butdm.setSelected(false);
        pan.setDiv(0);
        butde.setEnabled(false);
        butdm.setEnabled(true);
      }
    });
    butdm.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent event) {
        butde.setSelected(false);
        pan.setDiv(1);
        // game_logic.Div=1;
        butdm.setEnabled(false);
        butde.setEnabled(true);
      }
    });
    DifficultMenu.add(butde);
    DifficultMenu.add(butdm);
    Menu.add(DifficultMenu);

    setJMenuBar(Menu);

    setVisible(true);
  }
}

