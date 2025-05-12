import pandas as pd
import matplotlib.pyplot as plt

def ema_polynomial(N):                              # returns the list of consecutive terms of the polynomial in the denominator of the EMA
    alpha = 2 / (N + 1)                             # alpha coefficient

    return [(1 - alpha)**x for x in range(N+1)]

denominators = {}                                   # save denominators to avoid the same thing multiple times

def ema(N, samples):                                # calculate the EMA for N periods                  
    if len(samples) != N + 1:                       # check if received N+1 samples
        return
    
    alpha = 2 / (N + 1)                             # alpha coefficient

    # we assume that samples are ordered from oldest to newest 
    # the oldest sample is the first in the array

    if N in denominators:
        denominator = denominators[N]               # get the polynomial if it was calculated and saved earlier
    else:
        denominator = ema_polynomial(N)             # or calculate the polynomial ...
        denominators[N] = denominator               # ... and save it

    denominator_sum = sum(denominator)              # calculate the denominator

    nominator = [x * y for x, y in zip(denominator, samples[::-1])]     # the nominator is calculated the terms in the denominator by the respective samples
                                                                        # the last sample should be multiplied by the first term, so the order must be reversed
    nominator_sum = sum(nominator)                  # calculate the nominator

    return nominator_sum / denominator_sum          # final EMA_N value

def calculate_ema_values(data, n, values, result):
    new_data = pd.DataFrame(index = range(data.size))
    new_data[result] = pd.Series()                  # new data frame with an empty column

    for i in range(len(data)):                      # for all samples:
        if i - n >= 0:                              # condition to avoid pulling elements with a negative index
            calculation_frame = data.iloc[i - n : i + 1]            # get the appropriate amount ofelements
            calculation_list = calculation_frame[values].tolist()   
            new_data[result].at[i]= ema(n, calculation_list)        # save the EMA for the given period

    return new_data

def calculate_macd(input):
    input_data = pd.read_csv(input, header = None)

    ema12 = calculate_ema_values(input_data, 12, 0, 'ema12')                            # EMA from 12 periods
    input_data = pd.merge(input_data, ema12, left_index = True, right_index = True)

    ema26 = calculate_ema_values(input_data, 26, 0, 'ema26')                            # EMA from 26 periods
    input_data = pd.merge(input_data, ema26, left_index = True, right_index = True)

    input_data['macd'] = input_data['ema12'] - input_data['ema26']                      # MACD = EMA_12 - EMA_26

    signal = calculate_ema_values(input_data, 9, 'macd', 'signal')                      # SIGNAL = EMA_9(MACD)
    input_data = pd.merge(input_data, signal, left_index = True, right_index = True)

    return input_data


def simulate_trading(data):
    has_stock = True

    stock = 1000         # starting amount of stock
    money = 0               # starting amount of money

    print("Początkowa liczba jednostek instrumentu: " + str(stock))
    print("Początkowa odpowiadająca ilość gotówki: " + str(stock * data[0].at[0]))
    print()

    calculated_worth = []           # list of owned wealth converted to money at all points in time
    calculated_stocks = []          # list of owned wealth converted to stock at all points in time

    for i in range(len(data)):
        if has_stock:
            calculated_worth.append(stock * data[0].at[i])   # equivalent money
            calculated_stocks.append(stock)                  # owned stocks

            if data['macd'].at[i] < data['signal'].at[i]:       # lines crossed, signal to sell
                has_stock = False

                money = stock * data[0].at[i]                # convert stock to money
                stock = 0
        elif not has_stock:
            calculated_worth.append(money)                      # owned money
            calculated_stocks.append(money / data[0].at[i])     # equivalent stocks

            if data['macd'].at[i] > data['signal'].at[i]:       # lines crossed, signal to buy
                has_stock = True

                stock = money / data[0].at[i]                # convert money to stock
                money = 0

    if stock == 0:                                           # finished after selling
        print("Końcowa ilość gotówki: " + str(money))
        print("Końcowa odpowiadająca liczba jednostek instrumentu: " + str(money / data[0].iloc[-1]))   # equivalent stock using last price

    elif money == 0:                                            # finished after buying
        print("Końcowa liczba jednostek intrumentu: " + str(stock))
        print("Końcowa odpowiadająca ilość gotówki: " + str(stock * data[0].iloc[-1]))  # equivalent money using last price

    # plot the changes in worth in money
    plt.figure(figsize=(16, 10))
    plt.plot(calculated_worth, 'b-', linewidth = 1.0, label = 'Odpowiadająca ilość gotówki')

    plt.xlabel("Okres [dni]")
    plt.ylabel("Wartość [PLN]")
    plt.legend()

    plt.savefig("calculated_worth.png", dpi=1000)
    plt.show()

    # plot the changes in worth in stock
    plt.figure(figsize=(16, 10))
    plt.plot(calculated_stocks, 'g-', linewidth = 1.0, label = 'Odpowiadająca ilość złota')

    plt.xlabel("Okres [dni]")
    plt.ylabel("Wartość [gramy złota]")
    plt.legend()

    plt.savefig("calculated_stocks.png", dpi=1000)
    plt.show()
    
