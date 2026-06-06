// interface acuan untuk pola desain strategy pada sistem pembayaran
package service.payment;

import java.io.Serializable;

public interface PaymentStrategy extends Serializable {
    boolean pay(double amount);
    String getPaymentMethodName();
}
