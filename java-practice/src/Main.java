public class Main {
    public static void main(String[] args) {
        System.out.println("main start");
        Outer outer1 = Outer.getInstance();
        Outer outer2 = Outer.getInstance();
        System.out.println(outer1);
        System.out.println(outer2);
        System.out.println(outer1.count());
    }
}
