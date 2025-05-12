import os
os.environ['PYGAME_HIDE_SUPPORT_PROMPT'] = '1'

from przycisk import Przycisk
from radioprzycisk import RadioPrzycisk
from kwadratowywymiary import KwadratowyWymiary
from poletekstowe import PoleTekstowe

import pygame
from pygame.locals import *

from swiatKwadratowy import SwiatKwadratowy
from wspolrzedne import Wspolrzedne

from wilk import Wilk
from owca import Owca
from lis import Lis
from zolw import Zolw
from antylopa import Antylopa
from cyberowca import CyberOwca
from czlowiek import Czlowiek

from trawa import Trawa
from mlecz import Mlecz
from guarana import Guarana
from wilczejagody import WilczeJagody
from barszczsosnowskiego import BarszczSosnowskiego

class Launcher:
    def __init__(self):
        pygame.init()
        self.swiat = None
        self.wybierzSwiat()

    def wybierzSwiat(self):
        self.czcionka = pygame.font.Font(None, 90)
        self.ekran = pygame.display.set_mode((800, 600))
        pygame.display.set_caption("Wybierz rodzaj swiata")

        self.radioKwadratowy = RadioPrzycisk(250, 250, "Swiat Kwadratowy", 25)
        self.potwierdz = Przycisk(250, 425, 300, 75, "Wybierz")

        self.wykonuj()

    def wykonuj(self):
        while True:
            if not self.zdarzenia():
                return
            self.ekran.fill((20,20,20))

            self.radioKwadratowy.rysuj(self.ekran)
            self.potwierdz.rysuj(self.ekran)

            tytul = self.czcionka.render("WIRTUALNY SWIAT 3.0",True,(255,255,255))
            self.ekran.blit(tytul, (50, 60))

            pygame.display.flip()

    def zdarzenia(self):
        for zdarzenie in pygame.event.get():
            if zdarzenie.type == pygame.QUIT:
                pygame.quit()
                return False
            elif zdarzenie.type == pygame.MOUSEBUTTONDOWN:
                if zdarzenie.button == 1:
                    if self.radioKwadratowy.zostalNacisniety(zdarzenie.pos):
                        self.radioKwadratowy.wybrany = True
                    elif self.potwierdz.zostalNacisniety(zdarzenie.pos):
                        if self.radioKwadratowy.wybrany:
                            KwadratowyWymiary(self)
                            return False
                        else:
                           return True
        return True

    def wczytajZPliku(self):
        self.ekran = pygame.display.set_mode((500, 150))
        pygame.display.set_caption("Podaj nazwe pliku do zapisu")
        self.ekran.fill((20,20,20))

        poleNazwa = PoleTekstowe(20, 20, 460, 80)
        poleNazwa.wybrany = True
        potwierdz = Przycisk(200, 110, 125, 35, "Zapisz")

        while self.wczytywanieNazwyPliku(poleNazwa, potwierdz):
            poleNazwa.rysuj(self.ekran)
            potwierdz.rysuj(self.ekran)

            pygame.display.flip()

        self.ekran = pygame.display.set_mode((1200, 800))
        pygame.display.set_caption("Wirtualny Swiat 193483")

    def wczytywanieNazwyPliku(self, poleNazwa, potwierdz):
        for zdarzenie in pygame.event.get():
            if zdarzenie.type == pygame.QUIT:
                return False
            elif zdarzenie.type == pygame.MOUSEBUTTONDOWN:
                if zdarzenie.button == 1:
                    if potwierdz.zostalNacisniety(zdarzenie.pos) and poleNazwa != "":
                        self.operacjaWczytania(poleNazwa.tekst)
                        return False
            elif zdarzenie.type == pygame.KEYDOWN:
                if zdarzenie.key == pygame.K_RETURN and poleNazwa != "":
                    self.operacjaWczytania(poleNazwa.tekst)
                    return False
                elif zdarzenie.key == pygame.K_BACKSPACE:
                    poleNazwa.tekst = poleNazwa.tekst[:-1]
                else:
                    if len(poleNazwa.tekst) < 12:
                        poleNazwa.tekst += zdarzenie.unicode
        return True

    def operacjaWczytania(self, nazwaPliku):
        sciezkaPliku = nazwaPliku + ".txt"

        try:
            with open(sciezkaPliku, "r") as zapisanaGra:
                rodzajSwiata = zapisanaGra.readline().strip()
                szer = int(zapisanaGra.readline().strip())
                wys = int(zapisanaGra.readline().strip())
                tura = int(zapisanaGra.readline().strip())
                wiek = int(zapisanaGra.readline().strip())
                liczbaOrganizmow = int(zapisanaGra.readline().strip())

                if (rodzajSwiata == "Kwadratowy"):
                    self.swiat = SwiatKwadratowy(wys, szer, self, True, self.ekran)
                    self.swiat.tura = tura
                    self.swiat.wiek = wiek
                else:
                    return

                for i in range(liczbaOrganizmow):
                    linijka = zapisanaGra.readline()
                    slowa = linijka.split()

                    dziecko = None

                    if slowa[0] == "Wilk":
                        dziecko = Wilk(self.swiat, Wspolrzedne(int(slowa[1]), int(slowa[2])))
                    elif slowa[0] == "Owca":
                        dziecko = Owca(self.swiat, Wspolrzedne(int(slowa[1]), int(slowa[2])))
                    elif slowa[0] == "Lis":
                        dziecko = Lis(self.swiat, Wspolrzedne(int(slowa[1]), int(slowa[2])))
                    elif slowa[0] == "Zolw":
                        dziecko = Zolw(self.swiat, Wspolrzedne(int(slowa[1]), int(slowa[2])))
                    elif slowa[0] == "Antylopa":
                        dziecko = Antylopa(self.swiat, Wspolrzedne(int(slowa[1]), int(slowa[2])))
                    elif slowa[0] == "CyberOwca":
                        dziecko = CyberOwca(self.swiat, Wspolrzedne(int(slowa[1]), int(slowa[2])))
                    elif slowa[0] == "Trawa":
                        dziecko = Trawa(self.swiat, Wspolrzedne(int(slowa[1]), int(slowa[2])))
                    elif slowa[0] == "Mlecz":
                        dziecko = Mlecz(self.swiat, Wspolrzedne(int(slowa[1]), int(slowa[2])))
                    elif slowa[0] == "Guarana":
                        dziecko = Guarana(self.swiat, Wspolrzedne(int(slowa[1]), int(slowa[2])))
                    elif slowa[0] == "WilczeJagody":
                        dziecko = WilczeJagody(self.swiat, Wspolrzedne(int(slowa[1]), int(slowa[2])))
                    elif slowa[0] == "BarszczSosnowskiego":
                        dziecko = BarszczSosnowskiego(self.swiat, Wspolrzedne(int(slowa[1]), int(slowa[2])))
                    if slowa[0] == "Czlowiek":
                        dziecko = Czlowiek(self.swiat, Wspolrzedne(int(slowa[1]), int(slowa[2])))
                        dziecko.setTrwanieZdolnosci(int(slowa[5]))
                        dziecko.setOdnowienieZdolnosci(int(slowa[6]))
                        dziecko.setZdolnoscAktywna(bool(slowa[6]))
                        self.swiat.czlowiek = dziecko
                    
                    dziecko.setWiek(int(slowa[3]))
                    dziecko.setSila(int(slowa[4]))
                    self.swiat.dodajDziecko(dziecko)

        except FileNotFoundError:
            return
        except IOError as e:
            print("Nie udalo sie wczytac pliku:", e)

        self.swiat.rysujSwiat()
        quit()