//BeginModule:ChisNesPad

initial
    assume(reset==1'b1);

always@(posedge clock) begin
    if(reset == 1'b0) begin
        assert(regCount <= 17); 
    end
end

//EndModule:ChisNesPad
