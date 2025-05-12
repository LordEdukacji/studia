#pragma once
#include "Organizm.h"

#define SZANSA_ROZMNAZANIA 0.25

class Zwierze : public Organizm
{
protected:
	const double szansaRuchu;
	const int zasiegRuchu;
	const double szansaUcieczki;
	const double szansaRozmazania;
public:
	Zwierze(Swiat& swiat, const Wspolrzedne& wspolrzedne, const int sila, const int inicjatywa, const char oznaczenie,
		const double szansaRuchu, const int zasiegRuchu, const double szansaUcieczki, const double szansaRozmazania);
	~Zwierze();

	virtual void akcja() override;																			// akcja() polega na spojrzeniu na wybrane sasiednie pole
																											// jesli jest puste to sie na nie przemieszczamy
																											// jesli jest zajete nastepuje kolizja()
																											// ktora to odpowiada za ewentualne przemieszczenie organizmow po walce
	virtual void kolizja(Organizm& ofiara, const Wspolrzedne& wspolrzedne, bool atak) override;

	void przemiescSieNaPole(Wspolrzedne& wspolrzedne);														// zmienia lokalizacje organizmu

	bool mozeUciec() override;
	void ucieknij(Organizm& napastnik, const Wspolrzedne& wspolrzedne, bool atak) override;

	bool mozeSieRozmnazac() const;
	void rozmnazanie(Organizm& partner, const Wspolrzedne& wspolrzedne);

	void atakowanie(Organizm& ofiara, const Wspolrzedne& wspolrzedne);
};

