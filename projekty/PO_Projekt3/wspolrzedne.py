class Wspolrzedne:
    def __init__(self, x = 0, y = 0):
        self.x = x
        self.y = y

    def __str__(self):
        return f"({self.x}, {self.y})"

    def __eq__(self, inne):
        if isinstance(inne, Wspolrzedne):
            return self.x == inne.x and self.y == inne.y
        return False
