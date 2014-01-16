package Entity;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import javax.imageio.ImageIO;

import TileMap.TileMap;
public class Fast extends MapObject{

	private int health;
	private int maxHealth;
	private TileMap tiley;

	private boolean dead;
	
	private int jumped = 0;
	private int whereJump = 0;

	private ArrayList<BufferedImage[]> sprites;
	private final int[] numFrames = {
		1, 8, 3, 3, 1 
	};

	public static final int IDLE = 0;
	public static final int RUNNING = 1;	
	public static final int FLYING = 2;	
	public static final int SLIDING = 3; 
	public static final int JUMPING = 4;
	public Fast(TileMap tm, double dx){

		super(tm); 
		this.tiley = tm;

		width = 35;
		cwidth = 32;
		height = 160;
		cheight = 128
				;

		moveSpeed = dx;
		maxSpeed = dx;
		stopSpeed = 0.0;
		fallSpeed = 0.1;
		maxFallSpeed = 4.0;
		jumpStart = -6.5;//-5.8
		stopJumpSpeed = 0.2;
		
		facingRight = true;

		health = maxHealth = 2;
	try{

		BufferedImage spritesheet = ImageIO.read(
				getClass().getResourceAsStream("/Sprites/Image2.png"));

		sprites = new ArrayList<BufferedImage[]>();
		for(int i = 0; i < 5; i++){
			BufferedImage[] bi = new BufferedImage[numFrames[i]];

			for(int j = 0; j < numFrames[i]; j++){
				if(i == 0){
					bi[j] = spritesheet.getSubimage(
							j * width,
							i * height,
							width,
							height);
				}else if(i == 1 || i == 4){
					bi[j] = spritesheet.getSubimage(j* 46, i * height, 46, height);
				}else if(i == 2){
					bi[j] = spritesheet.getSubimage(j * 115,i * height, 115, height);
				}else if(i == 3){
					bi[j] = spritesheet.getSubimage(j * 85,i * height, 85, height);
				}
			}
			sprites.add(bi);
		}
	}catch(Exception e){
		e.printStackTrace();
	}
	animation = new Animation();
	currentAction = IDLE;
	animation.setFrames(sprites.get(IDLE));
	animation.setDelay(40);
	}

	public int getHealth() {return health;}
	public boolean isDead(){return dead;}
	public void kill(){dead = true;}
	public void revive(){dead = false;}
	public void setDx(double x){
		dx = x;
	}
	
	public void setCurrentAction(int action){
		currentAction = action;
	}
	public int getCurrentAction(){
		return currentAction;
	}

	public void checkFallDead(){
		if(y > tiley.getHeight() - 70){
			dead = true;
		} else {
			dead = false;
		}
	}
	
	public boolean fellDead(){
		return dead;
	}
	public void reset(){
		health = setMaxHealth(1);
	}
	
	private void getNextPosition() {
		
		if(left) {
			dx -= moveSpeed;
			if(dx < -maxSpeed) {
				dx = -maxSpeed;
			}
		}
		else if(right) {
			dx += moveSpeed;
			if(dx > maxSpeed) {
				dx = maxSpeed;
			}
		}
		else {
			if(dx > 0) {
				dx -= stopSpeed;
				if(dx < 0) {
					dx = 0;
				}
			}
			else if(dx < 0) {
				dx += stopSpeed;
				if(dx > 0) {
					dx = 0;
				}
			}
		}
		if(jumping && !falling) {
			
			dy = jumpStart;
			falling = true;	
		}
		if(!falling && !jumping){
			dy = 0;
		}
		
		
if(falling) {
			
			dy += fallSpeed;
			
			if(dy > 0) jumping = false;
			if(dy < 0 && !jumping) dy += stopJumpSpeed;
			
			if(dy > maxFallSpeed) dy = maxFallSpeed;
}
if(!jumping && !falling){
	if(dy != 0){
		dy = 0;
	}
	
}
	}
	
	public void update(){
		getNextPosition();
		checkTileMapCollision();
		checkFallDead();
		setPosition(xtemp, ytemp);

		if(dy > 0 || dy < 0){
			if(currentAction != JUMPING){
				currentAction = JUMPING;
				animation.setFrames(sprites.get(JUMPING));
				animation.setDelay(40);
				width = 46;
				cwidth = 46;
			}
		}else if(right || left){
			if(currentAction != RUNNING){
				currentAction = RUNNING;
				animation.setFrames(sprites.get(RUNNING));
				animation.setDelay(40);
				width = 46;
				cwidth = 46;
			}
		}else{
			if(currentAction != IDLE){
				currentAction = IDLE;
				animation.setFrames(sprites.get(IDLE));
				animation.setDelay(400);
				width = 35;
				cwidth = 35;
			}
		}
		animation.update();
	}
	
	public void draw(Graphics2D g){
		//
		//g.fill(getRectangle());
		setMapPosition();
		super.draw(g);
		
	}
	
	public int getMaxHealth() {
		return maxHealth;
	}

	public int setMaxHealth(int maxHealth) {
		this.maxHealth = maxHealth;
		return maxHealth;
	}

	
}