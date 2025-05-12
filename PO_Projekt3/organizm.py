from abc import ABC, ABCMeta, abstractmethod

class Organizm(metaclass = ABCMeta):
    def __init__(self, swiat, polozenie, sila, inicjatywa):
        self._swiat = swiat
        self._polozenie = polozenie
        self._sila = sila
        self.__inicjatywa = inicjatywa

        self._zyje = True

        self._wiek = swiat.nadajWiek()

    # GETTERY I SETTERY
    def getSwiat(self):
        return self._swiat

    def getPolozenie(self):
        return self._polozenie

    def setPolozenie(self, polozenie):
        self._polozenie = polozenie

    def getSila(self):
        return self._sila

    def setSila(self, sila):
        self._sila = sila

    def getInicjatywa(self):
        return self.__inicjatywa

    def getZyje(self):
        return self._zyje

    def setZyje(self, zyje):
        self._zyje = zyje

    def getWiek(self):
        return self._wiek

    def setWiek(self, wiek):
        self._wiek = wiek

    # PODSTAWOWE CZYNNOSCI
    @abstractmethod
    def akcja(self):
        pass

    @abstractmethod
    def kolizja(self, atak):
        pass

    def zakonczZycie(self):
        self._zyje = False
        self._swiat.usunOrganizmZPola(self)

    def zamienMiejscami(self, inny):
        self._swiat.usunOrganizmZPola(self)
        self._swiat.usunOrganizmZPola(inny)

        self._polozenie, inny._polozenie = inny._polozenie, self._polozenie

        self._swiat.dodajOrganizmDoPola(self);
        self._swiat.dodajOrganizmDoPola(inny);

    # EFEKTY SPECJALNE
    def odbilAtak(self, atakujacy):
        return False

    def mozeUciec(self):
        return False

    def ucieknij(self, przeciwnik, atak):
        self.zakonczZycie()

    # ROZMNAZANIE
    @abstractmethod
    def tegoSamegoTypu(self, inny):
        pass

    @abstractmethod
    def stworzDziecko(self, cel):
        pass

    # ZAPISYWANIE
    def __str__(self):
        return f"{self.__class__.__name__} {self._polozenie.x} {self._polozenie.y} {self._wiek} {self._sila}\n"

    # RYSOWANIE
    @abstractmethod
    def rysowanie(self):
        pass

    # KOMUNIKATY
    def nazwaKlasy(self):
        return self.__class__.__name__