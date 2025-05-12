from roslina import Roslina

class Guarana(Roslina):
    def __init__(self, swiat, polozenie):
        super().__init__(swiat, polozenie, 0, 0, 0.10, 1)

    def kolizja(self, ofiara, atak):
        if atak:
            return

        ofiara.setSila(ofiara.getSila() + 3)

        if self._zyje:
            self.zakonczZycie()

    def tegoSamegoTypu(self, inny):
        return isinstance(inny, Guarana)

    def stworzDziecko(self, cel):
        return Guarana(self._swiat, cel)

    def rysowanie(self):
        return (255, 102, 217)


