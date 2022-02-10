package fr.devsphinx.topquiz;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import fr.devsphinx.topquiz.model.Kit;
import fr.devsphinx.topquiz.model.Question;

public class DatabaseManager extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "UltimateToolQuizDB.db";
    private static final int DATABASE_VERSION = 1;

    public DatabaseManager(Context context){
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        db.execSQL("create table Kit (idKit INTEGER PRIMARY KEY, title TEXT not null, nbQuestions integer not null)");
        db.execSQL("create table Question (idQuestion INTEGER PRIMARY KEY, answer_index integer not null, question text not null, " +
                "first_ans text not null, second_ans text not null, third_ans text not null, fourth_ans text not null, " +
                "idKit integer not null, FOREIGN KEY(idKit) REFERENCES Kit(idKit) )");

        insertKit("Rap", 5, db);
        insertQuestion(db, getKitbyName("Rap", db).getId(),"Qui est le man of the year ?", "Laylow", "SCH", "PLK", "Soso Maness", 0);
        insertQuestion(db, getKitbyName("Rap", db).getId(),"Quel est la nationalité de Central Cee, celui qui chante 'Obessed with you' ? ", "Finlandais", "Britanique", "Américain", "Canadien", 1);
        insertQuestion(db, getKitbyName("Rap", db).getId(),"L'artiste noir et gay le plus connu au monde.", "Damso", "Kayne West+", "Dadju", "Lil Nas X", 3);
        insertQuestion(db, getKitbyName("Rap", db).getId(),"Qui chante 'God's Plan' ?","Bankrol Hayden", "Young Thug", "Juice WRLD", "Drake", 3);
        insertQuestion(db, getKitbyName("Rap", db).getId(),"Qui a composé 'Heat Waves' ?","Slowthai", "Gunna", "Glass Animals", "Patrick balkany", 2);
        insertQuestion(db, getKitbyName("Rap", db).getId(),"Qui est en feat sur 'Special' de laylow","Nekfeu et AlphaWann", "Orelsan et Vald", "Nekfeu et Fousheé", "Damso", 2);
        insertQuestion(db, getKitbyName("Rap", db).getId(),"Call me by your ... ?","Mouth", "Fat ass", "Name", "Dream", 2);
        insertQuestion(db, getKitbyName("Rap", db).getId(),"Sur la nouvelle mixtape de Seb, combien de titres y a-t-il ?","10", "12", "20", "15", 1);
        insertQuestion(db, getKitbyName("Rap", db).getId(),"Ma lamborgini a pris quelques ...","Rayures", "Dos d'âne", "Coup de feu", "Bosses", 1);

        insertKit("Histoire", 5, db);
        insertQuestion(db, getKitbyName("Histoire", db).getId(),"Entre 1945 et 1946, dans quel pays s'est déroulé le fameux procès de Nuremberg ?", "Russie", "Allemagne", "Pologne", "Autriche", 1);
        insertQuestion(db, getKitbyName("Histoire", db).getId(),"Que signifie le terme URSS ?", "Union des Royaumes Slovènes et Suédois", "Union des Républiques Socialistes Soviétiques", "Unification de la République Serbe et Slovaque", "Unification de la République Suédoise et Slovène ", 1);
        insertQuestion(db, getKitbyName("Histoire", db).getId(),"En quelle année l'URSS a-t-elle éclatée ?", "1989","1991", "1995", "L'URSS existe toujours", 1);
        insertQuestion(db, getKitbyName("Histoire", db).getId(),"En quelle année le Mur de Berlin a-t-il été détruit ?", "2001","1990", "1945", "1989", 3);
        insertQuestion(db, getKitbyName("Histoire", db).getId(),"Combien de septennats a fait François Mitterrand ?", "0","1", "2", "3", 2);
        insertQuestion(db, getKitbyName("Histoire", db).getId(),"Quel était le prénom de Mussolini ?", "Joseph","Antonio", "Benito", "Adolf", 2);
        insertQuestion(db, getKitbyName("Histoire", db).getId(),"En quelle année, Mao Zedong a pris le pouvoir chinois ?", "1939","1945", "1949", "1959", 2);
        insertQuestion(db, getKitbyName("Histoire", db).getId(),"Vercingétorix est vaincu à Alésia en 52 av. J.-C. par ", "Charlemagne","les Vinkings", "Jules César", "Trump", 2);
        insertQuestion(db, getKitbyName("Histoire", db).getId(),"Clovis Ier est le fondateur de la dynastie", "Carolingienne","Bourbonne", "Capétienne", "Mérovingienne", 3);
        insertQuestion(db, getKitbyName("Histoire", db).getId(),"Comment la révolution de Juillet 1830 est-elle surnomé ?", "Les Trois Glorieuses","Les Cinq Glorieuses", "Les Trentes Glorieuses", "Les Cinquantes Glorieuses", 0);

        Log.v("DATABASE", "onCreate invoked");
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
        // lorsque que l'on met a jour la database version
        String str = "drop table Kit";
        sqLiteDatabase.execSQL(str);
        this.onCreate(sqLiteDatabase);
        Log.v("DATABASE", "onUpgrade invoked");
    }
    public void insertKit(String title, int nbDisplayedQuestions, SQLiteDatabase db){
        title = title.replace("'", "''");
        String strSQL = "insert into Kit (title, nbQuestions) values ('" +
                title + "', " + nbDisplayedQuestions + ")";
        db.execSQL(strSQL);
        Log.v("DATABASE", "insert kit");
    }





    public void insertKit(String title, int nbDisplayedQuestions){ //
        title = title.replace("'", "''"); // On fait attention aux caractéres sucepetibles de générer une erreur
        String strSQL = "insert into Kit (title, nbQuestions) values ('" +
                title + "', " + nbDisplayedQuestions + ")"; // On ecrit la requete dans une variable String
        this.getWritableDatabase().execSQL(strSQL); // On l'execute dans la bd
        Log.v("DATABASE", "insert kit"); // On écrit un log pour avoir une trâce dans la console - Debug
    }

    public void insertQuestion(int idKit, String question, String f_ans, String s_ans,
                               String t_ans, String fourth_ans, int anwser_index){ // Même processus que pour inserer un Kit
        question = question.replace("'", "''");
        f_ans = f_ans.replace("'", "''");
        s_ans = s_ans.replace("'", "''");
        t_ans = t_ans.replace("'", "''");
        fourth_ans = fourth_ans.replace("'", "''");

        String strSQL = "insert into Question (idKit, question, first_ans, second_ans, third_ans, fourth_ans, answer_index) values ("+
                idKit + ", '" + question + "', '" + f_ans+ "', '" + s_ans + "', '" + t_ans + "', '" + fourth_ans
                + "', " + anwser_index +");";
        this.getWritableDatabase().execSQL(strSQL);
        Log.v("DATABASE", "insert question");
    }





    public void insertQuestion(SQLiteDatabase db, int idKit, String question, String f_ans, String s_ans,
                               String t_ans, String fourth_ans, int anwser_index){
        question = question.replace("'", "''");
        f_ans = f_ans.replace("'", "''");
        s_ans = s_ans.replace("'", "''");
        t_ans = t_ans.replace("'", "''");
        fourth_ans = fourth_ans.replace("'", "''");

        String strSQL = "insert into Question (idKit, question, first_ans, second_ans, third_ans, fourth_ans, answer_index) values ("+
                idKit + ", '" + question + "', '" + f_ans+ "', '" + s_ans + "', '" + t_ans + "', '" + fourth_ans
                + "', " + anwser_index +");";
        db.execSQL(strSQL);
        Log.v("DATABASE", "insert question");
    }

    public List<Kit> getAllKit(){

        List<Kit> kits = new ArrayList<>();

        String strSQL = "select idKit, nbQuestions, title from kit";
        Cursor cursor = this.getReadableDatabase().rawQuery(strSQL, null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()){
            // Tout les questions du kit
            List<Question> questionList = new ArrayList<Question>();
            String str = "select idQuestion, idKit, question, first_ans, second_ans, third_ans, fourth_ans, answer_index from question where idKit = " + cursor.getInt(0);
            Cursor cur = this.getReadableDatabase().rawQuery(str, null);
            cur.moveToFirst();
            //Met les resultats trouvés dans une list
            while (!cur.isAfterLast()){
                questionList.add(new Question(cur.getString(2), Arrays.asList(cur.getString(3), cur.getString(4),
                        cur.getString(5), cur.getString(6)), cur.getInt(7)));
                cur.moveToNext();
            }
            kits.add(new Kit(cursor.getInt(0), cursor.getInt(1), cursor.getString(2), questionList));
            cursor.moveToNext();
        }
        cursor.close();
        return kits;

    }

    public Kit getKitbyID(int id){

        String strSQL = "select idKit, nbQuestions, title from kit where idKit ="+id;
        Cursor cursor = this.getReadableDatabase().rawQuery(strSQL, null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()){
            // Tout les questions du kit
            List<Question> questionList = new ArrayList<Question>();
            String str = "select idQuestion, idKit, question, first_ans, second_ans, third_ans, fourth_ans, answer_index from question where idKit = " + cursor.getInt(0);
            Cursor cur = this.getReadableDatabase().rawQuery(str, null);
            cur.moveToFirst();
            //Met les resultats trouvés dans une list
            while (!cur.isAfterLast()){
                questionList.add(new Question(cur.getString(2), Arrays.asList(cur.getString(3), cur.getString(4),
                        cur.getString(5), cur.getString(6)), cur.getInt(7)));
                cur.moveToNext();
            }
            return new Kit(cursor.getInt(0), cursor.getInt(1), cursor.getString(2), questionList);
        }
        cursor.close();
        return new Kit(null, null);
    }
    public Kit getKitbyName(String name){
        String strSQL = "select idKit, nbQuestions, title from kit where title ='"+name+"'";
        Cursor cursor = this.getReadableDatabase().rawQuery(strSQL, null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()){
            // Tout les questions du kit
            List<Question> questionList = new ArrayList<Question>();
            String str = "select idQuestion, idKit, question, first_ans, second_ans, third_ans, fourth_ans, answer_index from question where idKit = " + cursor.getInt(0);
            Cursor cur = this.getReadableDatabase().rawQuery(str, null);
            cur.moveToFirst();
            //Met les resultats trouvés dans une list
            while (!cur.isAfterLast()){
                questionList.add(new Question(cur.getString(2), Arrays.asList(cur.getString(3), cur.getString(4),
                        cur.getString(5), cur.getString(6)), cur.getInt(7)));
                cur.moveToNext();
            }
            return new Kit(cursor.getInt(0), cursor.getInt(1), cursor.getString(2), questionList);
        }
        cursor.close();
        return new Kit(null, null);
    }
    public Kit getKitbyName(String name, SQLiteDatabase db){
        String strSQL = "select idKit, nbQuestions, title from Kit where title ='"+name+"'";
        Cursor cursor = db.rawQuery(strSQL, null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()){
            // Tout les questions du kit
            List<Question> questionList = new ArrayList<Question>();
            String str = "select idQuestion, idKit, question, first_ans, second_ans, third_ans, fourth_ans, answer_index from Question where idKit = " + cursor.getInt(0);
            Cursor cur = db.rawQuery(str, null);
            cur.moveToFirst();
            //Met les resultats trouvés dans une list
            while (!cur.isAfterLast()){
                questionList.add(new Question(cur.getString(2), Arrays.asList(cur.getString(3), cur.getString(4),
                        cur.getString(5), cur.getString(6)), cur.getInt(7)));
                cur.moveToNext();
            }
            return new Kit(cursor.getInt(0), cursor.getInt(1), cursor.getString(2), questionList);
        }
        cursor.close();
        return new Kit(null, null);
    }
    // with question
    // ps: Il y a des erreurs
    public void deletekitbyName(String name){
        name = name.replace("'", "''");
        String strSQL = "delete from Kit where title = '" + name +"'";
        this.getWritableDatabase().execSQL(strSQL);
        Kit k = getKitbyName(name);
        deleteQuestionofKit(k.getId());
        Log.v("DATABASE", "delete kit by name");
    }
    public void deletekitbyID(int id){
        deleteQuestionofKit(id);
        String strSQL = "delete from Kit where idKit ="+id;
        this.getWritableDatabase().execSQL(strSQL);
        Log.v("DATABASE", "delete kit by name");
    }
    public void deleteQuestionofKit(int id){
        String strSQL = "delete from Question where idKit = " + id;
        this.getWritableDatabase().execSQL(strSQL);
    }
}
