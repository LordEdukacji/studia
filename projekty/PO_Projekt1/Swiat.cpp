#include "Swiat.h"
#include "Zwierze.h"
#include "Wilk.h"
#include "Owca.h"
#include "Zolw.h"
#include "Antylopa.h"
#include "Lis.h"
#include "Trawa.h"
#include "Mlecz.h"
#include "Guarana.h"
#include "WilczeJagody.h"
#include "BarszczSosnowskiego.h"
#include "Czlowiek.h"

Swiat::Swiat()
{
    konsola = GetStdHandle(STD_OUTPUT_HANDLE);

    std::srand(std::time(0));

    tworzNowySwiat();
}

Swiat::~Swiat()
{
    resetSwiata();
}
//--------------------------------------------------------------------------
void Swiat::tworzNowySwiat()
{
    system("cls");

    wczytajWymiary();

    mapaOrganizmow.resize(wysokosc, std::vector<Organizm*>(szerokosc, nullptr));

    tura = 0;
    wiek = 0;

    dodajPoczatkoweOrganizmy();

    rysujSwiat();
}

void Swiat::resetSwiata()
{
    czlowiek = nullptr;

    komunikaty.clear();

    for (std::vector<Organizm*> rzad : mapaOrganizmow) rzad.clear();
    mapaOrganizmow.clear();

    for (Organizm* organizm : organizmy) delete organizm;
    organizmy.clear();
}

void Swiat::dodajPoczatkoweOrganizmy()
{
    int x = std::rand() % szerokosc;
    int y = std::rand() % wysokosc;

    Czlowiek* c = new Czlowiek(*this, Wspolrzedne(x, y));
    czlowiek = c;
    organizmy.push_back(c);

    for (int i = 0; i < wysokosc; i++) {
        for (int j = 0; j < szerokosc; j++) {
            if (poleZajete(Wspolrzedne(j, i)) == true) continue;
            if ((double)std::rand() / RAND_MAX > SZANSA_ZAJECIA_POLA) continue;
            dodajLosowyOrganizm(Wspolrzedne(j, i));
        }
    }
}

void Swiat::dodajLosowyOrganizm(const Wspolrzedne& wspolrzedne)
{
    std::srand(std::rand());
    int wybor = std::rand() % (LICZBA_TYPOW_ORGANIZMOW - 1); // nie mozemy wygenerowac kolejnego czlowieka
    Organizm* o;

    switch (wybor) {
    case 0:
        o = new Wilk(*this, wspolrzedne);
        break;
    case 1:
        o = new Owca(*this, wspolrzedne);
        break;
    case 2:
        o = new Lis(*this, wspolrzedne);
        break;
    case 3:
        o = new Zolw(*this, wspolrzedne);
        break;
    case 4:
        o = new Antylopa(*this, wspolrzedne);
        break;
    case 5:
        o = new Trawa(*this, wspolrzedne);
        break;
    case 6:
        o = new Mlecz(*this, wspolrzedne);
        break;
    case 7:
        o = new Guarana(*this, wspolrzedne);
        break;
    case 8:
        o = new WilczeJagody(*this, wspolrzedne);
        break;
    case 9:
        o = new BarszczSosnowskiego(*this, wspolrzedne);
        break;
    }

    organizmy.push_back(o);
}

