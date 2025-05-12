#!/bin/bash

# Author : Adrian Dybowski ( s193483@student.pg.edu.pl )
# Created On : 04.05.2023.
# Last Modified By : Adrian Dybowski ( s193483@student.pg.edu.pl )
# Last Modified On : 24.05.2023
# Version : 1.0
#
# Description :
# Przedstawianie statystyk na temat zużycia
# zasobów systemowych (takich jak procesor,
# pamięć, sieć) przez uruchomione aplikacje na
# przestrzeni czasu.

trap '' SIGINT              # zignorowanie ctrl+c

PLIK_PAUZY=$(mktemp)        # plik (ze wzgledu na uruchamianie glownej funkcji w tle) w ktorym przechowywana jest informacja o tym czy zapauzowac zbieranie danych
echo "0" > $PLIK_PAUZY      # zaczynami z niezapauzowanym skryptem

INTERWAL=10         # odstep czasu pomiedzy pomiarami (w praktyce wiekszy, bo przetwarzanie zajmuje dodatkowy czas)

WYKONANO=0          # liczba dotychczas wykonanych pomiarow
WIELOKROTNOSC=20    # co ile pomiarow spisywac tabelke do pliku / rysowac wykres

SLUPKI=6            # liczba slupkow na wykresie

MAXIMUM=9999        # maksymalna liczba pomiarow zanim skrypt dla bezpieczenstwa sie zakonczy

# tablice przechowujace dane, kolejno pamiec, procesor, siec oraz liczbe razy ile dany program zostal zaobserwowany
declare -A PAMIEC
declare -A PROCESOR
declare -A SIEC
declare -A POMIARY

KRESKA="----------------------------------------------------------"

while getopts ":hvi:u:a:o:m:fc:" opcja
do
    case $opcja in
        h)
            echo "Uzycie: $(basename $BASH_SOURCE) [OPCJE]"
            echo "Opcje:"
            echo "  -a      Wybor aplikacji do monitorowania"
            echo "  -c      Liczba kolumn na wykresie"
            echo "  -f      Format wyjsciowy w postaci wykresu"
            echo "  -h      Pomoc"
            echo "  -i      Ustawienie interwalu czasowego zbierania danych"
            echo "  -m      Liczba interwalow miedzy zapisami do pliku"
            echo "  -o      Plik w ktorym maja byc zapisywane raporty"
            echo "  -u      Monitorowanie statystyk tylko dla konkretnego uzytkownika"
            echo "  -v      Informacja o wersji i autorze"
            exit 0
            ;;
        v)
            echo "Resource Monitor, wersja 1.0, aktualna 24.05.2023, autor: Adrian Dybowski 193483"
            exit 0
            ;;
        i)
            if [[ $OPTARG =~ ^[1-9][0-9]*$  ]]; then        # sprawdzenie poprawnosci liczby
                INTERWAL=$OPTARG
            else
                echo "Interwal musi byc dodatnia liczba calkowita"
                exit 1
            fi
            ;;
        u)
            if getent passwd "$OPTARG" > /dev/null; then    # sprawdzenie istnienia uzytkownika
                UZYTKOWNIK=$OPTARG
            else
                echo "Podany uzytkownik nie istnieje"
                exit 1
            fi
            ;;
        a)
            APLIKACJA=$OPTARG
            # przygotowujemy pliki w ktorych po kazdym pomiarze beda zapisywane aktualne dane wybraje aplikacji
            PAMIEC_PLIK=$(mktemp)

            PROCESOR_PLIK=$(mktemp)

            SIEC_PLIK=$(mktemp)
            ;;
        o)
            WYJSCIE="$OPTARG.txt"   # dodajemy naglowek do pliku w ktorym zapiszemy tabele
            printf "%-20s %-14s %-10s %-10s\n" "Nazwa" "Pamiec [B]" "CPU [%]" "Siec [B]" > $WYJSCIE
            echo "$KRESKA" >> $WYJSCIE
            ;;
        m)
            if [[ $OPTARG =~ ^[1-9][0-9]*$  ]]; then    # sprawdzenie poprawnosci liczby
                WIELOKROTNOSC=$OPTARG
            else
                echo "Wielokrotnosc musi byc dodatnia liczba calkowita"
                exit 1
            fi
            ;;
        f)
            WYKRES=true

            if ! command -v gnuplot &> /dev/null; then      # sprawdzenie obecnosci gnuplot
                echo "Wymagana instalacja gnuplot, aby korzystac z wykresow"
                exit 1
            fi
            ;;
        c)
            if [[ $OPTARG =~ ^[1-9][0-9]*$  ]]; then        # sprawdzenie poprawnosci liczby
                SLUPKI=$OPTARG
            else
                echo "Liczba slupkow musi byc dodatnia liczba calkowita"
                exit 1
            fi
           
            ;;
        \?)
            echo "Niepoprawna opcja"
            exit 1
            ;;
        :)
            echo "Brak wymaganego argumentu"
            exit 1
            ;;
    esac
