package foodvisor.com.foodvisor.model;

public class RestaurantItem {
    String product_id;

    public String getProductId() {
        return product_id;
    }

    public void setProductId(String product_id) {
        this.product_id = product_id;
    }

    String product_name;

    public String getProductName() {
        return product_name;
    }

    public void setProductName(String product_name) {
        this.product_name = product_name;
    }

    String product_description;

    public String getProductDescription() {
        return product_description;
    }

    public void setProductDescription(String product_description) {
        this.product_description = product_description;
    }

    String product_rating;

    public String getProductRating() {
        return product_rating;
    }

    public void setProductRating(String product_rating) {
        this.product_rating = product_rating;
    }

    String product_price;

    public String getProductPrice() {
        return product_price;
    }

    public void setProductPrice(String product_price) {
        this.product_price = product_price;
    }

    String product_distance;

    public String getProductDistance() {
        return product_distance;
    }

    public void setProductDistance(String product_distance) {
        this.product_distance = product_distance;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    String imageUrl;
}
