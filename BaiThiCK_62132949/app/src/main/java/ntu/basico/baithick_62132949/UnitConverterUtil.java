package ntu.basico.baithick_62132949;

import android.util.Log;
import java.util.Objects;

public class UnitConverterUtil {
    private static final String TAG_UTIL = "UnitConverterUtil";
    private static final String TYPE_LENGTH = "Độ dài";
    private static final String TYPE_WEIGHT = "Khối lượng";
    private static final String TYPE_TEMPERATURE = "Nhiệt độ";
    private static final String TYPE_TIME = "Thời gian";
    private static final String TYPE_AREA = "Diện tích";

    // Độ dài
    public static final String MET = "Mét (m)";
    public static final String CM = "Centimét (cm)";
    public static final String INCH = "Inch (in)";
    public static final String FEET = "Feet (ft)";
    public static final String KM = "Kilômét (km)";
    public static final String MM = "Milimét (mm)";

    // Khối lượng
    public static final String KG = "Kilôgam (kg)";
    public static final String GRAM = "Gam (g)";
    public static final String POUND = "Pound (lb)";
    public static final String OUNCE = "Ounce (oz)";
    public static final String MG = "Miligam (mg)";

    // Nhiệt độ
    public static final String CELSIUS = "Độ C (°C)";
    public static final String FAHRENHEIT = "Độ F (°F)";
    public static final String KELVIN = "Độ K (K)";

    // Thời gian
    public static final String SECOND = "Giây (s)";
    public static final String MINUTE = "Phút (min)";
    public static final String HOUR = "Giờ (h)";
    public static final String DAY = "Ngày (day)";

    // Diện tích
    public static final String SQ_METER = "Mét vuông (m²)";
    public static final String SQ_KILOMETER = "Kilômét vuông (km²)";
    public static final String HECTARE = "Hecta (ha)";
    public static final String SQ_FEET = "Feet vuông (ft²)";
    public static final String ACRE = "Acre (ac)";

    // Chuyển đổi đơn vị và trả về kết quả
    public static double convert(double value, String fromUnit, String toUnit, String unitType) {
        Log.i(TAG_UTIL, "Attempting to convert: value=" + value + ", fromUnit='" + fromUnit + "', toUnit='" + toUnit + "', unitType='" + unitType + "'");

        if (unitType == null || fromUnit == null || toUnit == null) {
            Log.e(TAG_UTIL, "Conversion failed: Null input parameter(s).");
            return Double.NaN;
        }

        if (Objects.equals(unitType, TYPE_LENGTH)) {
            return convertLength(value, fromUnit, toUnit);
        } else if (Objects.equals(unitType, TYPE_WEIGHT)) {
            return convertWeight(value, fromUnit, toUnit);
        } else if (Objects.equals(unitType, TYPE_TEMPERATURE)) {
            return convertTemperature(value, fromUnit, toUnit);
        } else if (Objects.equals(unitType, TYPE_TIME)) {
            return convertTime(value, fromUnit, toUnit);
        } else if (Objects.equals(unitType, TYPE_AREA)) {
            return convertArea(value, fromUnit, toUnit);
        } else {
            Log.e(TAG_UTIL, "Conversion failed: Unknown unitType: '" + unitType + "'");
            return Double.NaN;
        }
    }

    // Các hàm chuyển đổi đơn vị
    private static double convertLength(double value, String fromUnit, String toUnit) {
        double valueInMeters;
        switch (fromUnit) {
            case MET: valueInMeters = value; break;
            case CM: valueInMeters = value / 100.0; break;
            case INCH: valueInMeters = value * 0.0254; break;
            case FEET: valueInMeters = value * 0.3048; break;
            case KM: valueInMeters = value * 1000.0; break;
            case MM: valueInMeters = value / 1000.0; break;
            default: Log.e(TAG_UTIL, "Length - Unknown fromUnit: '" + fromUnit + "'"); return Double.NaN;
        }

        switch (toUnit) {
            case MET: return valueInMeters;
            case CM: return valueInMeters * 100.0;
            case INCH: return valueInMeters / 0.0254;
            case FEET: return valueInMeters / 0.3048;
            case KM: return valueInMeters / 1000.0;
            case MM: return valueInMeters * 1000.0;
            default: Log.e(TAG_UTIL, "Length - Unknown toUnit: '" + toUnit + "'"); return Double.NaN;
        }
    }

