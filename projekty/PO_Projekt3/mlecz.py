from roslina import Roslina

class Mlecz(Roslina):
    def __init__(self, swiat, polozenie):
        super().__init__(swiat, polozenie, 0, 0, 0.10, 3)

    def tegoSamegoTypu(self, inny):
        return isinstance(inny, Mlecz)

    def stworzDziecko(self, cel):
        return Mlecz(self._swiat, cel)

    def rysowanie(self):
        return (255, 255, 102)
