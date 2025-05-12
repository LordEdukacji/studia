# this script converts .json files with raw data into appropriate .csv files

import pandas as pd
import os

to_include = 1000   # how many data points will be included in the resulting file

original_data = pd.DataFrame()

for filename in os.scandir("."):
    if filename.is_file():
        if filename.name.endswith(".json"):
            content = pd.read_json(filename.name)
            original_data = pd.concat([original_data, content], ignore_index = True)    # create a dataframe from all files

original_data['data'] = pd.to_datetime(original_data['data'], format='%Y-%m-%d')        # convert date written as a string into datetime

original_data.sort_values(by = ['data'])                                                # sort by date

original_data.to_csv('compiled_original.csv', index = False)                            # export all data

included_data = original_data.head(to_include)                                          # limit the number of data points

print("First included date:")
print(included_data['data'].iloc[0])
print()                                                                                 # print the start and end of the new limited range
print("Last included date:")
print(included_data['data'].iloc[-1])

gold_data = included_data['cena']                                                       # extract just the prices

gold_data.to_csv('input_data.csv', index = False, header = False)                       # export the selected number of prices
