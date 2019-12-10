# chisNesPad
Chisel Super Nes pad controller

## Intro
Control Super Nes pad controller with a chisel component. Git subproject named [chisverilogutils](https://github.com/Martoni/chisverilogutils) is used for cocotb waveform generation and for formal prove injection, then to clone this project, dont forget to add submodule option ::

```
$ git clone --recurse-submodules https://github.com/Martoni/chisNesPad.git
```

## Chisel

Description code is under directory src/main/scala/chisnespad/. To generate verilog synthesizable code do following ::

```
$ sbt "runMain chisnespad.chisnespad"
```

## Cocotb

Testbench code is done with Python Cocotb package in directory cocotb/chisenespad/. To launch tests do following::

```
$ cd cocotb/chisenespad/
$ make
```

## Yosys-smtbmc

Formal description and prove are in directory formal/. To launch prove do following::
```
$ cd formal
$ make
```
