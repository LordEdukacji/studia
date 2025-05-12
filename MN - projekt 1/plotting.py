import pandas as pd
import matplotlib.pyplot as plt

def plot_raw_data(input, name):
    input_data = pd.read_csv(input, header = None)                          # read the .csv file with prices

    plt.figure(figsize=(16, 10))

    plt.plot(input_data, linewidth = 1, label = 'Notowania cen złota')      # plot the file
    plt.grid()

    plt.xlabel("Okres [dni]")
    plt.ylabel("Cena złota w [PLN / 1 g]")
    plt.legend()

    plt.savefig(name + ".png", dpi=1000)
    plt.show()

def plot_macd(data, name):
    has_resource = True
    selling_points = []         # timestamps when MACD suggested selling
    selling_signal = []         # value of the SIGNAL line when MACD suggested selling 
    buying_points = []          # timestamps when MACD suggested buying
    buying_signal = []          # value of the signal line when MACD suggested buying 

    for i in range(len(data)):
        if has_resource:
            if data['macd'].at[i] < data['signal'].at[i]:       # lines crossed, signal to sell
                has_resource = False
                selling_points.append(i)
                selling_signal.append(data['signal'].at[i])

        elif not has_resource:
            if data['macd'].at[i] > data['signal'].at[i]:       # lines crossed, signal to buy
                has_resource = True
                buying_points.append(i)
                buying_signal.append(data['signal'].at[i])

    # plot the MACD and SIGNAL lines with selling and buying points
    plt.figure(figsize=(16, 10))

    plt.plot(data['macd'], 'b-', linewidth = 1.0, label = 'Linia MACD')
    plt.plot(data['signal'], 'r-', linewidth = 1.0, label = 'Linia SIGNAL')
    plt.grid()

    plt.scatter(selling_points, selling_signal, color = 'orange', alpha = 1.0, s = 25, label = 'Punkty sprzedaży')
    plt.scatter(buying_points, buying_signal, color = 'purple', alpha = 1.0, s = 25, label = 'Punkty zakupu')

    plt.xlabel("Okres [dni]")
    plt.ylabel("Wartość wskaźnika")
    plt.legend()

    plt.savefig(name + ".png", dpi=1000)
    plt.show()

def plot_trading(data, name, xstart, xend):
    has_resource = True
    selling_points = []         # timestamps when MACD suggested selling
    selling_prices = []         # price when MACD suggested selling 
    buying_points = []          # timestamps when MACD suggested buying
    buying_prices = []          # price when MACD suggested buying

    for i in range(len(data)):
        if has_resource:
            if data['macd'].at[i] < data['signal'].at[i]:       # lines crossed, signal to sell   
                has_resource = False

                selling_points.append(i)
                selling_prices.append(data[0].at[i])
        elif not has_resource:
            if data['macd'].at[i] > data['signal'].at[i]:       # lines crossed, signal to buy
                has_resource = True

                buying_points.append(i)
                buying_prices.append(data[0].at[i])

    # plot the price with selling and buying points
    plt.figure(figsize=(16, 10))
    
    plt.plot(data[0], linewidth = 1.0, label = 'Notowania cen złota')
    plt.scatter(selling_points, selling_prices, color = 'orange', alpha = 1.0, s = 15, label = 'Punkty sprzedaży')
    plt.scatter(buying_points, buying_prices, color = 'purple', alpha = 1.0, s = 15, label = 'Punkty zakupu')
    plt.grid()

    plt.xlim(xstart, xend)

    plt.xlabel("Okres [dni]")
    plt.ylabel("Cena złota w [PLN / 1 g]")
    plt.legend()

    plt.savefig(name + "-" + str(xstart) + "-" + str(xend) + ".png", dpi=1000)
    plt.show()
