package com.demo.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Rectangle;

import java.util.Random;

public class flappybird extends ApplicationAdapter {
    private int mFlapState = 0;
    private int mGameState = 0;

    SpriteBatch batch;
    Texture background;
    Texture[] birds;
    Texture topTube;
    Texture bottomTube;
    BitmapFont font;
    float birdY = 0;
    float velocity = 0;
    float gravity = 0.5f;
    float gap = 400;
    float maxGapOffset;
    Random randomGenerator;
    int numOfTubes = 4;
    float[] tubeOffset = new float[numOfTubes];
    float[] tubeX = new float[numOfTubes];
    float tubeVelocity = 4;
    float distanceBetweenTubes;
    int mScore = 0;
    int mScoreTube = 0;
    //collision detection
    Rectangle[] topTubeRectangles;
    Rectangle[] bottomTubeRectangles;
    Circle birdCircle;
    //ShapeRenderer shapeRenderer;
    Texture gameover;
    int mLives;
    int mLastLifePoint;


    @Override
    public void create() {

        batch = new SpriteBatch();
        background = new Texture("bg.png");
        font = new BitmapFont();
        birds = new Texture[2];
        birds[0] = new Texture("bird.png");
        birds[1] = new Texture("bird2.png");
        birdCircle = new Circle();
        topTube = new Texture("toptube.png");
        bottomTube = new Texture("bottomtube.png");
        topTubeRectangles = new Rectangle[numOfTubes];
        bottomTubeRectangles = new Rectangle[numOfTubes];
        randomGenerator = new Random();
        gameover = new Texture("gameover.png");
        resetGame();

        //shapeRenderer = new ShapeRenderer();
    }

    private void checkInput() {
        if (Gdx.input.justTouched()) {
            Gdx.app.log("flappyGame", "touched");
            switch (mGameState) {
                case 0:
                    mGameState = 1;
                    resetGame();
                    break;
                case 1:
                    velocity = -12;
                    break;
                case -1:
                    mGameState = 0;
                    break;
                default:
                    mGameState = 0;
                    break;
            }
        }
    }

    private void update() {
        if (mGameState == 1) {
            mFlapState = mFlapState == 0 ? 1 : 0;
            velocity += gravity;
            //keep track of score:
            if (tubeX[mScoreTube] + topTube.getWidth() < Gdx.graphics.getWidth() / 2) {
                mScore += 10;
                mScoreTube = (mScoreTube + 1) % numOfTubes;
                if(mScore % 50 == 0 && mScore - mLastLifePoint*50 > 0) {
                    mLives += 1;
                    mLastLifePoint = mScore/50;
                    Gdx.app.log("flappy debug","increase 1 life");
                }
            }
            birdCircle.set(Gdx.graphics.getWidth() / 2,
                    birdY + birds[mFlapState].getHeight() / 2, birds[mFlapState].getWidth() / 2);
            topTubeRectangles[mScoreTube].set(tubeX[mScoreTube], Gdx.graphics.getHeight() / 2 + gap / 2 + tubeOffset[mScoreTube],
                    topTube.getWidth(), topTube.getHeight());
            bottomTubeRectangles[mScoreTube].set(tubeX[mScoreTube], Gdx.graphics.getHeight() / 2 - gap / 2 - bottomTube.getHeight()
                    + tubeOffset[mScoreTube], bottomTube.getWidth(), bottomTube.getHeight());
            if (Intersector.overlaps(birdCircle, topTubeRectangles[mScoreTube]) || Intersector.overlaps(birdCircle, bottomTubeRectangles[mScoreTube])) {
                //collision detected!!!
                Gdx.app.log("FlappyBirds", "collision detected!!!");
                mScoreTube = (mScoreTube + 1) % numOfTubes;
                mScore -= 10;
                if (mScore < 0 || (mScore % 50 == 0 && mScore - mLastLifePoint*50 < 0)) {
                    Gdx.app.log("flappy debug","decrease 1 life");
                    mLives -= 1;
                }
                mScore = mScore <= 0? 0 : mScore;
                if (mLives == 0) {
                    mGameState = -1; //Game Over State
                }
            }
            for (int i = 0; i < numOfTubes; i++) {

                if (tubeX[i] < -topTube.getWidth()) {
                    //just moved out of the screen
                    tubeX[i] += numOfTubes * distanceBetweenTubes;
                } else {
                    tubeX[i] -= tubeVelocity;
                }
            }
            if (birdY - velocity > 0 && birdY - velocity < Gdx.graphics.getHeight() - birds[0].getHeight()) {
                birdY -= velocity;
            }
        }
    }

