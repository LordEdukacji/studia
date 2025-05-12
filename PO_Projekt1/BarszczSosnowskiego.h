#pragma once
#include "Roslina.h"
#include "Zwierze.h"	// barszcz musi umiec sam rozpoznac czy cos jest zwierzeciem

class BarszczSosnowskiego : public Roslina
{
public:
	BarszczSosnowskiego(Swiat& swiat, const Wspolrzedne& wspolrzedne);
	~BarszczSosnowskiego();

	virtual std::string podajTyp() override;

	bool tegoSamegoTypu(Organizm* inny);

	Organizm* stworzDziecko(const Wspolrzedne& wspolrzedne);

	void akcja() override;
	void kolizja(Organizm& ofiara, const Wspolrzedne& wspolrzedne, bool atak) override;
};