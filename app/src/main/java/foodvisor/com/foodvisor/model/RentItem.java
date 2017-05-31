package foodvisor.com.foodvisor.model;

public class RentItem {
    String rent_id;

    public String getRentId() {
        return rent_id;
    }

    public void setRentId(String rent_id) {
        this.rent_id = rent_id;
    }

    String rent_name;

    public String getRentName() {
        return rent_name;
    }

    public void setRentName(String rent_name) {
        this.rent_name = rent_name;
    }

    String rent_description;

    public String getRentDescription() {
        return rent_description;
    }

    public void setRentDescription(String rent_description) {
        this.rent_description = rent_description;
    }

    String rent__short_description;

    public String getRentShortDescription() {
        return rent__short_description;
    }

    public void setRentShortDescription(String rent__short_description) {
        this.rent__short_description = rent__short_description;
    }

    String rent_cost;

    public String getRentCost() {
        return rent_cost;
    }

    public void setRentCost(String rent_cost) {
        this.rent_cost = rent_cost;
    }

    String rent_uploader;

    public String getRentUploader() {
        return rent_uploader;
    }

    public void setRentUploader(String rent_uploader) {
        this.rent_uploader = rent_uploader;
    }


    String rent_uploaded_at;

    public String getRentUploadedAt() {
        return rent_uploaded_at;
    }

    public void setRentUploadedAt(String rent_uploaded_at) {
        this.rent_uploaded_at = rent_uploaded_at;
    }


    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    String imageUrl;
}
