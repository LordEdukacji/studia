import os
os.environ['PYGAME_HIDE_SUPPORT_PROMPT'] = '1'

from radioprzycisk import RadioPrzycisk
from przycisk import Przycisk
from poletekstowe import PoleTekstowe

import pygame
from pygame.locals import *

from swiatKwadratowy import SwiatKwadratowy

class KwadratowyWymiary:
    def __init__(self, launcher):
        self.launcher = launcher
        self.wczytujWymiary()

    def wczytujWymiary(self):
        pygame.init()
        self.czcionka = pygame.font.Font(None, 70)
        self.ekran = pygame.display.set_mode((800, 600))
        pygame.display.set_caption("Wybierz wymiary swiata")

        self.poleSzer = PoleTekstowe(300, 200, 200, 80)
        self.poleWys = PoleTekstowe(300, 300, 200, 80)
        self.potwierdz = Przycisk(225, 450, 350, 100, "Rozpocznij")

        self.wykonuj()

    def wykonuj(self):
        while True:
            if not self.zdarzenia():
                return
            self.ekran.fill((20,20,20))

            self.poleSzer.rysuj(self.ekran)
            self.poleWys.rysuj(self.ekran)
            self.potwierdz.rysuj(self.ekran)
                
            tytul = self.czcionka.render("Wybierz szerokosc i wysokosc",True,(255,255,255))
            self.ekran.blit(tytul, (50, 60))

            pygame.display.flip()

    def zdarzenia(self):
        for zdarzenie in pygame.event.get():
            if zdarzenie.type == pygame.QUIT:
                pygame.quit()
                return False
            elif zdarzenie.type == pygame.MOUSEBUTTONDOWN:
                if zdarzenie.button == 1:
                    if self.poleSzer.zostalNacisniety(zdarzenie.pos):
                        self.poleSzer.wybrany = True
                        self.poleWys.wybrany = False
                    elif self.poleWys.zostalNacisniety(zdarzenie.pos):
                        self.poleSzer.wybrany = False
                        self.poleWys.wybrany = True
                    elif self.potwierdz.zostalNacisniety(zdarzenie.pos) and self.poleWys.tekst != "" and self.poleSzer.tekst != "":
                        self.launcher.swiat = SwiatKwadratowy(int(self.poleWys.tekst), int(self.poleSzer.tekst), self.launcher, False, self.ekran)
                        return False
            elif zdarzenie.type == pygame.KEYDOWN:
                if zdarzenie.key == pygame.K_RETURN and self.poleWys.tekst != "" and self.poleSzer.tekst != "":
                    self.launcher.swiat = SwiatKwadratowy(int(self.poleWys.tekst), int(self.poleSzer.tekst), self.launcher, False, self.ekran)
                    return False
                elif zdarzenie.key == pygame.K_BACKSPACE:
                    if self.poleSzer.wybrany:
                        self.poleSzer.tekst = self.poleSzer.tekst[:-1]
                    elif self.poleWys.wybrany:
                        self.poleWys.tekst = self.poleWys.tekst[:-1]
                else:
                    if self.poleSzer.wybrany and zdarzenie.unicode.isnumeric() and len(self.poleSzer.tekst) < 6:
                        self.poleSzer.tekst += zdarzenie.unicode
                    elif self.poleWys.wybrany and zdarzenie.unicode.isnumeric() and len(self.poleWys.tekst) < 6:
                        self.poleWys.tekst += zdarzenie.unicode
        return True

