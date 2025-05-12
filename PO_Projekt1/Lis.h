#pragma once
#include "Zwierze.h"
class Lis : public Zwierze
{
public:
	Lis(Swiat& swiat, const Wspolrzedne& wspolrzedne);
	~Lis();

	void akcja() override;

	std::string podajTyp() override;

	bool tegoSamegoTypu(Organizm* inny) override;

	Organizm* stworzDziecko(const Wspolrzedne& wspolrzedne) override;
};

