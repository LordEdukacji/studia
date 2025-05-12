#include "Guarana.h"

Guarana::Guarana(Swiat& swiat, const Wspolrzedne& wspolrzedne)
	: Roslina(swiat, wspolrzedne, SILA_GUARANA, INICJATYWA_GUARANA, OZNACZENIE_GUARANA, PROBY_ROZMNAZANIA_GUARANA, SZANSA_ROZPRZESTRZENIANIA)
{
}

Guarana::~Guarana()
{
}

std::string Guarana::podajTyp()
{
	return TYP_GUARANA;
}

bool Guarana::tegoSamegoTypu(Organizm* inny)
{
	if (dynamic_cast<Guarana*>(inny) == nullptr) return false;
	return true;
}

Organizm* Guarana::stworzDziecko(const Wspolrzedne& wspolrzedne)
{
	return new Guarana(this->swiat, wspolrzedne);
}

void Guarana::kolizja(Organizm& ofiara, const Wspolrzedne& wspolrzedne, bool atak)
{
	ofiara.zmienSile(3);
	Roslina::kolizja(ofiara, wspolrzedne, atak);
}
