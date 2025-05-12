from organizm import Organizm
import random

class Zwierze(Organizm):
    def __init__(self, swiat, polozenie, sila, inicjatywa, szansaRuchu, zasiegRuchu, szansaUcieczki, szansaRozmnazania):
        super().__init__(swiat, polozenie, sila, inicjatywa)
        self._szansaRuchu = szansaRuchu
        self._zasiegRuchu = zasiegRuchu
        self._szansaUcieczki = szansaUcieczki
        self._szansaRozmnazania = szansaRozmnazania

    def getSzansaRuchu(self):
        return self._szansaRuchu

    def getZasiegRuchu(self):
        return self._zasiegRuchu

    def getSzansaUcieczki(self):
        return self._szansaUcieczki

    def getSzansaRozmnazania(self):
        return self._szansaRozmnazania

    def akcja(self):
        if not self._zyje:
            return

        if not random.random() < self._szansaRuchu:
            return

        for i in range(self._zasiegRuchu):
            cel = self._swiat.losoweSasiedniePole(self._polozenie)

            if self._swiat.poleZajete(cel):
                self.kolizja(self._swiat.organizmNaPolu(cel), True)
                return

            self.przemiescSieNaPole(cel)

    def kolizja(self, przeciwnik, atak):
        if not atak:
            return

        if self.tegoSamegoTypu(przeciwnik):
            self.rozmnazanie(przeciwnik)
            return

        self.atakowanie(przeciwnik)

    def mozeUciec(self):
        return random.random() < self._szansaUcieczki

    def przemiescSieNaPole(self, polozenie):
        self._swiat.usunOrganizmZPola(self)
        self._polozenie = polozenie
        self._swiat.dodajOrganizmDoPola(self)

    def ucieknij(self, przeciwnik, atak):
        cele = self._swiat.sasiednieWolnePola(self._polozenie)

        if atak:
            if len(cele) == 0:
                return

            cele.append(self._polozenie)
            self.przemiescSieNaPole(self._swiat.losowePole(cele))
        else:
            if len(cele) == 0:
                self.zamienMiejscami(przeciwnik)
                return

            self.przemiescSieNaPole(self._swiat.losowePole(cele))

    def rozmnazanie(self, partner):
        if not self.mozeSieRozmnazac():
            return

        pola = self._swiat.sasiednieWolnePola(self._polozenie)
        polaPartnera = self._swiat.sasiednieWolnePola(partner._polozenie)
        pola.extend(polaPartnera)

        if len(pola) == 0:
            return

        dziecko = self.stworzDziecko(self._swiat.losowePole(pola))
        self._swiat.dodajDziecko(dziecko)

        self._swiat.dodajKomunikat(str(self._polozenie) + ": " + self.nazwaKlasy() + " ma dziecko")

    def mozeSieRozmnazac(self):
        return random.random() < self._szansaRozmnazania

    def atakowanie(self, przeciwnik):
        if self.mozeUciec():
            self.ucieknij(przeciwnik, True)
            self._swiat.dodajKomunikat(str(self._polozenie) + ": " + self.nazwaKlasy() + " ucieka przed " + przeciwnik.nazwaKlasy())
        elif przeciwnik.mozeUciec():
            przeciwnik.ucieknij(self, False)
            self._swiat.dodajKomunikat(str(self._polozenie) + ": " + przeciwnik.nazwaKlasy() + " ucieka przed " + self.nazwaKlasy())
        elif przeciwnik.odbilAtak(self):
            self._swiat.dodajKomunikat(str(self._polozenie) + ": " + przeciwnik.nazwaKlasy() + " odbil atak od " + self.nazwaKlasy())
            return
        elif self._sila >= przeciwnik._sila:
            przeciwnik.kolizja(self, False)
            przeciwnik.zakonczZycie()
            self.przemiescSieNaPole(przeciwnik._polozenie)
            self._swiat.dodajKomunikat(str(self._polozenie) + ": " + self.nazwaKlasy() + " zabija " + przeciwnik.nazwaKlasy())
        else:
            self._swiat.dodajKomunikat(str(self._polozenie) + ": " + przeciwnik.nazwaKlasy() + " broni sie przed " + self.nazwaKlasy())
            przeciwnik.kolizja(self, False)
            self.zakonczZycie()