done

clear

# glowna funkcja
monitoruj() {
    declare -A NAPOTKANE    # do sprawdzania czy podczas obecnego przejscia petli zaobserwowano juz program o danej nazwie (wiele procesow tego samego programu)

    while read -r PID NAZWA CPU MEM UZYTK; do
        if [[ -v APLIKACJA ]]; then
            if [ "$NAZWA" != "$APLIKACJA" ]; then   # pomijamy aplikacje o niepasujacych nazwach
                continue
            fi
        fi

        if [[ -v UZYTKOWNIK ]]; then                # pomijamy procesy o niepasujacych uzytkownikach
            if [ "$UZYTK" != "$UZYTKOWNIK" ]; then
                continue
            fi
        fi

        if ! [ -v 'NAPOTKANE["$NAZWA"]' ]; then
            NAPOTKANE["$NAZWA"]=true                # oznaczamy program jako napotkamy i doliczamy jego spotkanie
            ((POMIARY["$NAZWA"]++))
        fi

        if [ -v 'PAMIEC["$NAZWA"]' ]; then
            PAMIEC["$NAZWA"]=$((PAMIEC["$NAZWA"]+MEM))                      # dodawanie
        else
            PAMIEC["$NAZWA"]=$MEM                                           # dodanie poczatkowej wartosci gdy jeszcze zadnej nie zmierzono wczesniej
        fi

        if [ -v 'PROCESOR["$NAZWA"]' ]; then
            PROCESOR["$NAZWA"]=$(echo "${PROCESOR["$NAZWA"]} + $(echo "$CPU" | tr ',' '.')" | bc)   # ewentualna zamiana przecinka na kropke i dodawanie
        else
            PROCESOR["$NAZWA"]=$(echo "$CPU" | tr ',' '.')                                          # dodanie poczatkowej wartosci gdy jeszcze zadnej nie zmierzono wczesniej
        fi

        WYJSCIE_NETSTAT=$(netstat -nap 2>/dev/null | grep "$PID")                       # zapisanie linii netstat w ktorych pojawia sie obecny proces
        NET=$(echo "$WYJSCIE_NETSTAT" | awk '{SUMA_SIEC+=$2} END {print SUMA_SIEC}')    # sumowanie wszystkich wartosci, ktore przynalezne sa danemu procesowi wedlug netstat

        if [ -v 'SIEC["$NAZWA"]' ]; then
            SIEC["$NAZWA"]=$(echo "${SIEC["$NAZWA"]} + $(echo "$NET" | tr ',' '.')" | bc)       # ewentualna zamiana przecinka na kropke i dodawanie
        else
            SIEC["$NAZWA"]=$(echo "$NET" | tr ',' '.')                                          # dodanie poczatkowej wartosci gdy jeszcze zadnej nie zmierzono wczesniej
        fi

        if [ -v APLIKACJA ]; then   # spisanie danych do plikow w przypadku obserwowania pojedynczej aplikacji
            echo "$WYKONANO $MEM" >> "$PAMIEC_PLIK"

            echo "$WYKONANO $CPU" >> "$PROCESOR_PLIK"

            echo "$WYKONANO $NET" >> "$SIEC_PLIK"
        fi
        
    done < <(top -w 128 -b -n 1 | tail -n +8 | awk '{print $1, $12, $9, $6, $2}')   # zabranie danych z odpowiednich kolumn

    ((WYKONANO++))

    if [ "${#POMIARY[@]}" -eq 0 ]; then     # pusta tablica pomiarow oznacza brak odpowiadajacych procesow
        echo "Nie znaleziono procesow odpowiadajacych podanym kryteriom"
        exit 1
    fi

    BUFFER=""

    BUFFER+=$(date)$'\n'    # dodanie daty nad aktualnymi statystykami
    BUFFER+=$KRESKA$'\n'

    for KLUCZ in "${!POMIARY[@]}"; do   # wyliczenie i wypisanie obecnej sredniej dla wszystkich programow
        SREDNIA_PAMIEC=$(echo "scale=2; ${PAMIEC[$KLUCZ]} / $WYKONANO" | bc)
        SREDNI_PROCESOR=$(echo "scale=2; ${PROCESOR[$KLUCZ]} / $WYKONANO" | bc)
        SREDNIA_SIEC=$(echo "scale=2; ${SIEC[$KLUCZ]} / $WYKONANO" | bc)

        BUFFER+=$(printf "%-15s | %-12s | %-10s | %-10s" "${KLUCZ:0:12}" "${SREDNIA_PAMIEC:0:10}" "${SREDNI_PROCESOR:0:10}" "${SREDNIA_SIEC:0:10}")$'\n'
    done
    
    clear

    printf "%-20s %-14s %-10s %-10s\n" "Nazwa" "Pamiec [B]" "CPU [%]" "Siec [B]"
    echo "$KRESKA"
    echo "$BUFFER"
    
    if (( WYKONANO % WIELOKROTNOSC == 0 )); then    # co wyznaczona ilosc pomiarow ma odbywac sie zapisywanie
        if [[ -v WYJSCIE ]]; then           # gdy wskazany jest plik poprzed -o to przepisujemy wyjscie jak do konsoli
            echo "$BUFFER" >> $WYJSCIE
        fi

        if [ -v WYKRES ]; then
            DATA_WYKRESU=$(date +'%d%m%Y%H%M%S')    # przygotowanie nazw plikow do wykresow

            NAZWA_WYKRESU_PAMIECI="mem"
            NAZWA_WYKRESU_PAMIECI+=$DATA_WYKRESU    # "mem[data]"

            NAZWA_WYKRESU_PROCESORA="cpu"           
            NAZWA_WYKRESU_PROCESORA+=$DATA_WYKRESU  # "cpu[data]"

            NAZWA_WYKRESU_SIECI="net"
            NAZWA_WYKRESU_SIECI+=$DATA_WYKRESU      # "net[data]"

            if [ -v APLIKACJA ]; then   # w przypadku pojedynczej aplikacja rysowane sa wykresy zasobu od czasu
                SKRYPT_GNUPLOT=$(mktemp)
                echo "set term png
                set output '$NAZWA_WYKRESU_PAMIECI.png'
                set key off
                plot '$PAMIEC_PLIK' with linespoints" > "$SKRYPT_GNUPLOT"

                gnuplot "$SKRYPT_GNUPLOT"

                echo "set term png
                set output '$NAZWA_WYKRESU_PROCESORA.png'
                set key off
                plot '$PROCESOR_PLIK' with linespoints" > "$SKRYPT_GNUPLOT"

                gnuplot "$SKRYPT_GNUPLOT"

                echo "set term png
                set output '$NAZWA_WYKRESU_SIECI.png'
                set key off
                plot '$SIEC_PLIK' with linespoints" > "$SKRYPT_GNUPLOT"

                gnuplot "$SKRYPT_GNUPLOT"

                rm "$SKRYPT_GNUPLOT"

            else            # w przypadku wielu aplikacji tworzony jest histogram
                PAMIEC_PLIK=$(mktemp)
                PAMIEC_PLIK_SORT=$(mktemp)
                PROCESOR_PLIK=$(mktemp)
                PROCESOR_PLIK_SORT=$(mktemp)
                SIEC_PLIK=$(mktemp)
                SIEC_PLIK_SORT=$(mktemp)

                for KLUCZ in "${!POMIARY[@]}"; do   # obliczenie srednich
                    SREDNIA_PAMIEC=$(echo "scale=2; ${PAMIEC[$KLUCZ]} / $WYKONANO" | bc)
                    SREDNI_PROCESOR=$(echo "scale=2; ${PROCESOR[$KLUCZ]} / $WYKONANO" | bc)
                    SREDNIA_SIEC=$(echo "scale=2; ${SIEC[$KLUCZ]} / $WYKONANO" | bc)

                    # zapisywane sa tylko niezerowe wartosci
                    if [[ $SREDNIA_PAMIEC != '0' ]]; then
                        echo "$KLUCZ $SREDNIA_PAMIEC" >> "$PAMIEC_PLIK"
                    fi

                    if [[ $SREDNI_PROCESOR != '0' ]]; then
                        echo "$KLUCZ $SREDNI_PROCESOR" >> "$PROCESOR_PLIK"
                    fi

                    if [[ $SREDNIA_SIEC != '0' ]]; then
                        echo "$KLUCZ $SREDNIA_SIEC" >> "$SIEC_PLIK"
                    fi
                done

                # sortujemy pliki malejaco i zostawiamy tylko wskazana ilosc najwyzszych wynikow
                sort -k2 -nr $PAMIEC_PLIK | head -n $SLUPKI > $PAMIEC_PLIK_SORT
                sort -k2 -nr $PROCESOR_PLIK | head -n $SLUPKI > $PROCESOR_PLIK_SORT
                sort -k2 -nr $SIEC_PLIK | head -n $SLUPKI > $SIEC_PLIK_SORT

                # wyrysowanie wykresow
                SKRYPT_GNUPLOT=$(mktemp)
                echo "set term png
                set output '$NAZWA_WYKRESU_PAMIECI.png'

                set style data histogram
                set style histogram rowstacked gap 1

                set key off

                zbior_danych="\"$PAMIEC_PLIK_SORT\""
                stats zbior_danych using 2 nooutput

                plot zbior_danych using 2:xtic(1) with boxes fill solid" > "$SKRYPT_GNUPLOT"

                gnuplot "$SKRYPT_GNUPLOT" 

                echo "set term png
                set output '$NAZWA_WYKRESU_PROCESORA.png'

                set style data histogram
                set style histogram rowstacked gap 1

                set key off

                zbior_danych="\"$PROCESOR_PLIK_SORT\""
                stats zbior_danych using 2 nooutput

                plot zbior_danych using 2:xtic(1) with boxes fill solid" > "$SKRYPT_GNUPLOT"

                gnuplot "$SKRYPT_GNUPLOT"

                echo "set term png
                set output '$NAZWA_WYKRESU_SIECI.png'

                set style data histogram
                set style histogram rowstacked gap 1

                set key off

                zbior_danych="\"$SIEC_PLIK_SORT\""
                stats zbior_danych using 2 nooutput

                plot zbior_danych using 2:xtic(1) with boxes fill solid" > "$SKRYPT_GNUPLOT"

                gnuplot "$SKRYPT_GNUPLOT"

                rm -f "$PAMIEC_PLIK" "$PROCESOR_PLIK" "$SIEC_PLIK" "$PAMIEC_PLIK_SORT" "$PROCESOR_PLIK_SORT" "$SIEC_PLIK_SORT" "$SKRYPT_GNUPLOT"
            fi
        fi
    fi 

    unset NAPOTKANE         # zapominamy jakie programy byly napotkane przy danym pomiarze aby poprawnie wykryc pojawianie sie ich pozniej
}

