
import org.junit.Assert;
import org.junit.Test;
import mapElements.positionAndDirection.Vector2d;

public class Vector2DTest {

    @Test
    public void testToString() {
        Vector2d vector1  = new Vector2d(1,1);
        Assert.assertEquals(vector1.toString(), "(1,1)");
    }

    @Test
    public void precedes() {
        Vector2d vector1  = new Vector2d(1,1);
        Vector2d vector2  = new Vector2d(0,0);
        Assert.assertTrue(vector2.precedes(vector1));
        Assert.assertTrue(vector2.precedes(vector2));
    }

    @Test
    public void follows() {
        Vector2d vector1  = new Vector2d(1,1);
        Vector2d vector2  = new Vector2d(0,0);
        Assert.assertTrue(vector1.follows(vector2));
        Assert.assertTrue(vector1.follows(vector1));
    }

    @Test
    public void upperRight() {
        Vector2d vector1  = new Vector2d(1,0);
        Vector2d vector2  = new Vector2d(0,1);
        Assert.assertEquals(vector1.upperRight(vector2),new Vector2d(1,1));
        Assert.assertEquals(vector2.upperRight(vector1),new Vector2d(1,1));
    }

    @Test
    public void lowerLeft() {
        Vector2d vector1  = new Vector2d(1,0);
        Vector2d vector2  = new Vector2d(0,1);
        Assert.assertEquals(vector1.lowerLeft(vector2),new Vector2d(0,0));
        Assert.assertEquals(vector2.lowerLeft(vector1),new Vector2d(0,0));
    }

    @Test
    public void add() {
        Vector2d vector1  = new Vector2d(1,1);
        Vector2d vector2  = new Vector2d(0,2);
        Assert.assertEquals(vector1.add(vector2),new Vector2d(1,3));
    }

    @Test
    public void subtract() {
        Vector2d vector1  = new Vector2d(1,1);
        Vector2d vector2  = new Vector2d(0,2);
        Assert.assertEquals(vector1.subtract(vector2),new Vector2d(1,-1));
        Assert.assertEquals(vector2.subtract(vector1),new Vector2d(-1,1));
    }

    @Test
    public void opposite() {
        Vector2d vector1  = new Vector2d(1,1);
        Assert.assertEquals(vector1.opposite(),new Vector2d(-1,-1));
    }

    @Test
    public void testEquals() {
        Vector2d vector1  = new Vector2d(1,1);
        Vector2d vector2  = new Vector2d(1,1);
        Assert.assertTrue(vector1.equals(vector2));
        Assert.assertTrue(vector1.equals(vector1));
        Assert.assertFalse(vector1.equals(vector2.opposite()));
    }
}
