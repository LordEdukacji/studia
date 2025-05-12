package pl.Dybowski.Adrian.Projekt2.WirtualnySwiat.Swiaty;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;

import java.util.HashSet;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import javax.swing.AbstractAction;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.KeyStroke;
import javax.swing.JTextArea;

import pl.Dybowski.Adrian.Projekt2.WirtualnySwiat.Kierunki;
import pl.Dybowski.Adrian.Projekt2.WirtualnySwiat.Launcher;
import pl.Dybowski.Adrian.Projekt2.WirtualnySwiat.Organizm;
import pl.Dybowski.Adrian.Projekt2.WirtualnySwiat.Swiat;
import pl.Dybowski.Adrian.Projekt2.WirtualnySwiat.Wspolrzedne2D;
import pl.Dybowski.Adrian.Projekt2.WirtualnySwiat.Zwierzeta.Czlowiek;

public class SwiatKwadratowy extends Swiat {
	private int wysokosc;
	private int szerokosc;
	
	public SwiatKwadratowy(int wysokosc, int szerokosc, Launcher launcher, Boolean zapis) {
		super(launcher);
		this.wysokosc = wysokosc;
		this.szerokosc = szerokosc;
		
		mapaOrganizmow = new Organizm[wysokosc][szerokosc];
		
		if (!zapis) dodajPoczatkoweOrganizmy();
		
		stworzOknoSwiata();
	}

	@Override
	public void stworzPola() {
		plansza = new JPanel(new GridLayout(wysokosc, szerokosc));
		
		for (int i = 0; i < wysokosc; i++) {
			for (int j = 0; j < szerokosc; j++) {
				JLabel pole = new JLabel();
				
				pole.setOpaque(true);
				pole.setBackground(Color.LIGHT_GRAY);
				pole.setBorder(BorderFactory.createLineBorder(Color.BLACK));
				pole.setPreferredSize(new Dimension(32,32));
				
				pole.addMouseListener(new SluchaczPola(j, i));
				
				pola[i][j] = pole;
				plansza.add(pole);
			}
		}
	}
	
