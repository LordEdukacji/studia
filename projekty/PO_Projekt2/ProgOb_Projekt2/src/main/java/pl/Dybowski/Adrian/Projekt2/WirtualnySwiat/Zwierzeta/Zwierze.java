package pl.Dybowski.Adrian.Projekt2.WirtualnySwiat.Zwierzeta;

import java.util.HashSet;

import pl.Dybowski.Adrian.Projekt2.WirtualnySwiat.Organizm;
import pl.Dybowski.Adrian.Projekt2.WirtualnySwiat.RodzajeKomunikatow;
import pl.Dybowski.Adrian.Projekt2.WirtualnySwiat.Swiat;
import pl.Dybowski.Adrian.Projekt2.WirtualnySwiat.Wspolrzedne2D;
import pl.Dybowski.Adrian.Projekt2.WirtualnySwiat.Rosliny.Roslina;

public abstract class Zwierze extends Organizm {
	protected final double szansaRuchu;
	protected final int zasiegRuchu;
	protected final double szansaUcieczki;
	protected final double szansaRozmazania;
	
	public Zwierze(Swiat swiat, Wspolrzedne2D polozenie, int sila, int inicjatywa, 
					double szansaRuchu, int zasiegRuchu, double szansaUcieczki, double szansaRozmazania) {
		super(swiat, polozenie, inicjatywa, sila);
		this.szansaRuchu = szansaRuchu;
		this.zasiegRuchu = zasiegRuchu;
		this.szansaUcieczki = szansaUcieczki;
		this.szansaRozmazania = szansaRozmazania;
	}
	
	@Override
	public void akcja() {
		if (!zyje) return;
		
		if (!swiat.szansaTrafiona(szansaRuchu)) return;
		
		for (int i = 0; i < zasiegRuchu; i++) {
			Wspolrzedne2D cel = swiat.losoweSasiedniePole(polozenie);
			
			if (swiat.poleZajete(cel)) {
				kolizja(swiat.organizmNaPolu(cel), true);
				return;
			}
			
			przemiescSieNaPole(cel);
		}
	}
	
	@Override
	public void kolizja(Organizm przeciwnik, boolean atak) {
		if (!atak) return;
		
		if (tegoSamegoTypu(przeciwnik)) {
			rozmnazanie(przeciwnik);
			return;
		}
		atakowanie(przeciwnik);
	}
	
	@Override
	public boolean mozeUciec() {
		return swiat.szansaTrafiona(szansaUcieczki);
	}
	
	public void przemiescSieNaPole(Wspolrzedne2D polozenie) {
		swiat.usunOrganizmZPola(this);
		this.polozenie = polozenie;
		swiat.dodajOrganizmDoPola(this);
	}
	
	@Override
	public void ucieknij(Organizm przeciwnik, boolean atak) {
		HashSet<Wspolrzedne2D> cele = swiat.sasiednieWolnePola(this.polozenie);
		
		if (atak) {
			if (cele.size() == 0) return;
			
			cele.add(this.polozenie);	// organizm moze wrocic na pole z ktorego atakowal
			
			this.przemiescSieNaPole(swiat.losowePole(cele));
		}
		else {
			if (cele.size() == 0) {
				this.zamienMiejscami(przeciwnik);
				return;
			}
			
			this.przemiescSieNaPole(swiat.losowePole(cele));
		}
	}
	
	public void rozmnazanie(Organizm partner) {
		if (!mozeSieRozmnazac()) return;
		
		HashSet<Wspolrzedne2D> pola = swiat.sasiednieWolnePola(this.polozenie);
		HashSet<Wspolrzedne2D> polaPartnera = swiat.sasiednieWolnePola(this.polozenie);
		pola.addAll(polaPartnera);	// dziecko moze pojawic sie przy ktorymkolwiek z rodzicow
		
		if (pola.size() == 0) return;
		
		Organizm dziecko = this.stworzDziecko(swiat.losowePole(pola));
		swiat.dodajDziecko(dziecko);
		
		swiat.dodajKomunikat(dziecko, RodzajeKomunikatow.rodziSie);
	}
	
	public boolean mozeSieRozmnazac() {
		return swiat.szansaTrafiona(szansaRozmazania);
	}
	
	public void atakowanie(Organizm przeciwnik) {
		if (this.mozeUciec()) {
			this.ucieknij(przeciwnik, true);
			swiat.dodajKomunikat(przeciwnik, this, RodzajeKomunikatow.ucieka);
		}
		else if (przeciwnik.mozeUciec()) {
			przeciwnik.ucieknij(this, false);
			swiat.dodajKomunikat(this, przeciwnik, RodzajeKomunikatow.ucieka);
		}
		else if (przeciwnik.odbilAtak(this)) {
			swiat.dodajKomunikat(this, przeciwnik, RodzajeKomunikatow.odbija);
			return;
		}
		else if (this.sila >= przeciwnik.getSila()) {
			przeciwnik.kolizja(this, false);
			przeciwnik.zakonczZycie();
			this.przemiescSieNaPole(przeciwnik.getPolozenie());
			
			if (przeciwnik instanceof Roslina) {
				swiat.dodajKomunikat(this, przeciwnik, RodzajeKomunikatow.zjada);
			}
			else {
				swiat.dodajKomunikat(this, przeciwnik, RodzajeKomunikatow.zabija);
			}
		}
		else {
			przeciwnik.kolizja(this, false);
			this.zakonczZycie();
			

			if (przeciwnik instanceof Roslina) {
				swiat.dodajKomunikat(this, przeciwnik, RodzajeKomunikatow.zatruwaSie);
			}
			else {
				swiat.dodajKomunikat(this, przeciwnik, RodzajeKomunikatow.broniSie);
			}
		}
	}

}
