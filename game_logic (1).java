package gg;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.sql.Time;
import java.util.concurrent.TimeUnit;

public class game_logic implements Consta {

  // An array of the playing field player
  public int[][] masP;
  // An array of computer gaming field
  public int[][] masC;
  public boolean Turn;
  public int endg;
  public int KolT;
  public static int Div = 1;
  public static boolean Bot = false;

  public game_logic() {
    masP = new int[10][10];
    masC = new int[10][10];
  }

  // Games Launch - the beginning of the game
  public void Start(int mod) {
    if (mod == 1) {
      for (int i = 0; i < 10; i++) {
        for (int j = 0; j < 10; j++) {
          masP[i][j] = 0;
          masC[i][j] = 0;
        }
      }
      endg = 0;
      KolT = 0;
      Turn = false;
      rasstanovkaKorabley(masP);
      rasstanovkaKorabley(masC);
    } else {
      loadFile();
    }
  }

  // Arrangement ships
  private void rasstanovkaKorabley(int[][] mas) {
    makeSeveralP(mas, 4);
    for (int i = 1; i <= 2; i++)
      makeSeveralP(mas, 3);
    for (int i = 1; i <= 3; i++)
      makeSeveralP(mas, 2);
    make1P(mas);
  }

  // Check the output of the array bounds
  private boolean testMasPoz(int i, int j) {
    if (((i >= 0) && (i <= 9)) && ((j >= 0) && (j <= 9))) {
      return true;
    } else
      return false;
  }

  // Write the value in the array with array bounds checking
  private void setMasValue(int[][] mas, int i, int j, int val) {
    if (testMasPoz(i, j) == true) {
      mas[i][j] = val;
    }
  }

  // Set the environment one element
  private void setOkr(int[][] mas, int i, int j, int val) {
    if (testMasPoz(i, j) && (mas[i][j] == 0))
      setMasValue(mas, i, j, val);
  }

  // Setting a single cell around
  private void okrBegin(int[][] mas, int i, int j, int val) {
    setOkr(mas, i - 1, j - 1, val);
    setOkr(mas, i - 1, j, val);
    setOkr(mas, i - 1, j + 1, val);
    setOkr(mas, i, j + 1, val);
    setOkr(mas, i + 1, j + 1, val);
    setOkr(mas, i + 1, j, val);
    setOkr(mas, i + 1, j - 1, val);
    setOkr(mas, i, j - 1, val);
  }

  // Create a four body single ship
  private void make1P(int[][] mas) {
    for (int k = 1; k <= 4; k++) {
      while (true) {
        int i = (int) (Math.random() * 10);
        int j = (int) (Math.random() * 10);
        if (mas[i][j] == 0) {
          mas[i][j] = 1;
          okrBegin(mas, i, j, -1);
          break;
        }
      }
    }
  }

  // The final environment
  private void okrEnd(int[][] mas) {
    for (int i = 0; i < 10; i++) {
      for (int j = 0; j < 10; j++) {
        if (mas[i][j] == -2)
          mas[i][j] = -1;
      }
    }
  }

  // Check the cell to be able to house the ship's deck
  private boolean testNewPaluba(int[][] mas, int i, int j) {
    if (testMasPoz(i, j) == false)
      return false;
    if ((mas[i][j] == 0) || (mas[i][j] == -2))
      return true;
    return false;
  }

  // Create a ship with multiple decks
  private void makeSeveralP(int[][] mas, int kolPaluba) {
    while (true) {
      boolean flag = false;
      int i = 0, j = 0;
      i = (int) (Math.random() * 10);
      j = (int) (Math.random() * 10);
      int napr = (int) (Math.random() * 4);
      if (testNewPaluba(mas, i, j) == true) {
        if (napr == 0) {
          if (testNewPaluba(mas, i - (kolPaluba - 1), j) == true)
            flag = true;
        } else if (napr == 1) {
          if (testNewPaluba(mas, i, j + (kolPaluba - 1)) == true)
            flag = true;
        } else if (napr == 2) {
          if (testNewPaluba(mas, i + (kolPaluba - 1), j) == true)
            flag = true;
        } else if (napr == 3) {
          if (testNewPaluba(mas, i, j - (kolPaluba - 1)) == true)
            flag = true;
        }
      }
      if (flag == true) {
        mas[i][j] = kolPaluba;
        okrBegin(mas, i, j, -2);
        if (napr == 0) {
          for (int k = kolPaluba - 1; k >= 1; k--) {
            mas[i - k][j] = kolPaluba;
            okrBegin(mas, i - k, j, -2);
          }
        } else if (napr == 1) {
          for (int k = kolPaluba - 1; k >= 1; k--) {
            mas[i][j + k] = kolPaluba;
            okrBegin(mas, i, j + k, -2);
          }
        } else if (napr == 2) {
          for (int k = kolPaluba - 1; k >= 1; k--) {
            mas[i + k][j] = kolPaluba;
            okrBegin(mas, i + k, j, -2);
          }
        } else if (napr == 3) {
          for (int k = kolPaluba - 1; k >= 1; k--) {
            mas[i][j - k] = kolPaluba;
            okrBegin(mas, i, j - k, -2);
          }
        }
        break;
      }
    }
    okrEnd(mas);
  }

