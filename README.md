# chisNesPad
Chisel Super Nes pad controller

## Intro
Control Super Nes pad controller with a chisel component. Git subproject named [chisverilogutils](https://github.com/Martoni/chisverilogutils) is used for cocotb waveform generation and for formal prove injection, then to clone this project ::

```
$ git clone https://github.com/Martoni/chisNesPad.git
```

## Chisel

Description code is under directory src/main/scala/chisnespad/. To generate verilog synthesizable code do following ::

```
$ sbt "runMain chisnespad.ChisNesPad"
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

## chisverilogutils

Tools from [chisverilogutils](https://github.com/Martoni/chisverilogutils.git) are required for this project. See the project
readme for install instruction.


# Ressources

SNES pad is composed of two 4021 shift buffers. The pinout is given
[here](https://pinoutguide.com/Game/snescontroller_pinout.shtml) and a datasheet
of shift register can be [found here](http://www.st.com/content/ccc/resource/technical/document/datasheet/aa/2e/a7/49/58/cc/4a/9e/CD00002651.pdf/files/CD00002651.pdf/jcr:content/translations/en.CD00002651.pdf)