    private static double convertWeight(double value, String fromUnit, String toUnit) {
        double valueInKg;
        switch (fromUnit) {
            case KG: valueInKg = value; break;
            case GRAM: valueInKg = value / 1000.0; break;
            case POUND: valueInKg = value * 0.45359237; break;
            case OUNCE: valueInKg = value * 0.0283495231; break;
            case MG: valueInKg = value / 1_000_000.0; break;
            default: Log.e(TAG_UTIL, "Weight - Unknown fromUnit: '" + fromUnit + "'"); return Double.NaN;
        }

        switch (toUnit) {
            case KG: return valueInKg;
            case GRAM: return valueInKg * 1000.0;
            case POUND: return valueInKg / 0.45359237;
            case OUNCE: return valueInKg / 0.0283495231;
            case MG: return valueInKg * 1_000_000.0;
            default: Log.e(TAG_UTIL, "Weight - Unknown toUnit: '" + toUnit + "'"); return Double.NaN;
        }
    }

    private static double convertTemperature(double value, String fromUnit, String toUnit) {
        if (Objects.equals(fromUnit, toUnit)) return value;
        double celsiusValue;
        switch (fromUnit) {
            case CELSIUS: celsiusValue = value; break;
            case FAHRENHEIT: celsiusValue = (value - 32) * 5.0 / 9.0; break;
            case KELVIN: celsiusValue = value - 273.15; break;
            default: Log.e(TAG_UTIL, "Temp - Unknown fromUnit: '" + fromUnit + "'"); return Double.NaN;
        }

        switch (toUnit) {
            case CELSIUS: return celsiusValue;
            case FAHRENHEIT: return (celsiusValue * 9.0 / 5.0) + 32;
            case KELVIN: return celsiusValue + 273.15;
            default: Log.e(TAG_UTIL, "Temp - Unknown toUnit: '" + toUnit + "'"); return Double.NaN;
        }
    }

    private static double convertTime(double value, String fromUnit, String toUnit) {
        double valueInSeconds;
        switch (fromUnit) {
            case SECOND: valueInSeconds = value; break;
            case MINUTE: valueInSeconds = value * 60.0; break;
            case HOUR: valueInSeconds = value * 3600.0; break;
            case DAY: valueInSeconds = value * 86400.0; break;
            default: Log.e(TAG_UTIL, "Time - Unknown fromUnit: '" + fromUnit + "'"); return Double.NaN;
        }

        switch (toUnit) {
            case SECOND: return valueInSeconds;
            case MINUTE: return valueInSeconds / 60.0;
            case HOUR: return valueInSeconds / 3600.0;
            case DAY: return valueInSeconds / 86400.0;
            default: Log.e(TAG_UTIL, "Time - Unknown toUnit: '" + toUnit + "'"); return Double.NaN;
        }
    }

    private static double convertArea(double value, String fromUnit, String toUnit) {
        double valueInSqMeters;
        switch (fromUnit) {
            case SQ_METER: valueInSqMeters = value; break;
            case SQ_KILOMETER: valueInSqMeters = value * 1_000_000.0; break;
            case HECTARE: valueInSqMeters = value * 10_000.0; break;
            case SQ_FEET: valueInSqMeters = value * 0.09290304; break;
            case ACRE: valueInSqMeters = value * 4046.8564224; break;
            default: Log.e(TAG_UTIL, "Area - Unknown fromUnit: '" + fromUnit + "'"); return Double.NaN;
        }

        switch (toUnit) {
            case SQ_METER: return valueInSqMeters;
            case SQ_KILOMETER: return valueInSqMeters / 1_000_000.0;
            case HECTARE: return valueInSqMeters / 10_000.0;
            case SQ_FEET: return valueInSqMeters / 0.09290304;
            case ACRE: return valueInSqMeters / 4046.8564224;
            default: Log.e(TAG_UTIL, "Area - Unknown toUnit: '" + toUnit + "'"); return Double.NaN;
        }
    }
}