  // Check the end of the game
  private void testEndGame() {
    int testNumber =
        (1 + 2 * Hit) * 4 + (2 + 2 * Hit) * 2 * 3 + (3 + 2 * Hit) * 3 * 2 + (4 + 2 * Hit) * 4;
    int kolComp = 0;
    int kolPlay = 0;
    for (int i = 0; i < 10; i++) {
      for (int j = 0; j < 10; j++) {
        if (masP[i][j] >= 1 + 2 * Hit)
          kolPlay += masP[i][j];
        if (masC[i][j] >= 1 + 2 * Hit)
          kolComp += masC[i][j];
      }
    }
    if (kolPlay == testNumber)
      endg = 2;
    else if (kolComp == testNumber)
      endg = 1;
  }

  // Set an element surrounding the stricken ship
  private void setOkrPodbit(int[][] mas, int i, int j) {
    if (testMasPoz(i, j) == true) {
      if ((mas[i][j] == -1) || (mas[i][j] == 6))
        mas[i][j]--;
    }
  }

  // Setting a single cell downed ship
  private void okrPodbit(int[][] mas, int i, int j) {
    setOkrPodbit(mas, i - 1, j - 1);
    setOkrPodbit(mas, i - 1, j);
    setOkrPodbit(mas, i - 1, j + 1);
    setOkrPodbit(mas, i, j + 1);
    setOkrPodbit(mas, i + 1, j + 1);
    setOkrPodbit(mas, i + 1, j);
    setOkrPodbit(mas, i + 1, j - 1);
    setOkrPodbit(mas, i, j - 1);
  }

  // Shot computer

  private boolean MediumBot(int ms[][]) {
    boolean rez = false;
    boolean flag = false;
    _for1: for (int i = 0; i < 10; i++) {
      for (int j = 0; j < 10; j++) {
        if ((ms[i][j] >= Hit + 2) && (ms[i][j] <= Hit + 4)) {
          flag = true;
          if (testMasPoz(i - 1, j) && (ms[i - 1][j] <= 4) && (ms[i - 1][j] != -2)) {
            ms[i - 1][j] += Hit;
            testUbit(ms, i - 1, j);
            if (ms[i - 1][j] >= Hit + 1)
              rez = true;
            break _for1;
          }

          else if (testMasPoz(i + 1, j) && (ms[i + 1][j] <= 4) && (ms[i + 1][j] != -2)) {
            ms[i + 1][j] += Hit;
            testUbit(ms, i + 1, j);
            if (ms[i + 1][j] >= Hit + 1)
              rez = true;
            break _for1;
          }

          if (testMasPoz(i, j - 1) && (ms[i][j - 1] <= 4) && (ms[i][j - 1] != -2)) {
            ms[i][j - 1] += Hit;
            testUbit(ms, i, j - 1);
            if (ms[i][j - 1] >= Hit + 1)
              rez = true;
            break _for1;
          }

          else if (testMasPoz(i, j + 1) && (ms[i][j + 1] <= 4) && (ms[i][j + 1] != -2)) {
            ms[i][j + 1] += Hit;
            testUbit(ms, i, j + 1);
            if (ms[i][j + 1] >= Hit + 1)
              rez = true;
            break _for1;
          }
        }
      }
    }
    if (flag == false) {
      for (int l = 1; l <= 100; l++) {
        int i = (int) (Math.random() * 10);
        int j = (int) (Math.random() * 10);
        if ((ms[i][j] <= 4) && (ms[i][j] != -2)) {
          ms[i][j] += Hit;
          testUbit(ms, i, j);
          if (ms[i][j] >= Hit + 1) {
            rez = true;
          }
          flag = true;
          break;
        }
      }

      if (flag == false) {
        _for2: for (int i = 0; i < 10; i++) {
          for (int j = 0; j < 10; j++) {
            if ((ms[i][j] <= 4) && (ms[i][j] != -2)) {
              ms[i][j] += Hit;
              testUbit(ms, i, j);
              if (ms[i][j] >= Hit + 1) {
                rez = true;
              }
              break _for2;
            }
          }
        }
      }
    }
    testEndGame();
    return rez;
  }

