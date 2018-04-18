import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.HashMap;
// S | S    [0]
// S | T    [1]
// T | T    [2]
// T | S    [3]
// E | S    [4]
// F | S    [6]
// E | T    [5]
// F | T    [7]
public class Markov{
    private HashMap<String, MarkovInput> probabilityMap;
    private static final int TRANSITIONAL_PROB_COUNT = 8;
    private MarkovInput[] transitionProbabilities;
    private static String sequence;
    private static String[] states;
    private static String[] measurableStates;
    public Markov(String inputFilename){
        this.transitionProbabilities = new MarkovInput[TRANSITIONAL_PROB_COUNT];
        this.probabilityMap = new HashMap<String, MarkovInput>();
        this.readFile(inputFilename);
    }
    public int computeNumerator(String str, String findStr){
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
    public int computeDenominator(String sequnece, String given){
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
        for(String base : this.states){
            for(String given : this.states){
                System.out.print(base+given+" ");
                System.out.println(computeNumerator(this.sequence,given+base) + " " + computeDenominator(this.sequence, given));
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
            for(String key : probabilityMap.keySet()){
                probabilityMap.get(key).printInput();
            }
            this.sequence = br.readLine();
            System.out.println(sequence);
            // while((line = br.readLine()) != null){
            //     System.out.println(line);
            // }
            computeTransitionProbabilities();
        }
        catch(Exception e){
            e.printStackTrace();
        }
    }

}