package x21l_5388_com.example.peezious;

public class Order {
    private String userId;
    private String phone;
    private String address;
    private int totalPrice;
    private String status;
    private String orderDetails;

    public Order(){}

    public Order(String userId, String phone, String address, int totalPrice, String status, String orderDetails) {
        this.userId = userId;
        this.phone = phone;
        this.address = address;
        this.totalPrice = totalPrice;
        this.status = status;
        this.orderDetails = orderDetails;
    }

    // Getters and Setters
    public String getUserId() {
        return userId;
    }

    public String getPhone() {
        return phone;
    }

    public String getAddress() {
        return address;
    }

    public int getTotalPrice() {
        return totalPrice;
    }

    public String getStatus() {
        return status;
    }

    public String getOrderDetails() {
        return orderDetails;
    }
}
