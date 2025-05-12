#pragma once

#include <string>

class Wspolrzedne
{
private:
	int x, y;
public:
	Wspolrzedne();
	Wspolrzedne(int x, int y);
	Wspolrzedne(const Wspolrzedne& inne);
	Wspolrzedne(Wspolrzedne&& inne);
	~Wspolrzedne();

	Wspolrzedne& operator=(const Wspolrzedne& inne);
	Wspolrzedne& operator=(Wspolrzedne&& inne);

	int getX() const;
	int getY() const;

	std::string podajTyp();

	friend bool operator==(const Wspolrzedne& lewe, const Wspolrzedne& prawe);
};

