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
    public MarkovInput(MarkovState base, double value){
        this.base = base;
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
        if(this.given != null) System.out.println(this.base.getVariable() + this.base.getSubscript()+ "|" + this.given.getVariable() + this.given.getSubscript() + " = " + this.value);
        else System.out.println(this.base.getVariable() + this.base.getSubscript() + " = " + this.value);
    }

}