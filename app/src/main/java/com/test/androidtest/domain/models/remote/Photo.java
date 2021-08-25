package com.test.androidtest.domain.models.remote;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Photo
{
    /**This is an example of retrieved data by the service
     *
     *     "id": "LBI7cgq3pbM",
     *     "likes": 12,
     *     "description": "A man drinking a coffee.",
     *     "user": {
     *       "username": "poorkane",
     *       "name": "Gilbert Kane",
     *       "bio": "XO",
     *       "total_likes": 5,
     *       "total_photos": 74,
     *       "profile_image": {
     *         "small": "https://images.unsplash.com/face-springmorning.jpg?q=80&fm=jpg&crop=faces&fit=crop&h=32&w=32",
     *         "medium": "https://images.unsplash.com/face-springmorning.jpg?q=80&fm=jpg&crop=faces&fit=crop&h=64&w=64",
     *         "large": "https://images.unsplash.com/face-springmorning.jpg?q=80&fm=jpg&crop=faces&fit=crop&h=128&w=128"
     *       }
     *     },
     *     "urls": {
     *       "raw": "https://images.unsplash.com/face-springmorning.jpg",
     *       "full": "https://images.unsplash.com/face-springmorning.jpg?q=75&fm=jpg",
     *       "regular": "https://images.unsplash.com/face-springmorning.jpg?q=75&fm=jpg&w=1080&fit=max",
     *       "small": "https://images.unsplash.com/face-springmorning.jpg?q=75&fm=jpg&w=400&fit=max",
     *       "thumb": "https://images.unsplash.com/face-springmorning.jpg?q=75&fm=jpg&w=200&fit=max"
     *     },
     * */

    private int databaseId;
    private int likes;
    @Expose
    @SerializedName("liked_by_user")
    private boolean likedByUser;
    private String description;
    private User user;
    private Url urls;
    private boolean saved;

    public int getId() {
        return databaseId;
    }

    public void setId(int id) {
        this.databaseId = id;
    }

    public int getLikes() {
        return likes;
    }

    public void setLikes(int likes) {
        this.likes = likes;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Url getUrls() {
        return urls;
    }

    public void setUrls(Url urls) {
        this.urls = urls;
    }

    public boolean isLikedByUser() {
        return likedByUser;
    }

    public void setLikedByUser(boolean likedByUser) {
        this.likedByUser = likedByUser;
    }

    public boolean isSaved() {
        return saved;
    }

    public void setSaved(boolean saved) {
        this.saved = saved;
    }
}
