package x21l_5388_com.example.peezious;

public class Product {
    private String name;
    private String description;
    private double price;
    private  String type;
    private String imageBase64;

    // No-argument constructor (required by Firebase)
    public Product() {
        // Empty constructor for Firebase
    }

    // Constructor with arguments (for easy initialization)
    public Product(String name, String description, double price,String type, String imageBase64) {
        this.name = name;
        this.description = description;
        this.price = price;
        this.type= type;
        this.imageBase64 = imageBase64;
    }

    // Getter and setter methods for each field

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getimageBase64() {
        return imageBase64;
    }

    public void setimageBase64(String imageBase64) {
        this.imageBase64 = imageBase64;
    }
    public String getType(){
        return type;
    }
    public void setType(String type){
        this.type=type;
    }
}
