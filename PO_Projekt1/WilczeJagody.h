#pragma once
#include "Roslina.h"

class WilczeJagody : public Roslina
{
public:
    WilczeJagody(Swiat& swiat, const Wspolrzedne& wspolrzedne);
    ~WilczeJagody();

    virtual std::string podajTyp() override;

    bool tegoSamegoTypu(Organizm* inny);

    Organizm* stworzDziecko(const Wspolrzedne& wspolrzedne);

    void kolizja(Organizm& ofiara, const Wspolrzedne& wspolrzedne, bool atak) override;
};
