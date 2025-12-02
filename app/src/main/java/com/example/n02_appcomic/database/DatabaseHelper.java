package com.example.n02_appcomic.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;


import com.example.n02_appcomic.model.Item;
import com.example.n02_appcomic.model.User;

import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DB_NAME = "ComicApp.db";
    private static final int DB_VERSION = 2;

    public DatabaseHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE users (id INTEGER PRIMARY KEY AUTOINCREMENT, name TEXT, email TEXT UNIQUE, password TEXT)");

        db.execSQL("CREATE TABLE favorites (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "user_id INTEGER, " +
                "manga_id TEXT, " +
                "slug TEXT, " +
                "title TEXT, " +
                "thumbnail TEXT, " +
                "author TEXT, " +
                "last_chapter TEXT, " +
                "UNIQUE(user_id, manga_id))");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS users");
        db.execSQL("DROP TABLE IF EXISTS favorites");
        onCreate(db);
    }

    // Đăng ký
    public boolean registerUser(String name, String email, String password) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("name", name);
        values.put("email", email);
        values.put("password", password);
        long result = db.insert("users", null, values);
        return result != -1;
    }

    // Đăng nhập
    public boolean loginUser(String email, String password) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM users WHERE email=? AND password=?", new String[]{email, password});
        boolean result = cursor.getCount() > 0;
        cursor.close();
        return result;
    }

    public int getUserIdByEmail(String email) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT id FROM users WHERE email = ?", new String[]{email});
        int userId = -1;
        if (cursor.moveToFirst()) {
            userId = cursor.getInt(0);
        }
        cursor.close();
        return userId;
    }

    public User getUserById(int userId) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM users WHERE id = ?", new String[]{String.valueOf(userId)});

        if (cursor != null && cursor.moveToFirst()) {
            String name = cursor.getString(cursor.getColumnIndexOrThrow("name"));
            String email = cursor.getString(cursor.getColumnIndexOrThrow("email"));
            cursor.close();
            return new User(userId, name, email);
        }

        if (cursor != null) cursor.close();
        return null;
    }

    /// ///
    // Thêm truyện vào yêu thích
    public boolean addFavorite(int userId, String mangaId, String slug, String title, String thumbnail, String author, String lastChapter) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("user_id", userId);
        values.put("manga_id", mangaId);
        values.put("slug", slug); // lưu slug
        values.put("title", title);
        values.put("thumbnail", thumbnail);
        values.put("author", author);
        values.put("last_chapter", lastChapter);
        long result = db.insertWithOnConflict("favorites", null, values, SQLiteDatabase.CONFLICT_IGNORE);
        return result != -1;
    }



    // Xóa truyện khỏi yêu thích
    public boolean removeFavorite(int userId, String mangaId) {
        SQLiteDatabase db = this.getWritableDatabase();
        int rows = db.delete("favorites", "user_id=? AND manga_id=?", new String[]{String.valueOf(userId), mangaId});
        return rows > 0;
    }

    public boolean removeFavoriteBySlug(int userId, String slug) {
        SQLiteDatabase db = this.getWritableDatabase();
        int rows = db.delete("favorites", "user_id=? AND slug=?", new String[]{String.valueOf(userId), slug});
        db.close();
        return rows > 0;
    }

    // Kiểm tra đã yêu thích chưa
    public boolean isFavorite(String userId, String mangaId) {
        if (mangaId == null || userId == null) {
            return false; // Không hợp lệ => mặc định không yêu thích
        }

        SQLiteDatabase db = null;
        Cursor cursor = null;
        try {
            db = this.getReadableDatabase();

            // Kiểm tra nếu bảng "favorites" có tồn tại
            Cursor tableCursor = db.rawQuery(
                    "SELECT name FROM sqlite_master WHERE type='table' AND name='favorites'", null
            );
            boolean tableExists = tableCursor.moveToFirst();
            tableCursor.close();

            if (!tableExists) {
                return false; // Bảng chưa tạo => trả về false
            }

            // Truy vấn xem có tồn tại bản ghi hay không
            cursor = db.query("favorites",
                    new String[]{"manga_id"},
                    "user_id=? AND manga_id=?",
                    new String[]{userId, mangaId},
                    null, null, null);

            return (cursor != null && cursor.moveToFirst());
        } catch (Exception e) {
            // Bắt mọi lỗi để tránh crash
            e.printStackTrace();
            return false;
        } finally {
            if (cursor != null) cursor.close();
            if (db != null) db.close();
        }
    }


    // Lấy danh sách yêu thích của user
    public List<Item> getFavorites(int userId) {
        List<Item> list = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.rawQuery(
                "SELECT manga_id, slug, title, thumbnail, author, last_chapter " +
                        "FROM favorites WHERE user_id=?",
                new String[]{String.valueOf(userId)}
        );

        if (cursor.moveToFirst()) {
            do {
                String mangaId = cursor.getString(cursor.getColumnIndexOrThrow("manga_id"));
                String slug = cursor.getString(cursor.getColumnIndexOrThrow("slug"));
                String title = cursor.getString(cursor.getColumnIndexOrThrow("title"));
                String thumbnail = cursor.getString(cursor.getColumnIndexOrThrow("thumbnail"));
                String author = cursor.getString(cursor.getColumnIndexOrThrow("author"));
                String lastChapter = cursor.getString(cursor.getColumnIndexOrThrow("last_chapter"));

                // tạo Item từ DB
                Item item = new Item(mangaId, title, thumbnail, author, lastChapter);
                item.setSlug(slug); // set thêm slug
                list.add(item);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return list;
    }

    public void logFavorites() {
        SQLiteDatabase db = null;
        Cursor cursor = null;
        try {
            db = this.getReadableDatabase();

            cursor = db.rawQuery("SELECT * FROM favorites", null);

            if (cursor.moveToFirst()) {
                do {
                    int userIdIndex = cursor.getColumnIndex("user_id");
                    int mangaIdIndex = cursor.getColumnIndex("manga_id");

                    String userId = (userIdIndex != -1) ? cursor.getString(userIdIndex) : "null";
                    String mangaId = (mangaIdIndex != -1) ? cursor.getString(mangaIdIndex) : "null";

                    Log.d("DB_DEBUG", "User ID: " + userId + " | Manga ID: " + mangaId);
                } while (cursor.moveToNext());
            } else {
                Log.d("DB_DEBUG", "Bảng favorites rỗng.");
            }
        } catch (Exception e) {
            Log.e("DB_DEBUG", "Lỗi khi đọc bảng favorites: " + e.getMessage());
        } finally {
            if (cursor != null) cursor.close();
            if (db != null) db.close();
        }
    }

    public List<String> getAllFavoriteSlugs(int userId) {
        List<String> slugs = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT slug FROM favorites WHERE user_id = ?", new String[]{String.valueOf(userId)});
        if (cursor.moveToFirst()) {
            do {
                slugs.add(cursor.getString(0));
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return slugs;
    }

}

