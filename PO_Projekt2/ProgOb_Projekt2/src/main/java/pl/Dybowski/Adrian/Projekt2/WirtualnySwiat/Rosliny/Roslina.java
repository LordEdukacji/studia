package pl.Dybowski.Adrian.Projekt2.WirtualnySwiat.Rosliny;

import java.util.HashSet;

import pl.Dybowski.Adrian.Projekt2.WirtualnySwiat.Organizm;
import pl.Dybowski.Adrian.Projekt2.WirtualnySwiat.RodzajeKomunikatow;
import pl.Dybowski.Adrian.Projekt2.WirtualnySwiat.Swiat;
import pl.Dybowski.Adrian.Projekt2.WirtualnySwiat.Wspolrzedne2D;

public abstract class Roslina extends Organizm {
	protected final int probyRozprzestrzeniania;
	protected final double szansaRozprzestrzeniania;
	
	public Roslina(Swiat swiat, Wspolrzedne2D polozenie, int sila, int inicjatywa,
					double szansaRozprzestrzeniania, int probyRozprzestrzeniania) {
		super(swiat, polozenie, inicjatywa, sila);
		this.probyRozprzestrzeniania = probyRozprzestrzeniania;
		this.szansaRozprzestrzeniania = szansaRozprzestrzeniania;
	}
	
	@Override
	public void akcja() {
		if (!zyje) return;
		
		HashSet<Wspolrzedne2D> pola = swiat.sasiednieWolnePola(polozenie);
		if (pola.size() == 0) return;

		for (int i = 0; i < probyRozprzestrzeniania; i++) {
			if (!mozeSieRozprzestrzeniac()) continue;
			
			Organizm dziecko = stworzDziecko(swiat.losowePole(pola));
			swiat.dodajDziecko(dziecko);
			
			swiat.dodajKomunikat(dziecko, RodzajeKomunikatow.wyrasta);
		}
	}
	
	@Override
	public void kolizja(Organizm ofiara, boolean atak) {
		if (atak) return;
		
		if (zyje) zakonczZycie();
	}
	
	private boolean mozeSieRozprzestrzeniac() {
		return swiat.szansaTrafiona(szansaRozprzestrzeniania);
	}
}
