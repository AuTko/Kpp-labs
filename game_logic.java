package gg;

import java.io.IOException;
import java.io.RandomAccessFile;

public class game_logic {

	// Массив для игрового поля игрока
	public int[][] masP;
	// Массив для игрового поля компьютера
	public int[][] masC;
	//Признак хода компьютера (false - ходит игрок)
	public boolean hod;
	// Признак конца игры
	// ( 0-игра идет, 1-победил игрок,2-победил компьютер)
	public int endg;
	public int kolh;

	// Конструктор класса
	public game_logic() {
		masP = new int[10][10];
		masC = new int[10][10];
	}

	// Запуск игры - начало игры
	public void Start(int mod) {		
		if(mod==1){  
			//Очищаем игровое поле игрока
			for (int i = 0; i < 10; i++) {
				for (int j = 0; j < 10; j++) {
					masP[i][j] = 0;
					masC[i][j] = 0;
				}
			}
			//Обнуляем признак чьей-то победы
			endg = 0;
			//Передаем первый ход игроку
			hod = false;
			kolh=0;
			//Расставляем корабли игрока
			rasstanovkaKorabley(masP);
			//Расставляем корабли компьютера
			rasstanovkaKorabley(masC);
		}
		else{
			loadFile();
		}
	}

	// Расстановка кораблей
	private void rasstanovkaKorabley(int[][] mas) {
		//Создаем один четырехпалубный корабль
		makeSeveralP(mas, 4);
		//Создаем два трехпалубных корабля
		for (int i = 1; i <= 2; i++)
			makeSeveralP(mas, 3);
		//Создаем три двухпалубных корабля
		for (int i = 1; i <= 3; i++)
			makeSeveralP(mas, 2);
		//Создаем четыре однопалубных корабля
		make1P(mas);
	}
	//Проверка невыхода за границы массива
	private boolean testMasPoz(int i, int j) {
		if (((i>= 0) && (i <= 9)) && ((j >= 0) && (j <= 9))) {
			return true;
		} 
		else
			return false;
	}

	//Запись значения в массив с проверкой границ массива
	private void setMasValue(int[][] mas, int i, int j, int val) {
		//Если не происходит выход за границы массива
		if (testMasPoz(i, j)==true) {
			//Записываем значение в массив
			mas[i][j] = val;
		}
	}

	//Установить один элемент окружения
	private void setOkr(int[][] mas, int i, int j, int val){
		//Если не происходит выход за пределы массива
		//и в ячейке нулевое значение
		if (testMasPoz(i, j) && (mas[i][j] == 0))
			//Устанавливаем необходимое значение
			setMasValue(mas, i, j, val);
	}

	//Окружение одной ячейки вокруг
	private void okrBegin(int[][] mas, int i, int j, int val) {
		setOkr(mas, i-1, j-1, val); // сверху слева
		setOkr(mas, i-1, j, val); // сверху
		setOkr(mas, i-1, j+1, val); // сверху справа
		setOkr(mas, i, j+1, val); // справа
		setOkr(mas, i+1, j+1, val); // снизу справа
		setOkr(mas, i+1, j, val); // снизу
		setOkr(mas, i+1, j-1, val); // снизу слева
		setOkr(mas, i, j-1, val); // слева
	}

	//Создание четырех однопалубных кораблей
	private void make1P(int[][] mas) {
		//Цикл for делает четыре шага - для четырех кораблей
		for (int k = 1; k <= 4; k++) {
			// Глухой циклwhile
			while (true) {
				// Находим случайную позицию на игровом поле
				int i = (int) (Math.random() * 10);
				int j = (int) (Math.random() * 10);
				// Проверяем, что там ничего нет и можно разместить
				// корабль
				if (mas[i][j] == 0) {
					// Размещаем однопалубный корабль
					mas[i][j] = 1;
					// Выполняем окружение
					okrBegin(mas, i, j, -1);
					// Прерываем цикл
					break;
				}
			}
		}
	}	

