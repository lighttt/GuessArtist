package np.com.manishtuladhar.guessartist;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    TextView highScoreTv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        highScoreTv = findViewById(R.id.highScoreTV);


    }

    /**
     * Starting a new game
     */
    public void newGame(View view) {
        Intent quizIntent = new Intent(this,QuizActivity.class);
        startActivity(quizIntent);
    }
}