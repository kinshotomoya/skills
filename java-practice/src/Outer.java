class Outer {
    private static Outer instance = new Outer();
    private static Inner inner;
    private static Integer count;

    private Outer() {
        System.out.println("construct Outer");
        inner = Inner.getInstance();
        System.out.println(inner);
    }

    public static Outer getInstance() {
        count = 10;
        return instance;
    }

    public Integer count() {
        return count;
    }

}
