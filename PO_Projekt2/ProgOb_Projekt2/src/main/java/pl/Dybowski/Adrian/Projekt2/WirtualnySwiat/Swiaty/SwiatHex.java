package pl.Dybowski.Adrian.Projekt2.WirtualnySwiat.Swiaty;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Polygon;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashSet;

import javax.swing.AbstractAction;
import javax.swing.BorderFactory;
import javax.swing.Icon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.KeyStroke;

import pl.Dybowski.Adrian.Projekt2.WirtualnySwiat.Kierunki;
import pl.Dybowski.Adrian.Projekt2.WirtualnySwiat.Launcher;
import pl.Dybowski.Adrian.Projekt2.WirtualnySwiat.Organizm;
import pl.Dybowski.Adrian.Projekt2.WirtualnySwiat.Swiat;
import pl.Dybowski.Adrian.Projekt2.WirtualnySwiat.Wspolrzedne2D;
import pl.Dybowski.Adrian.Projekt2.WirtualnySwiat.Swiat.SluchaczPola;
import pl.Dybowski.Adrian.Projekt2.WirtualnySwiat.Zwierzeta.Czlowiek;

public class SwiatHex extends Swiat {

	private int promien;
	private int wysokosc;
	private int szerokosc;
	
	private class PoleHex extends JLabel {		
		@Override
		protected void paintComponent(Graphics g) {
			super.paintComponent(g);
			Graphics2D g2 = (Graphics2D) g;
			
			int wys = 32;
			int szer = wys;
			
			int[] punktyX = {szer/4, szer*3/4, szer, szer*3/4, szer/4, 0};
			int[] punktyY = {0, 0, wys/2, wys, wys, wys/2};
			
			Polygon szesciokat = new Polygon(punktyX, punktyY, 6);
			
			g2.setColor(Color.BLACK);
			
			g2.draw(szesciokat);
		}
	}
	
	public SwiatHex(int promien, Launcher launcher, Boolean zapis) {
		super(launcher);
		this.promien = promien;
		
		// wspolrzedne w planszy szesciokatnej sa zapisane w postaci dwoch liczb
		// jedna okresla wysokosc na pionowej osi w ktorej pole sie znajduje
		// druga okresla polozenie na jednej z dwoch ukosnych osi (liczone od jednego z koncow osi)
		
		this.wysokosc = 2*promien - 1;
		this.szerokosc = 2*promien - 1;
		
		mapaOrganizmow = new Organizm[wysokosc][szerokosc];
		
		if (!zapis) dodajPoczatkoweOrganizmy();
		
		stworzOknoSwiata();
	}

	@Override
	public void stworzOknoSwiata() {
		pola = new PoleHex[wysokosc][szerokosc];
		
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
		listaKomunikatow.setText("");
		
		for (int i = 0; i < wysokosc; i++) {
			for (int j = 0; j < szerokosc; j++) {
				if (poleNaPlanszy(new Wspolrzedne2D(j, i))) {
					pola[i][j].setIcon(null);
					
					if (poleZajete(new Wspolrzedne2D(j, i))) {
						mapaOrganizmow[i][j].rysowanie(pola[i][j]);
					}
				}
			}
		}
		
		wypiszKomunikaty();
		komunikaty.clear();
		
		wypiszStanCzlowieka();
		
		oknoSwiata.repaint();
	}
	
	public int hexDystans(Wspolrzedne2D w1, Wspolrzedne2D w2) {
		return (Math.abs(w1.getX() - w2.getX()) + Math.abs(w1.getX() + w1.getY() - w2.getX() - w2.getY()) + Math.abs(w1.getY() - w2.getY()))/2;
	}
	
	public Boolean poleNaPlanszy(Wspolrzedne2D polozenie) {
		return hexDystans(polozenie, new Wspolrzedne2D(promien-1, promien-1)) < promien;
	}

