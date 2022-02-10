package fr.devsphinx.topquiz.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.*;

public class Question implements Parcelable {

    private String mQuestion;
    private List<String> mChoiceList;
    private int mAnswerIndex;

    public Question(String question, List<String> listquestion, int answerindex){
        this.mQuestion = question;
        this.mChoiceList = listquestion;
        this.mAnswerIndex = answerindex;
    }

    protected Question(Parcel in) {
        mQuestion = in.readString();
        mChoiceList = new ArrayList<>();
        in.readList(mChoiceList, String.class.getClassLoader());
        mAnswerIndex = in.readInt();
    }

    public static final Creator<Question> CREATOR = new Creator<Question>() {
        @Override
        public Question createFromParcel(Parcel in) {
            return new Question(in);
        }

        @Override
        public Question[] newArray(int size) {
            return new Question[size];
        }
    };

    public String getQuestion() {
        return mQuestion;
    }

    public void setQuestion(String question) {
        mQuestion = question;
    }

    public List<String> getChoiceList() {
        return mChoiceList;
    }

    public void setChoiceList(List<String> choiceList) {
        mChoiceList = choiceList;
    }

    public int getAnswerIndex() {
        return mAnswerIndex;
    }

    public void setAnswerIndex(int answerIndex) {
        mAnswerIndex = answerIndex;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(mQuestion);
        parcel.writeList(mChoiceList);
        parcel.writeInt(mAnswerIndex);
    }

    @Override
    public String toString() {
        return " - " + mQuestion;
    }
}
