#pragma once

#include "Zwierze.h"

class Wilk : public Zwierze
{
public:
    Wilk(Swiat& swiat, const Wspolrzedne& wspolrzedne);
    ~Wilk();

    std::string podajTyp() override;

    bool tegoSamegoTypu(Organizm* inny) override;

    Organizm* stworzDziecko(const Wspolrzedne& wspolrzedne) override;
};

