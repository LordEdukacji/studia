from elementformularzowy import ElementFormularzowy

import pygame
from pygame.locals import *

class Przycisk(ElementFormularzowy):
    def __init__(self, x, y, szer, wys, podpis):
        self.x = x
        self.y = y
        self.szer = szer
        self.wys = wys
        self.podpis = podpis
        self.czcionka = pygame.font.Font(None, int(wys*0.75))

    def rysuj(self, ekran):
        pygame.draw.rect(ekran, (255,255,255), (self.x, self.y, self.szer, self.wys))
        pygame.draw.rect(ekran, (0,0,0), (self.x, self.y, self.szer, self.wys), 1)
        tekstPodpisu = self.czcionka.render(self.podpis, True, (0,0,0))
        ekran.blit(tekstPodpisu, (self.x + self.szer/10, self.y + self.wys/4))

    def zostalNacisniety(self, wsp):
        return self.x < wsp[0] < self.x + self.szer and self.y < wsp[1] < self.y + self.wys

