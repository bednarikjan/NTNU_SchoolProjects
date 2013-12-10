ARCHIVE = aip-proj3-tomasdo-janbed.zip
FOLDER = $(shell basename `pwd`)

.PHONY: doc
all:
	ant < build.xml
zip pack:
	cp doc/latex/*.pdf doc/
	cd ../; zip -MM $(ARCHIVE) \
		$(FOLDER)/build.xml \
		$(FOLDER)/Makefile \
		$(FOLDER)/doc/*.pdf \
		$(FOLDER)/src/pso/*.java \
		$(FOLDER)/src/pso/*/*.java \
		$(FOLDER)/config/task*/* \
		$(FOLDER)/knapsack/pso-packages.txt; \
	cp $(ARCHIVE) $(FOLDER)