void Swiat::wczytajWymiary()
{
    while (true) {
        std::cout << "Podaj wysokosc: ";
        std::cin >> wysokosc;

        system("cls");

        if (std::cin.fail()) {
            std::cin.clear();
            std::cin.ignore(INT_MAX, '\n');
            std::cout << "Podaj dodatnia liczbe!" << std::endl;
            continue;
        }
        if (wysokosc <= 0) {
            std::cout << "Podaj dodatnia liczbe!" << std::endl;
            continue;
        }

        break;
    }

    while (true) {
        std::cout << "Podaj szerokosc: ";
        std::cin >> szerokosc;

        system("cls");

        if (std::cin.fail()) {
            std::cin.clear();
            std::cin.ignore(INT_MAX, '\n');
            std::cout << "Podaj dodatnia liczbe!" << std::endl;
            continue;
        }
        if (szerokosc <= 0) {
            std::cout << "Podaj dodatnia liczbe!" << std::endl;
            continue;
        }

        break;
    }
}
//--------------------------------------------------------------------------
char Swiat::wczytajKlawisz()
{
    char klawisz = _getch();
    return klawisz;
}
//--------------------------------------------------------------------------
void Swiat::wykonajTure()
{
    std::sort(organizmy.begin(), organizmy.end(),
        [](const Organizm* pierwszy, const Organizm* drugi) {
            if (pierwszy->getInicjatywa() != drugi->getInicjatywa()) return pierwszy->getInicjatywa() > drugi->getInicjatywa();

            return pierwszy->getWiek() < drugi->getWiek();
        });

    const size_t rozmiar = organizmy.size();

    for (int i = 0; i < rozmiar; i++) organizmy[i]->akcja();
    if (czlowiek != nullptr) czlowiek->odswiezZdolnosc();

    usunMartweOrganizmy();
}

void Swiat::usunMartweOrganizmy()
{
    organizmy.erase(std::remove_if(organizmy.begin(), organizmy.end(),          // usuniecie martwych organizmow
        [this](const Organizm* o) {
            if (o->czyZyje() == false) {
                if (o == czlowiek) czlowiek = nullptr;
                delete o;
                return true;
            }
            return false;
        }), organizmy.end());
}
//--------------------------------------------------------------------------
void Swiat::rysujSwiat()
{
    system("cls");
    wypiszPodpisAutora();
    wypiszInformacje();
    rysujMape();
    rysujOrganizmy();
    wypiszKomunikaty();
}

void Swiat::wypiszPodpisAutora()
{
    SetConsoleCursorPosition(konsola, { X_AUTOR, Y_AUTOR });
    std::cout << "| ADRIAN DYBOWSKI 193483 |" << std::endl;
    std::cout << "+------------------------+";

}

void Swiat::rysujMape()
{
    for (int i = -1; i < wysokosc+1; i++) {
        for (int j = -1; j < szerokosc+1; j++) {
            idzDoXYMapy(Wspolrzedne(j, i));
            if (i == -1 || j == -1 || i == wysokosc || j == szerokosc) std::cout << RAMKA;
            std::cout << PUSTE_POLE;
        }
    }
}

void Swiat::rysujOrganizmy()
{
    for (Organizm* organizm : organizmy) organizm->rysowanie();
}

void Swiat::wypiszInformacje()
{
    SetConsoleCursorPosition(konsola, { X_INFORMACJE, Y_INFORMACJE });

    if (czlowiek == nullptr) std::cout << "Gracz zostal pokonany" << std::endl;
    else {
        std::cout << "(" << czlowiek->getWspolrzedne().getX() << ", " << czlowiek->getWspolrzedne().getY() << ") ";
        if (czlowiek->czyZdolnoscAktywna() == true) std::cout << "Zdolnosc aktywna, pozostalo " << czlowiek->getTrwanieZdolnosci() << " tury";
        else if (czlowiek->getOdnowienieZdolnosci() != 0) std::cout << "Zdolnosc konczy sie za " << czlowiek->getOdnowienieZdolnosci() << " tury";
        else std::cout << "Zdolnosc gotowa do uzycia";

        std::cout << std::endl;
    }
    std::cout << std::endl << "Sterowanie:\nspacja - nowa tura\nstrzalki - ruch\nZ - zdolnosc\nN - nowa gra\nS - zapisz gre\nL - wczytaj gre\nQ - wyjdz z gry\n";

    std::cout << std::endl << "Legenda:\nA - antylopa\nB - barszcz sosnowskiego\nG - guarana\nL - lis\nM - mlecz\nO - owca\nT - trawa\nJ - wilcze jagody\nW - wilk\nZ - zolw\n@ - czlowiek\n";
}

