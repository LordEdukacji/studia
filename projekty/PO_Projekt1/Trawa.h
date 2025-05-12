#pragma once
#include "Roslina.h"
class Trawa : public Roslina
{
public:
	Trawa(Swiat& swiat, const Wspolrzedne& wspolrzedne);
	~Trawa();

	virtual std::string podajTyp() override;

	bool tegoSamegoTypu(Organizm* inny);

	Organizm* stworzDziecko(const Wspolrzedne& wspolrzedne);
};

