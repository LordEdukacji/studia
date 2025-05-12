from organizm import Organizm
import random

class Roslina(Organizm):
    def __init__(self, swiat, polozenie, sila, inicjatywa, szansaRozprzestrzeniania, probyRozprzestrzeniania):
        super().__init__(swiat, polozenie, sila, inicjatywa)
        self._szansaRozprzestrzeniania = szansaRozprzestrzeniania
        self._probyRozprzestrzeniania = probyRozprzestrzeniania

    def get_szansaRozprzestrzeniania(self):
        return self._szansaRozprzestrzeniania

    def set_szansaRozprzestrzeniania(self, szansaRozprzestrzeniania):
        self._szansaRozprzestrzeniania = szansaRozprzestrzeniania

    def get_probyRozprzestrzeniania(self):
        return self._probyRozprzestrzeniania

    def set_probyRozprzestrzeniania(self, probyRozprzestrzeniania):
        self._probyRozprzestrzeniania = probyRozprzestrzeniania

    def akcja(self):
        if not self._zyje:
            return
        
        pola = self._swiat.sasiednieWolnePola(self._polozenie)
        
        if len(pola) == 0:
            return
        
        for _ in range(self._probyRozprzestrzeniania):
            if not self.mozeSieRozprzestrzeniac():
                continue

            dziecko = self.stworzDziecko(self._swiat.losowePole(pola))
            self._swiat.dodajDziecko(dziecko)
            self._swiat.dodajKomunikat(str(self._polozenie) + ": " + self.nazwaKlasy() + " rozprzestrzenia sie")

    def kolizja(self, ofiara, atak):
        if atak:
            return

        if self._zyje:
            self.zakonczZycie()

    def mozeSieRozprzestrzeniac(self):
        return random.random() < self._szansaRozprzestrzeniania




