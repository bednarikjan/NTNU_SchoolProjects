ARCHIVE = aip-proj1-tomasdo-janbed.zip
FOLDER = $(shell basename `pwd`)

all:
	cd src; qmake; make;

run:
	src/Quarto
echo:
	echo $(FOLDER)
zip pack:
	cd src; make clean
	cd ../; zip $(ARCHIVE) \
		$(FOLDER)/Makefile \
		$(FOLDER)/src/*.cpp \
		$(FOLDER)/src/*.h \
		$(FOLDER)/src/Makefile \
		$(FOLDER)/src/optionparser/*.h \
		$(FOLDER)/src/Quarto.pro \
		$(FOLDER)/doc/quarto-doc.pdf; \
	cp $(ARCHIVE) $(FOLDER)

