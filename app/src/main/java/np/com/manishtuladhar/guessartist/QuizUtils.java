package np.com.manishtuladhar.guessartist;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

public class QuizUtils {

    private static final String CURRENT_SCORE_KEY = "current_score";
    private static final String HIGH_SCORE_KEY = "high_score";
    private static final String GAME_FINISHED = "game_finished";

    private static final int NUM_OPTIONS = 4;

    // ========================= QUESTIONS AND ANSWERS =======================

    /**
     * Generate random questions and shuffle them
     */
    static ArrayList<Integer> generateQuestion(ArrayList<Integer> remainingSongsIds)
    {
        //shuffle the ids
        Collections.shuffle(remainingSongsIds);

        ArrayList<Integer> options = new ArrayList<>();

        //pick the random first fours sample ids
        for(int i=0;i<NUM_OPTIONS;i++)
        {
            if(i<remainingSongsIds.size())
            {
                options.add(remainingSongsIds.get(i));
            }
        }
        return options;
    }

    /**
     * Randomly generates a answer between 4 options
     */
    public static int getCorrectAnswer(ArrayList<Integer> options)
    {
        Random r = new Random();
        int answerIndex = r.nextInt(options.size());
        return options.get(answerIndex);
    }

    /**
     * Check whether the correct answer is equal to user answer
     */
    public static boolean userCorrect(int correctAnswer,int userAnswer)
    {
        return userAnswer == correctAnswer;
    }

    // ========================= SCORE KEEPER =======================

    /**
     * Get the high score from shared preferences
     */
    public static int getHighScore(Context context)
    {
        SharedPreferences preferences = context.getSharedPreferences(
                context.getString(R.string.preference_key),Context.MODE_PRIVATE);
        return preferences.getInt(HIGH_SCORE_KEY,0);
    }

    /**
     * Set the high score from shared preferences
     */
    public static void setHighScore(Context context,int highScore)
    {
        SharedPreferences preferences = context.getSharedPreferences(
                context.getString(R.string.preference_key),Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putInt(HIGH_SCORE_KEY,highScore);
        editor.apply();
    }


    /**
     * Get the current score from shared preferences
     */
    public static int getCurrentScore(Context context)
    {
        SharedPreferences preferences = context.getSharedPreferences(
                context.getString(R.string.preference_key),Context.MODE_PRIVATE);
        return preferences.getInt(CURRENT_SCORE_KEY,0);
    }

    /**
     * Set the current score from shared preferences
     */
    public static void setCurrentScore(Context context,int currentScore)
    {
        SharedPreferences preferences = context.getSharedPreferences(
                context.getString(R.string.preference_key),Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putInt(CURRENT_SCORE_KEY,currentScore);
        editor.apply();
    }

    // ========================= END GAME =======================

    /**
     * End the game and go to main
     */
    static void endGame(Context context)
    {
        Intent endGame = new Intent(context,MainActivity.class);
        endGame.putExtra(GAME_FINISHED,true);
        context.startActivity(endGame);
    }
}
