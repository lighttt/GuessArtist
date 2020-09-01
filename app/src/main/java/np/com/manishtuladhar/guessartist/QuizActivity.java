package np.com.manishtuladhar.guessartist;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import java.util.ArrayList;

public class QuizActivity extends AppCompatActivity implements View.OnClickListener {

    //views
    ImageView artistIV;
    Button[] mButtons;
    int[] mButtonsIDs = {R.id.buttonA, R.id.buttonB, R.id.buttonC, R.id.buttonD};

    //delay
    private static final int CORRECT_ANSWER_DELAY = 1000;

    //vars
    private ArrayList<Integer> mQuestionsSongIds;
    private ArrayList<Integer> mRemainingSongIds;
    private int mCorrectSongId;
    private int mCurrentScore;
    private int mHighScore;

    //intents
    private static final String REMAINING_SONGS_KEY = "remaining_songs";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz);

        artistIV = findViewById(R.id.artistIV);

        //check whether its the new game
        boolean isNewGame = !getIntent().hasExtra(REMAINING_SONGS_KEY);

        if (isNewGame) {
            //score reset
            QuizUtils.setCurrentScore(this, 0);

            //get all songs
            mRemainingSongIds = Song.getAllSongIds(this);
        } else {
            //get the remaining songs
            mRemainingSongIds = getIntent().getIntegerArrayListExtra(REMAINING_SONGS_KEY);
        }

        // get the current and high score
        mCurrentScore = QuizUtils.getCurrentScore(this);
        mHighScore = QuizUtils.getHighScore(this);

        //generate question and also correct answer
        mQuestionsSongIds = QuizUtils.generateQuestion(mRemainingSongIds);
        //get the correct
        mCorrectSongId = QuizUtils.getCorrectAnswer(mQuestionsSongIds);

        //set album art
        artistIV.setImageBitmap(Song.getAlbumArtBySongId(this, mCorrectSongId));

        //end the game
        if(mQuestionsSongIds.size()<2)
        {
            QuizUtils.endGame(this);
            finish();
        }

        //add button with their question title
        mButtons = initializeButtons(mQuestionsSongIds);
    }

    /**
     * First add the buttons to their respective ids
     * Second, song ko id add
     */
    private Button[] initializeButtons(ArrayList<Integer> answerSongIds) {
        Button[] buttons = new Button[mButtonsIDs.length];
        for (int i = 0; i < answerSongIds.size(); i++) {
            Button currentButton = findViewById(mButtonsIDs[i]);
            Song currentSong = Song.getSongById(this, answerSongIds.get(i));
            buttons[i] = currentButton;
            currentButton.setOnClickListener(this);
            if (currentSong != null) {
                currentButton.setText(currentSong.getArtist());
            }
        }
        return buttons;
    }

    @Override
    public void onClick(View view) {
        //show correct answer
        showCorrectAnswer();

        //check the user answer
        Button pressedButton = (Button) view;

        //get the index of the button
        int userAnswerIndex = -1;
        for(int i =0;i<mButtons.length;i++)
        {
            if(pressedButton.getId() == mButtonsIDs[i])
            {
                userAnswerIndex =i;
            }
        }

        //check user correct
        if(QuizUtils.userCorrect(mCorrectSongId,userAnswerIndex))
        {
            mCurrentScore++;
            QuizUtils.setCurrentScore(this,mCurrentScore);
            if(mCurrentScore>mHighScore)
            {
                mHighScore = mCurrentScore;
                QuizUtils.setHighScore(this,mHighScore);
            }
        }

        //remove the answer song from list of all songs
        mRemainingSongIds.remove(Integer.valueOf(mCorrectSongId));

        //wait for 2-3 seconds so that user can see the correct answer
        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent nextQuestionIntent = new Intent(QuizActivity.this,QuizActivity.class);
                nextQuestionIntent.putExtra(REMAINING_SONGS_KEY,mRemainingSongIds);
                finish();
                startActivity(nextQuestionIntent);
            }
        },
                CORRECT_ANSWER_DELAY);

    }

    /**
     * Display the correct answer
     */
    private void showCorrectAnswer()
    {
        for(int i=0;i<mQuestionsSongIds.size();i++)
        {
            int buttonSongId = mQuestionsSongIds.get(i);
            mButtons[i].setEnabled(false);

            //check answer
            if(buttonSongId == mCorrectSongId)
            {
                mButtons[i].getBackground().setColorFilter(
                        ContextCompat.getColor(this, android.R.color.holo_green_light),
                        PorterDuff.Mode.MULTIPLY);
            }
            else{
                mButtons[i].getBackground().setColorFilter(
                        ContextCompat.getColor(this, android.R.color.holo_red_light),
                        PorterDuff.Mode.MULTIPLY);
            }
            mButtons[i].setTextColor(Color.WHITE);
        }
    }
}