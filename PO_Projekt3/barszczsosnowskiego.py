from roslina import Roslina
from zwierze import Zwierze

class BarszczSosnowskiego(Roslina):
    def __init__(self, swiat, polozenie):
        super().__init__(swiat, polozenie, 10, 0, 0.0, 1)

    def akcja(self):
        if not self._zyje:
            return
        
        cele = self._swiat.sasiedniePola(self._polozenie)

        for cel in cele:
            from cyberowca import CyberOwca
            if self._swiat.poleZajete(cel) and isinstance(self._swiat.organizmNaPolu(cel), Zwierze) and not isinstance(self._swiat.organizmNaPolu(cel), CyberOwca):
                self._swiat.dodajKomunikat(str(self._polozenie) + ": " + self.nazwaKlasy() + " zabija " + self._swiat.organizmNaPolu(cel).nazwaKlasy())
                self._swiat.organizmNaPolu(cel).zakonczZycie()

        super().akcja()

    def kolizja(self, ofiara, atak):
        if atak:
            return

        from cyberowca import CyberOwca
        if ofiara._zyje and not isinstance(ofiara, CyberOwca):
            ofiara.zakonczZycie()

        if self._zyje:
            self.zakonczZycie()

    def tegoSamegoTypu(self, inny):
        return isinstance(inny, BarszczSosnowskiego)

    def stworzDziecko(self, cel):
        return BarszczSosnowskiego(self._swiat, cel)

    def rysowanie(self):
        return (0, 77, 0)
