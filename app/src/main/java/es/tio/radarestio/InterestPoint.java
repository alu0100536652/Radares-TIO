package es.tio.radarestio;

public class InterestPoint {

    private double latitude;
    private double longitude;
    private Boolean status;
    private String message;

    InterestPoint(Double latitude, double longitude, Boolean status, String message){
        this.latitude = latitude;
        this.longitude = longitude;
        this.status = status;
        this.message = message;
    }

    public Boolean getStatus() {
            return status;
        }

    public void setStatus(Boolean status) {
            this.status = status;
        }

    public double getLongitude() {
            return longitude;
        }

    public void setLongitude(double longitude) {
            this.longitude = longitude;
        }

    public double getLatitude() {
            return latitude;
        }

    public void setLatitude(double latitude) {
            this.latitude = latitude;
        }

    public String getMessage() {
            return message;
        }

    public void setMessage(String message) {
            this.message = message;
        }
}

