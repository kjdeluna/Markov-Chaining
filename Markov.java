import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.HashMap;
import java.util.LinkedList;
public class Markov{
    private HashMap<String, MarkovInput> probabilityMap;\
    private static String sequence;
    private static String[] states;
    private static String[] measurableStates;
    private static MarkovInput[] inputs;
    public Markov(String inputFilename){
        this.probabilityMap = new HashMap<String, MarkovInput>();
        this.readFile(inputFilename);
        this.computeInputs();
        this.printProbabilityMap();
        this.writeInputs();
    }
    public int computeNumerator(String str, String findStr){
        // Count the occurrences of _given+_base in the sequence.
        int lastIndex = 1;
        int count = 0;
        while (lastIndex != -1) {

            lastIndex = str.indexOf(findStr, lastIndex-1);

            if (lastIndex != -1) {
                count++;
                lastIndex += findStr.length();
            }
        }
        return count;
    }
    public int computeDenominator(String sequence, String given){
        // Count the occurrences of _given letter in the sequence.
        // If the end of the sequence is a _given letter: decrement count
        char[] chars = sequence.toCharArray();
        char a = given.toCharArray()[0];
        int count = 0;
        for(int i = 0; i < chars.length; i++){
            if(a == chars[i]) count++;
        }
        if(a == chars[chars.length - 1]) count--;
        return count;
    }
    public void computeTransitionProbabilities(){
        char[] chars = this.sequence.toCharArray();
        for(String str  : states){
            char a = str.toCharArray()[0];
            if(a == chars[0]) this.probabilityMap.put(str + "0", new MarkovInput(new MarkovState(str, 0), 1));
            else this.probabilityMap.put(str + "0", new MarkovInput(new MarkovState(str, 0), 0));
        }
        double result = 0;
        for(String base : this.states){
            for(String given : this.states){
                System.out.print(base+given+" ");
                result = ((double) (computeNumerator(this.sequence, given+base))) / computeDenominator(this.sequence,given);
                this.probabilityMap.put(base+"|"+given, new MarkovInput(new MarkovState(base, -1), new MarkovState(given, -1), result));
    
            }
        }
    }
    private boolean containsState(String var){
        // A linear search that finds whether a _var state exists
        for(String state : this.states){
            if(state.equals(var)) return true;
        }
        return false;
    }
    private boolean containsMeasurableState(String var){
        // A linear search that finds whether a _var state in measurable states exists
        for(String state : this.measurableStates){
            if(state.equals(var)) return true;
        }
        return false;
    }
    private double computeStateProbability(MarkovState ms){
        double result = 0;
        if(this.probabilityMap.containsKey(ms.getVariable() + ms.getSubscript())){
            return this.probabilityMap.get(ms.getVariable() + ms.getSubscript()).getValue();
        }
        if(this.containsState(ms.getVariable())){
            // Means that this is a transition state
            //      Will use formula: P(S_n) = P(S_n|S_n-1) * P(S_n-1) + P(S_n|T_n-1) * P(T_n-1)
            for(String str : states){
                result += (this.probabilityMap.get(ms.getVariable() + "|" + str).getValue() * computeStateProbability(new MarkovState(str, ms.getSubscript() - 1)));
            }
        } else if(this.containsMeasurableState(ms.getVariable())){
            // This is a measurable state
            // Will use total probability. P(E_n) = P(E_n|S_n) * P(S_n) + P(E_n|T_n) * P(T_n)
            for(String str : states){
                result += (this.probabilityMap.get(ms.getVariable() + "|" + str).getValue() * computeStateProbability(new MarkovState(str, ms.getSubscript())));
            }
        }
        this.probabilityMap.put(ms.getVariable() + ms.getSubscript(), new MarkovInput(new MarkovState(ms.getVariable(), ms.getSubscript()), result));
        return result;
    }
    private double computeBayes(MarkovInput mi){
        if(this.probabilityMap.containsKey(mi.getBase().getVariable() + "|" + mi.getGiven().getVariable())){
            return this.probabilityMap.get((mi.getBase().getVariable() + "|" + mi.getGiven().getVariable())).getValue();
        }
        double numerator = this.probabilityMap.get(mi.getGiven().getVariable() + "|" + mi.getBase().getVariable()).getValue() * computeStateProbability(new MarkovState(mi.getBase().getVariable(), mi.getBase().getSubscript()));
        double denominator = this.computeStateProbability(new MarkovState(mi.getGiven().getVariable(), mi.getGiven().getSubscript()));
        if(probabilityMap.containsKey(mi.getBase().getVariable() + mi.getBase().getSubscript())){
            System.out.println("existing");
        } else {

        }
        this.probabilityMap.put(mi.getBase().getVariable() + mi.getBase().getSubscript() + "|" + mi.getGiven().getVariable() + mi.getGiven().getSubscript(), new MarkovInput(new MarkovState(mi.getBase().getVariable(), mi.getBase().getSubscript()), new MarkovState(mi.getGiven().getVariable(), mi.getGiven().getSubscript()), numerator/denominator));
        return numerator / denominator;
    }
    private void readFile(String inputFilename){
        try{
            FileReader fr = new FileReader(inputFilename);
            BufferedReader br = new BufferedReader(fr);
            String line;
            this.states = br.readLine().split(" ");
            this.measurableStates = br.readLine().split(" ");
            for(int i = 0; i < states.length; i++){
                String [] probabilities = br.readLine().split(" ");
                for(int j = 0; j < measurableStates.length; j++){
                    this.probabilityMap.put(measurableStates[j]+"|"+states[i],new MarkovInput(new MarkovState(measurableStates[j], -1), new MarkovState(states[i], -1), Double.parseDouble(probabilities[j])));
                }
            }
            this.sequence = br.readLine();
            System.out.println(sequence);
            int count = Integer.parseInt(br.readLine());
            this.inputs = new MarkovInput[count];
            computeTransitionProbabilities();
            int counter = 0;
            for(int i = 0; i < count; i++){
                line = br.readLine();
                String[] separated = line.split(" given ");
                LinkedList<MarkovState> collector = new LinkedList<MarkovState>();
                for(String s : separated){
                    collector.add(new MarkovState(Character.toString(s.charAt(0)), Integer.parseInt(s.substring(1))));
                }
                MarkovInput mi = new MarkovInput(collector.get(0), collector.get(1));
                this.inputs[counter++] = mi; 
            }

        }
        catch(Exception e){
            e.printStackTrace();
        }
    }
    private void computeInputs(){
        for(MarkovInput mi : this.inputs){
            mi.setValue(computeBayes(mi));
        }
    }
    public void printProbabilityMap(){
        for(String key : probabilityMap.keySet()){
            probabilityMap.get(key).printInput();
        }
    }
    public void writeInputs(){
        try{
            FileWriter fw = new FileWriter("hmm.out");
            for(MarkovInput mi : this.inputs){
                fw.write(mi.getBase().getVariable() + mi.getBase().getSubscript() 
                    + " given " 
                    + mi.getGiven().getVariable() + mi.getGiven().getSubscript() 
                    + " = " + mi.getValue() + "\n");
            }
            fw.close();
        } catch (Exception e){
            e.printStackTrace();
        }
    }
}