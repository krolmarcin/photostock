package pl.com.bottega.photostock.sales.model.client;

public enum ClientStatus {

    STANDARD("Standardowy"),
    VIP("Vip"),
    GOLD("Zoty"),
    SILVER("Srebrny"),
    PLATINUM("Platynowy");

    private String statusName;

    ClientStatus(String statusName) {
        this.statusName = statusName;
    }

    public String getStatusName() {
        return statusName;
    }
}
