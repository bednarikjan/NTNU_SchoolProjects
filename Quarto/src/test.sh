#!/bin/bash

LGREEN="\e[01;32m"
LRED="\e[01;31m"

tmp=`mktemp`

_run()
{
    count=$1
    A=$2
    Aname=$3
    B=$4
    Bname=$5

    echo "" > $tmp

    for i in {1..$count}; do
        echo ./Quarto $A $B >> $tmp
        rc=$?
        if [ $rc -ne 0 ]; then
            echo "FAIL. See $tmp"
            exit
        fi
        echo -n .
    done

    for i in {1..$count}; do
        echo ./Quarto $B $A >> $tmp
        rc=$?
        if [ $rc -ne 0 ]; then
            echo "FAIL. See $tmp"
            exit
        fi  
    done

    win1=`grep -c "$Aname has won!"` $tmp
    win2=`grep -c "$Bname has won!"` $tmp
    draw=`grep -c "draw" $tmp`
    [ $win1 -ge $win2 ] && echo -ne "$LGREEN" ||echo -ne $LRED
    echo -e "$Aname \t $Bname \t 2*$count \t $win1 \t $win2 \t $draw"
}

echo -e "1 \t 2 \t games \t  win 1 \t win 2 \t draw"
echo    "-------------------------------------------------------"

_run 50 --novice "Novice" --random "Random"

exit
for i in {1..50}; do ./Quarto   --novice --random  |& tail -n 1; done > $tmp
for i in {1..50}; do ./Quarto   --random --novice  |& tail -n 1; done >> $tmp
win1=`grep -c "Novice" $tmp`
win2=`grep -c "Random" $tmp`
draw=`grep -c "draw" $tmp`
[ $win1 -ge $win2 ] && echo -ne "$LGREEN" ||echo -ne $LRED
echo -e "novice      random      100      $win1 \t $win2 \t $draw"

for i in {1..10}; do ./Quarto   --novice --minimax 1  |& tail -n 1; done > $tmp
for i in {1..10}; do ./Quarto   --minimax 1 --novice  |& tail -n 1; done >> $tmp
win1=`grep -c "Novice" $tmp`
win2=`grep -c "Minimax-1" $tmp`
draw=`grep -c "draw" $tmp`
[ $win1 -ge $win2 ] && echo -en "$LGREEN" ||echo -ne $LRED
echo -e "novice      minimax-1    20      $win1 \t $win2 \t $draw "

for i in {1..10}; do ./Quarto   --novice --minimax 2  |& tail -n 1; done > $tmp
for i in {1..10}; do ./Quarto   --minimax 2 --novice  |& tail -n 1; done >> $tmp
win1=`grep -c "Novice" $tmp`
win2=`grep -c "Minimax-2" $tmp`
draw=`grep -c "draw" $tmp`
[ $win1 -ge $win2 ] && echo -en "$LRED" ||echo -ne $LGREEN
echo -e "novice      minimax-2    20      $win1 \t $win2 \t $draw "


for i in {1..10}; do ./Quarto   --novice --minimax 3  |& tail -n 1; done > $tmp
for i in {1..10}; do ./Quarto   --minimax 3 --novice  |& tail -n 1; done >> $tmp
win1=`grep -c "Novice" $tmp`
win2=`grep -c "Minimax-3" $tmp`
draw=`grep -c "draw" $tmp`
[ $win1 -ge $win2 ] && echo -en "$LRED" ||echo -ne $LGREEN
echo -e "novice      minimax-3    20      $win1 \t $win2 \t $draw "

for i in {1..10}; do ./Quarto   --minimax 2 --minimax 3 |& tail -n 1; done > $tmp
for i in {1..10}; do ./Quarto   --minimax 3 --minimax 2 |& tail -n 1; done >> $tmp
win1=`grep -c "Minimax-2" $tmp`
win2=`grep -c "Minimax-3" $tmp`
draw=`grep -c "draw" $tmp`
[ $win1 -ge $win2 ] && echo -en "$LRED" ||echo -ne $LGREEN
echo -e "minimax-2   minimax-3    20      $win1 \t $win2 \t $draw"

for i in {1..10}; do ./Quarto   --minimax 2 --minimax 4 |& tail -n 1; done > $tmp
for i in {1..10}; do ./Quarto   --minimax 4 --minimax 2 |& tail -n 1; done >> $tmp
win1=`grep -c "Minimax-2" $tmp`
win2=`grep -c "Minimax-4" $tmp`
draw=`grep -c "draw" $tmp`
[ $win1 -ge $win2 ] && echo -en "$LRED" ||echo -ne $LGREEN
echo -e "minimax-2   minimax-4    20      $win1 \t $win2 \t $draw"


for i in {1..10}; do ./Quarto   --minimax 3 --minimax 4 |& tail -n 1; done > $tmp
for i in {1..10}; do ./Quarto   --minimax 4 --minimax 3 |& tail -n 1; done >> $tmp
win1=`grep -c "Minimax-3" $tmp`
win2=`grep -c "Minimax-4" $tmp`
draw=`grep -c "draw" $tmp`
[ $win1 -ge $win2 ] && echo -en "$LRED" ||echo -ne $LGREEN
echo -e "minimax-3   minimax-4    20      $win1 \t $win2 \t $draw"


rm $tmp
