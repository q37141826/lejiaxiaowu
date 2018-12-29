package com.qixiu.lejia.core.me.profile;

import java.util.Observable;

/**
 * Created by Long on 2018/5/4
 */
public class ProfileEvent extends Observable {

    private static ProfileEvent instance = new ProfileEvent();

    private ProfileEvent() {
    }

    public static ProfileEvent getInstance() {
        return instance;
    }

   /* public void newNickname(String nickname) {
        setChanged();
        Pair<Integer, String> pair = new Pair<>(1, nickname);
        notifyObservers(pair);
    }

    public void newAvatar(String avatar) {
        setChanged();
        Pair<Integer, String> pair = new Pair<>(2, avatar);
    }*/

    public void profileUpdate() {
        setChanged();
        notifyObservers();
    }

}
