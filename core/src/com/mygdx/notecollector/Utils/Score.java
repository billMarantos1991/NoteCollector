package com.mygdx.notecollector.Utils;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;


/**
 * Created by bill on 7/25/16.
 */
public class Score  {


    private Preferences score;
    private ArrayList<String> numbers;

    public Score() {
        score = Gdx.app.getPreferences("Scores");
        numbers = new ArrayList<>();
    }


    public void WriteScore(String name, int ScoreNumber) {
        Object[] names = score.get().entrySet().toArray();


            if(score.contains(name)) {
                score.remove(name);
                score.flush();
                score.putInteger(name, ScoreNumber);
                score.flush();
            }else{
                score.putInteger(name, ScoreNumber);
                score.flush();
            }




    }


    public ArrayList<String> getScore() {


        ArrayList<String> scores = new ArrayList<String>();
        Object[] names = score.get().entrySet().toArray();
        ArrayList<String> scoreslist = new ArrayList<String>();



        int count =0;
        for ( int i=0; i<names.length ; i++) {
            String[] parts = names[i].toString().split("=");

            scores.add(parts[0]);
            numbers.add(parts[1]);
        }

        Collections.sort(numbers, new Comparator<String>()
        {
            public int compare(String s1, String s2)
            {
                return (Integer.valueOf(s2)) -Integer.valueOf(s1)  ;
            }
        });

        int numbersize = numbers.size();
        int scoressize = scores.size();
        for (int j=0;j<scoressize;j++){
                 for (int i =0;i<numbersize;i++) {

                 if (score.getInteger(scores.get(i), Integer.parseInt(numbers.get(j))) == Integer.parseInt(numbers.get(j))) {
                    scoreslist.add(scores.get(i) + ":" + numbers.get(j));
                }
            }

        }
        return scoreslist;
    }



}
