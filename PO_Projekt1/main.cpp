// Adrian Dybowski 193483
// PO projekt 1

#include "Wspolrzedne.h"
#include "Swiat.h"

#include <iostream>
using namespace std;

int main() {
	Swiat* swiat = new Swiat;

	bool graj = true;
	while (graj) {
		graj = swiat->wybierzAkcje();
	}

	delete swiat;

	return 0;
}