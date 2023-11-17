

package graphparser;

/**
 *
 * @author Felice
 */
public class EdgeClass {
    
    private int sourceNode;
    private int targetNode;
    private int weight;
    private String type;

    public int getSourceNode() {
        return sourceNode;
    }

    public void setSourceNode(int sourceNode) {
        this.sourceNode = sourceNode;
    }

    public int getTargetNode() {
        return targetNode;
    }

    public void setTargetNode(int targetNode) {
        this.targetNode = targetNode;
    }

    public int getWeight() {
        return weight;
    }

    public void setWeight(int weight) {
        this.weight = weight;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return "EdgeClass{" + "sourceNode=" + sourceNode + ", targetNode=" + targetNode + ", weight=" + weight + ", type=" + type + '}';
    }

    
    
}
