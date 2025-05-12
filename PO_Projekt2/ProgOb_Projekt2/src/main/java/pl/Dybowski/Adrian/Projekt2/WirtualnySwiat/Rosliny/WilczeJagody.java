package pl.Dybowski.Adrian.Projekt2.WirtualnySwiat.Rosliny;

import pl.Dybowski.Adrian.Projekt2.WirtualnySwiat.Organizm;
import pl.Dybowski.Adrian.Projekt2.WirtualnySwiat.Stale;
import pl.Dybowski.Adrian.Projekt2.WirtualnySwiat.Swiat;
import pl.Dybowski.Adrian.Projekt2.WirtualnySwiat.Wspolrzedne2D;

public class WilczeJagody extends Roslina {

	public WilczeJagody(Swiat swiat, Wspolrzedne2D polozenie) {
		super(swiat, polozenie, Stale.sila.get(WilczeJagody.class), Stale.inicjatywa.get(WilczeJagody.class),
				Stale.szansaRozprzestrzeniania.get(WilczeJagody.class), Stale.probyRozprzestrzeniania.get(WilczeJagody.class));
	}
	
	@Override
	public void kolizja(Organizm ofiara, boolean atak) {
		if (atak) return;
		
		if (ofiara.isZyje()) ofiara.zakonczZycie();
		
		if (zyje) zakonczZycie();
	}

	@Override
	public boolean tegoSamegoTypu(Organizm inny) {
		return inny instanceof WilczeJagody;
	}

	@Override
	public Organizm stworzDziecko(Wspolrzedne2D cel) {
		return new WilczeJagody(swiat, cel);
	}

}
