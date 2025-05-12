package pl.Dybowski.Adrian.Projekt2.WirtualnySwiat.Zwierzeta;

import pl.Dybowski.Adrian.Projekt2.WirtualnySwiat.Organizm;
import pl.Dybowski.Adrian.Projekt2.WirtualnySwiat.Stale;
import pl.Dybowski.Adrian.Projekt2.WirtualnySwiat.Swiat;
import pl.Dybowski.Adrian.Projekt2.WirtualnySwiat.Wspolrzedne2D;

public class Antylopa extends Zwierze {

	public Antylopa(Swiat swiat, Wspolrzedne2D polozenie) {
		super(swiat, polozenie, Stale.sila.get(Antylopa.class), Stale.inicjatywa.get(Antylopa.class),
				Stale.szansaRuchu.get(Antylopa.class), Stale.zasiegRuchu.get(Antylopa.class),
				Stale.szansaUcieczki.get(Antylopa.class), Stale.szansaRozmnazania.get(Antylopa.class));
	}

	@Override
	public boolean tegoSamegoTypu(Organizm inny) {
		return inny instanceof Antylopa;
	}

	@Override
	public Organizm stworzDziecko(Wspolrzedne2D cel) {
		return new Antylopa(swiat, cel);
	}

}
