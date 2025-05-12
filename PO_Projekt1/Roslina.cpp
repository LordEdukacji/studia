#include "Roslina.h"

Roslina::Roslina(Swiat& swiat, const Wspolrzedne& wspolrzedne, const int sila, const int inicjatywa, const char oznaczenie, const int probyRozprzestrzeniania, const double szansaRozprzestrzeniania)
	: Organizm(swiat, wspolrzedne, sila, inicjatywa, oznaczenie), probyRozprzestrzeniania(probyRozprzestrzeniania), szansaRozprzestrzeniania(szansaRozprzestrzeniania)
{
}

Roslina::~Roslina()
{
}

void Roslina::akcja()
{
	if (this->czyZyje() == false) return;

	for (int i = 0; i < probyRozprzestrzeniania; i++) {
		if ((double)std::rand() / RAND_MAX >= szansaRozprzestrzeniania) continue;

		std::vector<Wspolrzedne> pola = swiat.sasiednieWolnePola(this->getWspolrzedne());
		if (pola.size() == 0) return;	// mozemy od razu przerwac, wiecej wolnych pol sie nie pojawi teraz

		Wspolrzedne cel = pola[std::rand() % pola.size()];

		Organizm* dziecko = stworzDziecko(cel);
		swiat.dodajDziecko(dziecko);

		swiat.dodajKomunikat(this->podajTyp(), this->podajTyp(), rozmnazaSie, wspolrzedne);
	}
}

void Roslina::kolizja(Organizm& ofiara, const Wspolrzedne& wspolrzedne, bool atak)
{
	if (atak == false) {
		if (this->czyZyje() == false) swiat.dodajKomunikat(ofiara.podajTyp(), this->podajTyp(), zjada, wspolrzedne);
		else this->zakonczZycie();	// roslina zawsze umrze - jest zjadana

		if (ofiara.czyZyje() == false) swiat.dodajKomunikat(ofiara.podajTyp(), this->podajTyp(), zatruwaSie, wspolrzedne);
	}
}
