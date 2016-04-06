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

public class game_panel  extends JPanel {

	JButton btn1,btn2;
	private Timer tmDraw;//exit;
	game_logic myGame;
	private int mX;
	private  int mY;
	Image foni,kor,ran,ded,bom,vin,luz;
	game_panel(int mod)
	{
		//Попытка загрузки всех изображений для игры
		try
		{
			foni = ImageIO.read(new File("F:\\morskoy\\foni.png"));
			kor = ImageIO.read(new File("F:\\morskoy\\kor.png"));
			ran = ImageIO.read(new File("F:\\morskoy\\ran.png"));
			ded = ImageIO.read(new File("F:\\morskoy\\ded.png"));
			bom = ImageIO.read(new File("F:\\morskoy\\BOM.png"));
			vin = ImageIO.read(new File("F:\\morskoy\\vik.png"));
			luz = ImageIO.read(new File("F:\\morskoy\\luz.png"));
		}
		catch (IOException e) {
			e.printStackTrace();
		}

		super.setLayout(null);

		btn1 = new JButton();
		btn1.setText("Новая игра");
		btn1.setForeground(Color.BLUE);
		btn1.setFont(new Font(null, 0, 30));
		btn1.setBounds(130, 450, 200, 80);
		btn1.addActionListener(new ActionListener() {
			// Обработчик события при нажатии на кнопку Новая игра
			public void actionPerformed(ActionEvent arg0) {
				// Запуск - начало игры
				//myGame.saveFile(2);
				myGame.Start(1);
			}
		});
		add(btn1);

		//Создаем кнопку Выход
		btn2 = new JButton();
		btn2.setText("Выход");
		btn2.setForeground(Color.RED);
		btn2.setFont(new Font(null, 0, 30));
		btn2.setBounds(530, 450, 200, 80);
		btn2.addActionListener(new ActionListener() {
			// Обработчик события при нажатии на кнопку Новая игра
			public void actionPerformed(ActionEvent arg0) {
				// Выход их игры -завершение работы приложения
				if(myGame.endg == 0 && myGame.kolh>0)
					myGame.saveFile(1);
				else
					myGame.saveFile(2);
				System.exit(0);
			}
		});
		add(btn2);

		class myMouse1 implements MouseListener {
			public void mouseClicked(MouseEvent e) {}
			// При нажатии кнопки мыши
			public void mousePressed(MouseEvent e) {
				// Если сделано одиночное нажатие левой клавишей мыши
				if ((e.getButton() == 1) && (e.getClickCount() == 1)) {
					// Получаем текущие координаты курсора мыши
					mX = e.getX();
					mY = e.getY();
					// Если курсор мыши внутри игрового поля компьютера
					if ((mX > 100) && (mY > 100) && (mX < 400) && (mY < 400)) {
						// Если не конец игры и ход игрока
						if ((myGame.endg == 0) && (myGame.hod == false)) {
							// Вычисляем номер строки в массиве
							int i = (mY - 100) / 30;
							// Вычисляем номер элемента в строке в массиве
							int j = (mX - 100) / 30;
							// Если ячейка подходит для выстрела
							if (myGame.masC[i][j] <= 4){
								// Производим выстрел
								myGame.PlayTurn(i, j);
							}
						}
					}
				}
			}
			public void mouseReleased(MouseEvent e) { }
			public void mouseEntered(MouseEvent e) { }
			public void mouseExited(MouseEvent e) { }
		}

		class myMouse2 implements MouseMotionListener {
			public void mouseDragged(MouseEvent e) { }
			// При перемещении курсора мыши
			public void mouseMoved(MouseEvent e) {
				// Получаем координаты курсора
				mX = e.getX();
				mY = e.getY();
				// Если курсор в области поля игрока
				if ((mX>= 100) && (mY >= 100) && (mX <= 400) && (mY<= 400))
					setCursor(new Cursor(Cursor.HAND_CURSOR));
				else
					setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
			}
		}

		//Создаем, настраиваем и запускаем таймер
		//для отрисовки игрового поля
		tmDraw = new Timer(40,new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				// Вызываем перерисовку -paintComponent()
				repaint();
			}
		});

		addMouseListener(new myMouse1());
		addMouseMotionListener(new myMouse2());
		setFocusable(true); 
		myGame=new game_logic();////////////////////////////////////
		// Запускаем игру
		myGame.Start(mod);
		if(myGame.endg == 0){
			tmDraw.start();
		}
	}
	public void paintComponent(Graphics gr){

		super.paintComponent(gr);
		gr.drawImage(foni,0,0,900,600,null);
		gr.setFont(new Font("serif",3,40));
		//Установка цвета
		gr.setColor(Color.BLACK);
		//Выведение надписей
		gr.drawString("Компьютер", 150, 50);
		gr.drawString("Игрок", 590, 50);

		//Отрисовка игровых полей Компьютера
		//и Игрока на основании массивов
		for (int i = 0; i < 10; i++) {
			for (int j = 0; j < 10; j++) {
				//Игровое поле компьютера
				if (myGame.masC[i][j] != 0) {
					//Если это подбитая палуба корабля
					if ((myGame.masC[i][j] >= 8) && (myGame.masC[i][j] <= 11)) {
						gr.drawImage(ran, 100 + j * 30, 100 + i * 30, 30, 30,null);
					}
					//Если это палуба полностью подбитого корабля
					else if (myGame.masC[i][j] >= 15) {
						gr.drawImage(ded, 100 + j * 30, 100 + i * 30, 30, 30,null);
					}
					//Если был выстрел
					if (myGame.masC[i][j] >= 5 && myGame.masC[i][j]<8) {
						gr.drawImage(bom, 100 + j * 30, 100 + i * 30, 30, 30,null);
					}
				}

				//Игровое поле игрока
				if (myGame.masP[i][j] != 0) {
					//Если это палуба корабля
					if ((myGame.masP[i][j] >= 1) && (myGame.masP[i][j] <= 4)) {
						gr.drawImage(kor, 500 + j * 30, 100 + i * 30, 30, 30, null);
					}
					//Если это подбитая палуба корабля
					else if ((myGame.masP[i][j] >= 8) && (myGame.masP[i][j] <= 11)) {
						gr.drawImage(ran, 500 + j * 30, 100 + i * 30, 30, 30,null);
					}
					//Если это палуба полностью подбитого корабля
					else if (myGame.masP[i][j] >= 15) {
						gr.drawImage(ded, 500 + j * 30, 100 + i * 30, 30, 30,null);
					}
					//Если был выстрел
					if (myGame.masP[i][j] >= 5 && myGame.masP[i][j]<8) {
						gr.drawImage(bom, 500 + j * 30, 100 + i * 30, 30, 30,null);
					}
				}
			}
		}

		gr.setColor(Color.RED); // Красный цвет
		//Если курсор мыши внутри игрового поля компьютера
		if ((mX > 100) && (mY > 100) && (mX < 400) && (mY < 400)) {
			//Если не конец игры и ход игрока
			if ((myGame.endg == 0) && (myGame.hod == false)) {
				//Вычисляем номер строки в массиве
				int i = (mY - 100) / 30;
				//Вычисляем номер элемента в строке в массиве
				int j = (mX - 100) / 30;
				//Если ячейка подходит для выстрела
				if (myGame.masC[i][j] <= 4)
					//Рисуем квадрат с заливкой
					gr.fillRect(100 + j * 30, 100 + i * 30, 30, 30);
			}
		}
		gr.setColor(Color.BLACK);
		for (int i = 0; i <= 10; i++){
			//Рисование линий сетки игрового поля Компьютера
			gr.drawLine(100+i*30, 100, 100+i*30, 400);
			gr.drawLine(100, 100+i*30, 400, 100+i*30);
			//Рисование линий сетки игрового поля Игрока
			gr.drawLine(500+i*30, 100, 500+i*30, 400);
			gr.drawLine(500, 100+i*30, 800, 100+i*30);
		}
		//Установка шрифта
		gr.setFont(new Font("Verdana",0,20));
		//Установка цвета
		gr.setColor(Color.RED);
		//Введение цифр и букв слева и сверху от игровых полей
		for (int i = 1; i <= 10; i++){
			//Вывод цифр
			gr.drawString(""+i, 73, 93+i*30);
			gr.drawString(""+i, 473, 93+i*30);
			//Вывод букв
			gr.drawString(""+(char)('A'+i-1), 78+i*30, 93);
			gr.drawString(""+(char)('A'+i-1), 478+i*30, 93);
		}
		//Вывод изображения конца игры - при окончании игры  
		if (myGame.endg == 1){// Если победил Игрок
			gr.drawImage(vin, 300, 200, 300, 100, null);
		}
		else if (myGame.endg == 2){ // Если победил Компьютер
			gr.drawImage(luz, 300, 200, 300, 100, null);
		}
	}
}
