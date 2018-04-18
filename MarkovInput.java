public class MarkovInput{
    private MarkovState base;
    private MarkovState given;
    private double value;
    public MarkovInput(MarkovState base, MarkovState given){
        this.base = base;
        this.given = given;
    }
    public MarkovInput(MarkovState base, MarkovState given, double value){
        this.base = base;
        this.given = given;
        this.value = value;
    }    
    public MarkovState getBase(){
        return this.base;
    }
    public MarkovState getGiven(){
        return this.given;
    }
    public double getValue(){
        return this.value;
    }
    public void printInput(){
        System.out.println(this.base.getVariable() + "|" + this.given.getVariable() + " = " + this.value);
    }

}