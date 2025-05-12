#pragma once
#include "Zwierze.h"
class Owca : public Zwierze
{
public:
	Owca(Swiat& swiat, const Wspolrzedne& wspolrzedne);
	~Owca();

	std::string podajTyp() override;

	bool tegoSamegoTypu(Organizm* inny) override;

	Organizm* stworzDziecko(const Wspolrzedne& wspolrzedne) override;
};

