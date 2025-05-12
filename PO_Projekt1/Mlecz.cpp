#include "Mlecz.h"

Mlecz::Mlecz(Swiat& swiat, const Wspolrzedne& wspolrzedne)
	: Roslina(swiat, wspolrzedne, SILA_MLECZ, INICJATYWA_MLECZ, OZNACZENIE_MLECZ, PROBY_ROZMNAZANIA_MLECZ, SZANSA_ROZMNAZANIA_MLECZ)
{
}

Mlecz::~Mlecz()
{
}

std::string Mlecz::podajTyp()
{
	return TYP_MLECZ;
}

bool Mlecz::tegoSamegoTypu(Organizm* inny)
{
	if (dynamic_cast<Mlecz*>(inny) == nullptr) return false;
	return true;
}

Organizm* Mlecz::stworzDziecko(const Wspolrzedne& wspolrzedne)
{
	return new Mlecz(this->swiat, wspolrzedne);
}
