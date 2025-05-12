#pragma once
#include "Zwierze.h"
class Zolw : public Zwierze
{
public:
	Zolw(Swiat& swiat, const Wspolrzedne& wspolrzedne);
	~Zolw();

	std::string podajTyp() override;

	bool odbilAtak(Organizm& atakujacy) override;

	bool tegoSamegoTypu(Organizm* inny) override;

	Organizm* stworzDziecko(const Wspolrzedne& wspolrzedne) override;
};

