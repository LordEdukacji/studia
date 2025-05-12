#pragma once
#include "Zwierze.h"
class Antylopa : public Zwierze
{
public:
	Antylopa(Swiat& swiat, const Wspolrzedne& wspolrzedne);
	~Antylopa();

	std::string podajTyp() override;

	bool tegoSamegoTypu(Organizm* inny) override;

	Organizm* stworzDziecko(const Wspolrzedne& wspolrzedne) override;
};

