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
	float tubeOffset;
	float tubeX;
	float tubeVelocity;

	
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
		randomGenerator = new Random();
		tubeX = Gdx.graphics.getWidth()/2 - topTube.getWidth()/2;
	}

	private void checkInput() {
		if (Gdx.input.justTouched()) {
			Gdx.app.log("Touched","wow");
			mGameState = 1;
			velocity = -30;
			tubeOffset = (randomGenerator.nextFloat()*0.5f)*(Gdx.graphics.getHeight() - gap - 200);
		}
	}

	private void update () {
		if (mGameState != 0) {
			mFlapState = mFlapState == 0 ? 1 : 0;
			velocity += gravity;
			tubeX -= 4;
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
		batch.draw(topTube, tubeX,
				Gdx.graphics.getHeight()/2 + gap/2 + tubeOffset);
		batch.draw(bottomTube, tubeX,
				Gdx.graphics.getHeight()/2 - gap/2 - bottomTube.getHeight() + tubeOffset);
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
