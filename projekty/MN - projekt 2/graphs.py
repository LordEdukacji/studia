import matplotlib.pyplot as plt

def graphTaskB():
    jacobiErrs = []
    gaussSeidelErrs = []

    jacobiA = open("JacobiA.txt")

    for line in jacobiA:
        jacobiErrs.append(float(line))

    jacobiA.close()

    gaussSeidelA = open("GaussSeidelA.txt")

    for line in gaussSeidelA:
        gaussSeidelErrs.append(float(line))

    gaussSeidelA.close()

    plt.plot(jacobiErrs, label='Metoda Jacobiego')
    plt.plot(gaussSeidelErrs, label='Metoda Gaussa-Seidla')

    plt.yscale('log')
    plt.grid()

    plt.xlabel('Iteracje')
    plt.ylabel('Norma błędu rezydualnego (skala logarytmiczna)')

    plt.legend()

    plt.savefig("zadB.png", dpi=400)
    plt.show()

def graphTaskC():
    jacobiErrs = []
    gaussSeidelErrs = []

    jacobiA = open("JacobiC.txt")

    for line in jacobiA:
        jacobiErrs.append(float(line))

    jacobiA.close()

    gaussSeidelA = open("GaussSeidelC.txt")

    for line in gaussSeidelA:
        gaussSeidelErrs.append(float(line))

    gaussSeidelA.close()

    plt.plot(jacobiErrs, label='Metoda Jacobiega')
    plt.plot(gaussSeidelErrs, label='Metoda Gaussa-Seidla')

    plt.yscale('log')
    plt.grid()

    plt.xlabel('Iteracje')
    plt.ylabel('Norma błędu rezydualnego (skala logarytmiczna)')

    plt.legend()

    plt.savefig("zadC.png", dpi=400)
    plt.show()

def graphTaskE():
    sizes = []
    jacobiTimes = []
    gaussSeidelTimes = []
    LUTimes = []

    jacobiE = open("JacobiE.txt")

    for line in jacobiE:
        jacobiTimes.append(float(line))

    jacobiE.close()

    gaussSeidelE = open("GaussSeidelE.txt")

    for line in gaussSeidelE:
        gaussSeidelTimes.append(float(line))

    gaussSeidelE.close()

    LUE = open("LUE.txt")

    for line in LUE:
        LUTimes.append(float(line))

    LUE.close()

    sizesE = open("sizes.txt")

    for line in sizesE:
        sizes.append(float(line))

    sizesE.close()

    plt.plot(sizes, jacobiTimes, label='Metoda Jacobiego')
    plt.plot(sizes, gaussSeidelTimes, label='Metoda Gaussa-Seidla')
    plt.plot(sizes, LUTimes, label='Metoda faktoryzacji LU')

    #plt.yscale('log')
    plt.grid()

    plt.xlabel('Rozmiar macierzy')
    plt.ylabel('Czas wykonania [ms]')

    plt.legend()

    plt.savefig("zadE.png", dpi=400)
    plt.show()

    plt.plot(sizes, jacobiTimes, label='Metoda Jacobiego')
    plt.plot(sizes, gaussSeidelTimes, label='Metoda Gaussa-Seidla')
    plt.plot(sizes, LUTimes, label='Metoda faktoryzacji LU')

    plt.yscale('log')
    plt.grid()

    plt.xlabel('Rozmiar macierzy')
    plt.ylabel('Czas wykonania [ms]')

    plt.legend()

    plt.savefig("zadElog.png", dpi=400)
    plt.show()

#graphTaskB()
#graphTaskC()
graphTaskE()