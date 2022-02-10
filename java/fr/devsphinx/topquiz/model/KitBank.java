package fr.devsphinx.topquiz.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class KitBank implements Parcelable {
    private List<Kit> kitbank;

    public KitBank(List<Kit> kitbank) {
        this.kitbank = kitbank;
    }

    protected KitBank(Parcel in) {
        kitbank = new ArrayList<Kit>();
        in.readTypedList(kitbank, Kit.CREATOR);
    }

    public static final Creator<KitBank> CREATOR = new Creator<KitBank>() {
        @Override
        public KitBank createFromParcel(Parcel in) {
            return new KitBank(in);
        }

        @Override
        public KitBank[] newArray(int size) {
            return new KitBank[size];
        }
    };

    public static Question createQuestion(String question, String a, String a2, String a3, String a4, int answerindex){
        return new Question(question,
                Arrays.asList(
                        a,
                        a2,
                        a3,
                        a4
                ),
                answerindex
        );
    }
    public static List<Kit> generateKits(){

        Question question1 = createQuestion("Qui chante le titre 'Notes pour plus tard' ?", "Vald",
                "Nekfeu", "Damso", "Orelsan", 3);
        Question question2 = createQuestion("Quel est la nationalité de Central Cee, celui qui chante 'Obessed with you' ? ", "American",
                "Finlandais", "Britanique", "Canadien", 2);
        Question question3 = createQuestion("L'artiste noir et gay le plus connu au monde.", "Damso",
                "Lil nas X", "Kayne West", "Dadju", 2);
        Question question4 = createQuestion("Qui est le man of the year ?", "Laylow",
                "Luv Resval", "xxxTentacion", "SCH", 0);
        Question question5 = createQuestion("Qui chante 'God's Plan' ?", "Bankrol hayden",
                "Young Thug", "JuiceWRLD", "Drake", 3);
        Question question6 = createQuestion("Qui a composé 'Heat Waves' ?", "Glass Animal",
                "Patrick balkani", "Gunna", "Slowthai", 0);
        Question question7 = createQuestion("Qui est en feat sur 'Special' de laylow", "Nekfeu et AlphaWann",
                "Orelsan et Vald", "Nekfeu et Fousheé", "Damso", 2);
        Question question8 = createQuestion("Call me by your ... ?", "Dream",
                "Mouth", "Fat ass", "Name", 3);
        Question question9 = createQuestion("Sur la nouvelle mixtape de Seb, combien de titres y a-t-il ?  ", "10",
                "12", "20", "18", 1);
        Question question10 = createQuestion("Ma lamborgini a pris quelques ...", "Bosses",
                "Rayures", "Dos d'âne", "Coup de feu", 2);


        Kit rap = new Kit("Rap", Arrays.asList(question1, question2, question3, question4, question5, question6, question7,
                                        question8, question9, question10));
        Question q1 = createQuestion("Entre 1945 et 1946, dans quel pays s'est déroulé le fameux procès de Nuremberg ?", "Russie",
                "Allemagne", "Pologne", "Autriche", 1);
        Question q2 = createQuestion("Que signifie le terme URSS ?", "Union des Royaumes Slovènes et Suédois",
                "Union des Républiques Socialistes Soviétiques", "Unification de la République Serbe et Slovaque",
                "Unification de la République Suédoise et Slovène ", 1);
        Question q3 = createQuestion("En quelle année l'URSS a-t-elle éclatée ?", "1989",
                "1991", "1995", "L'URSS existe toujours", 1);
        Question q4 = createQuestion("En quelle année le Mur de Berlin a-t-il été détruit ?", "2001",
                "1990", "1945", "1989", 3);
        Question q5 = createQuestion("Combien de septennats a fait François Mitterrand ?", "0",
                "1", "2", "3", 2);
        Question q6 = createQuestion("Quel était le prénom de Mussolini ?", "Joseph",
                "Antonio", "Benito", "Adolf", 2);
        Question q7 = createQuestion("En quelle année, Mao Zedong a pris le pouvoir chinois ?", "1939",
                "1945", "1949", "1959", 2);
        Question q8 = createQuestion("Vercingétorix est vaincu à Alésia en 52 av. J.-C. par ", "Charlemagne",
                "les Vinkings", "Jules César", "Trump", 2);
        Question q9 = createQuestion("Clovis Ier est le fondateur de la dynastie", "Carolingienne",
                "Bourbonne", "Capétienne", "Mérovingienne", 3);
        Question q10 = createQuestion("Comment la révolution de Juillet 1830 est-elle surnomé ?", "Les Trois Glorieuses",
                "Les Cinq Glorieuses", "Les Trentes Glorieuses", "Les Cinquantes Glorieuses", 0);

        Kit histoire = new Kit("Histoire", Arrays.asList(q1, q2, q3, q4, q5, q6, q7, q8, q9, q10));

        List<Kit> list = new ArrayList<Kit>();
        list.add(rap);
        list.add(histoire);
        return list;
    }

    public List<Kit> getKitbank() {
        return kitbank;
    }
    public Kit getSpecificKit(int index){
        return kitbank.get(index);
    }
    public void addKit(Kit k){
        List<Kit> list = new ArrayList<Kit>();
        list.addAll(this.kitbank);
        list.add(k);
        this.kitbank = list;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeTypedList(kitbank);
    }
}