	//Конечное окружение
	private void okrEnd(int[][] mas) {
		for (int i = 0; i < 10; i++) {
			for (int j = 0; j < 10; j++) {
				//Если значение элемента массива -2,
				//то заменяем его на -1
				if (mas[i][j] == -2)
					mas[i][j] = -1;
			}
		}
	}

	//Проверка ячейки для возможности размещения в ней палубы корабля
	private boolean testNewPaluba(int[][] mas, int i, int j) {
		//Если выход за границы массива
		if (testMasPoz(i, j) == false)
			return false;
		//Если в этой ячейке 0 или -2, то она нам подходит
		if ((mas[i][j] == 0) || (mas[i][j] == -2))
			return true;
		return false;
	}

	//Создание одного многопалубного корабля
	//Создание корабля с несколькими палубами от 2-х до 4-х
	private void makeSeveralP(int[][] mas, int kolPaluba) {
		while (true) {
			boolean flag = false;
			//Координаты головы корабля
			int i = 0, j = 0;
			//Создание первой палубы - головы корабля
			i = (int) (Math.random() * 10);
			j = (int) (Math.random() * 10);
			//Выбираем случайное направление построения корабля
			//0 - вверх, 1 -вправо, 2 - вниз, 3 - влево
			int napr = (int) (Math.random() * 4);
			if (testNewPaluba(mas, i, j) == true) {
				if (napr == 0) // вверх
				{
					//Если можно расположить палубу
					if (testNewPaluba(mas, i -(kolPaluba - 1), j) == true)
						flag = true;
				}
				else if (napr == 1) // вправо
				{
					if (testNewPaluba(mas, i, j + (kolPaluba - 1)) == true)
						flag = true;
				}
				else if (napr == 2) // вниз
				{
					if (testNewPaluba(mas, i + (kolPaluba - 1), j) == true)
						flag = true;
				}
				else if (napr == 3) // влево
				{
					if (testNewPaluba(mas, i, j -(kolPaluba - 1)) == true)
						flag = true;
				}
			}
			if (flag == true) {
				//Помещаем в ячейку число палуб
				mas[i][j] = kolPaluba;
				//Окружаем минус двойками
				okrBegin(mas, i, j, -2);
				if (napr == 0) // вверх
				{
					for (int k = kolPaluba - 1; k >= 1; k--) {
						//Помещаем в ячейку число палуб
						mas[i -k][j] = kolPaluba;
						//Окружаем минус двойками
						okrBegin(mas, i - k, j, -2);
					}
				}
				else if (napr == 1) // вправо
				{
					for (int k = kolPaluba - 1; k >= 1; k--) {
						mas[i][j + k] = kolPaluba;
						okrBegin(mas, i, j + k, -2);
					}
				}
				else if (napr == 2) // вниз
				{
					for (int k = kolPaluba - 1; k >= 1; k--) {
						mas[i + k][j] = kolPaluba;
						okrBegin(mas, i + k, j, -2);
					}
				}
				else if (napr == 3) // влево
				{
					for (int k = kolPaluba - 1; k >= 1; k--) {
						mas[i][j -k] = kolPaluba;
						okrBegin(mas, i, j - k, -2);
					}
				}
				break;
			}
		}
		//Конечное окружение
		okrEnd(mas);
	}
	
	//Проверка окончания игры
	private void testEndGame()
	{
		//Тестовое число = 15*4+16*2*3+17*3*2+18*4
		//Ситуация, когда все корабли убиты
		int testNumber = 330;
		int kolComp=0; // Сумма убитых палуб компьютера
		int kolPlay=0; // Сумма убитых палуб игрокa
		for (int i = 0; i < 10; i++) {
			for (int j = 0; j < 10; j++) {
				//Суммируем подбитые палубы игрока
				if (masP[i][j] >= 15) kolPlay += masP[i][j];
				//Суммируем подбитые палубы компьютера
				if (masC[i][j] >= 15) kolComp += masC[i][j];
			}
		}
		if (kolPlay == testNumber) endg=2; // Если победил игрок
		else if (kolComp == testNumber) endg=1; // Если победил компьютер
	}

