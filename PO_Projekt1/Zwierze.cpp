#include "Zwierze.h"

Zwierze::Zwierze(Swiat& swiat, const Wspolrzedne& wspolrzedne, const int sila, const int inicjatywa, const char oznaczenie, const double szansaRuchu, const int zasiegRuchu, const double szansaUcieczki, const double szansaRozmazania)
	: Organizm(swiat, wspolrzedne, sila, inicjatywa, oznaczenie), szansaRuchu(szansaRuchu), zasiegRuchu(zasiegRuchu), szansaUcieczki(szansaUcieczki), szansaRozmazania(szansaRozmazania)
{
}

Zwierze::~Zwierze()
{
}

void Zwierze::akcja()
{
	if (this->zyje == false) return;
	if ((double)std::rand() / RAND_MAX > szansaRuchu) return;

	for (int i = 0; i < zasiegRuchu; i++) {
		std::vector<Wspolrzedne> pola = swiat.sasiedniePola(this->getWspolrzedne());

		if (pola.size() == 0) return;

		Wspolrzedne cel = pola[std::rand() % pola.size()];

		if (swiat.poleZajete(cel)) {
			kolizja(swiat.organizmNaPolu(cel), cel, true);		// w przypadku wystapienia kolizji, wynikajacymi z niej przemieszczeniami zajmuje sie wlasnie kolizja()
			return;
		}

		przemiescSieNaPole(cel);
	}
}

void Zwierze::kolizja(Organizm& ofiara, const Wspolrzedne& wspolrzedne, bool atak)
{
	if (atak == true) {
		if (this->tegoSamegoTypu(&ofiara)) {
			rozmnazanie(ofiara, wspolrzedne);
			return;
		}

		atakowanie(ofiara, wspolrzedne);
		return;
	}

	if (atak == false) {
		if (this->czyZyje() == false) swiat.dodajKomunikat(ofiara.podajTyp(), this->podajTyp(), zabija, wspolrzedne);

		if (ofiara.czyZyje() == false) swiat.dodajKomunikat(ofiara.podajTyp(), this->podajTyp(), broniSie, wspolrzedne);
	}
}

void Zwierze::przemiescSieNaPole(Wspolrzedne& wspolrzedne)
{
	swiat.usunOrganizmZMapy(*this);
	setWspolrzedne(wspolrzedne);
	swiat.dodajDoPolaMapy(*this);			// sprawiamy, ze pole na mapie przestaje wskazywac na dany organizm i wskazuje teraz na niego inne pole
}

bool Zwierze::mozeUciec()
{
	if ((double)std::rand() / RAND_MAX < szansaUcieczki) return true;
	return false;
}

void Zwierze::ucieknij(Organizm& napastnik, const Wspolrzedne& wspolrzedne, bool atak)
{
	std::vector<Wspolrzedne> pola = swiat.sasiednieWolnePola(wspolrzedne);

	if (atak == true) {
		if (pola.size() == 0) return;				// nie majac innych mozliwosci ucieczki, napastnik ucieka na pole z ktorego sam przyszedl

		pola.push_back(this->getWspolrzedne());		// zwierze moze uciec tez na pole z ktorego przyszlo nawet gdy ma inne opcje

		Wspolrzedne cel = pola[std::rand() % pola.size()];

		przemiescSieNaPole(cel);
	}
	
	if (atak == false) {
		if (pola.size() == 0) {
			this->zamienMiejscami(napastnik);	// nie majac innych mozliwosci ucieczki, zwierze ucieka na pole z ktorego przyszedl napastnik (unik!)
												// w przeciwnym wypadku probuje uciec potencjalnie dalej
			return;
		}
		Wspolrzedne cel = pola[std::rand() % pola.size()];

		przemiescSieNaPole(cel);
	}
}

bool Zwierze::mozeSieRozmnazac() const
{
	if ((double)std::rand() / RAND_MAX > szansaRozmazania) return false;
	return true;
}

void Zwierze::rozmnazanie(Organizm& partner, const Wspolrzedne& wspolrzedne)
{
	if (this->mozeSieRozmnazac() == false) return;

	std::vector<Wspolrzedne> pola1 = swiat.sasiednieWolnePola(this->getWspolrzedne());
	std::vector<Wspolrzedne> pola2 = swiat.sasiednieWolnePola(partner.getWspolrzedne());

	std::vector<Wspolrzedne> pola = pola1;
	pola.insert(pola.end(), pola2.begin(), pola2.end());

	std::sort(pola.begin(), pola.end(), [](const Wspolrzedne& a, const Wspolrzedne& b) {	// sortowanie pol wedlug X i Y aby moc usunac potem duplikaty
		if (a.getX() < b.getX()) return true;
		else if (a.getX() > b.getX()) return false;
		return a.getY() < b.getY();
		});

	pola.erase(std::unique(pola.begin(), pola.end()), pola.end());							// usuniecie duplikatow pol

	if (pola.size() == 0) return;

	Wspolrzedne cel = pola[std::rand() % pola.size()];

	Organizm* dziecko = stworzDziecko(cel);
	swiat.dodajDziecko(dziecko);

	swiat.dodajKomunikat(this->podajTyp(), partner.podajTyp(), rozmnazaSie, wspolrzedne);
}

void Zwierze::atakowanie(Organizm& ofiara, const Wspolrzedne& wspolrzedne)
{
	if (this->mozeUciec()) {
		this->ucieknij(ofiara, wspolrzedne, true);
		swiat.dodajKomunikat(ofiara.podajTyp(), this->podajTyp(), ucieka, wspolrzedne);
		return;
	}

	if (ofiara.mozeUciec()) {
		ofiara.ucieknij(*this, wspolrzedne, false);
		swiat.dodajKomunikat(this->podajTyp(), ofiara.podajTyp(), ucieka, wspolrzedne);
		return;
	}

	if (ofiara.odbilAtak(*this)) {
		swiat.dodajKomunikat(this->podajTyp(), ofiara.podajTyp(), odbija, wspolrzedne);
		return;
	}

	if (this->sila >= ofiara.getSila()) {	// gdy sily sa rowne napastnik wygra
		ofiara.zakonczZycie();
		Wspolrzedne cel = wspolrzedne;	// ze wzgledu na const referencjê
		przemiescSieNaPole(cel);
	}
	else {
		this->zakonczZycie();
	}

	ofiara.kolizja(*this, wspolrzedne, false);
}
