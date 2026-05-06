package tests.resources.exercise1;

/**
 * Person.java
 * 
 * Clase que representa a una persona con atributos físicos.
 * 
 * @author Jaime García González
 * @author Jean del Pozo Gómez
 * @version 1.0
 */
public class Person {

    private final String name;
    private final int age;
    private final double weight;
    private final int height;
    private final boolean male;

     /**
     * Constructor de la clase Person.
     * @param name Nombre de la persona.
     * @param age Edad en años.
     * @param weight Peso en kilogramos.
     * @param height Altura en centímetros.
     * @param male Booleano que indica sexo, true es hombre, false mujer.
     */
    public Person(String name, int age, double weight, int height, boolean male) {
        this.name = name;
        this.age = age;
        this.weight = weight;
        this.height = height;
        this.male = male;
    }

    /**
     * Devuelve el nombre.
     * @return El nombre de la persona.
     */
    public String name() {
        return name;
    }

     /**
     * Devuelve la edad.
     * @return La edad.
     */
    public int getAge() {
        return age;
    }

     /**
     * Devuelve el peso.
     * @return El peso.
     */
    public double weight() {
        return weight;
    }

    /**
     * Devuelve la altura.
     * @return La altura.
     */
    public int height() {
        return height;
    }

   /**
     * Devulve el sexo de la persona, convirtiendo al enum Gender.
     * @return MALE o FEMALE según corresponda.
     */
    public Gender gender() {
        return male ? Gender.MALE : Gender.FEMALE;
    }

     /**
     * Indica si la persona es hombre.
     * @return true si es hombre, false si es mujer.
     */
    public boolean isMale() { return male; }

    /**
     * Devuelve una representación de la persona.
     * @return Una representación de la persona.
     */
    @Override
public String toString() {
    return name + "{age: " + age + ", " + (male ? "male" : "female") + "}";
}

}
