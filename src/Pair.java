

/**
 This class describes a simple Pair class for storing character/double pairs. Used for A4 from previous version.
 */

public class Pair implements Comparable<Pair>{
    // declare all required fields
    private char value;
    private double prob;

    /**
     Constructor 1
     All args accounted for
     */
    public Pair(char v, double p){
        value = v;
        prob = p;
    }

    public void setValue(char v){
        value = v;
    }

    public void setProb(double p){
        prob = p;
    }

    public char getValue(){
        return value;
    }

    public double getProb(){
        return prob;
    }

    /**
     The compareTo method overrides the compareTo method of the Comparable
     interface.
     */
    @Override
    public int compareTo(Pair p){
        return Double.compare(this.getProb(), p.getProb());
    }

    /**
     The toString method overrides the toString method of the Object class.
     */
    @Override
    public String toString(){
        return value+" : "+prob;
    }
}
