package pl.Dybowski.Adrian.Projekt2.WirtualnySwiat.Zwierzeta;

import pl.Dybowski.Adrian.Projekt2.WirtualnySwiat.Kierunki;
import pl.Dybowski.Adrian.Projekt2.WirtualnySwiat.Organizm;
import pl.Dybowski.Adrian.Projekt2.WirtualnySwiat.RodzajeKomunikatow;
import pl.Dybowski.Adrian.Projekt2.WirtualnySwiat.Stale;
import pl.Dybowski.Adrian.Projekt2.WirtualnySwiat.Swiat;
import pl.Dybowski.Adrian.Projekt2.WirtualnySwiat.Wspolrzedne2D;

public class Czlowiek extends Zwierze {

	private int trwanieZdolnosci, odnowienieZdolnosci;
	private final int maxTrwanieZdolnosci, maxOdnowienieZdolnosci;
	private boolean zdolnoscAktywna;
	
	private Kierunki kierunek;
	
	public Czlowiek(Swiat swiat, Wspolrzedne2D polozenie) {
		super(swiat, polozenie, Stale.sila.get(Czlowiek.class), Stale.inicjatywa.get(Czlowiek.class),
				Stale.szansaRuchu.get(Czlowiek.class), Stale.zasiegRuchu.get(Czlowiek.class),
				Stale.szansaUcieczki.get(Czlowiek.class), Stale.szansaRozmnazania.get(Czlowiek.class));
		
		maxTrwanieZdolnosci = Stale.maxTrwanieZdolnosci;
		maxOdnowienieZdolnosci = Stale.maxOdnowienieZdolnosci;
		
		trwanieZdolnosci = 0;
		odnowienieZdolnosci = 0;
		zdolnoscAktywna = false;
		
		kierunek = Kierunki.stop;
	}
	
	public void setTrwanieZdolnosci(int trwanieZdolnosci) {
	    this.trwanieZdolnosci = trwanieZdolnosci;
	}

	public void setOdnowienieZdolnosci(int odnowienieZdolnosci) {
	    this.odnowienieZdolnosci = odnowienieZdolnosci;
	}

	public void setZdolnoscAktywna(boolean zdolnoscAktywna) {
	    this.zdolnoscAktywna = zdolnoscAktywna;
	}

	public int getTrwanieZdolnosci() {
	    return trwanieZdolnosci;
	}

	public int getOdnowienieZdolnosci() {
	    return odnowienieZdolnosci;
	}

	public boolean isZdolnoscAktywna() {
	    return zdolnoscAktywna;
	}

	
	public void setKierunek(Kierunki kierunek) {
		this.kierunek = kierunek;
	}
	
	@Override
	public boolean odbilAtak(Organizm atakujacy) {
		return zdolnoscAktywna;
	}
	
	@Override
	public void akcja() {
		if (!zyje) return;
		
		if (!swiat.szansaTrafiona(szansaRuchu)) return;
		
		for (int i = 0; i < zasiegRuchu; i++) {
			Wspolrzedne2D cel = swiat.poleWKierunku(polozenie, kierunek);
			
			if (swiat.poleZajete(cel)) {
				kolizja(swiat.organizmNaPolu(cel), true);
				kierunek = Kierunki.stop;
				return;
			}
			
			przemiescSieNaPole(cel);
		}
		
		kierunek = Kierunki.stop;
	}
	
	public void aktywujZdolnosc() {
		if (odnowienieZdolnosci > 0) return;
		if (zdolnoscAktywna) return;
		
		trwanieZdolnosci = maxTrwanieZdolnosci;
		zdolnoscAktywna = true;
		
		swiat.dodajKomunikat(this, RodzajeKomunikatow.aktywujeMoc);
	}
	
	public void odswiezZdolnosc() {
		if (trwanieZdolnosci > 0) trwanieZdolnosci--;

		if (trwanieZdolnosci == 0 && zdolnoscAktywna == true) {
			zdolnoscAktywna = false;
			odnowienieZdolnosci = maxOdnowienieZdolnosci;
			return;
		}

		if (odnowienieZdolnosci > 0) odnowienieZdolnosci--;
	}
	
	@Override
	public String toString() {
		return this.getClass().getSimpleName()+" "+polozenie.getX()+" "+polozenie.getY()+" "+Integer.toString(getWiek())+" "+sila+" "+trwanieZdolnosci+" "+odnowienieZdolnosci+" "+zdolnoscAktywna+"\n";
	}

	@Override
	public boolean tegoSamegoTypu(Organizm inny) {
		return inny instanceof Czlowiek;
	}

	@Override
	public Organizm stworzDziecko(Wspolrzedne2D cel) {
		return null;
	}

}
