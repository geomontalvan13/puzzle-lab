package puzzle.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class Puzzle extends ApplicationAdapter {
	SpriteBatch batch;
	Texture backgroundimg, resetimg, gameoverimg, playagainimg;
	Texture[] numeros = new Texture[9];
	int[][] puzzle = new int[3][3];
	int cont = 1, clicks = 0;
	boolean victoria = false, resetorder = true;
	Music music;
	
	@Override
	public void create () {
		batch = new SpriteBatch();
		
		backgroundimg = new Texture("background.png");
		resetimg = new Texture("reset.png");
		gameoverimg = new Texture("gameover.png");
		playagainimg = new Texture("playagain.png");
		music = Gdx.audio.newMusic(Gdx.files.internal("tokyo.mp3"));
		
		for(int cont = 0; cont<9; cont++)
			numeros[cont] = new Texture(cont+".png");
	}

	@Override
	public void render () {
		Gdx.gl.glClearColor(1, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		batch.begin();
		
		if(!victoria){
			if(resetorder){
				for(int x = 0; x<3; x++){
					for(int y = 0; y<3; y++){
						puzzle[x][y] = -1;
					}
				}
				
				for(int i = 0; i<9; i++){
					int x_rand = (int)(Math.random()*10%3);
					int y_rand = (int)(Math.random()*10%3);
					while(puzzle[y_rand][x_rand]!=-1){
						x_rand = (int)(Math.random()*10%3);
						y_rand = (int)(Math.random()*10%3);
					}
					puzzle[y_rand][x_rand] = i;
				}
			}
			
			resetorder = false;
		
			batch.draw(backgroundimg, 0, 0);
			
			for(int x = 0; x<3; x++){
				for(int y = 0; y<3; y++){
					batch.draw(numeros[puzzle[y][x]], 150*x, 150*y);
				}
			}
			batch.draw(resetimg, 460, 175);
		}
		
		else if(victoria == true){
			batch.draw(gameoverimg, 0, 0);
			batch.draw(playagainimg, 175, 5);
		}
		
		batch.end();
		
		music.play();
		music.setLooping(true);
		
		if(Gdx.input.justTouched()){
			int clickx = Gdx.input.getX();
			int clicky = Gdx.graphics.getHeight()-Gdx.input.getY();
			
			if(!victoria){
				if(clickx > 460 && clickx < 590 && clicky > 175 && clicky < 275){
					clicks = 0;
					resetorder = true;
				}
				
				for(int x = 0; x<3; x++){
					for(int y = 0; y<3; y++){
						if(clickx>x*150 && clickx<x*150+150 && clicky>y*150 && clicky<y*150+150){
							if(y+1<3 && puzzle[y+1][x]==0){//0 a la derecha
								puzzle[y+1][x] = puzzle[y][x];
								puzzle[y][x] = 0;
								clicks++;
							}
							
							if(y-1>=0 && puzzle[y-1][x]==0){//0 a la izquierda
								puzzle[y-1][x] = puzzle[y][x];
								puzzle[y][x] = 0;
								clicks++;
							}
							
							if(x+1<3 && puzzle[y][x+1]==0){//0 arriba
								puzzle[y][x+1] = puzzle[y][x];
								puzzle[y][x] = 0;
								clicks++;
							}
							
							if(x-1>=0 && puzzle[y][x-1]==0){//0 abajo
								puzzle[y][x-1] = puzzle[y][x];
								puzzle[y][x] = 0;
								clicks++;
							}
						}
					}
				}
				
				System.out.println("Clicks: "+clicks);
				
				cont = 1;
						
				for(int x = 2; x>=0; x--){
					for(int y = 0; y<3; y++){
						if(puzzle[x][y] == cont)
							cont++;
						
						else if(y == 2 && x == 0 && puzzle[x][y] == 0)
							cont++;
						
						else
							cont = 1;
					}
				}
				
				if(cont == 10){
					victoria = true;
					System.out.println("Ganastes");
				}
			}
			
			else if(victoria){
				if(clickx >175 && clickx<425 && clicky>15 && clicky<100){
					victoria = false;
					resetorder = true;
					clicks = 0;
				}
			}
		}
	}
}
