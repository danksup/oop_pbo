// implementasi pembayaran secara tunai
package service.payment;

public class CashPayment implements PaymentStrategy {
    @Override
    public boolean pay(double amount) {
        // pembayaran tunai
        System.out.println("Processing cash payment of: $" + amount);
        return true; 
    }

    @Override
    public String getPaymentMethodName() {
        return "Cash";
    }
}
