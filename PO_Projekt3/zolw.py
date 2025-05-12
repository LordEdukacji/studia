from zwierze import Zwierze

class Zolw(Zwierze):
    def __init__(self, swiat, polozenie):
        super().__init__(swiat, polozenie, 2, 1, 0.25, 1, 0.00, 1.00)

    def odbilAtak(self, atakujacy):
        return atakujacy.getSila() < 5

    def tegoSamegoTypu(self, inny):
        return isinstance(inny, Zolw)

    def stworzDziecko(self, cel):
        return Zolw(self._swiat, cel)

    def rysowanie(self):
        return (128, 51, 0)
