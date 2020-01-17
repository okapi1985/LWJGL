package world;

public class Tile {

    public static Tile tiles[] = new Tile[255];
    public static byte numberOfTiles = 0;

    public static final Tile testTile = new Tile("test");
    public static final Tile test2 = new Tile("checker").setSolid();
    public static final Tile skaven = new Tile("skaven");

    private byte id;
    private boolean solid;
    private String texture;

    public Tile(String texture){
        this.id = numberOfTiles;
        numberOfTiles++;
        this.texture = texture;
        this.solid = false;
        if (tiles[id] != null){
            throw new IllegalStateException("Tiles at: ["+id+"] is already being used!");
        }
        tiles[id] = this;
    }

    public Tile setSolid() {
        this.solid = true;
        return this;
    }

    public boolean isSolid() {
        return solid;
    }

    public byte getId() {
        return id;
    }

    public String getTexture() {
        return texture;
    }
}