void Swiat::wypiszKomunikaty()
{
    if (tura == 0) return;

    SetConsoleCursorPosition(konsola, { (short)X_KOMUNIKATY, (short)Y_KOMUNIKATY });

    std::cout << "Minela tura " << tura << ":" << std::endl;

    if (komunikaty.size() == 0) std::cout << "\tSpokojne zycie..." << std::endl;

    int licznik = 0;
    for (std::string komunikat : komunikaty) {
        std::cout << "\t" << komunikat << std::endl;
        licznik++;
        if (licznik >= LIMIT_KOMUNIKATOW) break;
    }
    int pozostale = komunikaty.size() - LIMIT_KOMUNIKATOW;
    if (pozostale > 0) std::cout << "+" << pozostale << " innych" << std::endl;

    komunikaty.clear();
}
//--------------------------------------------------------------------------
void Swiat::idzDoXYMapy(const Wspolrzedne& wspolrzedne)
{
    COORD c = { 0,0 };

    c.X = wspolrzedne.getX() + X_MAPY;
    c.Y = wspolrzedne.getY() + Y_MAPY;

    SetConsoleCursorPosition(konsola, c);
}
//--------------------------------------------------------------------------
void Swiat::zapisGry()
{
    std::string nazwaPliku;
    while (nazwaPliku.empty()) {
        system("cls");
        std::cout << "Podaj nazwe pliku do zapisania: ";
        std::cin >> nazwaPliku;
    }
    std::string sciezkaPliku = "zapisaneGry/" + nazwaPliku + ".txt";
    std::ofstream plikZapisu(sciezkaPliku);

    plikZapisu << wysokosc << std::endl << szerokosc << std::endl << tura << std::endl << wiek << std::endl << organizmy.size() << std::endl;
    for (Organizm* o : organizmy) plikZapisu << o->formaDoZapisu();

    plikZapisu.close();
}

