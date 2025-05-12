#include "Czlowiek.h"

Czlowiek::Czlowiek(Swiat& swiat, const Wspolrzedne& wspolrzedne)
	: Zwierze(swiat, wspolrzedne, 5, 4, '@', 1.00, 1, 0.00, 0.00),
	kierunek(stop), trwanieZdolnosci(0), odnowienieZdolnosci(0),
	maxTrwanieZdolnosci(MAX_TRWANIE_ZDOLNOSCI), maxOdnowienieZdolnosci(MAX_ODNOWIENIE_ZDOLNOSCI), zdolnoscAktywna(false)
{
}

Czlowiek::~Czlowiek()
{
}

std::string Czlowiek::podajTyp()
{
	return TYP_CZLOWIEK;
}

std::string Czlowiek::formaDoZapisu()
{
	std::string zapis = this->podajTyp()
				+ " " + std::to_string(this->getWspolrzedne().getX())
				+ " " + std::to_string(this->getWspolrzedne().getY())
				+ " " + std::to_string(this->getSila())
				+ " " + std::to_string(this->getTrwanieZdolnosci())
				+ " " + std::to_string(this->getOdnowienieZdolnosci())
				+ " " + std::to_string(this->czyZdolnoscAktywna()) + "\n";

	return zapis;
}

bool Czlowiek::odbilAtak(Organizm& atakujacy)
{
	if (zdolnoscAktywna == true) return true;
	return false;
}

bool Czlowiek::tegoSamegoTypu(Organizm* inny)
{
	if (dynamic_cast<Czlowiek*>(inny) == nullptr) return false;
	return true;
}

Organizm* Czlowiek::stworzDziecko(const Wspolrzedne& wspolrzedne)
{
	return new Czlowiek(this->swiat, wspolrzedne);
}

void Czlowiek::akcja()
{
	if (this->zyje == false) return;
	if ((double)std::rand() / RAND_MAX > szansaRuchu) return;

	for (int i = 0; i < zasiegRuchu; i++) {
		Wspolrzedne cel = swiat.poleWKierunku(this->getWspolrzedne(), this->getKierunek());

		if (swiat.poleZajete(cel)) {
			kolizja(swiat.organizmNaPolu(cel), cel, true);		// w przypadku wystapienia kolizji, wynikajacymi z niej przemieszczeniami zajmuje sie wlasnie kolizja()
			this->setKierunek(stop);
			return;
		}

		przemiescSieNaPole(cel);
	}
	this->setKierunek(stop);
}

void Czlowiek::aktywujZdolnosc()
{
	if (odnowienieZdolnosci > 0) return;

	trwanieZdolnosci = maxTrwanieZdolnosci;
	zdolnoscAktywna = true;
	swiat.dodajKomunikat(this->podajTyp(), this->podajTyp(), aktywujeZdolnosc, this->getWspolrzedne());
}

void Czlowiek::odswiezZdolnosc()
{
	if (trwanieZdolnosci > 0) trwanieZdolnosci--;

	if (trwanieZdolnosci == 0 && zdolnoscAktywna == true) {
		zdolnoscAktywna = false;
		odnowienieZdolnosci = maxOdnowienieZdolnosci;
		return;
	}

	if (odnowienieZdolnosci > 0) odnowienieZdolnosci--;
}

bool Czlowiek::czyZdolnoscAktywna() const
{
	return zdolnoscAktywna;
}

void Czlowiek::setZdoloscAktywna(bool zdolnoscAktywna)
{
	this->zdolnoscAktywna = zdolnoscAktywna;
}

int Czlowiek::getTrwanieZdolnosci() const
{
	return trwanieZdolnosci;
}

int Czlowiek::getOdnowienieZdolnosci() const
{
	return odnowienieZdolnosci;
}

void Czlowiek::setTrwanieZdolnosci(int trwanieZdolnosci)
{
	this->trwanieZdolnosci = trwanieZdolnosci;
}

void Czlowiek::setOdnowienieZdolnosci(int odnowienieZdolnosci)
{
	this->odnowienieZdolnosci = odnowienieZdolnosci;
}

Kierunki Czlowiek::getKierunek() const
{
	return kierunek;
}

void Czlowiek::setKierunek(const Kierunki kierunek)
{
	this->kierunek = kierunek;
}

