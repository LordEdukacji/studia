package pl.Dybowski.Adrian.Projekt2.WirtualnySwiat.Zwierzeta;

import pl.Dybowski.Adrian.Projekt2.WirtualnySwiat.Organizm;
import pl.Dybowski.Adrian.Projekt2.WirtualnySwiat.Stale;
import pl.Dybowski.Adrian.Projekt2.WirtualnySwiat.Swiat;
import pl.Dybowski.Adrian.Projekt2.WirtualnySwiat.Wspolrzedne2D;

public class Owca extends Zwierze {
	public Owca(Swiat swiat, Wspolrzedne2D polozenie) {
		super(swiat, polozenie, Stale.sila.get(Owca.class), Stale.inicjatywa.get(Owca.class),
				Stale.szansaRuchu.get(Owca.class), Stale.zasiegRuchu.get(Owca.class),
				Stale.szansaUcieczki.get(Owca.class), Stale.szansaRozmnazania.get(Owca.class));
	}

	@Override
	public boolean tegoSamegoTypu(Organizm inny) {
		return inny instanceof Owca;
	}

	@Override
	public Organizm stworzDziecko(Wspolrzedne2D cel) {
		return new Owca(swiat, cel);
	}
}
