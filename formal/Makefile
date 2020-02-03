SBY=sby
SBT=sbt
SMTBMCIFY=smtbmcify
TOPMODULE=ChisNesPad

all: $(TOPMODULE)/PASS

$(TOPMODULE)/PASS: $(TOPMODULE).sby $(TOPMODULE)Formal.sv
	$(SBY) $(TOPMODULE).sby

$(TOPMODULE)Formal.sv: $(TOPMODULE).v $(TOPMODULE)Rules.sv
	$(SMTBMCIFY) -v ../$(TOPMODULE).v -f $(TOPMODULE)Rules.sv -o $(TOPMODULE)Formal.sv

$(TOPMODULE).v: ../src/main/scala/chisnespad/chisnespad.scala
	cd ..;$(SBT) "runMain chisnespad.ChisNesPad"

mrproper:
	-rm -rf $(TOPMODULE)
	-rm $(TOPMODULE)Formal.sv
	-rm ../$(TOPMODULE).v
