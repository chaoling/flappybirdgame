package com.demo.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class flappybird extends ApplicationAdapter {
	private int mFlapState = 0;
	SpriteBatch batch;
	Texture background;
	Texture[] birds;
	
	@Override
	public void create () {
		batch = new SpriteBatch();
		background = new Texture("bg.png");
		birds = new Texture[2];
		birds[0] = new Texture("bird.png");
		birds[1] = new Texture("bird2.png");
	}


	public void update () {
		mFlapState = mFlapState == 0 ? 1:0;
	}

	@Override
	public void render () {
		/*Gdx.gl.glClearColor(1, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);*/
		update();
		batch.begin();
		batch.draw(background, 0, 0, Gdx.graphics.getWidth(),Gdx.graphics.getHeight());
		batch.draw(birds[mFlapState], Gdx.graphics.getWidth()/2 - birds[mFlapState].getWidth()/2,
				Gdx.graphics.getHeight()/2-birds[mFlapState].getHeight()/2);
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
