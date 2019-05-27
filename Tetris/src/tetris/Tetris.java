package tetris;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.Collections;

import javax.swing.JFrame;
import javax.swing.JPanel;

/**
 * Klasa gry odpowiadająca za wygląd planszy, wygenerowanie klocków, poruszanie nimi oraz za sumowanie punktów.
 * @author Konrad
 */
public class Tetris extends JPanel {

	
        
    
        /**
         * Odpowiada za stworzenie i kształt klocków.
         */
	private final Point[][][] Klocki = {
			// I-Klocek
			{
				{ new Point(0, 1), new Point(1, 1), new Point(2, 1), new Point(3, 1) },
				{ new Point(1, 0), new Point(1, 1), new Point(1, 2), new Point(1, 3) },
				{ new Point(0, 1), new Point(1, 1), new Point(2, 1), new Point(3, 1) },
				{ new Point(1, 0), new Point(1, 1), new Point(1, 2), new Point(1, 3) }
			},
			
			// J-Klocek
			{
				{ new Point(0, 1), new Point(1, 1), new Point(2, 1), new Point(2, 0) },
				{ new Point(1, 0), new Point(1, 1), new Point(1, 2), new Point(2, 2) },
				{ new Point(0, 1), new Point(1, 1), new Point(2, 1), new Point(0, 2) },
				{ new Point(1, 0), new Point(1, 1), new Point(1, 2), new Point(0, 0) }
			},
			
			// L-Klocek
			{
				{ new Point(0, 1), new Point(1, 1), new Point(2, 1), new Point(2, 2) },
				{ new Point(1, 0), new Point(1, 1), new Point(1, 2), new Point(0, 2) },
				{ new Point(0, 1), new Point(1, 1), new Point(2, 1), new Point(0, 0) },
				{ new Point(1, 0), new Point(1, 1), new Point(1, 2), new Point(2, 0) }
			},
			
			// O-Klocek
			{
				{ new Point(0, 0), new Point(0, 1), new Point(1, 0), new Point(1, 1) },
				{ new Point(0, 0), new Point(0, 1), new Point(1, 0), new Point(1, 1) },
				{ new Point(0, 0), new Point(0, 1), new Point(1, 0), new Point(1, 1) },
				{ new Point(0, 0), new Point(0, 1), new Point(1, 0), new Point(1, 1) }
			},
			
			// S-Klocek
			{
				{ new Point(1, 0), new Point(2, 0), new Point(0, 1), new Point(1, 1) },
				{ new Point(0, 0), new Point(0, 1), new Point(1, 1), new Point(1, 2) },
				{ new Point(1, 0), new Point(2, 0), new Point(0, 1), new Point(1, 1) },
				{ new Point(0, 0), new Point(0, 1), new Point(1, 1), new Point(1, 2) }
			},
			
			// T-Klocek
			{
				{ new Point(1, 0), new Point(0, 1), new Point(1, 1), new Point(2, 1) },
				{ new Point(1, 0), new Point(0, 1), new Point(1, 1), new Point(1, 2) },
				{ new Point(0, 1), new Point(1, 1), new Point(2, 1), new Point(1, 2) },
				{ new Point(1, 0), new Point(1, 1), new Point(2, 1), new Point(1, 2) }
			},
			
			// Z-Klocek
			{
				{ new Point(0, 0), new Point(1, 0), new Point(1, 1), new Point(2, 1) },
				{ new Point(1, 0), new Point(0, 1), new Point(1, 1), new Point(0, 2) },
				{ new Point(0, 0), new Point(1, 0), new Point(1, 1), new Point(2, 1) },
				{ new Point(1, 0), new Point(0, 1), new Point(1, 1), new Point(0, 2) }
			}
	};
	/**
         * Odpowiada za przydzielenie kolorów poszczególnym klockom.
         */
	private final Color[] Klocekkolory = {
		Color.cyan, Color.blue, Color.orange, Color.yellow, Color.green, Color.pink, Color.red
	};
	/**
         * Punkt początkowy klocka.
         */
	private Point poczatkowyK;
        /**
         * Pole wskazujące aktualne położenie klocka.
         */
	private int aktualnyK;
        /**
         * Pole obracające klocek.
         */
        private int obrot;
        
	private ArrayList<Integer> nastepnyK = new ArrayList<Integer>();
        /**
         * Pole pokazujące ilość zgromadzonych punktów.
         */
	private long punkty;
        /**
         * Odpowiada za wyświetlanie koloru planszy
         */
	private Color[][] plansza;
	/**
         * Tworzy ramki wokół planszy i inicjuje spadanie klocka.
         */
	private void init() {
		plansza = new Color[12][24];
		for (int i = 0; i < 12; i++) {
			for (int j = 0; j < 23; j++) {
				if (i == 0 || i == 11 || j == 22) {
					plansza[i][j] = Color.BLUE;
				} else {
					plansza[i][j] = Color.GRAY;
				}
			}
		}
		nowyKlocek();
	}
	/**
         * Tworzy nowy, losowy klocek i ustawia go na pozycji początkowej.
         */
	public void nowyKlocek() {
		poczatkowyK = new Point(5, 2);
		obrot = 0;
		if (nastepnyK.isEmpty()) {
			Collections.addAll(nastepnyK, 0, 1, 2, 3, 4, 5, 6);
			Collections.shuffle(nastepnyK);
		}
		aktualnyK = nastepnyK.get(0);
		nastepnyK.remove(0);
	}
	/**
         * Wykonuje test kolizji spadających klocków.
         * @param x
         * @param y
         * @param obrot
         * @return 
         */
	private boolean koliz(int x, int y, int obrot) {
		for (Point p : Klocki[aktualnyK][obrot]) {
			if (plansza[p.x + x][p.y + y] != Color.GRAY) {
				return true;
			}
		}
		return false;
	}
	/**
         * Funkcja obraca klocek zgodnie ze wskazówkami zgara lub na odwrót.
         * @param i 
         */
	public void obrot(int i) {
		int newObrot = (obrot + i) % 4;
		if (newObrot < 0) {
			newObrot = 3;
		}
		if (!koliz(poczatkowyK.x, poczatkowyK.y, newObrot)) {
			obrot = newObrot;
		}
		repaint();
	}
	/**
         * Ruch klocka w lewo lub prawo.
         * @param i 
         */
	public void ruch(int i) {
		if (!koliz(poczatkowyK.x + i, poczatkowyK.y, obrot)) {
			poczatkowyK.x += i;	
		}
		repaint();
	}
	/**
         * Odpowiada za upuszczenie klocka, umieszczenie na planszy jeżeli nie może zdobyc punktów (brak pełnej lini)
         */
	public void upusc() {
		if (!koliz(poczatkowyK.x, poczatkowyK.y + 1, obrot)) {
			poczatkowyK.y += 1;
		} else {
			sprkol();
		}	
		repaint();
	}
	
