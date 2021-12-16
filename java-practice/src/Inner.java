public class Inner {

    private static Inner instance;

    private Inner() {
        System.out.println("construct Inner");
    }

    public static Inner getInstance() {
        if (instance == null) {
            // マルチスレッド環境でシングルトンオブジェクトを作成する際にブロックする必要がある
            synchronized (Inner.class) {
                instance = new Inner();
            }
        }
        System.out.println("get inner instance");
        return instance;
    }
}
