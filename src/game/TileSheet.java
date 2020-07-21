package game;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import engine.graphics.Graphics3D;
import engine.main.KeyBoard;
import engine.main.Mouse;

class TileSheet {
	//map
	private boolean imported = false;
	private Tile[][] tiles;
	public  ArrayList<Ent> ents = new ArrayList<Ent>();
	
	static BufferedImage[] buildings = new BufferedImage[3];
	//media
	static private BufferedImage belowTileTex;
	//attributes
	public int height = 7;
    public int length = 12;
    public int spaceing=1;
    //owner
    public int tileScreenFit;
    public int zoom = 0;
    Level level = null;
    
    
	public TileSheet(Level parent,int length,int height) {
		if(!imported) {
			belowTileTex = Graphics3D.importImage("Tile/Tile3.png");
			for(int i = 0;i<3;i++) buildings[i] = engine.graphics.Graphics3D.importImage("Background/Building"+i+".png");
			imported = true;
		}
		this.length = length;
		this.height = height;
		tiles = new Tile[length][height];
		for(int i = 0;i<tiles.length;i++) {
			for(int j = 0;j<tiles[0].length;j++) {
				tiles[i][j] = new Tile(Tile.AIR,i,j,this);
			}
		}
		level = parent;
	}
	public TileSheet(Level parent,int spaceing,int length,int height) {
		belowTileTex = Graphics3D.importImage("Tile/Tile3.png");
		this.spaceing = spaceing;
		this.length = length;
		this.height = height;
		tiles = new Tile[length][height];
		for(int i = 0;i<tiles.length;i++) {
			for(int j = 0;j<tiles[0].length;j++) {
				tiles[i][j] = new Tile(Tile.AIR,i,j,this);
			}
		}
		level = parent;
	}
	
	public Tile getTile(int x,int y) {
		if(x<length&&y<height&&x>-1&&y>-1) {
			return tiles[x][y];
		}else {
			return new Tile(Tile.SOLID,x,y,this);
		}
	}
	
	public void setTile(int x,int y,boolean painful,double height,boolean coin,int textID,int hardness,int layer,int brightness) {
		Tile t = getTile(x,y);
		t.setTile(painful, height, coin, textID, hardness, layer, brightness);
	}
	
	public void render(Graphics2D g,double scrollX,int ww,int wh) {
		if(zoom<0) zoom = 0;
		if(zoom>10) zoom = 10;
		spaceing = wh/(height+1+zoom);
		tileScreenFit = (int)((double)ww/(double)spaceing);
		renderBuildings(g,scrollX);
		renderGround(g,scrollX,wh);
		renderLayer(g,scrollX,1);
		for(Ent c:ents) {
			double x = ((c.getX()-scrollX-0.5)+tileScreenFit/2)*spaceing;
			double y = ((c.getY()-0.5))*spaceing;
			g.translate(x, y);
			c.render(g);
			g.translate(-x, -y);
		}
		
		renderLayer(g,scrollX,0);
	}
	
	public void update() {
		Tile.updateTex();
		for(int i = 0;i<ents.size();i++) {
			Ent temp = ents.get(i);
			temp.update();
			if(temp.shouldBeDestroyed()==true) ents.remove(i);
		}
	}
	
	private void renderLayer(Graphics2D g,double x,int layer) {
		if(layer==1) {
			g.setColor(Color.black);
			g.drawRect((int)((-x+tileScreenFit/2)*spaceing), 0, length*spaceing, height*spaceing);
		}
		for(int i = -tileScreenFit/2;i<=tileScreenFit/2+2;i++) {
			if(i+(int)x>tiles.length-1) continue;
			if(i+(int)x<0) continue;
			for(int j = 0;j<tiles[0].length;j++) {
				Tile temp = tiles[i+(int)x][j];
				if(temp.layer==layer) continue;
				g.translate((i+tileScreenFit/2)*spaceing, j*spaceing);
				temp.render(g, ((int)x-x)*spaceing);
				g.translate(-(i+tileScreenFit/2)*spaceing, -j*spaceing);
			}
		}
		
	}
	
	public void renderBuildings(Graphics2D g,double scrollX) {
		for(int i = 0;i<tileScreenFit/2+2;i++) {
			int imageIndex = (int)(scrollX/4.0+i)%buildings.length;
			int buildingXpos = (int)(2.0*((-(scrollX/4.0)*spaceing)%spaceing+i*spaceing));
			int buildingYpos = height*spaceing-spaceing*4;
			g.drawImage(buildings[imageIndex],buildingXpos,buildingYpos,spaceing*2,spaceing*4, null);
		}
	}
	
	private void renderGround(Graphics2D g,double x,int wh) {
		for(int i = -tileScreenFit/2;i<=tileScreenFit/2+2;i++) {
			for(int j = 0;j<(int)(wh/spaceing)-height+1;j++) {
				g.translate((i+tileScreenFit/2)*spaceing, j*spaceing);
				g.drawImage(belowTileTex,(int)(((int)x-x)*spaceing),height*spaceing,spaceing,spaceing,null);
				//gradient on soil as depth increases
				if(j*50<255) {
					g.setColor(new Color(0,0,0,j*50));
					g.drawImage(belowTileTex,(int)(((int)x-x)*spaceing),height*spaceing,spaceing,spaceing,null);
				}
				else g.setColor(Color.black);
				g.fillRect((int)(((int)x-x)*spaceing),height*spaceing,spaceing,spaceing);
				g.translate(-(i+tileScreenFit/2)*spaceing, -j*spaceing);
			}
		}
	}
	
	public void input(KeyBoard keyBoard,Mouse mouse) {
		for(int i = 0;i<ents.size();i++) {
			Ent temp = ents.get(i);
			temp.input(keyBoard, mouse);
		}
	}
	
	
	public void reSize(int len,int hei) {
		length = len;
		height = hei;
		tiles = new Tile[length][height];
		for(int i = 0;i<tiles.length;i++) {
			for(int j = 0;j<tiles[0].length;j++) {
				tiles[i][j] = new Tile(Tile.AIR,i,j,this);
			}
		}
	}
	
	//this function should not be called manualy use setLevel instead
	void addEntity(Ent e) {
		ents.add(e);
	}
	
	public TileSheet copy() {
		TileSheet sheetClone = new TileSheet(level,length,height);
		//copy tiles
		for(int i = 0;i<tiles.length;i++) {
			for(int j = 0;j<tiles[0].length;j++) {
				Tile tmp = this.getTile(i, j);
				sheetClone.setTile(tmp.x,tmp.y,tmp.painful,tmp.height,tmp.isCoin,tmp.textID,tmp.hardness,tmp.layer,tmp.brightness);
			}
		}
		
		
		return sheetClone;
		
	}
	
}