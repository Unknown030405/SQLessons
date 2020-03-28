package interactor;

import android.content.Context;

import domain.*;
import repository.DatabaseRepository;

import java.util.ArrayList;

public class UserInteractor {
    DatabaseRepository repository;

    public UserInteractor(Context context) {
        repository = new DatabaseRepository(context);
    }

    public boolean insertUser(String name, String password) {
        return repository.insertUser(name, password);
    }

    public User getUser(String name, String password) {
        return repository.getUser(name, password);
    }

    public ArrayList<User> getUsers(int limit) {
        return repository.getUsers(limit);
    }

    public ArrayList<Friend> GetFriends(User user){
        return repository.getFriendlist(user);
    }

    public void sendRequest(String userId, String friendId){
        repository.insertFriendsRequest(userId, friendId);
    }

    public void deleteFromFriends(String userId, String friendId){
        repository.deleteFriend(userId, friendId);
    }

    public void acceptRequest(String userId, String friendId){
        repository.insertFriendsAccept(userId, friendId);
    }
}