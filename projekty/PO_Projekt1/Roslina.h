#pragma once
#include "Organizm.h"

#define SZANSA_ROZPRZESTRZENIANIA 0.08

class Roslina : public Organizm
{
protected:
	const int probyRozprzestrzeniania;
	const double szansaRozprzestrzeniania;
public:
	Roslina(Swiat& swiat, const Wspolrzedne& wspolrzedne, const int sila, const int inicjatywa, const char oznaczenie,
		const int probyRozprzestrzeniania, const double szansaRozprzestrzeniania);
	~Roslina();

	virtual void akcja() override;
	virtual void kolizja(Organizm& ofiara, const Wspolrzedne& wspolrzedne, bool atak) override;
};