  private boolean EasyBot(int ms[][]) {
    // Признак попадания в цель
    boolean rez = false;
    // Признак выстрела в раненый корабль
    boolean flag = false;
    // делаем 100 случайных попыток выстрела в случайное место
    for (int l = 1; l <= 100; l++) {
      int i = (int) (Math.random() * 10);
      int j = (int) (Math.random() * 10);
      // Проверяем, что можно сделать выстрел
      if ((ms[i][j] <= 4) && (ms[i][j] != -2)) {
        // делаем выстрел
        ms[i][j] += Hit;
        // проверяем, что убит
        testUbit(ms, i, j);
        // если произошло попадание
        if (ms[i][j] >= Hit + 1) {
          rez = true;
        }
        // выстрел произошел
        flag = true;
        break;
      }
    }

    // если выстрела еще не было
    if (flag == false) {
      // начинаем пробегать весь массив от начала до конца
      _for2: for (int i = 0; i < 10; i++) {
        for (int j = 0; j < 10; j++) {
          if ((ms[i][j] <= 4) && (ms[i][j] != -2)) {
            ms[i][j] += Hit;
            testUbit(ms, i, j);
            if (ms[i][j] >= Hit + 1) {
              rez = true;
            }
            // прерываем сразу все циклы
            break _for2;
          }
        }
      }
    }
    // проверяем конец игры
    testEndGame();
    return rez;
  }

  // Player Shot
  public void PlayTurn(int i, int j) {
    masC[i][j] += Hit;
    KolT++;
    testUbit(masC, i, j);
    testEndGame();
    if (masC[i][j] < Hit + 1) {
      Turn = true;
      while (Turn == true) {
        if (Div == 1) {
          Turn = MediumBot(masP);
        } else {
          Turn = EasyBot(masP);
        }
      }
    }
  }

  public void BotPlay() {
    if (Turn == true) {
      while (Turn == true) {
        Turn = MediumBot(masP);
      }
    } else {
      Turn = true;
      while (Turn == true) {
        Turn = MediumBot(masC);
      }
      Turn = true;
    }
  }

  // Check whether the ship killed
  private void testUbit(int[][] mas, int i, int j) {
    if (mas[i][j] == Hit + 1) {
      mas[i][j] += Hit;
      okrPodbit(mas, i, j);
    } else if (mas[i][j] == Hit + 2)
      analizUbit(mas, i, j, 2);
    else if (mas[i][j] == Hit + 3)
      analizUbit(mas, i, j, 3);
    else if (mas[i][j] == Hit + 4)
      analizUbit(mas, i, j, 4);
  }

  // The analysis of the downed ship
  private void analizUbit(int[][] mas, int i, int j, int kolPalub) {
    int kolRanen = 0;
    for (int k = i - (kolPalub - 1); k <= i + (kolPalub - 1); k++) {
      for (int g = j - (kolPalub - 1); g <= j + (kolPalub - 1); g++) {
        if (testMasPoz(k, g) && (mas[k][g] == kolPalub + Hit))
          kolRanen++;
      }
    }
    if (kolRanen == kolPalub) {
      for (int k = i - (kolPalub - 1); k <= i + (kolPalub - 1); k++) {
        for (int g = j - (kolPalub - 1); g <= j + (kolPalub - 1); g++) {
          if (testMasPoz(k, g) && (mas[k][g] == kolPalub + Hit)) {
            mas[k][g] += Hit;
            okrPodbit(mas, k, g);
          }
        }
      }
    }
  }


  public void Pause(long n) {

    try {
      TimeUnit.MILLISECONDS.sleep(n);
    } catch (InterruptedException e) {
      e.printStackTrace();
    }

  }

  // Save game progress when you exit
  public void saveFile(int mod) {
    RandomAccessFile file = null;
    try {
      file = new RandomAccessFile("F:\\morskoy\\data.txt", "rw");
    } catch (Throwable e) {
    }
    try {
      if (mod == 1) {
        for (int i = 0; i < 10; i++) {
          for (int j = 0; j < 10; j++) {
            file.writeInt(masP[i][j]);
            file.writeInt(masC[i][j]);
          }
        }
        file.writeBoolean(Turn);
        file.writeInt(endg);
        file.writeInt(KolT);
        file.writeInt(Div);
      } else
        file.writeInt(NoSave);
      file.close();
    } catch (IOException e) {
    }
  }

  // Read data from a saved file
  void loadFile() {
    int i, j;
    RandomAccessFile file = null;
    try {
      file = new RandomAccessFile("F:\\morskoy\\data.txt", "r");
    } catch (Throwable e) {
    }
    try {
      for (i = 0; i < 10; i++) {
        for (j = 0; j < 10; j++) {
          masP[i][j] = file.readInt();
          masC[i][j] = file.readInt();
        }
      }
      Turn = file.readBoolean();
      endg = file.readInt();
      KolT = file.readInt();
      Div = file.readInt();
      file.close();
    } catch (Exception e) {
    }
  }
}


