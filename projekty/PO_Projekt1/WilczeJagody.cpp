#include "WilczeJagody.h"

WilczeJagody::WilczeJagody(Swiat& swiat, const Wspolrzedne& wspolrzedne)
	: Roslina(swiat, wspolrzedne, SILA_JAGODY, INICJATYWA_JAGODY, OZNACZENIE_JAGODY, PROBY_ROZMNAZANIA_JAGODY, SZANSA_ROZMNAZANIA_JAGODY)
{
}

WilczeJagody::~WilczeJagody()
{
}

std::string WilczeJagody::podajTyp()
{
	return TYP_JAGODY;
}

bool WilczeJagody::tegoSamegoTypu(Organizm* inny)
{
	if (dynamic_cast<WilczeJagody*>(inny) == nullptr) return false;
	return true;
}

Organizm* WilczeJagody::stworzDziecko(const Wspolrzedne& wspolrzedne)
{
	return new WilczeJagody(this->swiat, wspolrzedne);
}

void WilczeJagody::kolizja(Organizm& ofiara, const Wspolrzedne& wspolrzedne, bool atak)
{
	if (ofiara.czyZyje() == true) ofiara.zakonczZycie();

	Roslina::kolizja(ofiara, wspolrzedne, atak);
}
