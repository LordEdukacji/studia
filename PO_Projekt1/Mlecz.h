#pragma once
#include "Roslina.h"

class Mlecz : public Roslina
{
public:
	Mlecz(Swiat& swiat, const Wspolrzedne& wspolrzedne);
	~Mlecz();

	virtual std::string podajTyp() override;

	bool tegoSamegoTypu(Organizm* inny);

	Organizm* stworzDziecko(const Wspolrzedne& wspolrzedne);
};