	@Override
	public JPanel stworzPanelZeStrzalkami() {
		JPanel strzalkiPanel = new JPanel(new GridBagLayout());
		
		JButton prawo = new JButton("Prawo (→)");
        JButton lewo = new JButton("Lewo (←)");
        JButton gora = new JButton("Gora (↑)");
        JButton dol = new JButton("Dol (↓)");
        
        prawo.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
            	if (czlowiek == null) return;
                czlowiek.setKierunek(Kierunki.E);
            }
        });
        prawo.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(KeyEvent.VK_RIGHT, 0), "right");
        prawo.getActionMap().put("right", new AbstractAction() {
			@Override
			public void actionPerformed(ActionEvent e) {
				prawo.doClick();
			}
		});
        lewo.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
            	if (czlowiek == null) return;
                czlowiek.setKierunek(Kierunki.W);
            }
        });
        lewo.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(KeyEvent.VK_LEFT, 0), "left");
        lewo.getActionMap().put("left", new AbstractAction() {
			@Override
			public void actionPerformed(ActionEvent e) {
				lewo.doClick();
			}
		});
        gora.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
            	if (czlowiek == null) return;
                czlowiek.setKierunek(Kierunki.N);
            }
        });
        gora.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(KeyEvent.VK_UP, 0), "up");
        gora.getActionMap().put("up", new AbstractAction() {
			@Override
			public void actionPerformed(ActionEvent e) {
				gora.doClick();
			}
		});
        dol.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
            	if (czlowiek == null) return;
                czlowiek.setKierunek(Kierunki.S);
            }
        });
        dol.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(KeyEvent.VK_DOWN, 0), "down");
        dol.getActionMap().put("down", new AbstractAction() {
			@Override
			public void actionPerformed(ActionEvent e) {
				dol.doClick();
			}
		});
        
        JButton zdolnosc = new JButton("Moc (M)");
        zdolnosc.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
            	if (czlowiek == null) return;
                czlowiek.aktywujZdolnosc();
            }
        });
        zdolnosc.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(KeyEvent.VK_M, 0), "m");
        zdolnosc.getActionMap().put("m", new AbstractAction() {
			@Override
			public void actionPerformed(ActionEvent e) {
				zdolnosc.doClick();
			}
		});
        
        prawo.setPreferredSize(new Dimension(110, 40));
        lewo.setPreferredSize(new Dimension(110, 40));
        gora.setPreferredSize(new Dimension(110, 40));
        dol.setPreferredSize(new Dimension(110, 40));
        zdolnosc.setPreferredSize(new Dimension(110, 40));
        
        GridBagConstraints strzalki = new GridBagConstraints();
        
        strzalki.gridx = 0;
        strzalki.gridy = 0;
        strzalkiPanel.add(zdolnosc, strzalki);
        strzalki.gridy = 1;
        strzalkiPanel.add(lewo, strzalki);
        strzalki.gridx = 1;
        strzalkiPanel.add(dol, strzalki);
        strzalki.gridx = 2;
        strzalkiPanel.add(prawo, strzalki);
        strzalki.gridx = 1;
        strzalki.gridy = 0;
        strzalkiPanel.add(gora, strzalki);
        
        return strzalkiPanel;
	}
	
	@Override
	public void stworzOknoSwiata() {
		pola = new JLabel[wysokosc][szerokosc];
		
		stworzPola();
		
		JPanel strzalkiPanel = stworzPanelZeStrzalkami();
		JPanel komunikatyPanel = stworzPanelKomunikatow();	
		
		JPanel prawyPanel = new JPanel(new BorderLayout());
		prawyPanel.add(komunikatyPanel, BorderLayout.CENTER);
		prawyPanel.add(strzalkiPanel, BorderLayout.SOUTH);
		
		JPanel stanPanel = stworzPanelStanu();
		
		JScrollPane przewijanaPlansza = new JScrollPane(plansza);
		
		JLabel podpis = new JLabel("Adrian Dybowski 193483, Informatyka, 2. semestr, PO projekt 2");
		
		JPanel panel = new JPanel(new BorderLayout());
		panel.add(podpis, BorderLayout.NORTH);
		panel.add(przewijanaPlansza, BorderLayout.CENTER);
		panel.add(prawyPanel, BorderLayout.EAST);
		panel.add(stanPanel, BorderLayout.WEST);
		
		oknoSwiata = new JFrame("Wirtualny Swiat");
		oknoSwiata.add(panel);
		oknoSwiata.pack();
		oknoSwiata.setVisible(true);
		
		rysujSwiat();
	}
	
	@Override
	public void rysujSwiat() {
		for (int i = 0; i < wysokosc; i++) {
			for (int j = 0; j < szerokosc; j++) {
				pola[i][j].setIcon(null);
				
				if (poleZajete(new Wspolrzedne2D(j, i))) {
					mapaOrganizmow[i][j].rysowanie(pola[i][j]);
				}
			}
		}
		
		wypiszKomunikaty();
		komunikaty.clear();
		
		wypiszStanCzlowieka();
		
		oknoSwiata.repaint();
	}	

	@Override
	public HashSet<Wspolrzedne2D> sasiedniePola(Wspolrzedne2D polozenie) {
		HashSet<Wspolrzedne2D> pola = new HashSet<Wspolrzedne2D>();
		
		if (polozenie.getX() > 0) pola.add(new Wspolrzedne2D(polozenie.getX() - 1, polozenie.getY()));
		if (polozenie.getY() > 0) pola.add(new Wspolrzedne2D(polozenie.getX(), polozenie.getY() - 1));
		
		if (polozenie.getX() < szerokosc - 1) pola.add(new Wspolrzedne2D(polozenie.getX() + 1, polozenie.getY()));
		if (polozenie.getY() < wysokosc - 1) pola.add(new Wspolrzedne2D(polozenie.getX(), polozenie.getY() + 1));
		
		return pola;
	}

	@Override
	public Wspolrzedne2D poleWKierunku(Wspolrzedne2D polozenie, Kierunki kierunek) {
		switch (kierunek) {
		case N:
			if (polozenie.getY() > 0) return new Wspolrzedne2D(polozenie.getX(), polozenie.getY() - 1);
			break;
		case S:
			if (polozenie.getY() < wysokosc - 1) return new Wspolrzedne2D(polozenie.getX(), polozenie.getY() + 1);
			break;
		case E:
			if (polozenie.getX() < szerokosc - 1) return new Wspolrzedne2D(polozenie.getX() + 1, polozenie.getY());
			break;
		case W:
			if (polozenie.getX() > 0) return new Wspolrzedne2D(polozenie.getX() - 1, polozenie.getY());
			break;
		default:
			return polozenie;
		}
		
		return polozenie;
	}

	@Override
	public void zapiszDoPliku() {
		String nazwaPliku = "";
        while (nazwaPliku.isEmpty()) {
            nazwaPliku = JOptionPane.showInputDialog(null, "Podaj nazwe pliku do zapisu:");
        }

        String sciezkaFolderu = "zapisaneGry/";
        File folder = new File(sciezkaFolderu);
        if (!folder.exists()) {
            if (folder.mkdir()) {
            }
            else {
                JOptionPane.showMessageDialog(null, "Nie udalo sie utworzyc folderu z zapisami.", "Blad", JOptionPane.ERROR_MESSAGE);
                return;
            }
        }

        String sciezkaPliku = sciezkaFolderu + nazwaPliku + ".txt";
        try {
            FileWriter zapisDoPliku = new FileWriter(sciezkaPliku);

            zapisDoPliku.write("Kwadratowy"+ "\n" + wysokosc + "\n" + szerokosc + "\n" + tura + "\n" + wiek + "\n" + organizmy.size() + "\n");
            for (Organizm o : organizmy)
                zapisDoPliku.write(o.toString());

            zapisDoPliku.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
	}

	@Override
	public void dodajPoczatkoweOrganizmy() {
		int x = (int) (losowosc.nextDouble() * szerokosc);
	    int y = (int) (losowosc.nextDouble() * wysokosc);

	    Czlowiek c = new Czlowiek(this, new Wspolrzedne2D(x, y));
	    czlowiek = c;
	    dodajDziecko(c);

	    for (int i = 0; i < wysokosc; i++) {
	        for (int j = 0; j < szerokosc; j++) {
	            if (poleZajete(new Wspolrzedne2D(j, i))) continue;
	            if (szansaTrafiona(0.80)) continue;
	            dodajLosowyOrganizm(new Wspolrzedne2D(j, i));
	        }
	    }
	}
}
