#pragma once
#include "Roslina.h"

class Guarana : public Roslina
{
public:
	Guarana(Swiat& swiat, const Wspolrzedne& wspolrzedne);
	~Guarana();

	virtual std::string podajTyp() override;

	bool tegoSamegoTypu(Organizm* inny);

	Organizm* stworzDziecko(const Wspolrzedne& wspolrzedne);

	void kolizja(Organizm& ofiara, const Wspolrzedne& wspolrzedne, bool atak) override;
};
