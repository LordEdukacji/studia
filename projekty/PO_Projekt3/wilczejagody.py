from roslina import Roslina

class WilczeJagody(Roslina):
    def __init__(self, swiat, polozenie):
        super().__init__(swiat, polozenie, 99, 0, 0.10, 1)

    def kolizja(self, ofiara, atak):
        if atak:
            return

        if ofiara.getZyje():
            ofiara.zakonczZycie()

        if self._zyje:
            self.zakonczZycie()

    def tegoSamegoTypu(self, inny):
        return isinstance(inny, WilczeJagody)

    def stworzDziecko(self, cel):
        return WilczeJagody(self._swiat, cel)

    def rysowanie(self):
        return (133, 51, 255)


