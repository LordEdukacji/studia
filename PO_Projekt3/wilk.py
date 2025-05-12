from zwierze import Zwierze

class Wilk(Zwierze):
    def __init__(self, swiat, polozenie):
        super().__init__(swiat, polozenie, 9, 5, 1.00, 1, 0.00, 1.00)

    def tegoSamegoTypu(self, inny):
        return isinstance(inny, Wilk)

    def stworzDziecko(self, cel):
        return Wilk(self._swiat, cel)

    def rysowanie(self):
        return (204, 204, 204)


