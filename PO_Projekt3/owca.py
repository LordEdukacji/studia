from zwierze import Zwierze

class Owca(Zwierze):
    def __init__(self, swiat, polozenie):
        super().__init__(swiat, polozenie, 4, 4, 1.00, 1, 0.00, 1.00)

    def tegoSamegoTypu(self, inny):
        return isinstance(inny, Owca)

    def stworzDziecko(self, cel):
        return Owca(self._swiat, cel)

    def rysowanie(self):
        return (153, 230, 255)
