#pragma once

#include "Wspolrzedne.h"
#include "Swiat.h"

class Swiat;

class Organizm
{
private:
	const int inicjatywa;
	const char oznaczenie;
	const int wiek;
protected:
	Swiat& swiat;

	Wspolrzedne wspolrzedne;

	int sila;
	
	bool zyje;
	
public:
	Organizm(Swiat& swiat, const Wspolrzedne& wspolrzedne, const int sila, const int inicjatywa, const char oznaczenie);
	~Organizm();

	Wspolrzedne getWspolrzedne() const;
	int getSila() const;
	int getInicjatywa() const;
	bool czyZyje() const;
	char getOznaczenie() const;
	int getWiek() const;

	void setWspolrzedne(const Wspolrzedne& wspolrzedne);
	void setSila(int sila);
	void zmienSile(const int zmiana);
	void setZyje(const bool zyje);

	virtual void akcja() = 0;

	virtual void kolizja(Organizm& ofiara, const Wspolrzedne& wspolrzedne, bool atak) = 0;		// przy starciu jest wywolywana kolizja() dla organizmu atakujacego z atak=true
																								// odpowiada wtedy za zabijanie, i przemieszczanie sie
																								// z tego poziomu jest wtedy wywolywana kolizja() dla organizmu broniacego sie z atak=false
																								// implementuje to specjalne efekty przy umieraniu / byciu zjadanym oraz nadanie komunikatu o smierci
	void rysowanie();

	virtual bool odbilAtak(Organizm& atakujacy);

	virtual bool mozeUciec();
	virtual void ucieknij(Organizm& napastnik, const Wspolrzedne& wspolrzedne, bool atak);

	void zamienMiejscami(Organizm& inny);

	void zakonczZycie();													// ustawia jako martwy (czyli zostanie usuniety) i usuwa odniesienie z mapy

	virtual bool tegoSamegoTypu(Organizm* inny) = 0;

	virtual Organizm* stworzDziecko(const Wspolrzedne& wspolrzedne) = 0;	// zwraca nowy organizm tego samego typu

	virtual std::string podajTyp();											// nazwa gatunku
	virtual std::string formaDoZapisu();									// forma do zapisania w pliku
};
