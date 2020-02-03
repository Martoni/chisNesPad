//BeginModule:ChisNesPad

always@(posedge clock) begin
    assume(io_dlatch == 1'b1);
    assert(stateReg == 2'b00); 
end

initial
    assume(reset==1'b1);

always@(posedge clock) begin
    if(reset == 1'b0) begin
        assert(regCount <= 16); 
    end
end

//EndModule:ChisNesPad
