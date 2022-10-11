package crypto;

public class Test {
    public static void main(String[] args) {
        Manager.CryptoBean bean= Manager.getInstance().get(Manager.Type.BTC);
        System.out.println("success get private and address is "+bean.toString());
    }
}
