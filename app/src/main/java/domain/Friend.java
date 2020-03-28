package domain;

import androidx.annotation.Nullable;

public class Friend {

    public String name;
    public short type;
    public String id;

    public Friend(String id, String name, short type) {
        this.name = name;
        this.type = type;
        this.id = id;
    }

    @Override
    public String toString() {
        return "Friend{" +
                "name='" + name + '\'' +
                ", type=" + type +
                ", id='" + id + '\'' +
                '}';
    }

    @Override
    public boolean equals(@Nullable Object obj) {
        return obj.toString().equals(this.toString());
    }
}
