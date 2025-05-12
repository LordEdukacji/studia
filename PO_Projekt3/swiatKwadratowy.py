import os
os.environ['PYGAME_HIDE_SUPPORT_PROMPT'] = '1'

from cmath import pi
from math import atan2

from swiat import Swiat
from wspolrzedne import Wspolrzedne
from kierunki import Kierunki
from czlowiek import Czlowiek

import pygame
from pygame.locals import *

from przycisk import Przycisk
from poletekstowe import PoleTekstowe

import random

class SwiatKwadratowy(Swiat):
    def __init__(self, wysokosc, szerokosc, launcher, zapis, ekran):
        super().__init__(launcher, zapis, ekran)
        self.wysokosc = wysokosc
        self.szerokosc = szerokosc

        self.mapaOrganizmow = [[None] * szerokosc for i in range(wysokosc)]

        if not zapis:
            self.dodajPoczatkoweOrganizmy()

        pygame.init()
        self.ekran = pygame.display.set_mode((1200, 800))
        pygame.display.set_caption("Wirtualny Swiat 193483")

        self.wyjdzPrzycisk = Przycisk(0,0,180,50, "Wyjdz")
        self.nowaGraPrzycisk = Przycisk(0,50,180,50, "Nowa gra")
        self.wczytajPrzycisk = Przycisk(0,100,180,50, "Wczytaj")
        self.zapiszPrzycisk = Przycisk(0,150,180,50, "Zapisz")
        self.nowaTuraPrzycisk = Przycisk(0,200,180,50, "Nowa tura")

        self.wGore = Przycisk(60, 250, 60, 40, "   /\\")
        self.wDol = Przycisk(60, 290, 60, 40, "   \\/")
        self.wPrawo = Przycisk(120, 290, 60, 40, "   ->")
        self.wLewo = Przycisk(0, 290, 60, 40, "   <-")
        self.mocPrzycisk = Przycisk(30, 330, 120, 40, "MOC")

        self.legenda = Przycisk(0,380, 180, 50, "Legenda")

        if not zapis:
            self.rysujSwiat()

    def rysujSwiat(self):
        while True:
            if not self.zdarzenia():
                return

            self.ekran.fill((20,20,20))

            self.wyjdzPrzycisk.rysuj(self.ekran)
            self.nowaGraPrzycisk.rysuj(self.ekran)
            self.wczytajPrzycisk.rysuj(self.ekran)
            self.zapiszPrzycisk.rysuj(self.ekran)
            self.nowaTuraPrzycisk.rysuj(self.ekran)

            self.wGore.rysuj(self.ekran)
            self.wDol.rysuj(self.ekran)
            self.wPrawo.rysuj(self.ekran)
            self.wLewo.rysuj(self.ekran)
            self.mocPrzycisk.rysuj(self.ekran)

            self.legenda.rysuj(self.ekran)

            for i in range(self.wysokosc):
                for j in range(self.szerokosc):
                    if not self.poleZajete(Wspolrzedne(j,i)):
                        pygame.draw.rect(self.ekran, (255,255,255), (252 + j*38, 22 + i*38, 33, 33))    # puste pola sa nieco mniejsze dla odroznienia
                    else:
                        pygame.draw.rect(self.ekran, self.organizmNaPolu(Wspolrzedne(j,i)).rysowanie(), (250 + j*38, 20 + i*38, 35, 35))

            licznik = 0

            for komunikat in self._komunikaty:
                if (licznik >= 24):
                    tekstKomunikatu = self.czcionka.render("I wiele innych...", True, (255, 255, 255))
                    self.ekran.blit(tekstKomunikatu, (3, 440 + 13*licznik))
                    break
                tekstKomunikatu = self.czcionka.render(komunikat, True, (255, 255, 255))
                self.ekran.blit(tekstKomunikatu, (3, 440 + 13*licznik))
                licznik += 1

            pygame.display.flip()

    def zdarzenia(self):
        for zdarzenie in pygame.event.get():
            if zdarzenie.type == pygame.QUIT:
                pygame.quit()
                return False
            elif zdarzenie.type == pygame.MOUSEBUTTONDOWN:
                if zdarzenie.button == 1:
                    if self.wyjdzPrzycisk.zostalNacisniety(zdarzenie.pos):
                        pygame.quit()
                        return False
                    elif self.nowaGraPrzycisk.zostalNacisniety(zdarzenie.pos):
                        self.launcher.wybierzSwiat()
                        return False
                    elif self.wczytajPrzycisk.zostalNacisniety(zdarzenie.pos):
                        self.launcher.wczytajZPliku()
                    elif self.zapiszPrzycisk.zostalNacisniety(zdarzenie.pos):
                        self.zapiszDoPliku()
                    elif self.nowaTuraPrzycisk.zostalNacisniety(zdarzenie.pos):
                        self.wykonajTure()
                    elif self.wGore.zostalNacisniety(zdarzenie.pos):
                        if self.czlowiek != None:
                            self.czlowiek.setKierunek(Kierunki.N)
                    elif self.wDol.zostalNacisniety(zdarzenie.pos):
                        if self.czlowiek != None:
                            self.czlowiek.setKierunek(Kierunki.S)
                    elif self.wPrawo.zostalNacisniety(zdarzenie.pos):
                        if self.czlowiek != None:
                            self.czlowiek.setKierunek(Kierunki.E)
                    elif self.wLewo.zostalNacisniety(zdarzenie.pos):
                        if self.czlowiek != None:
                            self.czlowiek.setKierunek(Kierunki.W)
                    elif self.mocPrzycisk.zostalNacisniety(zdarzenie.pos):
                        if self.czlowiek != None:
                            self.czlowiek.aktywujZdolnosc()
                    elif self.legenda.zostalNacisniety(zdarzenie.pos):
                        self.pokazLegende()
            elif zdarzenie.type == pygame.KEYDOWN:
                if zdarzenie.key == pygame.K_UP:
                    if self.czlowiek != None:
                            self.czlowiek.setKierunek(Kierunki.N)
                elif zdarzenie.key == pygame.K_DOWN:
                    if self.czlowiek != None:
                            self.czlowiek.setKierunek(Kierunki.S)
                elif zdarzenie.key == pygame.K_RIGHT:
                    if self.czlowiek != None:
                            self.czlowiek.setKierunek(Kierunki.E)
                elif zdarzenie.key == pygame.K_LEFT:
                    if self.czlowiek != None:
                            self.czlowiek.setKierunek(Kierunki.W)
                elif zdarzenie.key == pygame.K_SPACE:
                    self.wykonajTure()
                elif zdarzenie.key == pygame.K_q:
                    pygame.quit()
                    return False
                elif zdarzenie.key == pygame.K_n:
                    self.launcher.wybierzSwiat()
                    return False
                elif zdarzenie.key == pygame.K_l:
                    self.launcher.wczytajZPliku()
                elif zdarzenie.key == pygame.K_s:
                    self.zapiszDoPliku()
                elif zdarzenie.key == pygame.K_p:
                    if self.czlowiek != None:
                        self.czlowiek.aktywujZdolnosc()
        return True


    def sasiedniePola(self, polozenie):
        pola = []

        if polozenie.x > 0:
            pola.append(Wspolrzedne(polozenie.x - 1, polozenie.y))

        if polozenie.y > 0:
            pola.append(Wspolrzedne(polozenie.x, polozenie.y - 1))

        if polozenie.x < self.szerokosc - 1:
            pola.append(Wspolrzedne(polozenie.x + 1, polozenie.y))

        if polozenie.y < self.wysokosc - 1:
            pola.append(Wspolrzedne(polozenie.x, polozenie.y + 1))

        return pola

    def dodajPoczatkoweOrganizmy(self):
        x = int(random.random() * self.szerokosc)
        y = int(random.random() * self.wysokosc)

        c = Czlowiek(self, Wspolrzedne(x, y))
        self.czlowiek = c
        self.dodajDziecko(c)

        for i in range(self.wysokosc):
            for j in range(self.szerokosc):
                if self.poleZajete(Wspolrzedne(j,i)):
                    continue
                if random.random() < 0.50:
                    continue
                self.dodajLosowyOrganizm(Wspolrzedne(j,i))

    def poleNaPlanszy(self, polozenie):
        return polozenie.x >= 0 and polozenie.x < self.szerokosc and polozenie.y >=0 and polozenie.y < self.wysokosc

    def poleWKierunku(self, polozenie, kierunek):
        if kierunek == Kierunki.N:
            if self.poleNaPlanszy(Wspolrzedne(polozenie.x, polozenie.y - 1)):
                return Wspolrzedne(polozenie.x, polozenie.y - 1)
        elif kierunek == Kierunki.E:
            if self.poleNaPlanszy(Wspolrzedne(polozenie.x + 1, polozenie.y)):
                return Wspolrzedne(polozenie.x + 1, polozenie.y)
        elif kierunek == Kierunki.S:
            if self.poleNaPlanszy(Wspolrzedne(polozenie.x, polozenie.y + 1)):
                return Wspolrzedne(polozenie.x, polozenie.y + 1)
        elif kierunek == Kierunki.W:
            if self.poleNaPlanszy(Wspolrzedne(polozenie.x - 1, polozenie.y)):
                return Wspolrzedne(polozenie.x - 1, polozenie.y)
        
        return polozenie

    def odlegloscPol(self, start, koniec):
        return abs(start.x - koniec.x + start.y - koniec.y)     # metryka taxi

    def najblizszyOsobnik(self, polozenie, nazwaKlasy):
        minDystans = float('inf')
        najblizszy = None

        for organizm in self.organizmy:
            if isinstance(organizm, nazwaKlasy):
                odleglosc = self.odlegloscPol(polozenie, organizm._polozenie)
                if odleglosc < minDystans:
                    minDystans = odleglosc
                    najblizszy = organizm._polozenie

        return najblizszy

    def kierunekWDrodzeDo(self, start, koniec):
        wektor = (koniec.x - start.x, koniec.y - start.y)
        kat = atan2(wektor[1], wektor[0])   # funkcja zwraca wartosci miedzy -pi a pi

        if kat > pi/4 and kat < 3*pi/4:
            return Kierunki.S
        elif kat < -pi/4 and kat > -3*pi/4:
            return Kierunki.N
        elif kat >= -pi/4 and kat <= pi/4:
            return Kierunki.E
        else:
            return Kierunki.W

    def zapiszDoPliku(self):
        self.ekran = pygame.display.set_mode((500, 150))
        pygame.display.set_caption("Podaj nazwe pliku do zapisu")
        self.ekran.fill((20,20,20))

        poleNazwa = PoleTekstowe(20, 20, 460, 80)
        poleNazwa.wybrany = True
        potwierdz = Przycisk(200, 110, 125, 35, "Zapisz")

        while self.wczytywanieNazwyZapisu(poleNazwa, potwierdz):
            poleNazwa.rysuj(self.ekran)
            potwierdz.rysuj(self.ekran)

            pygame.display.flip()

        self.ekran = pygame.display.set_mode((1200, 800))
        pygame.display.set_caption("Wirtualny Swiat 193483")

    def wczytywanieNazwyZapisu(self, poleNazwa, potwierdz):
        for zdarzenie in pygame.event.get():
            if zdarzenie.type == pygame.QUIT:
                return False
            elif zdarzenie.type == pygame.MOUSEBUTTONDOWN:
                if zdarzenie.button == 1:
                    if potwierdz.zostalNacisniety(zdarzenie.pos) and poleNazwa != "":
                        self.operacjaZapisania(poleNazwa.tekst)
                        return False
            elif zdarzenie.type == pygame.KEYDOWN:
                if zdarzenie.key == pygame.K_RETURN and poleNazwa != "":
                    self.operacjaZapisania(poleNazwa.tekst)
                    return False
                elif zdarzenie.key == pygame.K_BACKSPACE:
                    poleNazwa.tekst = poleNazwa.tekst[:-1]
                else:
                    if len(poleNazwa.tekst) < 12:
                        poleNazwa.tekst += zdarzenie.unicode
        return True

    def operacjaZapisania(self, nazwaPliku):
        sciezkaPliku = nazwaPliku + ".txt"

        try:
            with open(sciezkaPliku, "w") as zapisDoPliku:
                zapisDoPliku.write("Kwadratowy\n" + str(self.wysokosc) + "\n" + str(self.szerokosc) + "\n" + str(self.tura) + "\n" + str(self.wiek) + "\n" + str(len(self.organizmy)) + "\n")
                for o in self.organizmy:
                    zapisDoPliku.write(str(o))
        except IOError as e:
            print("Nie udalo sie zapisac do pliku:", e)


    def pokazLegende(self):
        self.ekran = pygame.display.set_mode((250, 350))
        pygame.display.set_caption("Legenda Wirtualnego Swiata")

        tekstLegendy = "LEGENDA:\n\nWilk - szary\nOwca - niebieski\nLis - pomaranczowy\nZolw - brazowy\nAntylopa - bezowy\nCyberowca - granatowy\nCzlowiek - czerwony\nTrawa - zielony\nMlecz - zolty\nGuarana - rozowy\nWilcze Jagody - fioletowy\nBarszcz Sosnowskiego - ciemny zielony\n\nq - wyjdz\nn - nowa gra\nl - wczytaj\ns - zapisz\nspacja - nowa tura\nstrzalki - ruch\np - moc"
        linijkiLegendy = tekstLegendy.split("\n")

        tekstyDoWypisania = []
        for linijka in linijkiLegendy:
            tekstyDoWypisania.append(self.czcionka.render(linijka, True, (255,255,255)))

        legendaOtwarta = True
        while legendaOtwarta:
            for zdarzenie in pygame.event.get():
                if zdarzenie.type == pygame.QUIT:
                    legendaOtwarta = False

                self.ekran.fill((20,20,20))

                przesuniecie = 0
                for tekst in tekstyDoWypisania:
                    self.ekran.blit(tekst, (5, 5 + przesuniecie))
                    przesuniecie += 15

                pygame.display.flip()

        self.ekran = pygame.display.set_mode((1200, 800))
        pygame.display.set_caption("Wirtualny Swiat 193483")