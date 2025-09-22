package x21l_5388_com.example.peezious;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

public class DBHelper extends SQLiteOpenHelper {

    // Database constants
    private static final String DATABASE_NAME = "PeeziousDB";
    private static final int DATABASE_VERSION = 5;

    // Table and column names
    private static final String TABLE_FAVORITES = "favorites";
    private static final String COLUMN_NAME = "name"; // Replacing itemID with name

    private static final String TABLE_ADDRESSES = "addresses";
    private static final String COLUMN_ADDRESS_NAME = "CommonTongue";


    private static final String TABLE_CART = "cart";
    private static final String COLUMN_ID = "id"; // New column for unique ID
    private static final String COLUMN_CART_NAME = "name";
    private static final String COLUMN_PRICE = "price";
    private static final String COLUMN_QUANTITY = "quantity";

    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // Create Favorites table
        String createFavoritesTable = "CREATE TABLE " + TABLE_FAVORITES + " (" +
                COLUMN_NAME + " TEXT PRIMARY KEY)";
        db.execSQL(createFavoritesTable);

        // Create Addresses table
        String createAddressesTable = "CREATE TABLE " + TABLE_ADDRESSES + " (" +
                COLUMN_ADDRESS_NAME + " TEXT)";
        db.execSQL(createAddressesTable);

        // Create Cart table
        String createCartTable = "CREATE TABLE " + TABLE_CART + " (" +
                COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + // Auto-incremented ID
                COLUMN_CART_NAME + " TEXT, " +
                COLUMN_PRICE + " REAL, " +
                COLUMN_QUANTITY + " INTEGER)";
        db.execSQL(createCartTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_FAVORITES);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_ADDRESSES);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CART);
        onCreate(db);
    }

    // Function to toggle a favorite item by name
    public boolean toggleFavorite(String name) {
        SQLiteDatabase db = this.getWritableDatabase();

        // Check if the item already exists
        String query = "SELECT * FROM " + TABLE_FAVORITES + " WHERE " + COLUMN_NAME + "=?";
        Cursor cursor = db.rawQuery(query, new String[]{name});

        if (cursor.moveToFirst()) {
            // Item exists, remove it
            db.delete(TABLE_FAVORITES, COLUMN_NAME + "=?", new String[]{name});
            cursor.close();
            return false; // Removed and returning false
        } else {
            // Item does not exist, add it
            ContentValues values = new ContentValues();
            values.put(COLUMN_NAME, name);
            db.insert(TABLE_FAVORITES, null, values);
            cursor.close();
            return true; // Added and returning true
        }
    }

    // Function to get all favorite names
    public List<String> getAllFavorites() {
        List<String> favorites = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        String query = "SELECT " + COLUMN_NAME + " FROM " + TABLE_FAVORITES;
        Cursor cursor = db.rawQuery(query, null);

        if (cursor.moveToFirst()) {
            do {
                favorites.add(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_NAME)));
            } while (cursor.moveToNext());
        }

        cursor.close();
        return favorites;
    }
    // Function to add an address
    public void addAddress(String commonTongue) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_ADDRESS_NAME, commonTongue);

        db.insert(TABLE_ADDRESSES, null, values); // Insert the address
    }

    // Function to remove an address
    public void removeAddress(String commonTongue) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_ADDRESSES, COLUMN_ADDRESS_NAME + "=?", new String[]{commonTongue}); // Delete matching address
    }

    // Function to get all addresses
    public List<String> getAllAddresses() {
        List<String> addresses = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        // Query to get all addresses
        String query = "SELECT " + COLUMN_ADDRESS_NAME + " FROM " + TABLE_ADDRESSES;
        Cursor cursor = db.rawQuery(query, null);

        // Iterate through the results and add to the list
        if (cursor.moveToFirst()) {
            do {
                addresses.add(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_ADDRESS_NAME)));
            } while (cursor.moveToNext());
        }
        cursor.close();
        return addresses; // Return the list of addresses
    }

    public void addToCart(String name, double price, int quantity) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_CART_NAME, name);
        values.put(COLUMN_PRICE, price);
        values.put(COLUMN_QUANTITY, quantity);

        db.insert(TABLE_CART, null, values); // Insert the item into the cart
    }

    // Function to delete an item from the cart by ID
    public void deleteFromCart(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_CART, COLUMN_ID + "=?", new String[]{String.valueOf(id)}); // Delete by ID
    }

    // Function to get all cart items
    public List<CartItem> getAllCartItems() {
        List<CartItem> cartItems = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        // Query to get all cart items
        String query = "SELECT " + COLUMN_ID + ", " + COLUMN_CART_NAME + ", " + COLUMN_PRICE + ", " + COLUMN_QUANTITY + " FROM " + TABLE_CART;
        Cursor cursor = db.rawQuery(query, null);

        // Iterate through the results and create CartItem objects
        if (cursor.moveToFirst()) {
            do {
                int id = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_ID));
                String name = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_CART_NAME));
                double price = cursor.getDouble(cursor.getColumnIndexOrThrow(COLUMN_PRICE));
                int quantity = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_QUANTITY));
                cartItems.add(new CartItem(id, name, price, quantity)); // Add to the list
            } while (cursor.moveToNext());
        }
        cursor.close();
        return cartItems; // Return the list of cart items
    }
    public void clearCart() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_CART, null, null); // Delete all rows from the cart table
    }

    // Function to clear all saved addresses
    public void clearAddresses() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_ADDRESSES, null, null); // Delete all rows from the addresses table
    }

    // Function to clear all favorites
    public void clearFavorites() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_FAVORITES, null, null); // Delete all rows from the favorites table
    }

}
