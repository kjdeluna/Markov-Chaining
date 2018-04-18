public class MarkovState{
    private String variable;
    private int subscript;
    public MarkovState(String variable, int subscript){
        this.variable = variable;
        this.subscript = subscript;
    }
    public String getVariable(){
        return this.variable;
    }
    public int getSubscript(){
        return this.subscript;
    }
}