void Swiat::wczytanieGry()
{
    std::string nazwaPliku;
    while (nazwaPliku.empty()) {
        system("cls");
        std::cout << "Podaj nazwe pliku do wczytania: ";
        std::cin >> nazwaPliku;
    }
    std::string sciezkaPliku = "zapisaneGry/" + nazwaPliku + ".txt";
    std::ifstream plikOdczytu(sciezkaPliku);

    if (!plikOdczytu.is_open()) {
        std::cout << "Zapisana gra o takiej nazwie nie istnieje!" << std::endl << "(Nacisnij dowolny przycisk)";
        _getch();
        system("cls");
        return;
    }

    resetSwiata();
    plikOdczytu >> wysokosc >> szerokosc;
    mapaOrganizmow.resize(wysokosc, std::vector<Organizm*>(szerokosc, nullptr));
    plikOdczytu >> tura >> wiek;

    size_t iloscOrganizmow;
    plikOdczytu >> iloscOrganizmow;

    for (int i = 0; i < iloscOrganizmow; i++) {
        std::string typ;
        plikOdczytu >> typ;
        int x, y, sila;
        plikOdczytu >> x >> y >> sila;
        Organizm* o = nullptr;
        if (typ == TYP_WILK) o = new Wilk(*this, Wspolrzedne(x, y));
        if (typ == TYP_OWCA) o = new Owca(*this, Wspolrzedne(x, y));
        if (typ == TYP_LIS) o = new Lis(*this, Wspolrzedne(x, y));
        if (typ == TYP_ZOLW) o = new Zolw(*this, Wspolrzedne(x, y));
        if (typ == TYP_ANTYLOPA) o = new Antylopa(*this, Wspolrzedne(x, y));
        if (typ == TYP_TRAWA) o = new Trawa(*this, Wspolrzedne(x, y));
        if (typ == TYP_MLECZ) o = new Mlecz(*this, Wspolrzedne(x, y));
        if (typ == TYP_GUARANA) o = new Guarana(*this, Wspolrzedne(x, y));
        if (typ == TYP_JAGODY) o = new WilczeJagody(*this, Wspolrzedne(x, y));
        if (typ == TYP_BARSZCZ) o = new BarszczSosnowskiego(*this, Wspolrzedne(x, y));
        if (typ == TYP_CZLOWIEK) {
            Czlowiek* c = new Czlowiek(*this, Wspolrzedne(x, y));
            czlowiek = c;
            int trwanie, odnowienie;
            plikOdczytu >> trwanie >> odnowienie;
            c->setTrwanieZdolnosci(trwanie);
            c->setOdnowienieZdolnosci(odnowienie);
            bool zdolnosc;
            plikOdczytu >> zdolnosc;
            czlowiek->setZdoloscAktywna(zdolnosc);
            czlowiek->setSila(sila);

            organizmy.push_back(c);
        }

        if (o != nullptr) {
            o->setSila(sila);
            organizmy.push_back(o);
        }
    }
    plikOdczytu.close();
    rysujSwiat();
}
//--------------------------------------------------------------------------
bool Swiat::wybierzAkcje()
{
    char klawisz = wczytajKlawisz();

    switch (klawisz) {
    case STRZALKA:
        klawisz = wczytajKlawisz();
        if (czlowiek == nullptr) break;     // jesli czlowiek zostal zabity to nie mozemy sie do niego odnosic
        switch (klawisz) {
        case STRZALKA_GORA:
            czlowiek->setKierunek(gora);
            break;
        case STRZALKA_DOL:
            czlowiek->setKierunek(dol);
            break;
        case STRZALKA_PRAWO:
            czlowiek->setKierunek(prawo);
            break;
        case STRZALKA_LEWO:
            czlowiek->setKierunek(lewo);
            break;
        }
        break;
    case ZDOLNOSC:
        if (czlowiek != nullptr) czlowiek->aktywujZdolnosc();
        rysujSwiat();     // natychmiastowo zmienic informacje
        break;
    case SPACJA:
        wykonajTure();
        tura++;
        rysujSwiat();
        break;
    case ZAPISZ:
        zapisGry();
        rysujSwiat();
        break;
    case WCZYTAJ:
        wczytanieGry();
        rysujSwiat();
        break;
    case NOWA_GRA:
        resetSwiata();
        tworzNowySwiat();
        break;
    case WYJSCIE_Z_GRY:
        return false;
        break;
    }

    return true;
}
//--------------------------------------------------------------------------
void Swiat::dodajDziecko(Organizm* dziecko)
{
    organizmy.push_back(dziecko);
}
//--------------------------------------------------------------------------
bool Swiat::poleZajete(const Wspolrzedne& wspolrzedne)
{
    if (mapaOrganizmow[wspolrzedne.getY()][wspolrzedne.getX()] == nullptr) return false;
    return true;
}

Organizm& Swiat::organizmNaPolu(const Wspolrzedne& wspolrzedne)
{
    return *mapaOrganizmow[wspolrzedne.getY()][wspolrzedne.getX()];
}

void Swiat::wyczyscPoleMapy(const Wspolrzedne& wspolrzedne)
{
    mapaOrganizmow[wspolrzedne.getY()][wspolrzedne.getX()] = nullptr;
}

void Swiat::usunOrganizmZMapy(const Organizm& organizm)
{
    mapaOrganizmow[organizm.getWspolrzedne().getY()][organizm.getWspolrzedne().getX()] = nullptr;
}

void Swiat::dodajDoPolaMapy(Organizm& organizm)
{
    if (poleZajete(organizm.getWspolrzedne())) organizm.setZyje(false);     // jesli nowy organizm chce byc dodany do zajetego juz pola to nie pozwalamy, bezposrednio jest usmiercany
    mapaOrganizmow[organizm.getWspolrzedne().getY()][organizm.getWspolrzedne().getX()] = &organizm;
}
//--------------------------------------------------------------------------
int Swiat::nadajWiek()
{
    return wiek++;
}

