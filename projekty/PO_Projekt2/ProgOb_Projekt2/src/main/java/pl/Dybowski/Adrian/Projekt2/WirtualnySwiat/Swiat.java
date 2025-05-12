package pl.Dybowski.Adrian.Projekt2.WirtualnySwiat;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Random;
import java.util.Vector;

import javax.swing.AbstractAction;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.KeyStroke;

import pl.Dybowski.Adrian.Projekt2.WirtualnySwiat.Rosliny.BarszczSosnowskiego;
import pl.Dybowski.Adrian.Projekt2.WirtualnySwiat.Rosliny.Guarana;
import pl.Dybowski.Adrian.Projekt2.WirtualnySwiat.Rosliny.Mlecz;
import pl.Dybowski.Adrian.Projekt2.WirtualnySwiat.Rosliny.Trawa;
import pl.Dybowski.Adrian.Projekt2.WirtualnySwiat.Rosliny.WilczeJagody;
import pl.Dybowski.Adrian.Projekt2.WirtualnySwiat.Zwierzeta.Antylopa;
import pl.Dybowski.Adrian.Projekt2.WirtualnySwiat.Zwierzeta.Czlowiek;
import pl.Dybowski.Adrian.Projekt2.WirtualnySwiat.Zwierzeta.Lis;
import pl.Dybowski.Adrian.Projekt2.WirtualnySwiat.Zwierzeta.Owca;
import pl.Dybowski.Adrian.Projekt2.WirtualnySwiat.Zwierzeta.Wilk;
import pl.Dybowski.Adrian.Projekt2.WirtualnySwiat.Zwierzeta.Zolw;

public abstract class Swiat {
	protected int tura;
	protected int wiek;	// liczy ilosc stworzonych organizmow - przydziela im tak wiek
	
	protected Vector<Organizm> organizmy;
	protected Organizm[][] mapaOrganizmow;
	
	protected ArrayList<String> komunikaty;
	protected JTextArea listaKomunikatow;	// pole gdzie komunikaty sa wypisywane
	protected JLabel naglowekKomunikatow;	// nr minionej tury
	
	protected JLabel stanCzlowieka;			// polozenie i stan mocy czlowieka
	
	protected Czlowiek czlowiek;
	
	public Random losowosc;
	
	protected JPanel plansza;
	protected JLabel[][] pola;				// pola planszy
	protected JFrame oknoSwiata;
	
	protected Launcher launcher;			// potrzebne w przypadku wywolywania nowej gry
	
	public class SluchaczPola extends MouseAdapter {
		private int x, y;
		
		public SluchaczPola(int x, int y) {
			this.x = x;
			this.y = y;
		}
		
		@Override
		public void mouseClicked(MouseEvent e) {
			if (!poleNaPlanszy(new Wspolrzedne2D(x,y))) return;
            dodajRecznie(new Wspolrzedne2D(x, y));
        }
	}
	
	public Swiat(Launcher launcher) {
		tura = 0;
		wiek = 0;
		losowosc = new Random();
		organizmy = new Vector<Organizm>();
		this.launcher = launcher;
		
		komunikaty = new ArrayList<String>();
	}
	
	public JFrame getOknoSwiata() {
		return oknoSwiata;
	}
	
	public void wykonajTure() {		
		Collections.sort(organizmy);
		listaKomunikatow.setText("");
		
		int liczbaOrganizmow = organizmy.size();
		for (int i = 0; i < liczbaOrganizmow; i++) {
			organizmy.get(i).akcja();
		}
		
		usunMartweOrganizmy();
		
		if (czlowiek != null && !czlowiek.isZyje()) czlowiek = null;
		
		if (czlowiek != null) czlowiek.odswiezZdolnosc();
		
		tura++;
	}

	public abstract void stworzOknoSwiata();
	public abstract void rysujSwiat();
	
	//----------------------------------------POLA----------------------------------------------------
	public abstract HashSet<Wspolrzedne2D> sasiedniePola(Wspolrzedne2D polozenie);
	
