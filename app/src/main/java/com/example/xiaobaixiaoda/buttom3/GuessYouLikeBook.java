package com.example.xiaobaixiaoda.buttom3;

/**
 * Created by xiaobaixiaoda on 2017/10/27.
 */

public class GuessYouLikeBook {

    private String guess_you_like_bookName;
    private String guess_you_like_authorName;
    private int guess_you_like_imageID;

    //构造方法
    public GuessYouLikeBook(String guess_you_like_bookName,String guess_you_like_authorName,int guess_you_like_imageID){
        this.setGuess_you_like_bookName(guess_you_like_bookName);
        this.setGuess_you_like_authorName(guess_you_like_authorName);
        this.setGuess_you_like_imageID(guess_you_like_imageID);
    }

    public String getGuess_you_like_bookName() {
        return guess_you_like_bookName;
    }

    public String getGuess_you_like_authorName() {
        return guess_you_like_authorName;
    }

    public int getGuess_you_like_imageID() {
        return guess_you_like_imageID;
    }

    public void setGuess_you_like_bookName(String guess_you_like_bookName) {
        this.guess_you_like_bookName = guess_you_like_bookName;
    }

    public void setGuess_you_like_authorName(String guess_you_like_authorName) {
        this.guess_you_like_authorName = guess_you_like_authorName;
    }

    public void setGuess_you_like_imageID(int guess_you_like_imageID) {
        this.guess_you_like_imageID = guess_you_like_imageID;
    }
}
