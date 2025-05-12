#include "Trawa.h"

Trawa::Trawa(Swiat& swiat, const Wspolrzedne& wspolrzedne)
	: Roslina(swiat, wspolrzedne, SILA_TRAWA, INICJATYWA_TRAWA, OZNACZENIE_TRAWA, PROBY_ROZMNAZANIA_TRAWA, SZANSA_ROZMNAZANIA_TRAWA)
{

}

Trawa::~Trawa()
{
}

std::string Trawa::podajTyp()
{
	return TYP_TRAWA;
}

bool Trawa::tegoSamegoTypu(Organizm* inny)
{
	if (dynamic_cast<Trawa*>(inny) == nullptr) return false;
	return true;
}

Organizm* Trawa::stworzDziecko(const Wspolrzedne& wspolrzedne)
{
	return new Trawa(this->swiat, wspolrzedne);
}
