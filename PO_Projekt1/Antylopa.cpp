#include "Antylopa.h"

Antylopa::Antylopa(Swiat& swiat, const Wspolrzedne& wspolrzedne)
	: Zwierze(swiat, wspolrzedne, SILA_ANTYLOPA, INICJATYWA_ANTYLOPA, OZNACZENIE_ANTYLOPA, SZANSA_RUCHU_ANTYLOPA, ZASIEG_RUCHU_ANTYLOPA, SZANSA_UCIECZKI_ANTYLOPA, SZANSA_ROZMNAZANIA_ANTYLOPA)
{
}

Antylopa::~Antylopa()
{
}

std::string Antylopa::podajTyp()
{
	return TYP_ANTYLOPA;
}

bool Antylopa::tegoSamegoTypu(Organizm* inny)
{
	if (dynamic_cast<Antylopa*>(inny) == nullptr) return false;
	return true;
}

Organizm* Antylopa::stworzDziecko(const Wspolrzedne& wspolrzedne)
{
	return new Antylopa(this->swiat, wspolrzedne);
}
