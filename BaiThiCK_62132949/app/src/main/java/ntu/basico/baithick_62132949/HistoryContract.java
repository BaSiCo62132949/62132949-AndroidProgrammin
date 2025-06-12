package ntu.basico.baithick_62132949;

import android.provider.BaseColumns;

// Định nghĩa cấu trúc bảng lịch sử
public final class HistoryContract {
    private HistoryContract() {}

    /* Định nghĩa nội dung bảng lịch sử */
    public static class HistoryEntry implements BaseColumns {
        public static final String TABLE_NAME = "history_entries";

        public static final String COLUMN_NAME_CONVERSION_TYPE = "conversion_type";

        // Giá trị nguồn: "10", "100"... (TEXT để giữ nguyên định dạng)
        public static final String COLUMN_NAME_SOURCE_VALUE = "source_value";
        // Đơn vị nguồn: "cm", "USD"... (TEXT)
        public static final String COLUMN_NAME_SOURCE_UNIT = "source_unit";

        // Giá trị kết quả: "3.94", "23500"... (TEXT)
        public static final String COLUMN_NAME_RESULT_VALUE = "result_value";
        // Đơn vị đích: "inch", "VND"... (TEXT)
        public static final String COLUMN_NAME_RESULT_UNIT = "result_unit";

        // Thời gian lưu (INTEGER, lưu dưới dạng Unix timestamp milliseconds)
        public static final String COLUMN_NAME_TIMESTAMP = "timestamp";
    }
}
