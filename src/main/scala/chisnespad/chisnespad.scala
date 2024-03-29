package chisnespad

import chisel3._
import circt.stage.ChiselStage
import chisel3.util._

class ChisNesPad (val mainClockFreq: Int = 100,
                  val clockFreq: Int = 1,
                  val regLen: Int = 16) extends Module {
  val io = IO(new Bundle{
    /* SNES Pinout */
    val dclock = Output(Bool())
    val dlatch = Output(Bool())
    val sdata  = Input(Bool())
    /* read/valid output */
    val data = Decoupled(Output(UInt(16.W)))
  })

  /* counter declarations*/
  val maxCount = mainClockFreq/clockFreq
  assert(maxCount > 1, "maxCount can't be " + maxCount)
  val halfMaxCount = maxCount/2
  val countReg = RegInit(0.U(log2Ceil(maxCount + 1).W))
  val fallReg = RegInit(false.B)
  val riseReg = RegInit(false.B)

  /* shift register (pad) declarations */
  val regCount = RegInit((regLen + 1).U(5.W))
  val padReg = RegInit(0.U(regLen.W))

  /* State machine declarations */
  /*    000    001    010    011     100*/
  val sinit::schigh::sclow::svalid::slatch::Nil = Enum(5)
  val stateReg = RegInit(sinit)
  val validReg = RegInit(false.B)

  /* counter */
  riseReg := false.B
  fallReg := 0.U
  when(stateReg === sclow || stateReg === schigh || stateReg === slatch){
    when(countReg >= maxCount.U){
      countReg := 0.U
      riseReg := true.B
    }.elsewhen(countReg === halfMaxCount.U){
      fallReg := 1.U
      countReg := countReg + 1.U
    }.otherwise{
      countReg := countReg + 1.U
    }
  }

  /* reg shift */
   when(riseReg){
      regCount := regCount - 1.U
      padReg := padReg(regLen - 2, 0) ## io.sdata
   }
  

  /* state machine */
  validReg := 0.U
  switch(stateReg){
    is(sinit){
      regCount := (regLen + 1).U
      when(io.data.ready && !io.data.valid){
        stateReg := slatch
        countReg := 0.U
      }
    }
    is(slatch){
      when(riseReg){
        stateReg := sclow
      }
    }
    is(sclow){
      when(riseReg){
        stateReg := schigh
      }

    }
    is(schigh){
      when(fallReg){
        when(regCount =/= 0.U){
          stateReg := sclow
        }.otherwise{
          stateReg := svalid
        }
      }
    }
    is(svalid){
      stateReg := sinit
      validReg := 1.U
    }
  }

  io.dclock := RegNext(stateReg === schigh)
  io.dlatch := RegNext(stateReg === slatch)
  io.data.bits := padReg
  io.data.valid := validReg
}

object ChisNesPad extends App {
  if (args.length == 0) {
    val filename = "ChisNesPad.v"
    println("Generate verilog source for ChisNesPad Module")
    val verilog_src = chisel3.emitVerilog(new ChisNesPad)
    val filepath = os.pwd / filename
    if (os.exists(filepath)) os.remove(filepath)
    os.write(filepath, verilog_src)
  } else if (args(0) == "--systemVerilog") {
    println("Generating systemVerilog sources for ChisNesPad Module")
    ChiselStage.emitSystemVerilogFile( new ChisNesPad(),
      firtoolOpts = Array("-disable-all-randomization",
                          "--lowering-options=disallowLocalVariables", // avoid 'automatic logic'
                                                     // https://github.com/chipsalliance/chisel/issues/3706#issuecomment-1907373238
                          "-strip-debug-info"))
  }
}
