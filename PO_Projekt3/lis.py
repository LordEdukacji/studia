from zwierze import Zwierze

import random

class Lis(Zwierze):
    def __init__(self, swiat, polozenie):
        super().__init__(swiat, polozenie, 3, 7, 1.00, 1, 0.00, 1.00)

    def akcja(self):
        if not self._zyje:
            return

        if not random.random() < self._szansaRuchu:
            return

        for i in range(self._zasiegRuchu):
            pola = self._swiat.sasiedniePola(self._polozenie)
            pola = [pole for pole in pola if not self._swiat.poleZajete(pole) or self._swiat.organizmNaPolu(pole).getSila() <= self._sila]

            if len(pola) == 0:
                return

            cel = self._swiat.losowePole(pola)

            if self._swiat.poleZajete(cel):
                self.kolizja(self._swiat.organizmNaPolu(cel), True)
                return

            self.przemiescSieNaPole(cel)

    def tegoSamegoTypu(self, inny):
        return isinstance(inny, Lis)

    def stworzDziecko(self, cel):
        return Lis(self._swiat, cel)

    def rysowanie(self):
        return (255, 117, 26)
