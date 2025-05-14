package ntu.basico.baithick_62132949;

public class CurrencyDisplayItem {
    private String code; // VND, USD
    private String name; // Vietnamese Dong, US Dollar (tùy chọn)
    private String flagResourceName; // Tên file cờ trong drawable (ví dụ "flag_vn")
    private double rateAgainstBase; // Tỷ giá so với đồng tiền cơ sở (ví dụ USD)
    private String displayValue; // Giá trị đã định dạng để hiển thị
    private boolean isBaseCurrency; // Có phải là đồng tiền cơ sở đang nhập không

    public CurrencyDisplayItem(String code, double rateAgainstBase) {
        this.code = code;
        this.rateAgainstBase = rateAgainstBase;
        this.displayValue = "0"; // Mặc định
        this.isBaseCurrency = false;
        this.flagResourceName = mapCurrencyCodeToFlagResourceName(code);

    }

    private String mapCurrencyCodeToFlagResourceName(String currencyCode) {
        switch (currencyCode.toUpperCase()) {
            case "VND":
                return "flag_vn";
            case "USD":
                return "flag_us";
            case "JPY":
                return "flag_jp";
            case "KRW":
                return "flag_kr";
            case "CNY":
                return "flag_cn";
            case "EUR":
                return "flag_eu";

            default:
                return "flag_default";
        }
    }

        // Getters and Setters
    public String getCode() { return code; }
    public void setCode(String code) { this.code = code; }
    public String getFlagResourceName() { return flagResourceName; }

    public double getRateAgainstBase() { return rateAgainstBase; }
    public void setRateAgainstBase(double rateAgainstBase) { this.rateAgainstBase = rateAgainstBase; }
    public String getDisplayValue() { return displayValue; }
    public void setDisplayValue(String displayValue) { this.displayValue = displayValue; }
    public boolean isBaseCurrency() { return isBaseCurrency; }
    public void setBaseCurrency(boolean baseCurrency) { isBaseCurrency = baseCurrency; }
}