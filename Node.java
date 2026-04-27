import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;
import java.util.Collections;

public class Node<T> {
    private List<Condition<T>> conditions;
    private String name;
    private String otherwise;

    public Node(String name) {
        this.name = name;
        this.conditions = new ArrayList<>();
        this.otherwise = null;
    }

    public Node<T> withCondition(String targetNode, Predicate<T> predicate) {
        this.conditions.add(new Condition<>(targetNode, predicate));
        return this;
    }

    public Node<T> otherwise(String target) {
        this.otherwise = target;
        return this;
    }

    public String nextNode(T element){
        for (Condition<T> c : conditions){
            if(c.test(element)){
                return c.getTarget();
            }
        }
            if(otherwise!=null){
                return otherwise;
            }
            /*Aqui quiza habria que implementar alguna excepcion para el caso en el que se queda atrapada porque no hay otherwise */
            return null;
        }
    public String getName(){
        return name;
    }
    public String getOtherwiseTarget(){
        return otherwise;
    }
    public List<Condition<T>> getConditions(){
        return Collections.unmodifiableList(this.conditions);
    }
    public boolean isLeaf() {
        return conditions.isEmpty() && otherwise == null;
    }

}
