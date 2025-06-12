package ntu.basico.baithick_62132949;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import ntu.basico.baithick_62132949.HistoryItem;

import java.util.ArrayList;
import java.util.List;

// Hỗ trợ truy cập và thao tác với database lịch sử
public class HistoryDbHelper extends SQLiteOpenHelper {
    private static final String TAG = "HistoryDbHelper";

    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "ConversionHistory.db";

    // Câu lệnh tạo bảng SQL
    private static final String SQL_CREATE_ENTRIES =
            "CREATE TABLE " + HistoryContract.HistoryEntry.TABLE_NAME + " (" +
                    HistoryContract.HistoryEntry._ID + " INTEGER PRIMARY KEY," +
                    HistoryContract.HistoryEntry.COLUMN_NAME_CONVERSION_TYPE + " TEXT," +
                    HistoryContract.HistoryEntry.COLUMN_NAME_SOURCE_VALUE + " TEXT," +
                    HistoryContract.HistoryEntry.COLUMN_NAME_SOURCE_UNIT + " TEXT," +
                    HistoryContract.HistoryEntry.COLUMN_NAME_RESULT_VALUE + " TEXT," +
                    HistoryContract.HistoryEntry.COLUMN_NAME_RESULT_UNIT + " TEXT," +
                    HistoryContract.HistoryEntry.COLUMN_NAME_TIMESTAMP + " INTEGER)";

    // Câu lệnh xóa bảng SQL
    private static final String SQL_DELETE_ENTRIES =
            "DROP TABLE IF EXISTS " + HistoryContract.HistoryEntry.TABLE_NAME;

