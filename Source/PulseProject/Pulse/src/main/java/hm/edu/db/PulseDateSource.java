package hm.edu.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import java.util.Date;

/**
 * Created by toffermann on 15.11.13.
 */
public class PulseDateSource {

    // Database fields
    private SQLiteDatabase  database;
    private DbOpenHelper    dbHelper;
    private String[]        allColumns = { DbOpenHelper.COL_ADDED_ON,
                                           DbOpenHelper.COL_PULSE };

    public PulseDateSource(Context context) {
        dbHelper = new DbOpenHelper(context);
    }

    public void open() throws SQLException {
        database = dbHelper.getWritableDatabase();
    }

    public void close() {
        dbHelper.close();
    }

    public PulseModel createPulse(int pulse) {
        ContentValues values = new ContentValues();
        values.put(DbOpenHelper.COL_PULSE, pulse);
        long insertId = database.insert(DbOpenHelper.COL_PULSE, null,
                values);

        Cursor cursor = database.query(DbOpenHelper.COL_PULSE,
                allColumns, DbOpenHelper.COL_ID + " = " + insertId, null,
                null, null, null);
        cursor.moveToFirst();

        PulseModel pulseM = cursorToPulseModel(cursor);
        cursor.close();
        return pulseM;
    }

    private PulseModel cursorToPulseModel(Cursor cursor) {
        PulseModel pulse = new PulseModel();

        pulse.setId(cursor.getLong(0));

        long millis = cursor.getLong(cursor.getColumnIndexOrThrow("added_on"));
        Date addedOn = new Date(millis);
        pulse.setDateTime(addedOn);

        pulse.setPulse(cursor.getInt(2));
        return pulse;
    }


}
