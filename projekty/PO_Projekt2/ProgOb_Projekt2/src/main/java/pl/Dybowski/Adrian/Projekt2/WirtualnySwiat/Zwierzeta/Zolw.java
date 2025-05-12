package pl.Dybowski.Adrian.Projekt2.WirtualnySwiat.Zwierzeta;

import pl.Dybowski.Adrian.Projekt2.WirtualnySwiat.Organizm;
import pl.Dybowski.Adrian.Projekt2.WirtualnySwiat.Stale;
import pl.Dybowski.Adrian.Projekt2.WirtualnySwiat.Swiat;
import pl.Dybowski.Adrian.Projekt2.WirtualnySwiat.Wspolrzedne2D;

public class Zolw extends Zwierze {

	public Zolw(Swiat swiat, Wspolrzedne2D polozenie) {
		super(swiat, polozenie, Stale.sila.get(Zolw.class), Stale.inicjatywa.get(Zolw.class),
				Stale.szansaRuchu.get(Zolw.class), Stale.zasiegRuchu.get(Zolw.class),
				Stale.szansaUcieczki.get(Zolw.class), Stale.szansaRozmnazania.get(Zolw.class));
	}
	
	@Override
	public boolean odbilAtak(Organizm atakujacy) {
		return atakujacy.getSila() < 5;
	}

	@Override
	public boolean tegoSamegoTypu(Organizm inny) {
		return inny instanceof Zolw;
	}

	@Override
	public Organizm stworzDziecko(Wspolrzedne2D cel) {
		return new Zolw(swiat, cel);
	}

}
