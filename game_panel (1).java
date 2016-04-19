package gg;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.Timer;

public class game_panel extends JPanel implements Consta {

  JButton btn1, btn2;
  private Timer tmDraw;// exit;
  game_logic myGame;
  private int mX;
  private int mY;
  Image foni, kor, ran, ded, bom, vin, luz;

  game_panel(int mod) {
    // It tries to download all of the images for the game
    try {
      foni = ImageIO.read(new File("F:\\morskoy\\foni.png"));
      kor = ImageIO.read(new File("F:\\morskoy\\kor.png"));
      ran = ImageIO.read(new File("F:\\morskoy\\ran.png"));
      ded = ImageIO.read(new File("F:\\morskoy\\ded.png"));
      bom = ImageIO.read(new File("F:\\morskoy\\BOM.png"));
      vin = ImageIO.read(new File("F:\\morskoy\\vik.png"));
      luz = ImageIO.read(new File("F:\\morskoy\\luz.png"));
    } catch (IOException e) {
      e.printStackTrace();
    }

    super.setLayout(null);

 // button "New game"
    btn1 = new JButton();
    btn1.setText("New game");
    btn1.setForeground(Color.BLUE);
    btn1.setFont(new Font(null, 0, GameButText));
    btn1.setBounds(GameBut1x, GameBut1y, GameButX, GameButY);
    btn1.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent arg0) {
        myGame.Start(1);
        if (getBot()) {
          repaint();
        }
      }

    });
    add(btn1);

    // button "Exit"
    btn2 = new JButton();
    btn2.setText("Exit");
    btn2.setForeground(Color.RED);
    btn2.setFont(new Font(null, 0, GameButText));
    btn2.setBounds(GameBut2x, GameBut2y, GameButX, GameButY);
    btn2.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent arg0) {
        if (myGame.endg == 0 && myGame.KolT > 0)
          myGame.saveFile(1);
        else
          myGame.saveFile(2);
        System.exit(0);
      }
    });
    add(btn2);

    class myMouse1 implements MouseListener {
      public void mouseClicked(MouseEvent e) {}

      // When you press the mouse button
      public void mousePressed(MouseEvent e) {
        if (!getBot()) {
          if ((e.getButton() == 1) && (e.getClickCount() == 1)) {
            // Get the current position of the mouse cursor
            mX = e.getX();
            mY = e.getY();
            if ((mX > EnemyFieldX) && (mY > EnemyFieldY) && (mX < EnemyFieldX + 10 * KubSize)
                && (mY < EnemyFieldY + 10 * KubSize)) {
              if ((myGame.endg == 0) && (myGame.Turn == false)) {
                int i = (mY - EnemyFieldY) / KubSize;
                int j = (mX - EnemyFieldX) / KubSize;
                // If the cell is suitable for a shot
                if (myGame.masC[i][j] <= 4) {
                  myGame.PlayTurn(i, j);
                }
              }
            }
          }
        }
      }

      public void mouseReleased(MouseEvent e) {}

      public void mouseEntered(MouseEvent e) {}

      public void mouseExited(MouseEvent e) {}
    }

    class myMouse2 implements MouseMotionListener {
      public void mouseDragged(MouseEvent e) {}

      // When you move the mouse cursor
      public void mouseMoved(MouseEvent e) {
        if (!getBot()) {
          mX = e.getX();
          mY = e.getY();
          // If the cursor is in the player's field
          if ((mX >= EnemyFieldX) && (mY >= EnemyFieldY) && (mX <= EnemyFieldX + 10 * KubSize)
              && (mY <= EnemyFieldY + 10 * KubSize))
            setCursor(new Cursor(Cursor.HAND_CURSOR));
          else
            setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
        }
      }
    }

    // Timer to draw the playing field
    tmDraw = new Timer(Timer, new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent arg0) {
        // repaint paintComponent()
        repaint();
      }
    });

    addMouseListener(new myMouse1());
    addMouseMotionListener(new myMouse2());
    setFocusable(true);
    myGame = new game_logic();
    // Starting the game
    myGame.Start(mod);
    if (myGame.endg == 0) {
      tmDraw.start();
    }
    while (myGame.Bot == true && myGame.endg == 0) {
      myGame.BotPlay();
      myGame.Pause(350);
      repaint();
    }
  }

  public void paintComponent(Graphics gr) {

    super.paintComponent(gr);
    gr.drawImage(foni, 0, 0, GameWinX, GameWinY, null);
    gr.setFont(new Font("serif", 3, GameLabelText));
    gr.setColor(Color.BLACK);
    gr.drawString("Enemy", EnemylabelX, EnemyLabelY);
    gr.drawString("Player", PlayerLabelX, PlayerLabelY);

    // Drawing playing fields on the basis of arrays
    for (int i = 0; i < 10; i++) {
      for (int j = 0; j < 10; j++) {
        // The playing field of the computer
        if (myGame.masC[i][j] != 0) {
          if (myGame.endg != 0) {
            if ((myGame.masC[i][j] >= 1) && (myGame.masC[i][j] <= 4)) {
              gr.drawImage(kor, EnemyFieldX + j * KubSize, 100 + i * KubSize, KubSize, KubSize,
                  null);
            }
          }
          if ((myGame.masC[i][j] >= Hit + 1) && (myGame.masC[i][j] <= Hit + 4)) {
            gr.drawImage(ran, EnemyFieldX + j * KubSize, EnemyFieldY + i * KubSize, KubSize,
                KubSize, null);
          }
          else if (myGame.masC[i][j] >= 2 * Hit + 1) {
            gr.drawImage(ded, EnemyFieldX + j * KubSize, EnemyFieldY + i * KubSize, KubSize,
                KubSize, null);
          }
          if (myGame.masC[i][j] >= Hit - 2 && myGame.masC[i][j] < Hit + 1) {
            gr.drawImage(bom, EnemyFieldX + j * KubSize, EnemyFieldY + i * KubSize, KubSize,
                KubSize, null);
          }
        }

        // Playing Field Player
        if (myGame.masP[i][j] != 0) {
          if ((myGame.masP[i][j] >= 1) && (myGame.masP[i][j] <= 4)) {
            gr.drawImage(kor, PlayerFieldX + j * KubSize, 100 + i * KubSize, KubSize, KubSize,
                null);
          }
          else if ((myGame.masP[i][j] >= Hit + 1) && (myGame.masP[i][j] <= Hit + 4)) {
            gr.drawImage(ran, PlayerFieldX + j * KubSize, PlayerFieldY + i * KubSize, KubSize,
                KubSize, null);
          }
          else if (myGame.masP[i][j] >= 2 * Hit + 1) {
            gr.drawImage(ded, PlayerFieldX + j * KubSize, PlayerFieldY + i * KubSize, KubSize,
                KubSize, null);
          }
          if (myGame.masP[i][j] >= Hit - 2 && myGame.masP[i][j] < Hit + 1) {
            gr.drawImage(bom, PlayerFieldX + j * KubSize, PlayerFieldY + i * KubSize, KubSize,
                KubSize, null);
          }
        }
      }
    }

    // If the mouse cursor inside the computer gaming field
    gr.setColor(Color.RED);
    if ((mX > EnemyFieldX) && (mY > EnemyFieldY) && (mX < EnemyFieldX + 10 * KubSize)
        && (mY < EnemyFieldY + 10 * KubSize)) {
      if ((myGame.endg == 0) && (myGame.Turn == false)) {
        int i = (mY - EnemyFieldY) / KubSize;
        int j = (mX - EnemyFieldX) / KubSize;
        if (myGame.masC[i][j] < Hit - 2)
          gr.fillRect(EnemyFieldX + j * KubSize, EnemyFieldY + i * KubSize, KubSize, KubSize);
      }
    }
    // Drawing playing field gridlines
    gr.setColor(Color.BLACK);
    for (int i = 0; i <= 10; i++) {
      gr.drawLine(EnemyFieldX + i * KubSize, EnemyFieldY, EnemyFieldX + i * KubSize,
          EnemyFieldY + 10 * KubSize);
      gr.drawLine(EnemyFieldX, EnemyFieldY + i * KubSize, EnemyFieldX + 10 * KubSize,
          EnemyFieldY + i * KubSize);
      gr.drawLine(PlayerFieldX + i * KubSize, PlayerFieldY, PlayerFieldX + i * KubSize,
          PlayerFieldY + 10 * KubSize);
      gr.drawLine(PlayerFieldX, PlayerFieldY + i * KubSize, PlayerFieldX + 10 * KubSize,
          PlayerFieldY + i * KubSize);
    }
    gr.setFont(new Font("Verdana", 0, GameIndexText));
    gr.setColor(Color.RED);
    // Introduction of numbers and letters on the left and top of the playing fields
    for (int i = 1; i <= 10; i++) {
      gr.drawString("" + i, EnemyIndexX, EnemyIndexY + i * KubSize);
      gr.drawString("" + i, PlayerIndexX, PlayerIndexY + i * KubSize);

      gr.drawString("" + (char) ('A' + i - 1), EnemyIndexX + 5 + i * KubSize, EnemyIndexY);
      gr.drawString("" + (char) ('A' + i - 1), PlayerIndexX + 5 + i * KubSize, PlayerIndexY);
    }
    // Display the image end of the game - at the end of the game
    if (myGame.endg == 1) {
      gr.drawImage(vin, EndImageSetX, EndImageSetY, EndImageX, EndImageY, null);
    } else if (myGame.endg == 2) {
      gr.drawImage(luz, EndImageSetX, EndImageSetY, EndImageX, EndImageY, null);
    }
    if (!getBot() && myGame.Turn == true) {
      myGame.BotPlay();
    }
    if (getBot() && myGame.endg == 0) {
      myGame.BotPlay();
      myGame.Pause(350);
    }
  }

  public boolean getBot() {
    return myGame.Bot;
  }

  public void setBot(boolean b) {
    myGame.Bot = b;
  }

  public int getDiv() {
    return myGame.Div;
  }

  public void setDiv(int b) {
    myGame.Div = b;
  }
}