	//Установить один элемент окружения подбитого корабля
	private void setOkrPodbit(int[][] mas, int i, int j) {
		//Если не происходит выход за пределы массива
		//и в ячейке нулевое значение
		if (testMasPoz(i, j)==true)
		{
			//Устанавливаем необходимое значение
			if ((mas[i][j]==-1)||(mas[i][j]==6)) mas[i][j]--;
		}
	}

	//Окружение одной ячейки подбитого вокруг
	private void okrPodbit(int[][] mas, int i, int j) {
		setOkrPodbit(mas, i - 1, j - 1); // сверху слева
		setOkrPodbit(mas, i - 1, j); // сверху
		setOkrPodbit(mas, i - 1, j + 1); // сверху справа
		setOkrPodbit(mas, i, j + 1); // справа
		setOkrPodbit(mas, i + 1, j + 1); // снизу справа
		setOkrPodbit(mas, i + 1, j); // снизу
		setOkrPodbit(mas, i + 1, j - 1); // снизу слева
		setOkrPodbit(mas, i, j - 1); // слева
	}

	//Выстрел компьютера -
	private boolean CompTurn()
	{
		//Признак попадания в цель
		boolean rez = false;
		//Признак выстрела в раненый корабль
		boolean flag = false;
		_for1:
			//Пробегаем все игровое поле игрока
			for (int i = 0; i < 10; i++) {
				for (int j = 0; j < 10; j++) {
					//Если находим раненую палубу
					if ((masP[i][j]>=9)&&(masP[i][j]<=11))
					{
						flag = true;

						//ячейка сверху
						//Проверяем, что можно сделать выстрел
						if (testMasPoz(i-1, j)&&(masP[i-1][j]<=4)&&(masP[i-1][j] !=-2))
						{
							//делаем выстрел
							masP[i-1][j] += 7;
							//проверяем, что убит
							testUbit(masP, i-1, j);
							//если произошло попадание
							if (masP[i-1][j]>=8) rez = true;
							//прерываем сразу все циклы
							break _for1;
						}

						// ячейка снизу
						else if (testMasPoz(i+1, j)&&(masP[i+1][j]<=4)&&(masP[i+1][j] !=-2))
						{
							masP[i+1][j] += 7;
							testUbit(masP, i+1, j);
							if (masP[i+1][j]>=8) rez = true;
							break _for1;
						}

						// ячейка слева
						if (testMasPoz(i, j-1)&&(masP[i][j-1]<=4)&&(masP[i][j-1] !=-2))
						{
							masP[i][j-1] += 7;
							testUbit(masP, i, j-1);
							if (masP[i][j-1]>=8) rez = true;
							break _for1;
						}

						// ячейка справа
						else if (testMasPoz(i, j+1)&&(masP[i][j+1]<=4)&&(masP[i][j+1] !=-2))
						{
							masP[i][j+1] += 7;
							testUbit(masP, i, j+1);
							if (masP[i][j+1]>=8) rez = true;
							break _for1;
						}
					}
				}
			}
		// если не было выстрела в раненую палубу
		if (flag == false) {
			// делаем 100 случайных попыток выстрела в случайное место
			for (int l = 1; l <= 100; l++) {
				int i = (int) (Math.random() * 10);
				int j = (int) (Math.random() * 10);
				// Проверяем, что можно сделать выстрел
				if ((masP[i][j] <= 4) && (masP[i][j] != -2)) {
					// делаем выстрел
					masP[i][j] += 7;
					// проверяем, что убит
					testUbit(masP, i, j);
					// если произошло попадание
					if (masP[i][j] >= 8){
						rez = true;
					}
					// выстрел произошел
					flag = true;
					break;
				}
			}

			// если выстрела еще не было
			if (flag == false) {
				//начинаем пробегать весь массив от начала до конца
				_for2: for (int i = 0; i < 10; i++) {
					for (int j = 0; j < 10; j++) {
						if ((masP[i][j] <= 4)&& (masP[i][j] != -2)) {
							masP[i][j] += 7;
							testUbit(masP, i, j);
							if (masP[i][j] >= 8){
								rez = true;
							}
							// прерываем сразу все циклы
							break _for2;
						}
					}
				}
			}
		}
		//проверяем конец игры
		testEndGame();
		return rez;
	}

