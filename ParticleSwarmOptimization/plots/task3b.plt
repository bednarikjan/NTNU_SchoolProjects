# Gnuplot script
# ParticleSwarmOptimization
# Task 3b

set output "task3b.png"; set term png

#set style line 1 lt 1 lw 2 lc rgb "red"
#set style line 2 lt 2 lw 2 lc rgb "green"
#set style line 3 lt 3 lw 2 lc rgb "blue"

set grid
set autoscale                        # scale axes automatically
set xtic auto                          # set xtics automatically
set ytic auto                          # set ytics automatically

set title "Kanpsack problem - different c1, c2"
set xlabel "Steps"
set ylabel "Fitness"
#plot	"task3b-1.dat" using 1:2 w lines ls 1 title 'c1 = 2.0, c2 = 2.0', \
	#"task3b-2.dat" using 1:2 w lines ls 2 title 'c1 = 0.5, c2 = 1.5', \
	#"task3b-3.dat" using 1:2 w lines ls 3 title 'c1 = 1.5, c2 = 0.5'

plot	"task3b-1.dat" using 1:2 w lines lt 3 title 'c1 = 2.0, c2 = 2.0'