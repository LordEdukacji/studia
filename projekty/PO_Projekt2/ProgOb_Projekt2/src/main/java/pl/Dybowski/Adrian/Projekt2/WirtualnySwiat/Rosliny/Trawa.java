package pl.Dybowski.Adrian.Projekt2.WirtualnySwiat.Rosliny;

import pl.Dybowski.Adrian.Projekt2.WirtualnySwiat.Organizm;
import pl.Dybowski.Adrian.Projekt2.WirtualnySwiat.Stale;
import pl.Dybowski.Adrian.Projekt2.WirtualnySwiat.Swiat;
import pl.Dybowski.Adrian.Projekt2.WirtualnySwiat.Wspolrzedne2D;

public class Trawa extends Roslina {

	public Trawa(Swiat swiat, Wspolrzedne2D polozenie) {
		super(swiat, polozenie, Stale.sila.get(Trawa.class), Stale.inicjatywa.get(Trawa.class),
				Stale.szansaRozprzestrzeniania.get(Trawa.class), Stale.probyRozprzestrzeniania.get(Trawa.class));
	}

	@Override
	public boolean tegoSamegoTypu(Organizm inny) {
		return inny instanceof Trawa;
	}

	@Override
	public Organizm stworzDziecko(Wspolrzedne2D cel) {
		return new Trawa(swiat, cel);
	}

}