	@Override
	public HashSet<Wspolrzedne2D> sasiedniePola(Wspolrzedne2D polozenie) {
		HashSet<Wspolrzedne2D> pola = new HashSet<Wspolrzedne2D>();
		
		Wspolrzedne2D cel;
		
		cel = new Wspolrzedne2D(polozenie.getX(), polozenie.getY()-1);
		if (poleNaPlanszy(cel)) pola.add(cel);
		
		cel = new Wspolrzedne2D(polozenie.getX()+1, polozenie.getY()-1);
		if (poleNaPlanszy(cel)) pola.add(cel);
		
		cel = new Wspolrzedne2D(polozenie.getX()+1, polozenie.getY());
		if (poleNaPlanszy(cel)) pola.add(cel);
		
		cel = new Wspolrzedne2D(polozenie.getX(), polozenie.getY()+1);
		if (poleNaPlanszy(cel)) pola.add(cel);
		
		cel = new Wspolrzedne2D(polozenie.getX()-1, polozenie.getY()+1);
		if (poleNaPlanszy(cel)) pola.add(cel);
		
		cel = new Wspolrzedne2D(polozenie.getX()-1, polozenie.getY());
		if (poleNaPlanszy(cel)) pola.add(cel);
		
		return pola;
	}

	@Override
	public Wspolrzedne2D poleWKierunku(Wspolrzedne2D polozenie, Kierunki kierunek) {
		Wspolrzedne2D cel;
		
		switch (kierunek) {
		case N:
			cel = new Wspolrzedne2D(polozenie.getX(), polozenie.getY()-1);
			if (poleNaPlanszy(cel)) return cel;
			break;
		case NE:
			cel = new Wspolrzedne2D(polozenie.getX()+1, polozenie.getY()-1);
			if (poleNaPlanszy(cel)) return cel;
			break;
		case SE:
			cel = new Wspolrzedne2D(polozenie.getX()+1, polozenie.getY());
			if (poleNaPlanszy(cel)) return cel;
			break;
		case S:
			cel = new Wspolrzedne2D(polozenie.getX(), polozenie.getY()+1);
			if (poleNaPlanszy(cel)) return cel;
			break;
		case SW:
			cel = new Wspolrzedne2D(polozenie.getX()-1, polozenie.getY()+1);
			if (poleNaPlanszy(cel)) return cel;
			break;
		case NW:
			cel = new Wspolrzedne2D(polozenie.getX()-1, polozenie.getY());
			if (poleNaPlanszy(cel)) return cel;
			break;
		}
		
		return polozenie;
	}

