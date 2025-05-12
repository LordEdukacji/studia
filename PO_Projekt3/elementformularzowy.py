from abc import ABC, ABCMeta, abstractmethod

class ElementFormularzowy(metaclass = ABCMeta):
    @abstractmethod
    def rysuj(self, ekran):
        pass

    @abstractmethod
    def zostalNacisniety(self, wsp):
        pass

