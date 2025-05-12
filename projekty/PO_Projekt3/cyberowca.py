from zwierze import Zwierze
from barszczsosnowskiego import BarszczSosnowskiego
import random

class CyberOwca(Zwierze):
    def __init__(self, swiat, polozenie):
        super().__init__(swiat, polozenie, 11, 4, 1.00, 1, 0.00, 1.00)

    def akcja(self):
        if not self._zyje:
            return

        if not random.random() < self._szansaRuchu:
            return

        for i in range(self._zasiegRuchu):
            najblizszyBarszcz = self._swiat.najblizszyOsobnik(self._polozenie, BarszczSosnowskiego)
            if not najblizszyBarszcz == None:
                cel = self._swiat.poleWKierunku(self._polozenie, self._swiat.kierunekWDrodzeDo(self._polozenie, najblizszyBarszcz))
            else:
                cel = self._swiat.losoweSasiedniePole(self._polozenie)

            if self._swiat.poleZajete(cel):
                self.kolizja(self._swiat.organizmNaPolu(cel), True)
                return

            self.przemiescSieNaPole(cel)

    def atakowanie(self, przeciwnik):
        if isinstance(przeciwnik, BarszczSosnowskiego):
            przeciwnik.kolizja(self, False)
            przeciwnik.zakonczZycie()
            self.przemiescSieNaPole(przeciwnik._polozenie)
        else:
            super().atakowanie(przeciwnik)

    def tegoSamegoTypu(self, inny):
        return isinstance(inny, CyberOwca)

    def stworzDziecko(self, cel):
        return CyberOwca(self._swiat, cel)

    def rysowanie(self):
        return (0, 0, 204)