public class Cell {
    private String oem;
    private String model;
    private String launchAnnounced;
    private String launchStatus;
    private String bodyDimensions;
    private String bodyWeight;
    private String bodySim;
    private String displayType;
    private String displaySize;
    private String displayResolution;
    private String featuresSensors;
    private String platformOs;

    // Constructor
    public Cell(String oem, String model, String launchAnnounced, String launchStatus,
                String bodyDimensions, String bodyWeight, String bodySim, String displayType,
                String displaySize, String displayResolution, String featuresSensors, String platformOs) {
        this.oem = oem;
        this.model = model;
        this.launchAnnounced = launchAnnounced;
        this.launchStatus = launchStatus;
        this.bodyDimensions = bodyDimensions;
        this.bodyWeight = bodyWeight;
        this.bodySim = bodySim;
        this.displayType = displayType;
        this.displaySize = displaySize;
        this.displayResolution = displayResolution;
        this.featuresSensors = featuresSensors;
        this.platformOs = platformOs;
    }

    // Getters and setters for all attributes
    public String getOem() {
        return oem;
    }

    public void setOem(String oem) {
        this.oem = oem;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getLaunchAnnounced() {
        return launchAnnounced;
    }

    public void setLaunchAnnounced(String launchAnnounced) {
        this.launchAnnounced = launchAnnounced;
    }

    public String getLaunchStatus() {
        return launchStatus;
    }

    public void setLaunchStatus(String launchStatus) {
        this.launchStatus = launchStatus;
    }

    public String getBodyDimensions() {
        return bodyDimensions;
    }

    public void setBodyDimensions(String bodyDimensions) {
        this.bodyDimensions = bodyDimensions;
    }

    public String getBodyWeight() {
        return bodyWeight;
    }

    public void setBodyWeight(String bodyWeight) {
        this.bodyWeight = bodyWeight;
    }

    public String getBodySim() {
        return bodySim;
    }

    public void setBodySim(String bodySim) {
        this.bodySim = bodySim;
    }

    public String getDisplayType() {
        return displayType;
    }

    public void setDisplayType(String displayType) {
        this.displayType = displayType;
    }

    public String getDisplaySize() {
        return displaySize;
    }

    public void setDisplaySize(String displaySize) {
        this.displaySize = displaySize;
    }

    public String getDisplayResolution() {
        return displayResolution;
    }

    public void setDisplayResolution(String displayResolution) {
        this.displayResolution = displayResolution;
    }

    public String getFeaturesSensors() {
        return featuresSensors;
    }

    public void setFeaturesSensors(String featuresSensors) {
        this.featuresSensors = featuresSensors;
    }

    public String getPlatformOs() {
        return platformOs;
    }

    public void setPlatformOs(String platformOs) {
        this.platformOs = platformOs;
    }

    // Additional methods

    // Method to check if the phone's launch status is active
    public boolean isActive() {
        return launchStatus.equalsIgnoreCase("active");
    }

    // Method to determine if the phone's body weight is lightweight
    public boolean isLightweight() {
        // Assuming lightweight if body weight is less than 150 grams
        return Float.parseFloat(bodyWeight.split(" ")[0]) < 150;
    }

    // Method to extract the primary camera resolution from the features
    public String extractPrimaryCameraResolution() {
        // Assuming primary camera resolution is mentioned in features
        // and follows the format "Primary: 48 MP, (wide), 13 MP, (ultrawide), 5 MP, (depth)"
        String[] parts = featuresSensors.split(",");
        for (String part : parts) {
            if (part.contains("Primary")) {
                return part.trim().split(":")[1].trim();
            }
        }
        return "Not specified";
    }

    // Method to determine if the phone's display type is OLED
    public boolean isOLED() {
        return displayType.contains("OLED");
    }

    // Method to calculate the aspect ratio of the display
    public float calculateAspectRatio() {
        // Assuming aspect ratio is calculated as width / height
        float width = Float.parseFloat(displayResolution.split("x")[0]);
        float height = Float.parseFloat(displayResolution.split("x")[1]);
        return width / height;
    }

    // Method to check if the phone's platform OS is upgradable
    public boolean isUpgradable() {
        return platformOs.contains("upgradable");
    }

    // Method to extract the chipset information from the platform OS
    public String extractChipset() {
        // Assuming chipset information follows the format "Chipset: Qualcomm SM8250 Snapdragon 865 (7 nm+)"
        return platformOs.split(":")[1].trim().split(" ")[0] + " " + platformOs.split(":")[1].trim().split(" ")[1];
    }

    // ToString method
    @Override
    public String toString() {
        return "Cell{" +
                "oem='" + oem + '\'' +
                ", model='" + model + '\'' +
                ", launchAnnounced='" + launchAnnounced + '\'' +
                ", launchStatus='" + launchStatus + '\'' +
                ", bodyDimensions='" + bodyDimensions + '\'' +
                ", bodyWeight='" + bodyWeight + '\'' +
                ", bodySim='" + bodySim + '\'' +
                ", displayType='" + displayType + '\'' +
                ", displaySize='" + displaySize + '\'' +
                ", displayResolution='" + displayResolution + '\'' +
                ", featuresSensors='" + featuresSensors + '\'' +
                ", platformOs='" + platformOs + '\'' +
                '}';
    }
}