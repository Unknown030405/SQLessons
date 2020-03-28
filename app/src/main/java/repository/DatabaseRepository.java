package repository;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import domain.Friend;
import domain.User;

import java.util.ArrayList;
import java.util.Arrays;

public class DatabaseRepository {
    private final String TAG = "DatabaseRepository";
    DatabaseHelper databaseHelper;

    public DatabaseRepository(Context context) {

        initDb(context);
    }

    private void initDb(Context context) {
        databaseHelper = new DatabaseHelper(context, "UserDb", null, 1);

    }

    public boolean insertUser(String name, String password) {
        SQLiteDatabase db = databaseHelper.getWritableDatabase();
        if (getUser(name, password) != null) {
            return false;
        } else {
            db.execSQL(SQLScripts.insertUserScript(name, password));
            return true;
        }
    }

    public User getUser(String name, String password) {
        SQLiteDatabase db = databaseHelper.getWritableDatabase();
        Cursor userCursor = db.rawQuery(SQLScripts.getUserScript(name, password), null);
        // Cтавим позицию курсора на первую строку выборки
        // Eсли в выборке нет строк, вернется false
        if (userCursor.moveToFirst()) {
            // определяем номера столбцов по имени в выборке
            int idColIndex = userCursor.getColumnIndex("id");
            int nameColIndex = userCursor.getColumnIndex("name");
            int passwordColIndex = userCursor.getColumnIndex("password");

            do {
                // получаем значения по номерам столбцов
                User user = new User(userCursor.getString(idColIndex),
                        userCursor.getString(nameColIndex),
                        userCursor.getString(passwordColIndex));
                userCursor.close();
                return user;
                // переход на следующую строку
                // а если следующей нет (текущая - последняя), то false - выходим из цикла
            } while (userCursor.moveToNext());
        } else {
            userCursor.close();
            return null;
        }
    }

    public ArrayList<User> getUsers(int limit) {
        SQLiteDatabase db = databaseHelper.getWritableDatabase();
        Cursor userCursor = db.rawQuery(SQLScripts.getAllUsersScript(limit), null);
        // Cтавим позицию курсора на первую строку выборки
        // Eсли в выборке нет строк, вернется false
        if (userCursor.moveToFirst()) {
            // определяем номера столбцов по имени в выборке
            int idColIndex = userCursor.getColumnIndex("id");
            int nameColIndex = userCursor.getColumnIndex("name");
            int passwordColIndex = userCursor.getColumnIndex("password");

            ArrayList<User> userList = new ArrayList();
            do {
                // получаем значения по номерам столбцов
                User user = new User(userCursor.getString(idColIndex),
                        userCursor.getString(nameColIndex),
                        userCursor.getString(passwordColIndex));
                userList.add(user);
                // переход на следующую строку
                // а если следующей нет (текущая - последняя), то false - выходим из цикла
            } while (userCursor.moveToNext());
            userCursor.close();
            return userList;
        } else {
            userCursor.close();
            return null;
        }
    }

    public void insertFriendsRequest(String user1, String user2){
        try {
            databaseHelper.getWritableDatabase().execSQL(SQLScripts.insertFriendsScript(user1, user2, 0));
            databaseHelper.getWritableDatabase().execSQL(SQLScripts.insertFriendsScript(user2, user1, 3));
        }catch (Exception e){
            try{
                databaseHelper.getWritableDatabase().execSQL(SQLScripts.changeFriendshipScript(user1, user2, 0));
                databaseHelper.getWritableDatabase().execSQL(SQLScripts.changeFriendshipScript(user2, user1, 3));
            }catch (Exception e1){
                e1.printStackTrace();
            }
            e.printStackTrace();
        }
    }

    public void insertFriendsAccept(String user1, String user2){
        databaseHelper.getWritableDatabase().execSQL(SQLScripts.changeFriendshipScript(user1,user2, 1));
        databaseHelper.getWritableDatabase().execSQL(SQLScripts.changeFriendshipScript(user2,user1, 1));
    }

    public void deleteFriend(String user1, String user2){
        databaseHelper.getWritableDatabase().execSQL(SQLScripts.changeFriendshipScript(user1,user2, 2));
        databaseHelper.getWritableDatabase().execSQL(SQLScripts.changeFriendshipScript(user2,user1, 2));
    }

    public ArrayList<Friend> getFriendlist(User user1){
        ArrayList<Friend> friends = new ArrayList<>();
        ArrayList<User> users = getUsers(999999999);
        Cursor cursor = databaseHelper.getWritableDatabase().rawQuery(SQLScripts.getFriendshipScript(user1.id), null);
        if(cursor.moveToFirst()){
            int firstid = cursor.getColumnIndex("id_friend1");
            int secondid = cursor.getColumnIndex("id_friend2");
            int type = cursor.getColumnIndex("type");

            do{
                Log.d("Friends check", cursor.getInt(firstid) + " " + cursor.getInt(secondid) + " " + cursor.getInt(type));
            }while(cursor.moveToNext());

            cursor.moveToFirst();

            do{
                if(cursor.getInt(type) == 0){
                    Log.d("Friends check", "" + cursor.getInt(type));
                    int userid = cursor.getInt(firstid) == Integer.valueOf(user1.id) ? cursor.getInt(secondid) : cursor.getInt(firstid);
                    Log.d("Friends check", friends.toString() + " rjytw");
                    Log.d("Friends check", users.toString() + " phuque");
                    friends.add(new Friend(users.get(userid-1).id, users.get(userid-1).name, cursor.getShort(type)));
                }
            }while (cursor.moveToNext());

            cursor.moveToFirst();

            do{
                if(cursor.getInt(type) == 1){
                    int userid = cursor.getInt(firstid) == Integer.valueOf(user1.id) ? cursor.getInt(secondid) : cursor.getInt(firstid);
                    friends.add(new Friend(users.get(userid-1).id, users.get(userid-1).name, cursor.getShort(type)));
                }
            }while (cursor.moveToNext());

            cursor.moveToFirst();

            do{
                if(cursor.getInt(type) == 3){
                    int userid = cursor.getInt(firstid) == Integer.valueOf(user1.id) ? cursor.getInt(secondid) : cursor.getInt(firstid);
                    friends.add(new Friend(users.get(userid-1).id, users.get(userid-1).name, cursor.getShort(type)));
                }
            }while (cursor.moveToNext());
        }

        for(User user : users){
            if(user.id.equals(user1.id)){
                continue;
            }
            if(friends.contains(new Friend(user.id, user.name, (short) 0)) || friends.contains(new Friend(user.id, user.name, (short) 3)) || friends.contains(new Friend(user.id, user.name, (short) 1)) || user.id == user1.id){
                continue;
            }else{
                friends.add(new Friend(user.id, user.name, (short) 2));
            }
        }

        cursor.close();
        Log.d("Friends check", friends.toString());
        return friends;
    }
}