	@Override
	public void zapiszDoPliku() {
		String nazwaPliku = "";
        while (nazwaPliku.isEmpty()) {
            nazwaPliku = JOptionPane.showInputDialog(null, "Podaj nazwe pliku do zapisania:");
        }

        String sciezkaFolderu = "zapisaneGry/";
        File folder = new File(sciezkaFolderu);
        if (!folder.exists()) {
            if (folder.mkdir()) {
            }
            else {
            	JOptionPane.showMessageDialog(null, "Nie udało się utworzyć folderu z zapisami.", "Blad", JOptionPane.ERROR_MESSAGE);
                return;
            }
        }

        String sciezkaPliku = sciezkaFolderu + nazwaPliku + ".txt";
        try {
            FileWriter zapisDoPliku = new FileWriter(sciezkaPliku);

            zapisDoPliku.write("Hex"+ "\n" + promien + "\n" + tura + "\n" + wiek + "\n" + organizmy.size() + "\n");
            for (Organizm o : organizmy)
                zapisDoPliku.write(o.toString());

            zapisDoPliku.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
	}

	@Override
	public JPanel stworzPanelZeStrzalkami() {
		JPanel strzalkiPanel = new JPanel(new GridBagLayout());
		
		JButton n = new JButton("N (U)");  
		JButton ne = new JButton("NE (I)"); 
		JButton se = new JButton("SE (K)"); 
		JButton s = new JButton("S (J)"); 
		JButton sw = new JButton("SW (H)"); 
		JButton nw = new JButton("NW (Y)"); 
        
        n.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
            	if (czlowiek == null) return;
                czlowiek.setKierunek(Kierunki.N);
            }
        });
        n.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(KeyEvent.VK_U, 0), "u");
        n.getActionMap().put("u", new AbstractAction() {
			@Override
			public void actionPerformed(ActionEvent e) {
				n.doClick();
			}
		});
		
        ne.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
            	if (czlowiek == null) return;
                czlowiek.setKierunek(Kierunki.NE);
            }
        });
        ne.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(KeyEvent.VK_I, 0), "i");
        ne.getActionMap().put("i", new AbstractAction() {
			@Override
			public void actionPerformed(ActionEvent e) {
				ne.doClick();
			}
		});

        se.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
            	if (czlowiek == null) return;
                czlowiek.setKierunek(Kierunki.SE);
            }
        });
        se.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(KeyEvent.VK_K, 0), "k");
        se.getActionMap().put("k", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                se.doClick();
            }
        });

        s.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
            	if (czlowiek == null) return;
                czlowiek.setKierunek(Kierunki.S);
            }
        });
        s.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(KeyEvent.VK_J, 0), "j");
        s.getActionMap().put("j", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                s.doClick();
            }
        });

        sw.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
            	if (czlowiek == null) return;
                czlowiek.setKierunek(Kierunki.SW);
            }
        });
        sw.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(KeyEvent.VK_H, 0), "h");
        sw.getActionMap().put("h", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                sw.doClick();
            }
        });

        nw.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
            	if (czlowiek == null) return;
                czlowiek.setKierunek(Kierunki.NW);
            }
        });
        nw.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(KeyEvent.VK_Y, 0), "y");
        nw.getActionMap().put("y", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                nw.doClick();
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
        
        GridBagConstraints strzalki = new GridBagConstraints();
        
        strzalki.gridx = 1;
        strzalki.gridy = 0;
        strzalkiPanel.add(zdolnosc, strzalki);
        
        strzalki.gridy = 1;
        strzalki.gridx = 0; 
        strzalkiPanel.add(nw, strzalki);
        strzalki.gridx = 1;
        strzalkiPanel.add(n, strzalki);
        strzalki.gridx = 2;
        strzalkiPanel.add(ne, strzalki);
        
        strzalki.gridy = 2;
        strzalki.gridx = 0; 
        strzalkiPanel.add(sw, strzalki);
        strzalki.gridx = 1;
        strzalkiPanel.add(s, strzalki);
        strzalki.gridx = 2;
        strzalkiPanel.add(se, strzalki);
        
        return strzalkiPanel;
        
	}

	@Override
	public void stworzPola() {
		plansza = new JPanel(new GridLayout(wysokosc, szerokosc));
		plansza.setBackground(Color.LIGHT_GRAY);
		
		for (int i = 0; i < wysokosc; i++) {
			for (int j = 0; j < szerokosc; j++) {
				PoleHex pole = new PoleHex();
				
				pole.setOpaque(false);
				pole.setPreferredSize(new Dimension(32,32));
				
				pole.addMouseListener(new SluchaczPola(j, i));
				
				pola[i][j] = pole;
				
				if (!poleNaPlanszy(new Wspolrzedne2D(j,i))) plansza.add(new JLabel());
				else plansza.add(pole);
			}
		}
	}

	@Override
	public void dodajPoczatkoweOrganizmy() {
		Czlowiek c = new Czlowiek(this, new Wspolrzedne2D(promien - 1, promien - 1));
	    czlowiek = c;
	    dodajDziecko(c);

	    for (int i = 0; i < wysokosc; i++) {
	        for (int j = 0; j < szerokosc; j++) {
	        	if (!poleNaPlanszy(new Wspolrzedne2D(j, i))) continue;
	            if (poleZajete(new Wspolrzedne2D(j, i))) continue;
	            if (szansaTrafiona(0.80)) continue;
	            dodajLosowyOrganizm(new Wspolrzedne2D(j, i));
	        }
	    }		
	}

}
