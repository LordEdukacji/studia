#pragma once

#include "Wspolrzedne.h"
#include "Organizm.h"

#include "stale.h"

#include <vector>
#include <conio.h>
#include <iostream>
#include <string>
#include <Windows.h>
#include <algorithm>
#include <fstream>
#include <ctime>

class Organizm;
class Czlowiek;

enum Czynnosci {
	zabija, broniSie, zjada, zatruwaSie, ucieka, odbija, rozmnazaSie, aktywujeZdolnosc
};

enum Kierunki {
	gora, dol, prawo, lewo, stop
};

class Swiat
{
private:
	int wysokosc, szerokosc;
	int tura;					// nie obecna tura - ale tyle tur ile juz minelo
	int wiek;					// wiek swiata jako liczba organizmow, ktore zostaly w nim stworzone

	std::vector<Organizm*> organizmy;						// zbior wszystkich organizmow

	std::vector<std::vector<Organizm*>> mapaOrganizmow;		// mapa organizmow

	std::vector<std::string> komunikaty;

	HANDLE konsola;

	Czlowiek* czlowiek;

	void tworzNowySwiat();
	void resetSwiata();
	void dodajPoczatkoweOrganizmy();
	void dodajLosowyOrganizm(const Wspolrzedne& wspolrzedne);
	void wczytajWymiary();

	char wczytajKlawisz();

	void wykonajTure();
	void usunMartweOrganizmy();									// organizmy oznaczone jako martwe w biezacej turze sa usuwane

	void rysujSwiat();
	void wypiszPodpisAutora();
	void rysujMape();
	void rysujOrganizmy();
	void wypiszInformacje();
	void wypiszKomunikaty();

	void idzDoXYMapy(const Wspolrzedne& wspolrzedne);

	void zapisGry();
	void wczytanieGry();

public:
	Swiat();
	~Swiat();
	
	bool wybierzAkcje();										// zwraca bool by wiedziec czy kontynuowac dzialanie programu

	void dodajDziecko(Organizm* dziecko);						// dodaje dziecko do zbioru organizmow

	bool poleZajete(const Wspolrzedne& wspolrzedne);
	Organizm& organizmNaPolu(const Wspolrzedne& wspolrzedne);
	void wyczyscPoleMapy(const Wspolrzedne& wspolrzedne);		// organizmy beda widziec dane pole jako wolne
	void usunOrganizmZMapy(const Organizm& organizm);			// pole na ktorym znajduje sie organizm stanie sie wolne
	void dodajDoPolaMapy(Organizm& organizm);

	int nadajWiek();											// unikalna liczba uzywana do porownywania starszenstwa organizmow

	std::vector<Wspolrzedne> sasiedniePola(const Wspolrzedne& wspolrzedne);
	std::vector<Wspolrzedne> sasiednieWolnePola(const Wspolrzedne& wspolrzedne);
	Wspolrzedne poleWKierunku(const Wspolrzedne& wspolrzedne, const Kierunki& kierunek);

	void rysujSymbolNaPolu(char symbol, Wspolrzedne& wspolrzedne);

	void dodajKomunikat(std::string napastnik, std::string ofiara, Czynnosci czynnosc, Wspolrzedne wspolrzedne);
};

