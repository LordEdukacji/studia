#include "Wilk.h"

Wilk::Wilk(Swiat& swiat, const Wspolrzedne& wspolrzedne)
	: Zwierze(swiat, wspolrzedne, SILA_WILK, INICJATYWA_WILK, OZNACZENIE_WILK, SZANSA_RUCHU_WILK, ZASIEG_RUCHU_WILK, SZANSA_UCIECZKI_WILK, SZANSA_ROZMNAZANIA_WILK)
{
}

Wilk::~Wilk()
{
}

std::string Wilk::podajTyp()
{
	return TYP_WILK;
}

bool Wilk::tegoSamegoTypu(Organizm* inny)
{
	if (dynamic_cast<Wilk*>(inny) == nullptr) return false;
	return true;
}

Organizm* Wilk::stworzDziecko(const Wspolrzedne& wspolrzedne)
{
	return new Wilk(this->swiat, wspolrzedne);
}
