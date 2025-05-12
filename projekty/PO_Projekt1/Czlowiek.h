#pragma once
#include "Zwierze.h"

class Swiat;

class Czlowiek : public Zwierze
{
private:
	Kierunki kierunek;
	int trwanieZdolnosci, odnowienieZdolnosci;
	const int maxTrwanieZdolnosci, maxOdnowienieZdolnosci;
	bool zdolnoscAktywna;

public:
	Czlowiek(Swiat& swiat, const Wspolrzedne& wspolrzedne);
	~Czlowiek();

	bool odbilAtak(Organizm& atakujacy) override;

	bool tegoSamegoTypu(Organizm* inny) override;

	Organizm* stworzDziecko(const Wspolrzedne& wspolrzedne) override;	// nie powinno zostac uzyte

	void akcja() override;

	void aktywujZdolnosc();
	void odswiezZdolnosc();

	bool czyZdolnoscAktywna() const;
	void setZdoloscAktywna(bool zdolnoscAktywna);
	int getTrwanieZdolnosci() const;
	int getOdnowienieZdolnosci() const;
	void setTrwanieZdolnosci(int trwanieZdolnosci);
	void setOdnowienieZdolnosci(int odnowienieZdolnosci);

	Kierunki getKierunek() const;
	void setKierunek(const Kierunki kierunek);

	std::string podajTyp() override;
	virtual std::string formaDoZapisu() override;
};

