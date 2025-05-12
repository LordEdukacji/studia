#include "Organizm.h"

Organizm::Organizm(Swiat& swiat, const Wspolrzedne& wspolrzedne, const int sila, const int inicjatywa, const char oznaczenie)
	: swiat(swiat), wspolrzedne(wspolrzedne), sila(sila), inicjatywa(inicjatywa), zyje(true), oznaczenie(oznaczenie), wiek(swiat.nadajWiek())
{
	swiat.dodajDoPolaMapy(*this);
}

Organizm::~Organizm()
{
}

Wspolrzedne Organizm::getWspolrzedne() const
{
	return wspolrzedne;
}

int Organizm::getSila() const
{
	return sila;
}

int Organizm::getInicjatywa() const
{
	return inicjatywa;
}

bool Organizm::czyZyje() const
{
	return zyje;
}

char Organizm::getOznaczenie() const
{
	return oznaczenie;
}

int Organizm::getWiek() const
{
	return wiek;
}

void Organizm::setWspolrzedne(const Wspolrzedne& wspolrzedne)
{
	this->wspolrzedne = wspolrzedne;
}

void Organizm::setSila(int sila)
{
	this->sila = sila;
}

void Organizm::zmienSile(const int zmiana)
{
	sila += zmiana;
}

void Organizm::setZyje(const bool zyje)
{
	this->zyje = zyje;
}

void Organizm::rysowanie()
{
	swiat.rysujSymbolNaPolu(oznaczenie, wspolrzedne);
}

bool Organizm::odbilAtak(Organizm& atakujacy)
{
	return false;
}

bool Organizm::mozeUciec()
{
	return false;
}

void Organizm::ucieknij(Organizm& napastnik, const Wspolrzedne& wspolrzedne, bool atak)
{
	zakonczZycie();	// na wypadek gdyby organizm, ktory nie moze uciekac, uciekal (nie powinno sie wydarzyc)
}

void Organizm::zamienMiejscami(Organizm& inny)
{
	swiat.usunOrganizmZMapy(*this);
	swiat.usunOrganizmZMapy(inny);

	Wspolrzedne tmp = this->getWspolrzedne();
	this->setWspolrzedne(inny.getWspolrzedne());
	inny.setWspolrzedne(tmp);

	swiat.dodajDoPolaMapy(*this);
	swiat.dodajDoPolaMapy(inny);
}

void Organizm::zakonczZycie()
{
	zyje = false;
	swiat.usunOrganizmZMapy(*this);
}

std::string Organizm::podajTyp()
{
	return "Organizm";
}

std::string Organizm::formaDoZapisu()
{
	std::string zapis = this->podajTyp()
				+ " " + std::to_string(this->getWspolrzedne().getX())
				+ " " + std::to_string(this->getWspolrzedne().getY())
				+ " " + std::to_string(this->getSila()) + "\n";

	return zapis;
}

