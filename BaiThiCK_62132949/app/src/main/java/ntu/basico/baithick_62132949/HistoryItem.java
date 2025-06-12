package ntu.basico.baithick_62132949;

public class HistoryItem {
    private long id;
    private String conversionType;
    private String sourceValue;
    private String sourceUnit;
    private String resultValue;
    private String resultUnit;
    private long timestamp;

    public HistoryItem(long id, String conversionType, String sourceValue, String sourceUnit, String resultValue, String resultUnit, long timestamp) {
        this.id = id;
        this.conversionType = conversionType;
        this.sourceValue = sourceValue;
        this.sourceUnit = sourceUnit;
        this.resultValue = resultValue;
        this.resultUnit = resultUnit;
        this.timestamp = timestamp;
    }

    // Getters
    public long getId() { return id; }
    public String getConversionType() { return conversionType; }
    public String getSourceValue() { return sourceValue; }
    public String getSourceUnit() { return sourceUnit; }
    public String getResultValue() { return resultValue; }
    public String getResultUnit() { return resultUnit; }
    public long getTimestamp() { return timestamp; }
}