std::vector<Wspolrzedne> Swiat::sasiedniePola(const Wspolrzedne& wspolrzedne)
{
    std::vector<Wspolrzedne> pola;

    if (wspolrzedne.getY() > 0) pola.push_back(Wspolrzedne(wspolrzedne.getX(), wspolrzedne.getY() - 1));

    if (wspolrzedne.getY() < wysokosc - 1) pola.push_back(Wspolrzedne(wspolrzedne.getX(), wspolrzedne.getY() + 1));

    if (wspolrzedne.getX() > 0) pola.push_back(Wspolrzedne(wspolrzedne.getX() - 1, wspolrzedne.getY()));

    if (wspolrzedne.getX() < szerokosc - 1) pola.push_back(Wspolrzedne(wspolrzedne.getX() + 1, wspolrzedne.getY()));

    return pola;
}

std::vector<Wspolrzedne> Swiat::sasiednieWolnePola(const Wspolrzedne& wspolrzedne)
{
    std::vector<Wspolrzedne> pola = sasiedniePola(wspolrzedne);

    pola.erase(std::remove_if(pola.begin(), pola.end(), [this](Wspolrzedne wsp) {return mapaOrganizmow[wsp.getY()][wsp.getX()] != nullptr;  }), pola.end());    // usuniecie zajetych pol

    return pola;
}

Wspolrzedne Swiat::poleWKierunku(const Wspolrzedne& wspolrzedne, const Kierunki& kierunek)
{
    switch (kierunek) {
    case gora:
        if (wspolrzedne.getY() > 0) return Wspolrzedne(wspolrzedne.getX(), wspolrzedne.getY() - 1);
        break;
    case dol:
        if (wspolrzedne.getY() < wysokosc - 1) return Wspolrzedne(wspolrzedne.getX(), wspolrzedne.getY() + 1);
        break;
    case lewo:
        if (wspolrzedne.getX() > 0) return Wspolrzedne(wspolrzedne.getX() - 1, wspolrzedne.getY());
        break;
    case prawo:
        if (wspolrzedne.getX() < szerokosc - 1) return Wspolrzedne(wspolrzedne.getX() + 1, wspolrzedne.getY());
        break;
    }
    return wspolrzedne;
}
//--------------------------------------------------------------------------
void Swiat::rysujSymbolNaPolu(char symbol, Wspolrzedne& wspolrzedne)
{
    idzDoXYMapy(wspolrzedne);

    std::cout << symbol;
}
//--------------------------------------------------------------------------
void Swiat::dodajKomunikat(std::string napastnik, std::string ofiara, Czynnosci czynnosc, Wspolrzedne wspolrzedne)
{
    std::string komunikat;

    komunikat += "Na polu ";
    komunikat += wspolrzedne.podajTyp();
    komunikat += ": ";

    switch (czynnosc) {
    case zabija:
        komunikat += napastnik;
        komunikat += " zabija ";
        komunikat += ofiara;
        komunikat += ".";
        break;
    case broniSie:
        komunikat += ofiara;
        komunikat += " broni sie przed ";
        komunikat += napastnik;
        komunikat += ".";
        break;
    case zjada:
        komunikat += napastnik;
        komunikat += " zjada ";
        komunikat += ofiara;
        komunikat += ".";
        break;
    case zatruwaSie:
        komunikat += napastnik;
        komunikat += " zatruwa sie ";
        komunikat += ofiara;
        komunikat += ".";
        break;
    case ucieka:
        komunikat += ofiara;
        komunikat += " ucieka przed ";
        komunikat += napastnik;
        komunikat += ".";
        break;
    case odbija:
        komunikat += ofiara;
        komunikat += " odbija atak ";
        komunikat += napastnik;
        break;
    case rozmnazaSie:
        komunikat += napastnik;
        komunikat += " rozmnaza sie.";
        break;
    case aktywujeZdolnosc:
        komunikat += napastnik;
        komunikat += " aktywuje zdolnosc specjalna.";
        break;
    }

    komunikaty.push_back(komunikat);
}
