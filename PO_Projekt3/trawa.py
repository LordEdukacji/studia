from roslina import Roslina

class Trawa(Roslina):
    def __init__(self, swiat, polozenie):
        super().__init__(swiat, polozenie, 0, 0, 0.10, 1)

    def tegoSamegoTypu(self, inny):
        return isinstance(inny, Trawa)

    def stworzDziecko(self, cel):
        return Trawa(self._swiat, cel)

    def rysowanie(self):
        return (170, 255, 0)


