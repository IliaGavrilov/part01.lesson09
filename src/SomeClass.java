public class SomeClass implements Worker {

    /* Пустой конструктор */
    public SomeClass(){
    }

    /* Основной метод класса */
    public void doWork(){
        System.out.println("Заливаю фундамент");
    }

    /* Статическое поле */
    static {
        System.out.println("Вася");
    }
}