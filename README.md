# Vacations

This repository contains a few helpful tools for planning vacations.

## Elevation

I'm not a big fan of hot weather, so every July me and my family try to escape the heat.

Going north is not always an option, so sometimes we go up to the mountains. While there are sites that allow you to see altitude of given coordinates (like [this](https://www.advancedconverter.com/map-tools/find-altitude-by-coordinates), it might be a hassle to check every location.

Hence, I wrote a script that given an [airbnb](https://www.airbnb.com/) search url goes over the places and prints out the altitude based on approximate coordinates. 

Usage example:

1. Put your Google Map API key in `src/resources/api.key`
2. Start the Spark server (`Vacations.main`)
3. Fetch listing altitude
```bash
curl http://localhost:4567/altitudes --data 'https://www.airbnb.com/rooms/40854734?adults=2&children=1&infants=1&check_in=2021-07-18&check_out=2021-07-24&translate_ugc=false&federated_search_id=56ea90c6-050c-4b76-90da-44d17c20676c&source_impression_id=p3_1623698837_edzGQJ75UQTS%2FvgX&guests=1'
```
3a. Fetch listing altitude with minimal altitude filter:
```bash
curl http://localhost:4567/altitudes?min=50 --data 'https://www.airbnb.com/rooms/40854734?adults=2&children=1&infants=1&check_in=2021-07-18&check_out=2021-07-24&translate_ugc=false&federated_search_id=56ea90c6-050c-4b76-90da-44d17c20676c&source_impression_id=p3_1623698837_edzGQJ75UQTS%2FvgX&guests=1'
```