package repository;

public class SQLScripts {
    public static String initDbScript() {
    return "create table user(" +
                "id integer primary key autoincrement," +
                "name text not null," +
                "password text not null" +
                ");";
    }

    public static String initFriendshipScript(){
        return "create table friendship( " +
                "id_friend1 integer, " +
                "id_friend2 integer, " +
                "type short, " +
                "foreign key(id_friend1) references user(id)," +
                "foreign key(id_friend2) references user(id));";
    }

    public static String insertUserScript(String name, String password) {
        String _name = "\"" + name + "\"";
        String _password = "\"" + password + "\"";

        return "insert into user" +
                "(name, password)" +
                "values" +
                "(" + _name + "," + _password +
                ");";
    }

    public static String getUserScript(String name, String password) {
        String _name = "\"" + name + "\"";
        String _password = "\"" + password + "\"";

        return "select * from user" +
                " where name = " + _name +
                " and password = " + _password +
                ";";
    }

    public static String getAllUsersScript() {
        return "select * from user;";
    }

    public static String getAllUsersScript(int limit) {
        return "select * from user" +
                " limit " + limit +
                ";";
    }

    public static String getFriendshipScript(String userID){
        return "select * from friendship f where f.id_friend1 = " + userID + ";";
    }

    public static String insertFriendsScript(String user1, String user2, int type){
        return "insert into friendship(id_friend1, id_friend2, type)" +
                "values(" +
                user1 +
                ", " + user2 +
                ", " + type +
                ");";
    }

    public static String changeFriendshipScript(String user1, String user2, int type){
        return "update friendship set " +
                "type = " + type +
                " where id_friend1 = " + user1 +
                " and id_friend2 = " + user2;
    }
}
