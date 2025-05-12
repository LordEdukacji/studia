package pl.Dybowski.Adrian.Projekt2.WirtualnySwiat.Rosliny;

import pl.Dybowski.Adrian.Projekt2.WirtualnySwiat.Organizm;
import pl.Dybowski.Adrian.Projekt2.WirtualnySwiat.Stale;
import pl.Dybowski.Adrian.Projekt2.WirtualnySwiat.Swiat;
import pl.Dybowski.Adrian.Projekt2.WirtualnySwiat.Wspolrzedne2D;

public class Mlecz extends Roslina {

	public Mlecz(Swiat swiat, Wspolrzedne2D polozenie) {
		super(swiat, polozenie, Stale.sila.get(Mlecz.class), Stale.inicjatywa.get(Mlecz.class),
				Stale.szansaRozprzestrzeniania.get(Mlecz.class), Stale.probyRozprzestrzeniania.get(Mlecz.class));
	}

	@Override
	public boolean tegoSamegoTypu(Organizm inny) {
		return inny instanceof Mlecz;
	}

	@Override
	public Organizm stworzDziecko(Wspolrzedne2D cel) {
		return new Mlecz(swiat, cel);
	}

}
