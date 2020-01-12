package chisnespad

import chisel3._
import chisel3.util._
import chisel3.Driver
import fpgamacro.generic.ResetGen

class SNesPadLed(val mainClockFreq: Int = 100,
                 val clockFreq: Int = 1) extends RawModule {
  val clock = IO(Input(Clock()))
  val io = IO(new Bundle{
    /* SNES Pinout */
    val dclock = Output(Bool())
    val dlatch = Output(Bool())
    val sdata  = Input(Bool())
    /* leds */
    val ledred = Output(Bool())
    val ledgreen = Output(Bool())
    val ledblue = Output(Bool())
  })


  val rstgen = Module(new ResetGen())
  rstgen.io.clk := clock
  withClockAndReset(clock, rstgen.io.rst){
    /* registering pad values */
    val sNesPadReg = RegInit(0.U(16.W))

    val blinkCount = RegInit(0.U(32.W))
    val countMax = 10000000.U
    when(blinkCount < (countMax/2.U)){
      blinkCount := blinkCount + 1.U
      io.ledred := true.B
    }.elsewhen(blinkCount < countMax){
      blinkCount := blinkCount + 1.U
      io.ledred := false.B
    }.otherwise{
      blinkCount := 0.U
      io.ledred := true.B
    }

    val cnpd = Module(new ChisNesPad(mainClockFreq, clockFreq, 16))
    cnpd.io.data.ready := true.B
    cnpd.io.sdata := io.sdata
    io.dclock := cnpd.io.dclock
    io.dlatch := cnpd.io.dlatch
 
    when(cnpd.io.data.valid){
      sNesPadReg := cnpd.io.data.bits
    }

    /* connecting some leds to pad */
//    io.ledred := sNesPadReg(0)
    io.ledgreen := sNesPadReg(1)
    io.ledblue := sNesPadReg(2)
  } 
}

object SNesPadLed extends App {
  println("Generating snespadled verilog")
  chisel3.Driver.execute(Array[String](), () => new SNesPadLed())
}