while true; do
    read -n1 znak < $PLIK_PAUZY # sprawdzamy zawartosc pliku pauzy

    if [[ $znak == "0" ]]; then # funkcja jest wykonywana tylko wtedy gdy nie jest zapauzowana
        monitoruj
    fi

    if [ "$WYKONANO" -ge "$MAXIMUM" ]; then     # kontrola liczby pomiarow
        echo "Osiagnieto maksymalna liczbe pomiarow w jednym uruchomieniu skryptu (nacisnij 'q' aby wyjsc)"
        exit 0
    fi

    sleep "$INTERWAL"
done &  # proces w tle

while read -n 1 -s klawisz; do
    if [[ $klawisz == 'q' ]]; then
        kill %1     # zabijamy proces w tle

        if [ -v APLIKACJA ]; then
            rm -f "$PAMIEC_PLIK" "$PROCESOR_PLIK" "$SIEC_PLIK" "$PLIK_PAUZY"
        fi        

        exit 0      # i konczymy program
    fi

    if [[ $klawisz == 'p' ]]; then
        read -n1 znak < $PLIK_PAUZY     # przy nacisnieciu 'p' zmieniamy stan zapauzowania poprzez zmiane zawartosci pliku pauzy

        if [[ $znak == "1" ]]; then
            echo "0" > $PLIK_PAUZY
        else 
            echo "1" > $PLIK_PAUZY
        fi
    fi
done