package com.example.quizgame;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import com.example.quizgame.QuizContract.*;

import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

public class QuizDbHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME="quiz.db";
    private static final int DATABASE_VERSION=1;
    private SQLiteDatabase db;


    public QuizDbHelper(@Nullable Context context) {
        super (context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        this.db = db;

        final String SQL_CREATE_QUESTIONS_TABLE = "CREATE TABLE " +
             QuestionsTable.TABLE_NAME + " ( " +
                QuestionsTable._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                QuestionsTable.COLUMN_QUESTION + " TEXT, " +
                QuestionsTable.COLUMN_OPTION1 + " TEXT, " +
                QuestionsTable.COLUMN_OPTION2 + " TEXT, " +
                QuestionsTable.COLUMN_OPTION3 + " TEXT, " +
                QuestionsTable.COLUMN_ANSWERNR + " TEXT " +
                ")";

        db.execSQL (SQL_CREATE_QUESTIONS_TABLE);
        fillQuestionsTable();
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    db.execSQL ("DROP TABLE IF EXISTS " + QuestionsTable.TABLE_NAME) ;
    onCreate (db);
    }
    private void fillQuestionsTable(){
        Question q1 = new Question ("what's my full name", "sadina magar", "sadina rana magar", "sadina jarga magar",3);
        addQuestion (q1);
        Question q2 = new Question ("what's my chilhood friend", "alka magar", "alisha rana magar", "simran magar",1);
        addQuestion (q2);
        Question q3 = new Question ("what's my School name", "DPS", "st.xaviers", "secondary boarding school",3);
        addQuestion (q3);
        Question q4 = new Question ("what's my favorite color", "pink", "Blue", "black",3);
        addQuestion (q4);
    }
    private void addQuestion(Question question){
        ContentValues cv = new ContentValues ();
        cv.put (QuestionsTable.COLUMN_QUESTION, question.getQuestion ());
        cv.put(QuestionsTable.COLUMN_OPTION1, question.getOption1 ());
        cv.put(QuestionsTable.COLUMN_OPTION2, question.getOption2 ());
        cv.put(QuestionsTable.COLUMN_OPTION3, question.getOption3 ());
        cv.put(QuestionsTable.COLUMN_ANSWERNR, question.getAnswernr ());
        db.insert (QuestionsTable.TABLE_NAME,null,cv);
    }
    public List<Question> getAllQuestions() {
        List<Question> questionList = new ArrayList<> ();
        db = getReadableDatabase ();
        Cursor c = db.rawQuery ("SELECT * FROM " + QuestionsTable.TABLE_NAME, null);
        if (c.moveToFirst ()) {
            do {
                Question question = new Question();
                question.setQuestion (c.getString (c.getColumnIndex (QuestionsTable.COLUMN_QUESTION)));
                question.setOption1 (c.getString (c.getColumnIndex (QuestionsTable.COLUMN_OPTION1)));
                question.setOption2 (c.getString (c.getColumnIndex (QuestionsTable.COLUMN_OPTION2)));
                question.setOption3 (c.getString (c.getColumnIndex (QuestionsTable.COLUMN_OPTION3)));
                question.setAnswernr (c.getInt (c.getColumnIndex (QuestionsTable.COLUMN_ANSWERNR)));
                questionList.add (question);
            }
            while (c.moveToNext ());
        }
        c.close ();
        return questionList;
    }
    }
