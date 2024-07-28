package model;

public class Payment {
    // Instance variables
    private double amount;
    private String paymentMethod;

    // Constructor
    public Payment(double amount, String paymentMethod) {
        this.amount = amount;
        this.paymentMethod = paymentMethod;
    }

    // Getter methods
    public double getAmount() {
        return amount;
    }

    public String getPaymentMethod() {
        return paymentMethod;
    }

    // Setter methods (if needed)
    public void setAmount(double amount) {
        this.amount = amount;
    }

    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    // Method to process the payment
    public boolean processPayment() {
        return true;
    }

    public String toString() {
        return "Payment{" +
                "amount=" + amount +
                ", paymentMethod='" + paymentMethod + '\'' +
                '}';
    }
}

