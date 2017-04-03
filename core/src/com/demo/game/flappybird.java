package com.demo.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Circle;
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
	float gravity = 2;
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
	ShapeRenderer shapeRenderer;

	
	@Override
	public void create () {

		batch = new SpriteBatch();
		background = new Texture("bg.png");
		font = new BitmapFont();
		mScore = 0;
		mScoreTube = 0;
		birds = new Texture[2];
		birds[0] = new Texture("bird.png");
		birds[1] = new Texture("bird2.png");
		birdCircle = new Circle();
		topTube = new Texture("toptube.png");
		bottomTube = new Texture("bottomtube.png");
		birdY = Gdx.graphics.getHeight()/2-birds[0].getHeight()/2;
		maxGapOffset = Gdx.graphics.getHeight()/2 - gap / 2 - 100;
		distanceBetweenTubes = Gdx.graphics.getWidth()*3/4;
		topTubeRectangles = new Rectangle[numOfTubes];
		bottomTubeRectangles = new Rectangle[numOfTubes];

		randomGenerator = new Random();
		for (int i = 0; i < numOfTubes; i++) {
			topTubeRectangles[i] = new Rectangle();
			bottomTubeRectangles[i] = new Rectangle();
			tubeX[i] = Gdx.graphics.getWidth() / 2 - topTube.getWidth() / 2 + i * distanceBetweenTubes;
			tubeOffset[i] = (randomGenerator.nextFloat()-0.5f)*(Gdx.graphics.getHeight() - gap - 200);
		}
		shapeRenderer = new ShapeRenderer();
	}

	private void checkInput() {
		if (Gdx.input.justTouched()) {
			Gdx.app.log("flappyGame","touched");
			mGameState = 1;
			velocity = -20;
		}
	}

	private void update () {
		if (mGameState != 0) {
			mFlapState = mFlapState == 0 ? 1 : 0;
			velocity += gravity;
			//keep track of score:
			if (tubeX[mScoreTube]+topTube.getWidth() < Gdx.graphics.getWidth()/2) {
				mScore +=10;
				mScoreTube = (mScoreTube + 1)%numOfTubes;
			}
			for (int i=0; i < numOfTubes; i++) {
				topTubeRectangles[i].set(tubeX[i],Gdx.graphics.getHeight() / 2 + gap / 2 + tubeOffset[i],
						topTube.getWidth(),topTube.getHeight());
				bottomTubeRectangles[i].set(tubeX[i],Gdx.graphics.getHeight()/2 - gap/2 - bottomTube.getHeight()
						+ tubeOffset[i], bottomTube.getWidth(),bottomTube.getHeight());
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
	public void render () {
		/*Gdx.gl.glClearColor(1, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);*/
		checkInput();
		update();

		batch.begin();
		switch (mGameState) {
			case 0: //Game Entry Page
				batch.draw(background, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
				font.setColor(1.0f, 0.0f, 0.0f,1.0f);
				font.getData().setScale(8);
				font.draw(batch,"Start Game",Gdx.graphics.getWidth()/2-font.getRegion().getRegionWidth()-font.getSpaceWidth()*2, Gdx.graphics.getHeight()/2+font.getRegion().getRegionHeight()/2);
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


				font.setColor(1.0f, 0.0f, 0.0f,1.0f);
				font.getData().setScale(8);
				font.draw(batch,"Score: "+String.valueOf(mScore),100, 200);
				break;
			case -1:
				//Game Over
				batch.draw(background, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
				font.setColor(1.0f, 0.0f, 0.0f,1.0f);
				font.getData().setScale(8);
				font.draw(batch,"Game Over",Gdx.graphics.getWidth()/2-font.getRegion().getRegionWidth()-font.getSpaceWidth()*2, Gdx.graphics.getHeight()/2+font.getRegion().getRegionHeight()/2);
				font.draw(batch,"Score: "+String.valueOf(mScore),100, 200);
				break;
		}
		batch.end();
		//shape renderer
		birdCircle.set(Gdx.graphics.getWidth() / 2,
				birdY+birds[mFlapState].getHeight()/2, birds[mFlapState].getWidth()/2);
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

	}
	
	@Override
	public void dispose () {

		batch.dispose();
		background.dispose();
		birds[0].dispose();
		birds[1].dispose();
		topTube.dispose();
		bottomTube.dispose();
	}
}
