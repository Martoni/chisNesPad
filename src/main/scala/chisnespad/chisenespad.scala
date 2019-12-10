package chisnespad

import chisel3._
import chisel3.util._
import chisel3.Driver

class ChisNesPad extends Module {
  val io = IO(new Bundle{
    /* SNES Pinout */
    val dclock = Output(Bool())
    val dlatch = Output(Bool())
    val sdata  = Input(Bool())
    /* read/valid output */
    val data = Decoupled(Output(UInt(16.W)))
  })

  io.dclock := DontCare
  io.dlatch := DontCare
  io.data.bits := DontCare
  io.data.valid := DontCare
}

object ChisNesPad extends App {
  println("Generating Verilog sources for ChisNesPad Module")
  chisel3.Driver.execute(Array[String](), () => new ChisNesPad)
}
