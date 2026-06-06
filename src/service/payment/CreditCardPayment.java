// implementasi pembayaran via kartu kredit dengan sedikit sistem validasi digit
package service.payment;

public class CreditCardPayment implements PaymentStrategy {
    private String cardNumber;
    private String cardHolderName;

    public CreditCardPayment(String cardNumber, String cardHolderName) {
        this.cardNumber = cardNumber;
        this.cardHolderName = cardHolderName;
    }

    @Override
    public boolean pay(double amount) {
        // Simulasi validasi kartu kredit
        if (cardNumber != null && cardNumber.length() >= 16) {
            System.out.println("Processing credit card payment of: $" + amount + " for " + cardHolderName);
            return true;
        }
        return false;
    }

    @Override
    public String getPaymentMethodName() {
        return "Credit Card";
    }
}
