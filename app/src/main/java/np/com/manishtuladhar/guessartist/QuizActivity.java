package np.com.manishtuladhar.guessartist;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import java.util.ArrayList;

public class QuizActivity extends AppCompatActivity implements View.OnClickListener {

    //views
    ImageView artistIV;
    Button[] mButtons;
    int[] mButtonsIDs = {R.id.buttonA,R.id.buttonB,R.id.buttonC,R.id.buttonD};

    //vars
    private ArrayList<Integer> mQuestionsSampleIds;
    private int mAnswerSampleId;
    private int mCurrentScore;
    private int mHighScore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz);

        artistIV = findViewById(R.id.artistIV);

        mButtons = initializeButtons(mQuestionsSampleIds);
    }

    /**
     * First add the buttons to their respective ids
     * Second, song ko id add
     */
    private Button[] initializeButtons(ArrayList<Integer> answerSampleIds)
    {
        Button[] buttons = new Button[mButtonsIDs.length];
        for(int i =0;i<answerSampleIds.size();i++)
        {
            Button currentButton = findViewById(mButtonsIDs[i]);
            Sample currentSample = Sample.getSampleById(this,answerSampleIds.get(i));
            buttons[i] = currentButton;
            currentButton.setOnClickListener(this);
            if(currentSample!=null)
            {
                currentButton.setText(currentSample.getArtist());
            }
        }
        return buttons;
    }

    @Override
    public void onClick(View view) {

    }
}