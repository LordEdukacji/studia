from zwierze import Zwierze
from kierunki import Kierunki
import random

class Czlowiek(Zwierze):
    def __init__(self, swiat, polozenie):
        super().__init__(swiat, polozenie, 5, 4, 1.00, 1, 0.00, 0.00)
        self.__MAX_TRWANIE_ZDOLNOSCI = 5
        self.__MAX_ODNOWIENIE_ZDOLNOSCI = 5
        self.__trwanieZdolnosci = 0
        self.__odnowienieZdolnosci = 0
        self.__zdolnoscAktywna = False
        self.__kierunek = Kierunki.stop

    def getMaxTrwanieZdolnosci(self):
        return self.__MAX_TRWANIE_ZDOLNOSCI

    def getMaxOdnowienieZdolnosci(self):
        return self.__MAX_ODNOWIENIE_ZDOLNOSCI

    def getTrwanieZdolnosci(self):
        return self.__trwanieZdolnosci

    def setTrwanieZdolnosci(self, trwanieZdolnosci):
        self.__trwanieZdolnosci = trwanieZdolnosci

    def getOdnowienieZdolnosci(self):
        return self.__odnowienieZdolnosci

    def setOdnowienieZdolnosci(self, odnowienieZdolnosci):
        self.__odnowienieZdolnosci = odnowienieZdolnosci

    def odbilAtak(self, atakujacy):
        return self.__zdolnoscAktywna

    def getZdolnoscAktywna(self):
        return self.__zdolnoscAktywna

    def setZdolnoscAktywna(self, zdolnoscAktywna):
        self.__zdolnoscAktywna = zdolnoscAktywna

    def getKierunek(self):
        return self.__kierunek

    def setKierunek(self, kierunek):
        self.__kierunek = kierunek

    def akcja(self):
        if not self._zyje:
            return

        if not random.random() < self._szansaRuchu:
            return

        for i in range(self._zasiegRuchu):
            cel = self._swiat.poleWKierunku(self._polozenie, self.__kierunek)

            if self._swiat.poleZajete(cel):
                self.kolizja(self._swiat.organizmNaPolu(cel), True)
                return

            self.przemiescSieNaPole(cel)

        self.__kierunek = Kierunki.stop

    def aktywujZdolnosc(self):
        if self.__odnowienieZdolnosci > 0:
            return

        if self.__zdolnoscAktywna:
            return

        self.__trwanieZdolnosci = self.__MAX_TRWANIE_ZDOLNOSCI
        self.__zdolnoscAktywna = True
        self._swiat.dodajKomunikat(str(self._polozenie) + ": " + self.nazwaKlasy() + " aktywuje zdolnosc")

    def odswiezZdolnosc(self):
        if self.__trwanieZdolnosci > 0:
            self.__trwanieZdolnosci -= 1

        if self.__trwanieZdolnosci == 0 and self.__zdolnoscAktywna == True:
            self.__zdolnoscAktywna = False
            self.__odnowienieZdolnosci = self.__MAX_ODNOWIENIE_ZDOLNOSCI
            return

        if self.__odnowienieZdolnosci > 0:
            self.__odnowienieZdolnosci -= 1

    def tegoSamegoTypu(self, inny):
        return isinstance(inny, Czlowiek)

    def stworzDziecko(self, cel):
        return None     # czlowiek sie nie rozmnaza

    def __str__(self):
        return f"{self.__class__.__name__} {self._polozenie.x} {self._polozenie.y} {self._wiek} {self._sila} {self.__trwanieZdolnosci} {self.__odnowienieZdolnosci} {self.__zdolnoscAktywna}\n"

    def rysowanie(self):
        return (255, 0, 0)
