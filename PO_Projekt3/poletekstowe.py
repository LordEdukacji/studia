from elementformularzowy import ElementFormularzowy

import pygame
from pygame.locals import *

class PoleTekstowe(ElementFormularzowy):
    def __init__(self, x, y, szer, wys):
        self.x = x
        self.y = y
        self.szer = szer
        self.wys = wys
        self.tekst = ""
        self.wybrany = False
        self.czcionka = pygame.font.Font(None, 60)

    def rysuj(self, ekran):
        pygame.draw.rect(ekran, (0,0,0), (self.x, self.y, self.szer, self.wys))
        if self.wybrany:
            pygame.draw.rect(ekran, (255,255,255), (self.x, self.y, self.szer, self.wys), 2)

        tresc = self.czcionka.render(self.tekst,True,(255,255,255))
        ekran.blit(tresc, (self.x + 25, self.y + self.wys/4))

    def zostalNacisniety(self, wsp):
        return self.x < wsp[0] < self.x + self.szer and self.y < wsp[1] < self.y + self.wys