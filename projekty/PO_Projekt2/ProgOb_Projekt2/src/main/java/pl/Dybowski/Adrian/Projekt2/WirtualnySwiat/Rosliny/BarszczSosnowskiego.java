package pl.Dybowski.Adrian.Projekt2.WirtualnySwiat.Rosliny;

import java.util.HashSet;

import pl.Dybowski.Adrian.Projekt2.WirtualnySwiat.Organizm;
import pl.Dybowski.Adrian.Projekt2.WirtualnySwiat.RodzajeKomunikatow;
import pl.Dybowski.Adrian.Projekt2.WirtualnySwiat.Stale;
import pl.Dybowski.Adrian.Projekt2.WirtualnySwiat.Swiat;
import pl.Dybowski.Adrian.Projekt2.WirtualnySwiat.Wspolrzedne2D;
import pl.Dybowski.Adrian.Projekt2.WirtualnySwiat.Zwierzeta.Zwierze;

public class BarszczSosnowskiego extends Roslina {

	public BarszczSosnowskiego(Swiat swiat, Wspolrzedne2D polozenie) {
		super(swiat, polozenie, Stale.sila.get(BarszczSosnowskiego.class), Stale.inicjatywa.get(BarszczSosnowskiego.class),
				Stale.szansaRozprzestrzeniania.get(BarszczSosnowskiego.class), Stale.probyRozprzestrzeniania.get(BarszczSosnowskiego.class));
	}
	
	@Override
	public void akcja() {
		if (!zyje) return;
		
		HashSet<Wspolrzedne2D> cele = swiat.sasiedniePola(polozenie);
		
		for (Wspolrzedne2D cel : cele) {
			if (!swiat.poleZajete(cel)) continue;
			
			if (swiat.organizmNaPolu(cel) instanceof Zwierze) {
				swiat.dodajKomunikat(this, swiat.organizmNaPolu(cel), RodzajeKomunikatow.zabija);
				swiat.organizmNaPolu(cel).zakonczZycie();
			}
		}
		
		super.akcja();
	}
	
	@Override
	public void kolizja(Organizm ofiara, boolean atak) {
		if (atak) return;
		
		if (ofiara.isZyje()) ofiara.zakonczZycie();
		
		if (zyje) zakonczZycie();
	}

	@Override
	public boolean tegoSamegoTypu(Organizm inny) {
		return inny instanceof BarszczSosnowskiego;
	}

	@Override
	public Organizm stworzDziecko(Wspolrzedne2D cel) {
		return new BarszczSosnowskiego(swiat, cel);
	}

}
