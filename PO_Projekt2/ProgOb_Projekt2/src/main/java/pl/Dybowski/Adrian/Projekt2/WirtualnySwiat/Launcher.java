package pl.Dybowski.Adrian.Projekt2.WirtualnySwiat;

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

import pl.Dybowski.Adrian.Projekt2.WirtualnySwiat.Rosliny.BarszczSosnowskiego;
import pl.Dybowski.Adrian.Projekt2.WirtualnySwiat.Rosliny.Guarana;
import pl.Dybowski.Adrian.Projekt2.WirtualnySwiat.Rosliny.Mlecz;
import pl.Dybowski.Adrian.Projekt2.WirtualnySwiat.Rosliny.Trawa;
import pl.Dybowski.Adrian.Projekt2.WirtualnySwiat.Rosliny.WilczeJagody;
import pl.Dybowski.Adrian.Projekt2.WirtualnySwiat.Swiaty.SwiatHex;
import pl.Dybowski.Adrian.Projekt2.WirtualnySwiat.Swiaty.SwiatKwadratowy;
import pl.Dybowski.Adrian.Projekt2.WirtualnySwiat.Zwierzeta.Antylopa;
import pl.Dybowski.Adrian.Projekt2.WirtualnySwiat.Zwierzeta.Czlowiek;
import pl.Dybowski.Adrian.Projekt2.WirtualnySwiat.Zwierzeta.Lis;
import pl.Dybowski.Adrian.Projekt2.WirtualnySwiat.Zwierzeta.Owca;
import pl.Dybowski.Adrian.Projekt2.WirtualnySwiat.Zwierzeta.Wilk;
import pl.Dybowski.Adrian.Projekt2.WirtualnySwiat.Zwierzeta.Zolw;

public class Launcher {

	private Swiat swiat;
	
	public Launcher() {
		wybierzSwiat();
	}
	
