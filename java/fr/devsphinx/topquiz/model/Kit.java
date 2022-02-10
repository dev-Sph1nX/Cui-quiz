package fr.devsphinx.topquiz.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.*;

public class Kit implements Parcelable {

    private String title;
    private List<Question> mQuestionList;
    private int mQuestionIndex;
    private int mDisplayedQuestions;
    private int id;

    public static int DEFAULT_DISPLAYED_QUESTIONS = 5;

    public Kit (int id, int nbquestions, String title){
        this.id = id;
        this.title = title;
        this.mQuestionList = new ArrayList<Question>();
        this.mDisplayedQuestions = nbquestions;
    }
    public Kit (int id, int nbquestions, String title, List<Question> list){
        this.id = id;
        this.title = title;
        this.mQuestionList = list;
        this.mDisplayedQuestions = nbquestions;
    }
    public Kit(String title, List<Question> questionList) {
        this.title = title;
        mQuestionList = questionList;
        Collections.shuffle(mQuestionList);
        this.mDisplayedQuestions = DEFAULT_DISPLAYED_QUESTIONS;
    }

    protected Kit(Parcel in) {
        title = in.readString();
        mQuestionList = new ArrayList<Question>();
        in.readTypedList(mQuestionList, Question.CREATOR);
        mQuestionIndex = in.readInt();
        mDisplayedQuestions = in.readInt();
    }

    public static final Creator<Kit> CREATOR = new Creator<Kit>() {
        @Override
        public Kit createFromParcel(Parcel in) {
            return new Kit(in);
        }

        @Override
        public Kit[] newArray(int size) {
            return new Kit[size];
        }
    };

    public Question getCurrentQuestion() {
        return mQuestionList.get(mQuestionIndex);
    }


    public Question getNextQuestion() {
        mQuestionIndex++;
        return getCurrentQuestion();
    }

    public String getTitle() {
        return title;
    }

    public int getId() {
        return id;
    }
    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(title);
        parcel.writeTypedList(mQuestionList);
        parcel.writeInt(mQuestionIndex);
        parcel.writeInt(mDisplayedQuestions);
    }
    public void addQuestion(Question question){
        mQuestionList.add(question);
    }

    @Override
    public String toString(){
        StringBuilder chaine = new StringBuilder();
        chaine.append("Name : " + this.getTitle() + " / Number of questions : " + this.count() + "/ id : " + this.id).append("\n");
        for (Question q : mQuestionList) {
            chaine.append(q.toString()).append("\n");
        }
        return chaine.toString();
    }

    public int count(){
        return this.mQuestionList.size();
    }

    public void setDisplayedQuestions(int value){
        this.mDisplayedQuestions = value;
    }

    public int getDisplayedQuestions(){
        return this.mDisplayedQuestions;
    }

    public List<Question> getAllQuestions(){
        return mQuestionList;
    }
    public void clearAllQuestions(){
        this.mQuestionList = new ArrayList<Question>();
    }

    public void shuffle(){
        Collections.shuffle(this.mQuestionList);
    }
}
