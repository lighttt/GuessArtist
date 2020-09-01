package np.com.manishtuladhar.guessartist;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    TextView highScoreTv;

    private static final String GAME_FINISHED = "game_finished";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        highScoreTv = findViewById(R.id.highScoreTV);

        //get the high score
        int highScore = QuizUtils.getHighScore(this);
        int maxScore = Song.getAllSongIds(this).size() - 1;

        //Set the high score
        String highScoreText= getString(R.string.high_score,highScore,maxScore);
        highScoreTv.setText(highScoreText);

        //if the game is over
        if(getIntent().hasExtra(GAME_FINISHED))
        {
            TextView gameFinishedTV = findViewById(R.id.gameResult);
            TextView resultScoreTV = findViewById(R.id.resultScore);

            Integer yourScore = QuizUtils.getCurrentScore(this);
            String yourScoreString = getString(R.string.score_result,yourScore,maxScore);
            resultScoreTV.setText(yourScoreString);

            gameFinishedTV.setVisibility(View.VISIBLE);
            resultScoreTV.setVisibility(View.VISIBLE);
        }
    }

    /**
     * Starting a new game
     */
    public void newGame(View view) {
        Intent quizIntent = new Intent(this,QuizActivity.class);
        startActivity(quizIntent);
    }
}