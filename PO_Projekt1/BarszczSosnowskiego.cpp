#include "BarszczSosnowskiego.h"

BarszczSosnowskiego::BarszczSosnowskiego(Swiat& swiat, const Wspolrzedne& wspolrzedne)
	: Roslina(swiat, wspolrzedne, SILA_BARSZCZ, INICJATYWA_BARSZCZ, OZNACZENIE_BARSZCZ, PROBY_ROZMNAZANIA_BARSZCZ, SZANSA_ROZMNAZANIA_BARSZCZ)
{
}

BarszczSosnowskiego::~BarszczSosnowskiego()
{
}

std::string BarszczSosnowskiego::podajTyp()
{
	return TYP_BARSZCZ;
}

bool BarszczSosnowskiego::tegoSamegoTypu(Organizm* inny)
{
	if (dynamic_cast<BarszczSosnowskiego*>(inny) == nullptr) return false;
	return true;
}

Organizm* BarszczSosnowskiego::stworzDziecko(const Wspolrzedne& wspolrzedne)
{
	return new BarszczSosnowskiego(this->swiat, wspolrzedne);
}

void BarszczSosnowskiego::akcja()
{
	std::vector<Wspolrzedne> pola = swiat.sasiedniePola(this->getWspolrzedne());

	for (Wspolrzedne w : pola) {
		if (swiat.poleZajete(w) == false) continue;

		Organizm& sasiad = swiat.organizmNaPolu(w);

		if (dynamic_cast<Zwierze*>(&sasiad) != nullptr) {
			sasiad.zakonczZycie();
			swiat.dodajKomunikat(this->podajTyp(), sasiad.podajTyp(), zabija, sasiad.getWspolrzedne());
		}
	}

	Roslina::akcja();
}

void BarszczSosnowskiego::kolizja(Organizm& ofiara, const Wspolrzedne& wspolrzedne, bool atak)
{
	if (ofiara.czyZyje() == true) ofiara.zakonczZycie();
	Roslina::kolizja(ofiara, wspolrzedne, atak);
}
