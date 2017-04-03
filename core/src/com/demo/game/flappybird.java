package com.demo.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import java.util.Random;

public class flappybird extends ApplicationAdapter {
	private int mFlapState = 0;
	private int mGameState = 0;
	SpriteBatch batch;
	Texture background;
	Texture[] birds;
	Texture topTube;
	Texture bottomTube;
	float birdY = 0;
	float velocity = 0;
	float gravity = 2;
	float gap = 400;
	float maxGapOffset;
	Random randomGenerator;
	int numOfTubes = 4;
	float[] tubeOffset = new float[numOfTubes];
	float[] tubeX = new float[numOfTubes];
	float tubeVelocity = 4;
	float distanceBetweenTubes;


	
	@Override
	public void create () {
		batch = new SpriteBatch();
		background = new Texture("bg.png");
		birds = new Texture[2];
		birds[0] = new Texture("bird.png");
		birds[1] = new Texture("bird2.png");
		topTube = new Texture("toptube.png");
		bottomTube = new Texture("bottomtube.png");
		birdY = Gdx.graphics.getHeight()/2-birds[0].getHeight()/2;
		maxGapOffset = Gdx.graphics.getHeight()/2 - gap / 2 - 100;
		distanceBetweenTubes = Gdx.graphics.getWidth() *3/4;
		randomGenerator = new Random();
		for (int i = 0; i < 4; i++) {
			tubeX[i] = Gdx.graphics.getWidth() / 2 - topTube.getWidth() / 2 + i* distanceBetweenTubes;
			tubeOffset[i] = (randomGenerator.nextFloat()*0.5f)*(Gdx.graphics.getHeight() - gap - 200);
		}

	}

	private void checkInput() {
		if (Gdx.input.justTouched()) {
			Gdx.app.log("Touched","wow");
			mGameState = 1;
			velocity = -30;

		}
	}

	private void update () {
		if (mGameState != 0) {
			mFlapState = mFlapState == 0 ? 1 : 0;
			velocity += gravity;
			for (int i=0; i < 4; i++) {
				if (tubeX[i] < -topTube.getWidth()) {
					//just moved out of the screen
					tubeX[i] += numOfTubes * distanceBetweenTubes;
				} else {
					tubeX[i] -= tubeVelocity;
				}
			}
			if (birdY - velocity > 0 || velocity <0) {
				birdY -= velocity;
			}
		}
	}

	@Override
	public void render () {
		/*Gdx.gl.glClearColor(1, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);*/
		checkInput();
		update();
		batch.begin();

		batch.draw(background, 0, 0, Gdx.graphics.getWidth(),Gdx.graphics.getHeight());
		for (int i=0; i < 4; i++) {
			batch.draw(topTube, tubeX[i],
					Gdx.graphics.getHeight() / 2 + gap / 2 + tubeOffset[i]);
			batch.draw(bottomTube, tubeX[i],
					Gdx.graphics.getHeight() / 2 - gap / 2 - bottomTube.getHeight() + tubeOffset[i]);
		}
		batch.draw(birds[mFlapState], Gdx.graphics.getWidth()/2 - birds[mFlapState].getWidth()/2,
				birdY);
		batch.end();
	}
	
	@Override
	public void dispose () {
		batch.dispose();
		background.dispose();
		birds[0].dispose();
		birds[1].dispose();
	}
}
