package datasets;

public class Person {

    private final String name;
    private final int age;
    private final double weight;
    private final int height;
    private final boolean male;

    // Guarda todos los datos originales de una persona.
    public Person(String name, int age, double weight, int height, boolean male) {
        this.name = name;
        this.age = age;
        this.weight = weight;
        this.height = height;
        this.male = male;
    }

    // Devuelve el nombre.
    public String name() {
        return name;
    }

    // Devuelve la edad.
    public int age() {
        return age;
    }

    // Devuelve el peso.
    public double weight() {
        return weight;
    }

    // Devuelve la altura.
    public int height() {
        return height;
    }

    // Convierte el booleano interno en un valor mas legible del enum Gender.
    public Gender gender() {
        return male ? Gender.MALE : Gender.FEMALE;
    }

}