	public HashSet<Wspolrzedne2D> sasiednieWolnePola(Wspolrzedne2D polozenie) {
		HashSet<Wspolrzedne2D> pola = sasiedniePola(polozenie);
		
		HashSet<Wspolrzedne2D> wolnePola = new HashSet<Wspolrzedne2D>();
		
		for (Wspolrzedne2D wspolrzedne : pola) {
			if (!poleZajete(wspolrzedne)) wolnePola.add(wspolrzedne);
		}

		return wolnePola;
	}
	
	public Wspolrzedne2D losoweSasiedniePole(Wspolrzedne2D polozenie) {
		return losowePole(sasiedniePola(polozenie));
	}

	public Wspolrzedne2D losoweSasiednieWolnePole(Wspolrzedne2D polozenie) {
		return losowePole(sasiednieWolnePola(polozenie));
	}
	
	public Wspolrzedne2D losowePole(HashSet<Wspolrzedne2D> polozenia) {
		if (polozenia.size() == 0) return null;
		
		Iterator<Wspolrzedne2D> iterator = polozenia.iterator();
		
		int indeks = losowosc.nextInt(polozenia.size());
		
		for (int i = 0; i < indeks; i++) {
			iterator.next();
		}
		
		return iterator.next();
	}
	
	public boolean poleZajete(Wspolrzedne2D polozenie) {
		return mapaOrganizmow[polozenie.getY()][polozenie.getX()] != null;
	}
	
	public Organizm organizmNaPolu(Wspolrzedne2D polozenie) {
		return mapaOrganizmow[polozenie.getY()][polozenie.getX()];
	}

	public void wyczyscPoleMapy(Wspolrzedne2D polozenie) {
		mapaOrganizmow[polozenie.getY()][polozenie.getX()] = null;
	}
	public void usunOrganizmZPola(Organizm organizm) {
		mapaOrganizmow[organizm.getPolozenie().getY()][organizm.getPolozenie().getX()] = null;
	}
	
	public void dodajOrganizmDoPola(Organizm organizm) {
		if (!(poleZajete(organizm.getPolozenie()))) {
			mapaOrganizmow[organizm.getPolozenie().getY()][organizm.getPolozenie().getX()] = organizm;
		}
	}
	
