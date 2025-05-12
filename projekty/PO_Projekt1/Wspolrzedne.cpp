#include "Wspolrzedne.h"

Wspolrzedne::Wspolrzedne()
	: Wspolrzedne(0, 0)
{
}

Wspolrzedne::Wspolrzedne(int x, int y)
	: x(x), y(y)
{
}

Wspolrzedne::Wspolrzedne(const Wspolrzedne& inne)
	: x(inne.x), y(inne.y)
{
}

Wspolrzedne::Wspolrzedne(Wspolrzedne&& inne)
	: x(inne.x), y(inne.y)
{
}

Wspolrzedne::~Wspolrzedne()
{
}

Wspolrzedne& Wspolrzedne::operator=(const Wspolrzedne& inne)
{
	this->x = inne.x;
	this->y = inne.y;

	return *this;
}

Wspolrzedne& Wspolrzedne::operator=(Wspolrzedne&& inne)
{
	this->x = inne.x;
	this->y = inne.y;

	return *this;
}

int Wspolrzedne::getX() const
{
	return x;
}

int Wspolrzedne::getY() const
{
	return y;
}

std::string Wspolrzedne::podajTyp()
{
	std::string s;

	s += "(";
	s += std::to_string(x);
	s += ", ";
	s += std::to_string(y);
	s += ")";

	return s;

}

bool operator==(const Wspolrzedne& lewe, const Wspolrzedne& prawe)
{
	if (lewe.x == prawe.x && lewe.y == prawe.y) return true;

	return false;
}