    @Override
    public void render() {
        /*Gdx.gl.glClearColor(1, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);*/
        checkInput();
        update();

        batch.begin();
        switch (mGameState) {
            case 0: //Game Entry Page
                batch.draw(background, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
                font.setColor(1.0f, 0.0f, 0.0f, 1.0f);
                font.getData().setScale(8);
                font.draw(batch, "Start Game", Gdx.graphics.getWidth() / 2 - font.getRegion().getRegionWidth() - font.getSpaceWidth() * 2, Gdx.graphics.getHeight() / 2 + font.getRegion().getRegionHeight() / 2);
                break;
            case 1: //game level 1
                batch.draw(background, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
                for (int i = 0; i < numOfTubes; i++) {
                    batch.draw(topTube, tubeX[i],
                            Gdx.graphics.getHeight() / 2 + gap / 2 + tubeOffset[i]);
                    batch.draw(bottomTube, tubeX[i],
                            Gdx.graphics.getHeight() / 2 - gap / 2 - bottomTube.getHeight() + tubeOffset[i]);
                }
                batch.draw(birds[mFlapState], Gdx.graphics.getWidth() / 2 - birds[mFlapState].getWidth() / 2,
                        birdY);
                font.setColor(1.0f, 0.0f, 0.0f, 1.0f);
                font.getData().setScale(8);
                font.draw(batch, "Score: " + String.valueOf(mScore)+(mLives > 1? " Lives: ":" Life: ")+String.valueOf(mLives), 80, 200);
                break;
            case -1:
                //Game Over
                batch.draw(background, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
                batch.draw(birds[mFlapState], Gdx.graphics.getWidth() / 2 - birds[mFlapState].getWidth() / 2,
                        birdY);
                batch.draw(gameover,Gdx.graphics.getWidth() / 2 - gameover.getWidth() / 2,
                        Gdx.graphics.getHeight()/2 - gameover.getHeight()/2);
                font.setColor(1.0f, 0.0f, 0.0f, 1.0f);
                font.getData().setScale(8);
                font.draw(batch, "Score: " + String.valueOf(mScore), 100, 200);
                break;
        }
        batch.end();
        //shape renderer
		/*
		shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
		shapeRenderer.setColor(Color.RED);
		shapeRenderer.circle(birdCircle.x,birdCircle.y,birdCircle.radius);
		for (int i = 0; i < numOfTubes; i++) {
			shapeRenderer.rect(topTubeRectangles[i].x, topTubeRectangles[i].y,
					topTubeRectangles[i].getWidth(),topTubeRectangles[i].getHeight());
			shapeRenderer.rect(bottomTubeRectangles[i].x, bottomTubeRectangles[i].y,
					bottomTubeRectangles[i].getWidth(), bottomTubeRectangles[i].getHeight());
		}
		shapeRenderer.end();
		*/
    }

    @Override
    public void dispose() {
        batch.dispose();
        background.dispose();
        birds[0].dispose();
        birds[1].dispose();
        topTube.dispose();
        bottomTube.dispose();
        gameover.dispose();
    }

    private void resetGame() {
        mLives = 3;
        mScore = 0;
        mScoreTube = 0;
        mLastLifePoint = 0;
        birdY = Gdx.graphics.getHeight() / 2 - birds[0].getHeight() / 2;
        maxGapOffset = Gdx.graphics.getHeight() / 2 - gap / 2 - 100;
        distanceBetweenTubes = Gdx.graphics.getWidth() * 3 / 4;
        for (int i = 0; i < numOfTubes; i++) {
            topTubeRectangles[i] = new Rectangle();
            bottomTubeRectangles[i] = new Rectangle();
            tubeX[i] = Gdx.graphics.getWidth() - topTube.getWidth() / 2 + i * distanceBetweenTubes;
            tubeOffset[i] = (randomGenerator.nextFloat() - 0.5f) * (Gdx.graphics.getHeight() - gap - 200);
        }
    }
}
