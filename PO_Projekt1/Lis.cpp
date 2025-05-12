#include "Lis.h"

Lis::Lis(Swiat& swiat, const Wspolrzedne& wspolrzedne)
	: Zwierze(swiat, wspolrzedne, SILA_LIS, INICJATYWA_LIS, OZNACZENIE_LIS, SZANSA_RUCHU_LIS, ZASIEG_RUCHU_LIS, SZANSA_UCIECZKI_LIS, SZANSA_ROZMNAZANIA)
{
}

Lis::~Lis()
{
}

void Lis::akcja()
{
	if (this->zyje == false) return;

	if ((double)std::rand() / RAND_MAX > szansaRuchu) return;

	for (int i = 0; i < zasiegRuchu; i++) {
		std::vector<Wspolrzedne> pola = swiat.sasiedniePola(this->getWspolrzedne());

		pola.erase(std::remove_if(pola.begin(), pola.end(),									// usuniecie z rozwazanych pol te, na ktorych sa organizmy silniejsze
			[this](const Wspolrzedne& w) {
				if (swiat.poleZajete(w) == false) return false;
				if (swiat.organizmNaPolu(w).getSila() > this->getSila()) return true;		
				return false;
			}), pola.end());

		if (pola.size() == 0) return;

		Wspolrzedne cel = pola[std::rand() % pola.size()];

		if (swiat.poleZajete(cel)) {
			kolizja(swiat.organizmNaPolu(cel), cel, true);		// w przypadku wystapienia kolizji, wynikajacymi z niej przemieszczeniami zajmuje sie wlasnie kolizja()
			return;
		}

		przemiescSieNaPole(cel);
	}
}

std::string Lis::podajTyp()
{
	return TYP_LIS;
}

bool Lis::tegoSamegoTypu(Organizm* inny)
{
	if (dynamic_cast<Lis*>(inny) == nullptr) return false;
	return true;
}

Organizm* Lis::stworzDziecko(const Wspolrzedne& wspolrzedne)
{
	return new Lis(this->swiat, wspolrzedne);
}
