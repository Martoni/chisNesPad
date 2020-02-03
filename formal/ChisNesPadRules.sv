//BeginModule:ChisNesPad

always@(posedge clock) begin
    assume(io_dlatch == 1'b1);
    assert(stateReg == 2'b00); 
end

//EndModule:ChisNesPad
