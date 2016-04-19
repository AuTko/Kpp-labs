package gg;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;

import javax.imageio.ImageIO;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class nub extends JFrame implements Consta {
  nub() {
    super("Sea fight");
    setResizable(false);
    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    setSize(nubWinX, nubWinY);
    setLocationRelativeTo(null);

    Cursor cr = new Cursor(Cursor.HAND_CURSOR); // cursor
    Font fn = new Font(null, Font.ITALIC, nubButText); // text
    JPanel panel = new JPanel() {
      public void paintComponent(Graphics g) {
        Image fon = null;
        try {
          fon = ImageIO.read(new File("F:\\morskoy\\fon.png"));
        } catch (IOException e) {
          e.printStackTrace();
        }
        g.drawImage(fon, 0, 0, null);
      }
    };
    panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
    panel.add(Box.createRigidArea(new Dimension(0, nubRigidArea)));
    if (checkSave()) {

      JButton but2 = new JButton("Continue");
      but2.setMaximumSize(new Dimension(nubButX, nubButY));
      but2.setBackground(Color.LIGHT_GRAY);
      but2.setFont(fn);
      but2.setCursor(cr);
      but2.setAlignmentX(JComponent.CENTER_ALIGNMENT);
      but2.addActionListener(new ActionListener() {
        public void actionPerformed(ActionEvent event) {
          setVisible(false);
          JFrame g = new game_okno(2);
        }
      });
      panel.add(but2);

      panel.add(Box.createRigidArea(new Dimension(0, nubButRigidArea)));
    }
    JButton but1 = new JButton("New game");
    but1.setMaximumSize(new Dimension(nubButX, nubButY));
    but1.setBackground(Color.LIGHT_GRAY);// background color
    but1.setFont(fn);// view text
    but1.setCursor(cr);
    but1.setAlignmentX(JComponent.CENTER_ALIGNMENT); // alignment
    but1.addActionListener(new ActionListener() { // action
      public void actionPerformed(ActionEvent event) {
        setVisible(false);
        JFrame g = new game_okno(1);
      }
    });
    panel.add(but1);
    /*
     * panel.add(Box.createRigidArea(new Dimension(0,25))); // интервал
     * 
     * JButton but2 = new JButton("Настройки"); but2.setMaximumSize(new Dimension(250, 100));
     * but2.setBackground(Color.LIGHT_GRAY); but2.setFont(fn); but2.setCursor(cr);
     * but2.setAlignmentX(JComponent.CENTER_ALIGNMENT); but2.addActionListener(new ActionListener()
     * { //действие ПОКА НЕ НУЖНАЯ КНОПКА public void actionPerformed(ActionEvent event) {
     * setVisible(false); JFrame set=new settings(); // System.exit(0); } }); panel.add(but2);
     */
    panel.add(Box.createRigidArea(new Dimension(0, nubButRigidArea)));

    JButton but3 = new JButton("Exit");
    but3.setMaximumSize(new Dimension(nubButX, nubButY));
    but3.setBackground(Color.LIGHT_GRAY);
    but3.setFont(fn);
    but3.setCursor(cr);
    but3.setAlignmentX(JComponent.CENTER_ALIGNMENT);
    but3.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent event) {
        exit ex = new exit();
      }
    });
    panel.add(but3);
    add(panel);
    setVisible(true);

  }

  // saving test
  boolean checkSave() {
    int i = 0;
    RandomAccessFile file = null;

    try {
      file = new RandomAccessFile("F:\\morskoy\\data.txt", "r");// Open for reading only
    } catch (Throwable e) {
    }
    try {
      i = file.readInt();
      file.close();// close the file
    } catch (Exception e) {
    }
    if (i == NoSave)
      return false;
    else
      return true;
  }


  public static void main(String sarg[]) {
    JFrame menu = new nub();
  }
}