	//Выстрел игрока
	public void PlayTurn(int i, int j)
	{
		//При выстреле прибавляем число 7
		masC[i][j] += 7;
		kolh++;
		//Проверяем убит ли корабль
		testUbit(masC, i, j);
		//Проверяем конец игры
		testEndGame();
		//Если был промах - передаем ход компьютеру
		if (masC[i][j]<8)
		{
			hod=true;
			//Ходит компьютер - пока попадает в цель
			while (hod==true) hod = CompTurn();
		}
	}

	// Проверка убит ли корабль
	private void testUbit(int[][] mas, int i, int j)
	{
		//Если однопалубный
		if (mas[i][j]==8){
			// делаем выстрел
			mas[i][j] += 7;
			// окружаем убитый корабль
			okrPodbit(mas, i, j);
		}
		// Если двухпалубный
		else if (mas[i][j]==9) analizUbit(mas, i, j, 2);
		// Если трехпалубный
		else if (mas[i][j]==10) analizUbit(mas, i, j, 3);
		// Если четырехпалубный
		else if (mas[i][j]==11) analizUbit(mas, i, j, 4);
	}

	//Анализ убитого корабля
	private void analizUbit(int[][] mas, int i, int j, int kolPalub)
	{
		//Количество раненых палуб
		int kolRanen=0;
		//Выполняем подсчет раненых палуб
		for (int k=i-(kolPalub-1);k<=i+(kolPalub-1);k++)
		{
			for (int g=j-(kolPalub-1);g<=j+(kolPalub-1);g++)
			{
				//Если это палуба раненого корабля
				if (testMasPoz(k, g)&&(mas[k][g]==kolPalub+7)) kolRanen++;
			}
		}
		//Если количество раненых палуб совпадает с количеством палуб корабля прибавляем число7
		if (kolRanen==kolPalub)
		{
			for (int k=i-(kolPalub-1);k<=i+(kolPalub-1);k++)
			{
				for (int g=j-(kolPalub-1);g<=j+(kolPalub-1);g++)
				{
					//Если это палуба раненого корабля
					if (testMasPoz(k, g)&&(mas[k][g]==kolPalub+7))
					{
						//помечаем палубой убитого корабля
						mas[k][g]+=7;
						//окружаем палубу убитого корабля
						okrPodbit(mas, k, g);
					}
				}
			}
		}
	}

	//сохраение прогресса игры при выходе
	public void saveFile(int mod)
	{
		RandomAccessFile file = null;
		try{
			file = new RandomAccessFile("F:\\morskoy\\data.txt", "rw");//попытка открыть файл
		}catch(Throwable e){}
		try{//файл был открыт - попробуем записать в него
			if(mod==1){
				for(int i = 0; i < 10; i++){
					for(int j = 0;j < 10; j++){
						file.writeInt(masP[i][j]);//записываем число
						file.writeInt(masC[i][j]);
					}
				}
				file.writeBoolean(hod);
				file.writeInt(endg);
				file.writeInt(kolh);
			}
			else
				file.writeInt(99);
			file.close();//всё записали - надо закрыть файл
		}catch(IOException e){}
	}
	
	//чтение данных из файла сохранения
	void loadFile()
	{
		int i,j;
		RandomAccessFile file = null;
		try{
			file = new RandomAccessFile("F:\\morskoy\\data.txt", "r");//открыть только для чтения
		}catch(Throwable e){}
		try{//файл был открыт - попробуем прочесть его
			for( i = 0; i < 10; i++){
				for(j = 0;j < 10; j++){
					masP[i][j]=file.readInt();
					masC[i][j]=file.readInt();
				}
			}
			hod=file.readBoolean();
			endg=file.readInt();
			kolh=file.readInt();
			file.close();//закрыть файл
		}catch(Exception  e){}
	}
	
}



