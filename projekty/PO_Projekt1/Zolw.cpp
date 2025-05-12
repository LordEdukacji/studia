#include "Zolw.h"

Zolw::Zolw(Swiat& swiat, const Wspolrzedne& wspolrzedne)
	: Zwierze(swiat, wspolrzedne, SILA_ZOLW, INICJATYWA_ZOLW, OZNACZENIE_ZOLW, SZANSA_RUCHU_ZOLW, ZASIEG_RUCHU_ZOLW, SZANSA_UCIECZKI_ZOLW, SZANSA_ROZMNAZANIA_ZOLW)
{
}

Zolw::~Zolw()
{
}

std::string Zolw::podajTyp()
{
	return TYP_ZOLW;
}

bool Zolw::odbilAtak(Organizm& atakujacy)
{
	if (atakujacy.getSila() < 5) return true;
	return false;
}

bool Zolw::tegoSamegoTypu(Organizm* inny)
{
	if (dynamic_cast<Zolw*>(inny) == nullptr) return false;
	return true;
}

Organizm* Zolw::stworzDziecko(const Wspolrzedne& wspolrzedne)
{
	return new Zolw(this->swiat, wspolrzedne);
}