	// dodawanie organizmu po kliknieciu pola
	public void dodajRecznie(Wspolrzedne2D polozenie) {
		if (poleZajete(polozenie)) return;
		
		JFrame oknoWyboru = new JFrame();
		JDialog dialog = new JDialog(oknoWyboru, "Wybierz organizm do dodania", true);
		
		JRadioButton wilkRadio = new JRadioButton("Wilk");
        JRadioButton owcaRadio = new JRadioButton("Owca");
        JRadioButton lisRadio = new JRadioButton("Lis");
        JRadioButton zolwRadio = new JRadioButton("Zolw");
        JRadioButton antylopaRadio = new JRadioButton("Antylopa");
        
        JRadioButton trawaRadio = new JRadioButton("Trawa");
        JRadioButton mleczRadio = new JRadioButton("Mlecz");
        JRadioButton guaranaRadio = new JRadioButton("Guarana");
        JRadioButton wilczeJagodyRadio = new JRadioButton("Wilcze Jagody");
        JRadioButton barszczSosnowskiegoRadio = new JRadioButton("Barszcz Sosnowskiego");
        
        JRadioButton czlowiekRadio = new JRadioButton("Czlowiek");
		
        ButtonGroup przyciski = new ButtonGroup();
        przyciski.add(wilkRadio);
        przyciski.add(owcaRadio);
        przyciski.add(lisRadio);
        przyciski.add(zolwRadio);
        przyciski.add(antylopaRadio);
        przyciski.add(trawaRadio);
        przyciski.add(mleczRadio);
        przyciski.add(guaranaRadio);
        przyciski.add(wilczeJagodyRadio);
        przyciski.add(barszczSosnowskiegoRadio);
        przyciski.add(czlowiekRadio);
        
        JPanel panelPrzyciskow = new JPanel();
        panelPrzyciskow.add(wilkRadio);
        panelPrzyciskow.add(owcaRadio);
        panelPrzyciskow.add(lisRadio);
        panelPrzyciskow.add(zolwRadio);
        panelPrzyciskow.add(antylopaRadio);
        panelPrzyciskow.add(trawaRadio);
        panelPrzyciskow.add(mleczRadio);
        panelPrzyciskow.add(guaranaRadio);
        panelPrzyciskow.add(wilczeJagodyRadio);
        panelPrzyciskow.add(barszczSosnowskiegoRadio);
        panelPrzyciskow.add(czlowiekRadio);
		
		JButton potwierdzPrzycisk = new JButton("Potwierdz");
		
		Swiat obecnySwiat = this;
		potwierdzPrzycisk.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                	 if (wilkRadio.isSelected()) dodajDziecko(new Wilk(obecnySwiat, polozenie));
                else if (owcaRadio.isSelected()) dodajDziecko(new Owca(obecnySwiat, polozenie));
                else if (lisRadio.isSelected()) dodajDziecko(new Lis(obecnySwiat, polozenie));
                else if (zolwRadio.isSelected()) dodajDziecko(new Zolw(obecnySwiat, polozenie));
                else if (antylopaRadio.isSelected()) dodajDziecko(new Antylopa(obecnySwiat, polozenie));
                else if (trawaRadio.isSelected()) dodajDziecko(new Trawa(obecnySwiat, polozenie));
                else if (mleczRadio.isSelected()) dodajDziecko(new Mlecz(obecnySwiat, polozenie));
                else if (guaranaRadio.isSelected()) dodajDziecko(new Guarana(obecnySwiat, polozenie));
                else if (wilczeJagodyRadio.isSelected()) dodajDziecko(new WilczeJagody(obecnySwiat, polozenie));
                else if (barszczSosnowskiegoRadio.isSelected()) dodajDziecko(new BarszczSosnowskiego(obecnySwiat, polozenie));
                else if (czlowiekRadio.isSelected()) {
                	if (czlowiek == null) {
                		Czlowiek c = new Czlowiek(obecnySwiat, polozenie);
                		czlowiek = c;
                		dodajDziecko(c);
                	}
                }
                
            	oknoWyboru.dispose();
            	rysujSwiat();
            }
        });
        
        JPanel panel = new JPanel(new GridLayout(2,1));
        
        panel.add(panelPrzyciskow);
        panel.add(potwierdzPrzycisk);
        
        dialog.add(panel);
        dialog.pack();
        dialog.setVisible(true);
	}

	// do poruszania sie czlowieka
	public abstract Wspolrzedne2D poleWKierunku(Wspolrzedne2D polozenie, Kierunki kierunek);
	
	public Boolean poleNaPlanszy(Wspolrzedne2D polozenie) {
		return true;
	}
	//----------------------------------------ORGANIZMY-----------------------------------------------
	public int nadajWiek() {
		return wiek++;
	}

	public void usunMartweOrganizmy() {
		Iterator<Organizm> iterator = organizmy.iterator();
		
		while (iterator.hasNext()) {
			Organizm organizm = iterator.next();
			
			if (!organizm.isZyje()) {
				iterator.remove();
			}
		}
	}
	
	public void dodajDziecko(Organizm dziecko) {
		organizmy.add(dziecko);
		dodajOrganizmDoPola(dziecko);
	}
	
	public void setCzlowiek(Czlowiek czlowiek) {
		this.czlowiek = czlowiek;
	}
	
	public void unsetCzlowiek() {
		czlowiek = null;
	}
	
	public abstract void dodajPoczatkoweOrganizmy();
	
	public void dodajLosowyOrganizm(Wspolrzedne2D polozenie) {
		int wybor = losowosc.nextInt() % 10; // nie mozemy wygenerowac kolejnego czlowieka
	    Organizm o;

	    switch (wybor) {
	        case 0:
	            o = new Wilk(this, polozenie);
	            break;
	        case 1:
	            o = new Owca(this, polozenie);
	            break;
	        case 2:
	            o = new Lis(this, polozenie);
	            break;
	        case 3:
	            o = new Zolw(this, polozenie);
	            break;
	        case 4:
	            o = new Antylopa(this, polozenie);
	            break;
	        case 5:
	            o = new Trawa(this, polozenie);
	            break;
	        case 6:
	            o = new Mlecz(this, polozenie);
	            break;
	        case 7:
	            o = new Guarana(this, polozenie);
	            break;
	        case 8:
	            o = new WilczeJagody(this, polozenie);
	            break;
	        case 9:
	        default:
	            o = new BarszczSosnowskiego(this, polozenie);
	            break;
	    }
	    
	    dodajDziecko(o);
	}
	//----------------------------------------LOSOWOSC------------------------------------------------
	public boolean szansaTrafiona(double szansa) {
		if (losowosc.nextDouble() < szansa || szansa >= 1) return true;
		else return false;
	}
	//--------------------------------------KOMUNIKATY------------------------------------------------
	public void dodajKomunikat(Organizm organizm, RodzajeKomunikatow akcja) {
		switch (akcja) {
			case rodziSie:
				komunikaty.add("Na polu "+organizm.getPolozenie().toString()+" rodzi sie "+organizm.getClass().getSimpleName());
				break;
			case wyrasta:
				komunikaty.add("Na polu "+organizm.getPolozenie().toString()+" wyrasta "+organizm.getClass().getSimpleName());
				break;
			case aktywujeMoc:
				komunikaty.add("Na polu "+organizm.getPolozenie().toString()+" "+organizm.getClass().getSimpleName()+" aktywuje moc");
				break;	
		}
	}
	
	public void dodajKomunikat(Organizm napastnik, Organizm ofiara, RodzajeKomunikatow akcja) {
		switch (akcja) {
		case zabija:
			komunikaty.add("Na polu "+ofiara.getPolozenie().toString()+" "+napastnik.getClass().getSimpleName()+" zabija "+ofiara.getClass().getSimpleName());
			break; 
		case broniSie:
			komunikaty.add("Na polu "+ofiara.getPolozenie().toString()+" "+ofiara.getClass().getSimpleName()+" broni sie przed "+napastnik.getClass().getSimpleName());
			break; 
		case zjada:
			komunikaty.add("Na polu "+ofiara.getPolozenie().toString()+" "+napastnik.getClass().getSimpleName()+" zjada "+ofiara.getClass().getSimpleName());
			break; 
		case zatruwaSie:
			komunikaty.add("Na polu "+ofiara.getPolozenie().toString()+" "+napastnik.getClass().getSimpleName()+" zatruwa sie "+ofiara.getClass().getSimpleName());
			break; 
		case ucieka:
			komunikaty.add("Na polu "+ofiara.getPolozenie().toString()+" "+ofiara.getClass().getSimpleName()+" ucieka przed "+napastnik.getClass().getSimpleName());
			break; 
		case odbija:
			komunikaty.add("Na polu "+ofiara.getPolozenie().toString()+" "+ofiara.getClass().getSimpleName()+" odbija atak "+napastnik.getClass().getSimpleName());
			break; 
		}
		
	}
	
	public void wypiszKomunikaty() {
		if (tura != 0) naglowekKomunikatow.setText("Wydarzenia w "+tura+" turze:");
		else naglowekKomunikatow.setText("Tu wyswietla sie wydarzenia:");
		
		for (String komunikat : komunikaty) {
			listaKomunikatow.append(komunikat+"\n");
		}
		if (komunikaty.isEmpty() && listaKomunikatow.getText() == "") listaKomunikatow.append("Spokojne zycie...");
	}
	
	public void wypiszStanCzlowieka() {
		if (czlowiek == null || !czlowiek.isZyje()) {
			stanCzlowieka.setText("Czlowiek zostal pokonany");
			return;
		}
		
		if (czlowiek.isZdolnoscAktywna()) {
			stanCzlowieka.setText(czlowiek.getPolozenie().toString()+" Zdolnosc czlowieka aktywna przez jeszcze "+czlowiek.getTrwanieZdolnosci()+" tur");
			return;
		}
		
		if (czlowiek.getOdnowienieZdolnosci() == 0) {
			stanCzlowieka.setText(czlowiek.getPolozenie().toString()+" Zdolnosc czlowieka gotowa do aktywacji");
			return;
		}
		
		stanCzlowieka.setText(czlowiek.getPolozenie().toString()+" Zdolnosc czlowieka bedzie gotowa za "+czlowiek.getOdnowienieZdolnosci()+" tur");
	}
	//----------------------------------------ZAPIS---------------------------------------------------
	public abstract void zapiszDoPliku();
	
	public void setWiek(int wiek) {
		this.wiek = wiek;
	}
	
	public void setTura(int tura) {
		this.tura = tura;
	}
	//----------------------------------------OKNO----------------------------------------------------
	public JButton stworzPrzyciskNowejTury() {
		JButton nowaTura = new JButton("Nowa tura (E)");
		nowaTura.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                wykonajTure();
                rysujSwiat();
            }
        });
		nowaTura.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(KeyEvent.VK_E, 0), "e");
		nowaTura.getActionMap().put("e", new AbstractAction() {
			@Override
			public void actionPerformed(ActionEvent e) {
				nowaTura.doClick();
			}
		});
		
		return nowaTura;
	}
	
	public JPanel stworzPanelStanu() {
		JPanel stanPanel = new JPanel(new GridLayout(5,1));
		
		JButton nowaTura = stworzPrzyciskNowejTury();
		
		JButton nowaGra = new JButton("Nowa gra (N)");
		
		JButton zapisz = new JButton("Zapisz (S)");
		JButton wczytaj = new JButton("Wczytaj (L)");
		
		JButton wyjdz = new JButton("Wyjdz (Q)");
		
		zapisz.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                zapiszDoPliku();
            }
        });
		zapisz.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(KeyEvent.VK_S, 0), "s");
		zapisz.getActionMap().put("s", new AbstractAction() {
			@Override
			public void actionPerformed(ActionEvent e) {
				zapisz.doClick();
			}
		});
		wczytaj.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                launcher.wczytajGre();
            }
        });
		wczytaj.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(KeyEvent.VK_L, 0), "l");
		wczytaj.getActionMap().put("l", new AbstractAction() {
			@Override
			public void actionPerformed(ActionEvent e) {
				wczytaj.doClick();
			}
		});
		
		wyjdz.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                oknoSwiata.dispose();
            }
        });
		wyjdz.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(KeyEvent.VK_Q, 0), "q");
		wyjdz.getActionMap().put("q", new AbstractAction() {
			@Override
			public void actionPerformed(ActionEvent e) {
				wyjdz.doClick();
			}
		});
		
		nowaGra.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                oknoSwiata.dispose();
                launcher.wybierzSwiat();
            }
        });
		nowaGra.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(KeyEvent.VK_N, 0), "n");
		nowaGra.getActionMap().put("n", new AbstractAction() {
			@Override
			public void actionPerformed(ActionEvent e) {
				nowaGra.doClick();			
			}
		});
		
		stanPanel.add(wyjdz);
		stanPanel.add(nowaGra);
		stanPanel.add(wczytaj);
		stanPanel.add(zapisz);
		stanPanel.add(nowaTura);
		
		return stanPanel;
	}
	
	public JPanel stworzPanelKomunikatow() {
		JPanel komunikatyPanel = new JPanel(new BorderLayout());
		
		naglowekKomunikatow = new JLabel();
		stanCzlowieka = new JLabel();

		listaKomunikatow = new JTextArea(6, 12);
		listaKomunikatow.setEditable(false);

        JScrollPane przewijaneKomunikaty = new JScrollPane(listaKomunikatow);
        
        komunikatyPanel.add(naglowekKomunikatow, BorderLayout.NORTH);
        komunikatyPanel.add(przewijaneKomunikaty, BorderLayout.CENTER);
        komunikatyPanel.add(stanCzlowieka, BorderLayout.SOUTH);
        
        return komunikatyPanel;
	}

	public abstract JPanel stworzPanelZeStrzalkami();

	public abstract void stworzPola();
}
