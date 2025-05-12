import plotting
import calculating     

plotting.plot_raw_data('input_data.csv', 'prices')              # figure 1 - prices over time

working_data = calculating.calculate_macd('input_data.csv')     # prepare data frame with necessary information

plotting.plot_macd(working_data, 'macd')                        # figure 2 - MACD, SIGNAl, buy and sell points

plotting.plot_trading(working_data, 'trading', 0, 1000)         # figure 3 - prices with buy and sell points, full range
plotting.plot_trading(working_data, 'trading', 0, 200)          # figure 4 - prices with buy and sell points, subrange I
plotting.plot_trading(working_data, 'trading', 250, 450)        # figure 5 - prices with buy and sell points, subrange II

calculating.simulate_trading(working_data)                      # trading algorithm results, figure 6 & figure 7 - worth in money, worth in gold