        /**
         * Utworzenie planszy tak aby była dostępna dla wykrywania kolizji.
         */
	public void sprkol() {
		for (Point p : Klocki[aktualnyK][obrot]) {
			plansza[poczatkowyK.x + p.x][poczatkowyK.y + p.y] = Klocekkolory[aktualnyK];
		}
		usunpaski();
		nowyKlocek();
	}
	/**
         * Usunięcie paska (lini) które zostało zapełnione klockami.
         * @param pasek linia składająca sie z klockow
         */
	public void usunpasek(int pasek) {
		for (int j = pasek-1; j > 0; j--) {
			for (int i = 1; i < 11; i++) {
				plansza[i][j+1] = plansza[i][j];
			}
		}
	}
	
        /**
         * Usuniecie pasków (lini) które zostały zapełnione klockami oraz przyznanie nagrody w postaci punktów.
         */
	public void usunpaski() {
		boolean gap;
		int numClears = 0;
		
		for (int j = 21; j > 0; j--) {
			gap = false;
			for (int i = 1; i < 11; i++) {
				if (plansza[i][j] == Color.GRAY) {
					gap = true;
					break;
				}
			}
			if (!gap) {
				usunpasek(j);
				j += 1;
				numClears += 1;
			}
		}
		
		switch (numClears) {
		case 1:
			punkty += 100;
			break;
		case 2:
			punkty += 300;
			break;
		case 3:
			punkty += 500;
			break;
		case 4:
			punkty += 800;
			break;
		}
	}
	
	/**
         * Rysowanie spadającego klocka.
         * @param g 
         */
	private void rysK(Graphics g) {		
		g.setColor(Klocekkolory[aktualnyK]);
		for (Point p : Klocki[aktualnyK][obrot]) {
			g.fillRect((p.x + poczatkowyK.x) * 26, 
					   (p.y + poczatkowyK.y) * 26, 
					   25, 25);
		}
	}
	/**
         * Kolorowanie planszy, wyswietlanie zdobytych punktow, rysowanie aktualnie spadajacego klocka.
         * @param g 
         */
	@Override 
	public void paintComponent(Graphics g)
	{
		
		g.fillRect(0, 0, 26*12, 26*23);
		for (int i = 0; i < 12; i++) {
			for (int j = 0; j < 23; j++) {
				g.setColor(plansza[i][j]);
				g.fillRect(26*i, 26*j, 25, 25);
			}
		}
	
		g.setColor(Color.WHITE);
		g.drawString("Punkty " + punkty, 18*12, 25);
                rysK(g);
}
                /**
                 * Metoda jest odpowiedzialna za rozmiar okna aplikacji, zakończenie operacji oraz rozpoczęcie gry.
                 */
	public static void main(String[] args) {
		JFrame f = new JFrame("Tetris");
		f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		f.setSize(12*26+10, 26*23+25);
		f.setVisible(true);
		
		final Tetris game = new Tetris();
		game.init();
		f.add(game);
		
		/**
                 * Sterowanie klockiem.
                 */
		f.addKeyListener(new KeyListener() {
                    /**
                     * Sterowanie klockiem.
                     */
			public void keyTyped(KeyEvent e) {
			}
			/**
                         * Funkcja wywoływana gdy wciśniemy przycisk.
                         * @param e 
                         */
			public void keyPressed(KeyEvent e) {
				switch (e.getKeyCode()) {
				case KeyEvent.VK_UP:
					game.obrot(-1);
					break;
				case KeyEvent.VK_DOWN:
					game.upusc();
					break;
				case KeyEvent.VK_LEFT:
					game.ruch(-1);
					break;
				case KeyEvent.VK_RIGHT:
					game.ruch(+1);
					break;
				
				} 
			}
			/**
                         * Brak reakcji gdy puścimy klawisz. Funkcja nic nie zwraca.
                         * @param e 
                         */
			public void keyReleased(KeyEvent e) {
			}
		});
		
		/**
                 * Odpowiada za czas spadania klocka, wartośći podawane są w milisekundach (x>wolniej)
                 */
		new Thread() {
			@Override public void run() {
				while (true) {
					try {
						Thread.sleep(800);
						game.upusc();
					} catch ( InterruptedException e ) {}
				}
			}
		}.start();
	}
}
