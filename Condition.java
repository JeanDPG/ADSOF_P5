import java.util.function.Predicate;

public class Condition<T> {
    private final String target;
    private final Predicate<T> predicate;

    public Condition(String target, Predicate<T> prediction) {
        this.target = target;
        this.predicate = prediction;
    }

    public boolean test(T element) {
        return predicate.test(element);
    }
    public String getTarget(){
        return target;
    }
}
