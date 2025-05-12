#include "Owca.h"

Owca::Owca(Swiat& swiat, const Wspolrzedne& wspolrzedne)
	: Zwierze(swiat, wspolrzedne, SILA_OWCA, INICJATYWA_OWCA, OZNACZENIE_OWCA, SZANSA_RUCHU_OWCA, ZASIEG_RUCHU_OWCA, SZANSA_UCIECZKI_OWCA, SZANSA_ROZMNAZANIA)
{
}

Owca::~Owca()
{
}

std::string Owca::podajTyp()
{
	return "Owca";
}

bool Owca::tegoSamegoTypu(Organizm* inny)
{
	if (dynamic_cast<Owca*>(inny) == nullptr) return false;
	return true;
}

Organizm* Owca::stworzDziecko(const Wspolrzedne& wspolrzedne)
{
	return new Owca(this->swiat, wspolrzedne);
}
