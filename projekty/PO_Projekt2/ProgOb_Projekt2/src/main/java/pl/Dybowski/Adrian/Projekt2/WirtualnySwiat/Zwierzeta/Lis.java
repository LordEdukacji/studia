package pl.Dybowski.Adrian.Projekt2.WirtualnySwiat.Zwierzeta;

import java.util.HashSet;
import java.util.Iterator;

import pl.Dybowski.Adrian.Projekt2.WirtualnySwiat.Organizm;
import pl.Dybowski.Adrian.Projekt2.WirtualnySwiat.Stale;
import pl.Dybowski.Adrian.Projekt2.WirtualnySwiat.Swiat;
import pl.Dybowski.Adrian.Projekt2.WirtualnySwiat.Wspolrzedne2D;

public class Lis extends Zwierze {

	public Lis(Swiat swiat, Wspolrzedne2D polozenie) {
		super(swiat, polozenie, Stale.sila.get(Lis.class), Stale.inicjatywa.get(Lis.class),
				Stale.szansaRuchu.get(Lis.class), Stale.zasiegRuchu.get(Lis.class),
				Stale.szansaUcieczki.get(Lis.class), Stale.szansaRozmnazania.get(Lis.class));
	}
	
	@Override
	public void akcja() {
		if (!zyje) return;
		
		if (!swiat.szansaTrafiona(szansaRuchu)) return;
		
		for (int i = 0; i < zasiegRuchu; i++) {
			HashSet<Wspolrzedne2D> cele = swiat.sasiedniePola(polozenie);
			
			Iterator<Wspolrzedne2D> iterator = cele.iterator();
			while (iterator.hasNext()) {
				Wspolrzedne2D polozenie = iterator.next();
				
				if (swiat.poleZajete(polozenie) && swiat.organizmNaPolu(polozenie).getSila() > sila) {
					iterator.remove();
				}
			}
			
			Wspolrzedne2D cel = swiat.losowePole(cele);
			
			if (swiat.poleZajete(cel)) {
				kolizja(swiat.organizmNaPolu(cel), true);
				return;
			}
					
			przemiescSieNaPole(cel);
		}
	}

	@Override
	public boolean tegoSamegoTypu(Organizm inny) {
		return inny instanceof Lis;
	}

	@Override
	public Organizm stworzDziecko(Wspolrzedne2D cel) {
		return new Lis(swiat, cel);
	}
}
