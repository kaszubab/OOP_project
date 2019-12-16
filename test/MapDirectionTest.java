package test;

import mapElements.positionAndDirection.MapDirection;
import org.junit.Assert;
import org.junit.Test;

public class MapDirectionTest {
    @Test
    public void testNext() {
        MapDirection kierunek = MapDirection.EAST;
        Assert.assertEquals(kierunek.next(),MapDirection.SOUTH);
        kierunek = MapDirection.NORTH;
        Assert.assertEquals(kierunek.next(),MapDirection.EAST);
        kierunek = MapDirection.SOUTH;
        Assert.assertEquals(kierunek.next(),MapDirection.WEST);
        kierunek = MapDirection.WEST;
        Assert.assertEquals(kierunek.next(),MapDirection.NORTH);
    }

    @Test
    public void testPrevious() {
        MapDirection kierunek = MapDirection.EAST;
        Assert.assertEquals(kierunek.previous(),MapDirection.NORTH);
        kierunek = MapDirection.NORTH;
        Assert.assertEquals(kierunek.previous(),MapDirection.WEST);
        kierunek = MapDirection.SOUTH;
        Assert.assertEquals(kierunek.previous(),MapDirection.EAST);
        kierunek = MapDirection.WEST;
        Assert.assertEquals(kierunek.previous(),MapDirection.SOUTH);
    }

}
