NAME = gps-doc

all:
	pdflatex $(NAME)
	bibtex $(NAME)
	pdflatex $(NAME)
	cp -f $(NAME).pdf ../$(NAME).pdf

vlna:
	vlna documentation.tex

clean:
	rm -f \
		*.aux \
		*.bbl \
		$(NAME).log *.blg \
		$(NAME).out \
		$(NAME).toc \
		$(NAME).pdf

open:
	gnome-open $(NAME).pdf