    public HistoryDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        Log.d(TAG, "onCreate: Creating database table: " + SQL_CREATE_ENTRIES);
        db.execSQL(SQL_CREATE_ENTRIES);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Chính sách nâng cấp đơn giản là hủy và tạo lại database
        Log.w(TAG, "onUpgrade: Dropping table and recreating. Old version: " + oldVersion + ", New version: " + newVersion);
        db.execSQL(SQL_DELETE_ENTRIES);
        onCreate(db);
    }

    // --- Các hàm CRUD (Create, Read, Update, Delete) ---

    /**
     * Thêm một mục lịch sử mới vào database
     * @return true nếu thành công, false nếu thất bại
     */
    public boolean insertHistory(String type, String sourceValue, String sourceUnit, String resultValue, String resultUnit) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(HistoryContract.HistoryEntry.COLUMN_NAME_CONVERSION_TYPE, type);
        values.put(HistoryContract.HistoryEntry.COLUMN_NAME_SOURCE_VALUE, sourceValue);
        values.put(HistoryContract.HistoryEntry.COLUMN_NAME_SOURCE_UNIT, sourceUnit);
        values.put(HistoryContract.HistoryEntry.COLUMN_NAME_RESULT_VALUE, resultValue);
        values.put(HistoryContract.HistoryEntry.COLUMN_NAME_RESULT_UNIT, resultUnit);
        values.put(HistoryContract.HistoryEntry.COLUMN_NAME_TIMESTAMP, System.currentTimeMillis()); // Thời gian hiện tại

        long newRowId = db.insert(HistoryContract.HistoryEntry.TABLE_NAME, null, values);
        // db.close(); // Không nên close db ở đây nếu dùng helper nhiều lần
        Log.d(TAG, "insertHistory: Inserted row with ID: " + newRowId);
        return newRowId != -1; // Trả về true nếu insert thành công (ID khác -1)
    }

    /**
     c
     * @return Danh sách các đối tượng HistoryItem
     */
    public List<HistoryItem> getAllHistoryItems() {
        List<HistoryItem> historyList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        String[] projection = {
                HistoryContract.HistoryEntry._ID,
                HistoryContract.HistoryEntry.COLUMN_NAME_CONVERSION_TYPE,
                HistoryContract.HistoryEntry.COLUMN_NAME_SOURCE_VALUE,
                HistoryContract.HistoryEntry.COLUMN_NAME_SOURCE_UNIT,
                HistoryContract.HistoryEntry.COLUMN_NAME_RESULT_VALUE,
                HistoryContract.HistoryEntry.COLUMN_NAME_RESULT_UNIT,
                HistoryContract.HistoryEntry.COLUMN_NAME_TIMESTAMP
        };

        // Sắp xếp theo timestamp giảm dần (mới nhất trước)
        String sortOrder = HistoryContract.HistoryEntry.COLUMN_NAME_TIMESTAMP + " DESC";

        Cursor cursor = null;
        try {
            cursor = db.query(
                    HistoryContract.HistoryEntry.TABLE_NAME,   // Bảng để query
                    projection,                               // Các cột trả về
                    null,                                     // Các cột cho WHERE clause
                    null,                                     // Các giá trị cho WHERE clause
                    null,                                     // group by
                    null,                                     // filter by row groups (having)
                    sortOrder                                 // Thứ tự sắp xếp
            );

            Log.d(TAG, "getAllHistoryItems: Found " + cursor.getCount() + " history items.");

            int idCol = cursor.getColumnIndexOrThrow(HistoryContract.HistoryEntry._ID);
            int typeCol = cursor.getColumnIndexOrThrow(HistoryContract.HistoryEntry.COLUMN_NAME_CONVERSION_TYPE);
            int sourceValCol = cursor.getColumnIndexOrThrow(HistoryContract.HistoryEntry.COLUMN_NAME_SOURCE_VALUE);
            int sourceUnitCol = cursor.getColumnIndexOrThrow(HistoryContract.HistoryEntry.COLUMN_NAME_SOURCE_UNIT);
            int resultValCol = cursor.getColumnIndexOrThrow(HistoryContract.HistoryEntry.COLUMN_NAME_RESULT_VALUE);
            int resultUnitCol = cursor.getColumnIndexOrThrow(HistoryContract.HistoryEntry.COLUMN_NAME_RESULT_UNIT);
            int timestampCol = cursor.getColumnIndexOrThrow(HistoryContract.HistoryEntry.COLUMN_NAME_TIMESTAMP);


            while (cursor.moveToNext()) {
                long itemId = cursor.getLong(idCol);
                String type = cursor.getString(typeCol);
                String sourceValue = cursor.getString(sourceValCol);
                String sourceUnit = cursor.getString(sourceUnitCol);
                String resultValue = cursor.getString(resultValCol);
                String resultUnit = cursor.getString(resultUnitCol);
                long timestamp = cursor.getLong(timestampCol);

                HistoryItem item = new HistoryItem(itemId, type, sourceValue, sourceUnit, resultValue, resultUnit, timestamp);
                historyList.add(item);
            }
        } catch (IllegalArgumentException e) {
            Log.e(TAG, "Error getting column index", e);
        } finally {
            if (cursor != null) {
                cursor.close();
            }
            // db.close(); // Không nên close ở đây
        }
        return historyList;
    }

    /**
     * Xóa một mục lịch sử dựa vào ID
     * @param id ID của mục cần xóa
     * @return Số lượng dòng bị ảnh hưởng (thường là 1 nếu thành công)
     */
    public int deleteHistoryItem(long id) {
        SQLiteDatabase db = this.getWritableDatabase();
        String selection = HistoryContract.HistoryEntry._ID + " = ?";
        String[] selectionArgs = { String.valueOf(id) };
        int deletedRows = db.delete(HistoryContract.HistoryEntry.TABLE_NAME, selection, selectionArgs);
        Log.d(TAG, "deleteHistoryItem: Deleted " + deletedRows + " rows with ID: " + id);
        // db.close();
        return deletedRows;
    }

    /**
     * Xóa tất cả lịch sử
     * @return Số lượng dòng bị xóa
     */
    public int deleteAllHistory() {
        SQLiteDatabase db = this.getWritableDatabase();
        int deletedRows = db.delete(HistoryContract.HistoryEntry.TABLE_NAME, null, null);
        Log.d(TAG, "deleteAllHistory: Deleted " + deletedRows + " rows.");
        // db.close();
        return deletedRows;
    }
}

