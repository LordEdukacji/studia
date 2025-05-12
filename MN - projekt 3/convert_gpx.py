import gpxpy
import os
import csv

for filename in os.scandir('.'):
    if filename.is_file():
        if filename.name.endswith(".gpx"):
            with open(filename.name) as file:
                data = gpxpy.parse(file)

                sum = 0

                datapoints = []

                for track in data.tracks:
                    for segment in track.segments:
                        points = segment.points

                        datapoints.append([0, points[0].elevation])

                        for i in range(1, len(points)):
                            sum += points[i].distance_2d(points[i-1])

                            datapoints.append([sum, points[i].elevation])

                with open(filename.name.removesuffix('.gpx') + '.csv', 'w', newline='') as target:
                    csv_writer = csv.writer(target)

                    csv_writer.writerows(datapoints)







    