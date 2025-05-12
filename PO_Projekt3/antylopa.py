from zwierze import Zwierze

class Antylopa(Zwierze):
    def __init__(self, swiat, polozenie):
        super().__init__(swiat, polozenie, 4, 4, 1.00, 2, 0.50, 1.00)

    def tegoSamegoTypu(self, inny):
        return isinstance(inny, Antylopa)

    def stworzDziecko(self, cel):
        return Antylopa(self._swiat, cel)

    def rysowanie(self):
        return (217, 186, 140)