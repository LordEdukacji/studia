package pl.Dybowski.Adrian.Projekt2.WirtualnySwiat.Zwierzeta;

import pl.Dybowski.Adrian.Projekt2.WirtualnySwiat.Organizm;
import pl.Dybowski.Adrian.Projekt2.WirtualnySwiat.Stale;
import pl.Dybowski.Adrian.Projekt2.WirtualnySwiat.Swiat;
import pl.Dybowski.Adrian.Projekt2.WirtualnySwiat.Wspolrzedne2D;

public class Wilk extends Zwierze {	
	public Wilk(Swiat swiat, Wspolrzedne2D polozenie) {
		super(swiat, polozenie, Stale.sila.get(Wilk.class), Stale.inicjatywa.get(Wilk.class),
				Stale.szansaRuchu.get(Wilk.class), Stale.zasiegRuchu.get(Wilk.class),
				Stale.szansaUcieczki.get(Wilk.class), Stale.szansaRozmnazania.get(Wilk.class));
	}

	@Override
	public boolean tegoSamegoTypu(Organizm inny) {
		return inny instanceof Wilk;
	}

	@Override
	public Organizm stworzDziecko(Wspolrzedne2D cel) {
		return new Wilk(swiat, cel);
	}
}
