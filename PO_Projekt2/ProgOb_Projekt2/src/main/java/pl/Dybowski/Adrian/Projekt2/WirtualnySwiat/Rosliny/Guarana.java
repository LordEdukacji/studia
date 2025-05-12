package pl.Dybowski.Adrian.Projekt2.WirtualnySwiat.Rosliny;

import pl.Dybowski.Adrian.Projekt2.WirtualnySwiat.Organizm;
import pl.Dybowski.Adrian.Projekt2.WirtualnySwiat.Stale;
import pl.Dybowski.Adrian.Projekt2.WirtualnySwiat.Swiat;
import pl.Dybowski.Adrian.Projekt2.WirtualnySwiat.Wspolrzedne2D;

public class Guarana extends Roslina {

	public Guarana(Swiat swiat, Wspolrzedne2D polozenie) {
		super(swiat, polozenie, Stale.sila.get(Guarana.class), Stale.inicjatywa.get(Guarana.class),
				Stale.szansaRozprzestrzeniania.get(Guarana.class), Stale.probyRozprzestrzeniania.get(Guarana.class));
	}
	
	@Override
	public void kolizja(Organizm ofiara, boolean atak) {
		if (atak) return;
		
		ofiara.zmienSila(3);
		
		if (zyje) zakonczZycie();
	}

	@Override
	public boolean tegoSamegoTypu(Organizm inny) {
		return inny instanceof Guarana;
	}

	@Override
	public Organizm stworzDziecko(Wspolrzedne2D cel) {
		return new Guarana(swiat, cel);
	}

}
