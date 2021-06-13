package com.example.dolap;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "mydatabase";

    // --------------------------------------------------------

    private String TableNameDrawer = "drawer";
    private String DrawerID = "id";
    private String DrawerName = "name";

    public String CreateQueryDrawer = "CREATE TABLE " + TableNameDrawer + " ( " +
            DrawerID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            DrawerName + " TEXT )";

    // --------------------------------------------------------

    private String TableNameClothes = "clothes";
    private String ClothesID = "id";
    private String ClothesType = "Type";
    private String ClothesColor = "color";
    private String ClothesPattern = "pattern";
    private String ClothesBuyDate = "buydate";
    private String ClothesPrice = "price";
    private String ClothesPhoto = "photo";
    private String ClothesDrawerID = "drawerid";

    public String CreateQueryClothes = "CREATE TABLE " + TableNameClothes + " ( " +
            ClothesID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            ClothesType + " TEXT, " +
            ClothesColor + " TEXT, " +
            ClothesPattern + " TEXT, " +
            ClothesBuyDate + " TEXT, " +
            ClothesPrice + " TEXT, " +
            ClothesPhoto + " BLOB, " +
            ClothesDrawerID + " INTEGER )";

    // ---------------------------------------------------------

    private String TableNameCombines = "combines";
    private String CombinesID = "id";
    private String CombineHead = "head";
    private String CombineFace = "face";
    private String CombineTop = "top";
    private String CombineBottom = "bottom";
    private String CombineFoot = "foot";

    public String CreateQueryCombines = "CREATE TABLE " + TableNameCombines + " ( " +
            CombinesID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            CombineHead + " INTEGER, " +
            CombineFace + " INTEGER, " +
            CombineTop + " INTEGER, " +
            CombineBottom + " INTEGER, " +
            CombineFoot + " INTEGER )";

    // -----------------------------------------------------------

    private String TableNameActivity = "activities";
    private String ActivityID = "id";
    private String ActivityName = "name";
    private String ActivityType = "type";
    private String ActivityDate = "date";
    private String ActivityLocation = "location";
    private String ActivityClothes = "clothes";

    public String CreateQueryActivities = "CREATE TABLE " + TableNameActivity + " ( " +
            ActivityID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            ActivityName + " TEXT, " +
            ActivityType + " TEXT, " +
            ActivityDate + " TEXT, " +
            ActivityLocation + " TEXT, " +
            ActivityClothes + " TEXT )";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CreateQueryDrawer);
        db.execSQL(CreateQueryClothes);
        db.execSQL(CreateQueryCombines);
        db.execSQL(CreateQueryActivities);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TableNameClothes);
        db.execSQL("DROP TABLE IF EXISTS " + TableNameDrawer);
        db.execSQL("DROP TABLE IF EXISTS " + TableNameCombines);
        db.execSQL("DROP TABLE IF EXISTS " + TableNameActivity);

        onCreate(db);
    }

    public void addDrawer(Drawer drawer){
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(DrawerName, drawer.getName());

        db.insert(TableNameDrawer,null,values);
        db.close();
    }

    public List<Drawer> getAllDrawers(){
        String[] columns = {
                DrawerID,
                DrawerName
        };

        List<Drawer> drawerList = new ArrayList<Drawer>();

        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TableNameDrawer,
                columns,
                null,
                null,
                null,
                null,
                null);

        if (cursor.moveToFirst()){
            do {
                Drawer drawer = new Drawer();
                drawer.setId(Integer.parseInt(cursor.getString(cursor.getColumnIndex(DrawerID))));
                drawer.setName(cursor.getString(cursor.getColumnIndex(DrawerName)));

                drawerList.add(drawer);
            }while (cursor.moveToNext());
        }
        cursor.close();

        return drawerList;
    }

    public void deletedrawer(int drawerid){
        SQLiteDatabase db = this.getReadableDatabase();

        List<Clothes> clothes = new ArrayList<>();
        clothes = getClothes(drawerid);

        List<Combine> combineList = new ArrayList<>();
        combineList = getAllCombines();

        for (Clothes cloth : clothes){
            for (Combine onecombine:combineList) {
                if (cloth.getId() == onecombine.getHeadid() || cloth.getId() == onecombine.getFaceid() || cloth.getId() == onecombine.getTopid() ||
                        cloth.getId() == onecombine.getBottomid() || cloth.getId() == onecombine.getFootid())
                    deleteCombines(onecombine.getCombineid());
            }
        }


        db.delete(TableNameClothes, ClothesDrawerID + "=" + drawerid,null);
        db.delete(TableNameDrawer, DrawerID + "=" + drawerid, null);
    }

    public void addClothes(Clothes clothes){
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(ClothesType, clothes.getType());
        values.put(ClothesColor, clothes.getColor());
        values.put(ClothesPattern, clothes.getPattern());
        values.put(ClothesBuyDate, clothes.getBuyDate());
        values.put(ClothesPrice, clothes.getPrice());
        values.put(ClothesDrawerID, clothes.getDrawerid());
        values.put(ClothesPhoto, clothes.getPhoto());

        db.insert(TableNameClothes,null,values);
        db.close();
    }

    public void deleteClothes(int clothesid){
        SQLiteDatabase db = this.getReadableDatabase();
        List<Combine> combineList = new ArrayList<>();
        combineList = getAllCombines();

        for (Combine onecombine:combineList) {
            if (clothesid == onecombine.getHeadid() || clothesid == onecombine.getFaceid() || clothesid == onecombine.getTopid() ||
                    clothesid == onecombine.getBottomid() || clothesid == onecombine.getFootid())
                deleteCombines(onecombine.getCombineid());
        }
        db.delete(TableNameClothes, ClothesID + "=" + clothesid, null);
    }

    public List<Clothes> getClothes(int drawer_id){
        String id = String.valueOf(drawer_id);
        String[] columns = {
                ClothesID,
                ClothesType,
                ClothesColor,
                ClothesPattern,
                ClothesBuyDate,
                ClothesPrice,
                ClothesPhoto,
                ClothesDrawerID
        };

        List<Clothes> clothesList = new ArrayList<Clothes>();

        SQLiteDatabase db = this.getReadableDatabase();

        String selection = ClothesDrawerID + " = ?";
        String[] selectionArgs = {id};

        Cursor cursor = db.query(TableNameClothes,
                columns,
                selection,
                selectionArgs,
                null,
                null,
                null);

        if (cursor.moveToFirst()){
            do {
                Clothes clothes = new Clothes();
                clothes.setId(Integer.parseInt(cursor.getString(cursor.getColumnIndex(ClothesID))));
                clothes.setType(cursor.getString(cursor.getColumnIndex(ClothesType)));
                clothes.setColor(cursor.getString(cursor.getColumnIndex(ClothesColor)));
                clothes.setPattern(cursor.getString(cursor.getColumnIndex(ClothesPattern)));
                clothes.setBuyDate(cursor.getString(cursor.getColumnIndex(ClothesBuyDate)));
                clothes.setPrice(cursor.getString(cursor.getColumnIndex(ClothesPrice)));
                clothes.setPhoto(cursor.getBlob(cursor.getColumnIndex(ClothesPhoto)));
                clothes.setDrawerid(Integer.parseInt(cursor.getString(cursor.getColumnIndex(ClothesDrawerID))));

                clothesList.add(clothes);
            }while (cursor.moveToNext());
        }
        cursor.close();

        return clothesList;
    }

    public List<Clothes> getAllClothes(){
        String[] columns = {
                ClothesID,
                ClothesType,
                ClothesColor,
                ClothesPattern,
                ClothesBuyDate,
                ClothesPrice,
                ClothesPhoto,
                ClothesDrawerID
        };

        List<Clothes> clothesList = new ArrayList<Clothes>();

        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TableNameClothes,
                columns,
                null,
                null,
                null,
                null,
                null);

        if (cursor.moveToFirst()){
            do {
                Clothes clothes = new Clothes();
                clothes.setId(Integer.parseInt(cursor.getString(cursor.getColumnIndex(ClothesID))));
                clothes.setType(cursor.getString(cursor.getColumnIndex(ClothesType)));
                clothes.setColor(cursor.getString(cursor.getColumnIndex(ClothesColor)));
                clothes.setPattern(cursor.getString(cursor.getColumnIndex(ClothesPattern)));
                clothes.setBuyDate(cursor.getString(cursor.getColumnIndex(ClothesBuyDate)));
                clothes.setPrice(cursor.getString(cursor.getColumnIndex(ClothesPrice)));
                clothes.setPhoto(cursor.getBlob(cursor.getColumnIndex(ClothesPhoto)));
                clothes.setDrawerid(Integer.parseInt(cursor.getString(cursor.getColumnIndex(ClothesDrawerID))));

                clothesList.add(clothes);
            }while (cursor.moveToNext());
        }
        cursor.close();

        return clothesList;
    }

    public List<Clothes> getOneClothes(int cloth_id){
        String id = String.valueOf(cloth_id);
        String[] columns = {
                ClothesID,
                ClothesType,
                ClothesColor,
                ClothesPattern,
                ClothesBuyDate,
                ClothesPrice,
                ClothesPhoto,
                ClothesDrawerID
        };

        List<Clothes> clothesList = new ArrayList<Clothes>();

        SQLiteDatabase db = this.getReadableDatabase();

        String selection = ClothesID + " = ?";
        String[] selectionArgs = {id};

        Cursor cursor = db.query(TableNameClothes,
                columns,
                selection,
                selectionArgs,
                null,
                null,
                null);

        if (cursor.moveToFirst()){
            do {
                Clothes clothes = new Clothes();
                clothes.setId(Integer.parseInt(cursor.getString(cursor.getColumnIndex(ClothesID))));
                clothes.setType(cursor.getString(cursor.getColumnIndex(ClothesType)));
                clothes.setColor(cursor.getString(cursor.getColumnIndex(ClothesColor)));
                clothes.setPattern(cursor.getString(cursor.getColumnIndex(ClothesPattern)));
                clothes.setBuyDate(cursor.getString(cursor.getColumnIndex(ClothesBuyDate)));
                clothes.setPrice(cursor.getString(cursor.getColumnIndex(ClothesPrice)));
                clothes.setPhoto(cursor.getBlob(cursor.getColumnIndex(ClothesPhoto)));
                clothes.setDrawerid(Integer.parseInt(cursor.getString(cursor.getColumnIndex(ClothesDrawerID))));

                clothesList.add(clothes);
            }while (cursor.moveToNext());
        }
        cursor.close();

        return clothesList;
    }

    public void updateClothes(int id, String type, String color, String pattern, String date , String price, byte[] photo, int drawer_id) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(ClothesID, id);
        values.put(ClothesType, type);
        values.put(ClothesColor, color);
        values.put(ClothesPattern, pattern);
        values.put(ClothesBuyDate, date);
        values.put(ClothesPrice, price);
        values.put(ClothesPhoto, photo);
        values.put(ClothesDrawerID, drawer_id);

        db.update(TableNameClothes, values, ClothesID + " = ?",
                new String[] { String.valueOf(id) });
    }

    public void addCombines(Combine combine){
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(CombineHead, combine.getHeadid());
        values.put(CombineFace, combine.getFaceid());
        values.put(CombineTop, combine.getTopid());
        values.put(CombineBottom, combine.getBottomid());
        values.put(CombineFoot, combine.getFootid());

        db.insert(TableNameCombines,null,values);
        db.close();
    }

    public List<Combine> getAllCombines(){
        String[] columns = {
                CombinesID,
                CombineHead,
                CombineFace,
                CombineTop,
                CombineBottom,
                CombineFoot
        };

        List<Combine> combineList = new ArrayList<Combine>();

        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TableNameCombines,
                columns,
                null,
                null,
                null,
                null,
                null);

        if (cursor.moveToFirst()){
            do {
                Combine combine = new Combine();
                combine.setCombineid(Integer.parseInt(cursor.getString(cursor.getColumnIndex(CombinesID))));
                combine.setHeadid(Integer.parseInt(cursor.getString(cursor.getColumnIndex(CombineHead))));
                combine.setFaceid(Integer.parseInt(cursor.getString(cursor.getColumnIndex(CombineFace))));
                combine.setTopid(Integer.parseInt(cursor.getString(cursor.getColumnIndex(CombineTop))));
                combine.setBottomid(Integer.parseInt(cursor.getString(cursor.getColumnIndex(CombineBottom))));
                combine.setFootid(Integer.parseInt(cursor.getString(cursor.getColumnIndex(CombineFoot))));

                combineList.add(combine);
            }while (cursor.moveToNext());
        }
        cursor.close();

        return combineList;
    }

    public void deleteCombines(int combine_id){
        SQLiteDatabase db = this.getReadableDatabase();
        db.delete(TableNameCombines, CombinesID + "=" + combine_id, null);
    }

    public void addActivity(Activity activity){
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(ActivityName, activity.getName());
        values.put(ActivityType, activity.getType());
        values.put(ActivityDate, activity.getDate());
        values.put(ActivityLocation, activity.getLocation());
        values.put(ActivityClothes, activity.getClothes());

        db.insert(TableNameActivity,null,values);
        db.close();
    }

    public List<Activity> getAllActivities(){
        String[] columns = {
                ActivityID,
                ActivityName,
                ActivityType,
                ActivityDate,
                ActivityLocation,
                ActivityClothes
        };

        List<Activity> activityList = new ArrayList<Activity>();

        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TableNameActivity,
                columns,
                null,
                null,
                null,
                null,
                null);

        if (cursor.moveToFirst()){
            do {
                Activity activity = new Activity();
                activity.setActivity_id(Integer.parseInt(cursor.getString(cursor.getColumnIndex(ActivityID))));
                activity.setName(cursor.getString(cursor.getColumnIndex(ActivityName)));
                activity.setType(cursor.getString(cursor.getColumnIndex(ActivityType)));
                activity.setDate(cursor.getString(cursor.getColumnIndex(ActivityDate)));
                activity.setLocation(cursor.getString(cursor.getColumnIndex(ActivityLocation)));
                activity.setClothes(cursor.getString(cursor.getColumnIndex(ActivityClothes)));

                activityList.add(activity);
            }while (cursor.moveToNext());
        }
        cursor.close();

        return activityList;
    }

    public void deleteActivity(int activity_id){
        SQLiteDatabase db = this.getReadableDatabase();
        db.delete(TableNameActivity, ActivityID + "=" + activity_id, null);
    }

    public List<Activity> getOneActivities(int activity_id){
        String id = String.valueOf(activity_id);
        String[] columns = {
                ActivityID,
                ActivityName,
                ActivityType,
                ActivityDate,
                ActivityLocation,
                ActivityClothes
        };

        List<Activity> activityList = new ArrayList<Activity>();

        String selection = ActivityID + " = ?";
        String[] selectionArgs = {id};

        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TableNameActivity,
                columns,
                selection,
                selectionArgs,
                null,
                null,
                null);

        if (cursor.moveToFirst()){
            do {
                Activity activity = new Activity();
                activity.setActivity_id(Integer.parseInt(cursor.getString(cursor.getColumnIndex(ActivityID))));
                activity.setName(cursor.getString(cursor.getColumnIndex(ActivityName)));
                activity.setType(cursor.getString(cursor.getColumnIndex(ActivityType)));
                activity.setDate(cursor.getString(cursor.getColumnIndex(ActivityDate)));
                activity.setLocation(cursor.getString(cursor.getColumnIndex(ActivityLocation)));
                activity.setClothes(cursor.getString(cursor.getColumnIndex(ActivityClothes)));

                activityList.add(activity);
            }while (cursor.moveToNext());
        }
        cursor.close();

        return activityList;
    }

    public void updateActivity(int id, String name, String type, String date, String location, String checks) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(ActivityID, id);
        values.put(ActivityName, name);
        values.put(ActivityType, type);
        values.put(ActivityDate, date);
        values.put(ActivityLocation, location);
        values.put(ActivityClothes, checks);

        db.update(TableNameActivity, values, ActivityID + " = ?",
                new String[] { String.valueOf(id) });
    }

    public List<Combine> getOneCombine(int combine_id){
        String id = String.valueOf(combine_id);
        String[] columns = {
                CombinesID,
                CombineHead,
                CombineFace,
                CombineTop,
                CombineBottom,
                CombineFoot
        };

        List<Combine> combineList = new ArrayList<Combine>();

        SQLiteDatabase db = this.getReadableDatabase();

        String selection = CombinesID + " = ?";
        String[] selectionArgs = {id};

        Cursor cursor = db.query(TableNameCombines,
                columns,
                selection,
                selectionArgs,
                null,
                null,
                null);

        if (cursor.moveToFirst()){
            do {
                Combine combine = new Combine();
                combine.setCombineid(Integer.parseInt(cursor.getString(cursor.getColumnIndex(CombinesID))));
                combine.setHeadid(Integer.parseInt(cursor.getString(cursor.getColumnIndex(CombineHead))));
                combine.setFaceid(Integer.parseInt(cursor.getString(cursor.getColumnIndex(CombineFace))));
                combine.setTopid(Integer.parseInt(cursor.getString(cursor.getColumnIndex(CombineTop))));
                combine.setBottomid(Integer.parseInt(cursor.getString(cursor.getColumnIndex(CombineBottom))));
                combine.setFootid(Integer.parseInt(cursor.getString(cursor.getColumnIndex(CombineFoot))));

                combineList.add(combine);
            }while (cursor.moveToNext());
        }
        cursor.close();

        return combineList;
    }

    public byte[] getClothPhoto(int cloth_id){
        String id = String.valueOf(cloth_id);
        String[] columns = {
                ClothesID,
                ClothesPhoto
        };

        SQLiteDatabase db = this.getReadableDatabase();

        String selection = ClothesID + " = ?";
        String[] selectionArgs = {id};

        Cursor cursor = db.query(TableNameClothes,
                columns,
                selection,
                selectionArgs,
                null,
                null,
                null);

        byte[] imageArray = null;

        if (cursor.moveToFirst()){
            do {
                imageArray = cursor.getBlob(cursor.getColumnIndex(ClothesPhoto));
            }while (cursor.moveToNext());
        }
        cursor.close();

        return imageArray;

    }

}
