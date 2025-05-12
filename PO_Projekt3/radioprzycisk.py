from elementformularzowy import ElementFormularzowy

import pygame
from pygame.locals import *

class RadioPrzycisk(ElementFormularzowy):
    def __init__(self, x, y, podpis, promien):
        self.x = x
        self.y = y
        self.podpis = podpis
        self.wybrany = False
        self.czcionka = pygame.font.Font(None, 40)
        self.promien = promien

    def rysuj(self, ekran):
        pygame.draw.circle(ekran, (0,0,0), (self.x, self.y), self.promien)
        pygame.draw.circle(ekran, (255,255,255), (self.x, self.y), self.promien, 2)

        if self.wybrany:
            pygame.draw.circle(ekran, (255,255,255), (self.x, self.y), self.promien*0.6)
        tekstPodpisu = self.czcionka.render(self.podpis, True, (255,255,255))
        ekran.blit(tekstPodpisu, (self.x + 2*self.promien, self.y - self.promien/2))

    def zostalNacisniety(self, wsp):
        return self.x - self.promien < wsp[0] < self.x + self.promien and self.y - self.promien < wsp[1] < self.y + self.promien