	public void wybierzSwiat() {
		JFrame okno = new JFrame("Wirtualny Swiat - wybierz plansze");
        okno.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel ksztaltyPanel = new JPanel();
        JRadioButton kwadratowyPrzycisk = new JRadioButton("Swiat kwadratowy");
        JRadioButton hexPrzycisk = new JRadioButton("Swiat szesciokatny");
        ButtonGroup ksztaltyGrupa = new ButtonGroup();
        ksztaltyGrupa.add(kwadratowyPrzycisk);
        ksztaltyGrupa.add(hexPrzycisk);
        ksztaltyPanel.add(kwadratowyPrzycisk);
        ksztaltyPanel.add(hexPrzycisk);

        JPanel kwadratowyPanel = new JPanel(new GridLayout(2, 2));
        JTextField szerokoscTextField = new JTextField(12);
        JTextField wysokoscTextField = new JTextField(12);
        kwadratowyPanel.add(new JLabel("Szerokosc:"));
        kwadratowyPanel.add(szerokoscTextField);
        kwadratowyPanel.add(new JLabel("Wysokosc:"));
        kwadratowyPanel.add(wysokoscTextField);

        JPanel hexPanel = new JPanel();
        JTextField promienTextField = new JTextField(12);
        hexPanel.add(new JLabel("Promien:"));
        hexPanel.add(promienTextField);

        Launcher launcher = this;
        
        JButton potwierdzButton = new JButton("Potwierdz");
        potwierdzButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int szerokosc = 0;
                int wysokosc = 0;
                int promien = 0;
                if (kwadratowyPrzycisk.isSelected()) {
                	wysokosc = Integer.parseInt(wysokoscTextField.getText());
                    szerokosc = Integer.parseInt(szerokoscTextField.getText());
                    
                    swiat = new SwiatKwadratowy(wysokosc, szerokosc, launcher, false);
                } else if (hexPrzycisk.isSelected()) {
                    promien = Integer.parseInt(promienTextField.getText());
                    
                    swiat = new SwiatHex(promien, launcher, false);
                }
                
                okno.dispose();
            }
        });

        kwadratowyPrzycisk.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                kwadratowyPanel.setVisible(true);
                hexPanel.setVisible(false);
            }
        });

        hexPrzycisk.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                kwadratowyPanel.setVisible(false);
                hexPanel.setVisible(true);
            }
        });

        kwadratowyPanel.setVisible(false);
        hexPanel.setVisible(false);

        JPanel panel = new JPanel(new GridLayout(4, 1));
        panel.add(ksztaltyPanel);
        panel.add(kwadratowyPanel);
        panel.add(hexPanel);
        panel.add(potwierdzButton);

        okno.add(panel);
        okno.pack();
        okno.setVisible(true);
	}

	public void wczytajGre() {
		String nazwaPliku = null;
		try {
		    while (nazwaPliku == null || nazwaPliku.isEmpty()) {
		        nazwaPliku = JOptionPane.showInputDialog(null, "Podaj nazwe pliku z zapisana gra:");
		    }
		    String sciezkaPliku = "zapisaneGry/" + nazwaPliku + ".txt";

		    File plikOdczytu = new File(sciezkaPliku);
		    if (!plikOdczytu.exists()) {
		        JOptionPane.showMessageDialog(null, "Zapisana gra o takiej nazwie nie istnieje!", "Blad wczytywania", JOptionPane.ERROR_MESSAGE);
		        return;
		    }

		    swiat.oknoSwiata.dispose();
		    
		    Scanner skaner = new Scanner(plikOdczytu);
	    	String rodzajSwiata = skaner.next();
	    	
	    	if (rodzajSwiata.equals("Kwadratowy")) {
	    		int wysokosc = skaner.nextInt();
	            int szerokosc = skaner.nextInt();
	            swiat = new SwiatKwadratowy(wysokosc, szerokosc, this, true);
	    	}
	    	else if (rodzajSwiata.equals("Hex")) {
	    		int promien = skaner.nextInt();
	    		swiat = new SwiatHex(promien, this, true);
	    	}
	    	else {
	    		System.out.println(rodzajSwiata+"|");
	    		JOptionPane.showMessageDialog(null, "Plik zapisu zawiera bledne dane", "Blad wczytywania", JOptionPane.ERROR_MESSAGE);
	    		return;
	    	}
	    	
            int tura = skaner.nextInt();
            int wiekSwiata = skaner.nextInt();
            
            swiat.setTura(tura);
            swiat.setWiek(wiekSwiata);

            int iloscOrganizmow = skaner.nextInt();

            for (int i = 0; i < iloscOrganizmow; i++) {
                String typ = skaner.next();
                int x = skaner.nextInt();
                int y = skaner.nextInt();
                int wiek = skaner.nextInt();
                int sila = skaner.nextInt();
                
                Organizm o = null;

                if (typ.equals("Wilk")) o = new Wilk(swiat, new Wspolrzedne2D(x, y));
                else if (typ.equals("Owca")) o = new Owca(swiat, new Wspolrzedne2D(x, y));
                else if (typ.equals("Lis")) o = new Lis(swiat, new Wspolrzedne2D(x, y));
                else if (typ.equals("Zolw")) o = new Zolw(swiat, new Wspolrzedne2D(x, y));
                else if (typ.equals("Antylopa")) o = new Antylopa(swiat, new Wspolrzedne2D(x, y));
                else if (typ.equals("Trawa")) o = new Trawa(swiat, new Wspolrzedne2D(x, y));
                else if (typ.equals("Mlecz")) o = new Mlecz(swiat, new Wspolrzedne2D(x, y));
                else if (typ.equals("Guarana")) o = new Guarana(swiat, new Wspolrzedne2D(x, y));
                else if (typ.equals("WilczeJagody")) o = new WilczeJagody(swiat, new Wspolrzedne2D(x, y));
                else if (typ.equals("BarszczSosnowskiego")) o = new BarszczSosnowskiego(swiat, new Wspolrzedne2D(x, y));

                
                else if (typ.equals("Czlowiek")) {
                	int trwanieZdolnosci = skaner.nextInt();
                	int odnowienieZdolnosci = skaner.nextInt();
                	boolean zdolnoscAktywna = skaner.nextBoolean();
                	
                	if (swiat.czlowiek == null) {
                		Czlowiek czlowiek = new Czlowiek(swiat, new Wspolrzedne2D(x, y));
                    	swiat.setCzlowiek(czlowiek);
                    	
                    	czlowiek.setTrwanieZdolnosci(trwanieZdolnosci);
                    	czlowiek.setOdnowienieZdolnosci(odnowienieZdolnosci);
                    	czlowiek.setZdolnoscAktywna(zdolnoscAktywna);
                    	
                    	o = czlowiek;
                	}                	
                }
                
                else {
		    		JOptionPane.showMessageDialog(null, "Plik zapisu zawiera bledne dane" + typ, "Blad wczytywania", JOptionPane.ERROR_MESSAGE);
		    		return;
		    	}
                
                if (o != null) {
                	o.setSila(sila);
                    o.setWiek(wiek);
                    
                    swiat.dodajDziecko(o);
                }
            }
            
            swiat.rysujSwiat();
            
            skaner.close();
        }

		catch (Exception e) {
		    JOptionPane.showMessageDialog(null, "Blad wczytywania pliku: " + e.getMessage(), "Blad wczytywania", JOptionPane.ERROR_MESSAGE);
		    e.printStackTrace();
		}
	}
}
