from abc import ABC, ABCMeta, abstractmethod
import random

import pygame
from pygame.locals import *

from wilk import Wilk
from owca import Owca
from lis import Lis
from zolw import Zolw
from antylopa import Antylopa
from cyberowca import CyberOwca

from trawa import Trawa
from mlecz import Mlecz
from guarana import Guarana
from wilczejagody import WilczeJagody
from barszczsosnowskiego import BarszczSosnowskiego

class Swiat(metaclass = ABCMeta):
    def __init__(self, launcher, zapis, ekran):
        self.tura = 0
        self.wiek = 0

        self.launcher = launcher
        self.ekran = ekran

        self.organizmy = []
        self.mapaOrganizmow = None

        self.czlowiek = None

        self._komunikaty = []
        self.czcionka = pygame.font.Font(None, 15)

    def wykonajTure(self):
        self.tura += 1

        self.organizmy = sorted(self.organizmy, key = self.kolejnoscOrganizmow)

        self._komunikaty = []

        self._komunikaty.append("Tura " + str(self.tura) + ":")

        iloscOrganizmow = len(self.organizmy)
        for i in range(iloscOrganizmow):
            self.organizmy[i].akcja()

        self.usunMartweOrganizmy()

        if self.czlowiek != None and not self.czlowiek.getZyje():
            self.czlowiek = None
        if self.czlowiek != None:
            self.czlowiek.odswiezZdolnosc()

        if self.czlowiek != None:
            if self.czlowiek.getZdolnoscAktywna():
                self._komunikaty.append("Tarcza alzura aktywna jeszcze " + str(self.czlowiek.getTrwanieZdolnosci()) + " tur")
            else:
                if self.czlowiek.getOdnowienieZdolnosci() == 0:
                    self._komunikaty.append("Tarcza alzura gotowa")
                else:
                    self._komunikaty.append("Tarcza alzura gotowa za " + str(self.czlowiek.getOdnowienieZdolnosci()) + " tur")
        else:
            self._komunikaty.append("Czlowiek zostal pokonany")

    def kolejnoscOrganizmow(self, organizm):
        return (-organizm.getInicjatywa(), organizm.getWiek())

    @abstractmethod
    def rysujSwiat(self):
        pass

    # POLA
    @abstractmethod
    def sasiedniePola(self, polozenie):
        pass

    def sasiednieWolnePola(self, polozenie):
        pola = self.sasiedniePola(polozenie)
        wolnePola = [wspolrzedne for wspolrzedne in pola if not self.poleZajete(wspolrzedne)]
        return wolnePola

    def losoweSasiedniePole(self, polozenie):
        return self.losowePole(self.sasiedniePola(polozenie))

    def losoweSasiednieWolnePole(self, polozenie):
        return self._swiat.losowePole(self._swiat.sasiednieWolnePola(polozenie))

    def losowePole(self, polozenia):
        if len(polozenia) == 0:
            return None;

        iterator = iter(polozenia)
        indeks = random.randint(0, len(polozenia) - 1)

        for i in range(indeks):
            next(iterator)

        return next(iterator)

    def poleZajete(self, polozenie):
        return self.mapaOrganizmow[polozenie.y][polozenie.x] != None

    def organizmNaPolu(self, polozenie):
        return self.mapaOrganizmow[polozenie.y][polozenie.x]

    def wyczyszPoleMapy(self, polozenie):
        self.mapaOrganizmow[polozenie.y][polozenie.x] = None

    def usunOrganizmZPola(self, organizm):
        self.mapaOrganizmow[organizm._polozenie.y][organizm._polozenie.x] = None

    def dodajOrganizmDoPola(self, organizm):
        if not self.poleZajete(organizm.getPolozenie()):
            self.mapaOrganizmow[organizm.getPolozenie().y][organizm.getPolozenie().x] = organizm

    @abstractmethod
    def poleWKierunku(self, polozenie, kierunek):
        pass

    @abstractmethod
    def poleNaPlanszy(self, polozenie):
        pass

    @abstractmethod
    def odlegloscPol(self, start, koniec):
        pass

    @abstractmethod
    def najblizszyOsobnik(self, polozenie, nazwaKlasy):
        pass

    @abstractmethod
    def kierunekWDrodzeDo(self, start, koniec):
        pass

    # ORGANIZMY
    def nadajWiek(self):
        self.wiek += 1
        return self.wiek

    def usunMartweOrganizmy(self):
        self.organizmy = [organizm for organizm in self.organizmy if organizm.getZyje()]

    def dodajDziecko(self, dziecko):
        self.organizmy.append(dziecko)
        self.dodajOrganizmDoPola(dziecko)

    @abstractmethod
    def dodajPoczatkoweOrganizmy(self):
        pass

    def dodajLosowyOrganizm(self, polozenie):
        lista = (Wilk, Owca, Lis, Zolw, Antylopa, CyberOwca, Trawa, Mlecz, Guarana, WilczeJagody, BarszczSosnowskiego)

        wylosowany = random.choice(lista)

        o = wylosowany(self, polozenie)
        self.dodajDziecko(o)

    # ZAPIS
    @abstractmethod
    def zapiszDoPliku(self):
        pass

    def dodajKomunikat(self, komunikat):
        self._komunikaty.append(komunikat)