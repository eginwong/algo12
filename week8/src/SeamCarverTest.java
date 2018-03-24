import edu.princeton.cs.algs4.Picture;
import org.junit.Assert;
import org.junit.Test;

public class SeamCarverTest {

    @Test(expected = IllegalArgumentException.class)
    public void shouldThrowIllegalArgumentForNullPicture() {
        Picture picture = null;
        new SeamCarver(picture);
    }

    @Test
    public void shouldReturnLocalCopyExactlyWhenNew() {
        Picture picture = new Picture("./in/1x1.png");
        SeamCarver seamCarver = new SeamCarver(picture);
        Assert.assertEquals(picture, seamCarver.picture());
    }

    @Test
    public void shouldReturnHeightAndWidth() {
        Picture picture = new Picture("./in/1x1.png");
        SeamCarver seamCarver = new SeamCarver(picture);
        Assert.assertEquals(picture.height(), seamCarver.height());
        Assert.assertEquals(picture.width(), seamCarver.width());
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldThrowForNullHSeam() {
        Picture picture = new Picture("./in/3x7.png");
        SeamCarver seamCarver = new SeamCarver(picture);
        seamCarver.removeHorizontalSeam(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldThrowForNullVSeam() {
        Picture picture = new Picture("./in/3x7.png");
        SeamCarver seamCarver = new SeamCarver(picture);
        seamCarver.removeVerticalSeam(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldThrowForTooSmallHSeam() {
        Picture picture = new Picture("./in/1x1.png");
        SeamCarver seamCarver = new SeamCarver(picture);
        seamCarver.removeHorizontalSeam(new int[]{1});
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldThrowForTooSmallVSeam() {
        Picture picture = new Picture("./in/1x1.png");
        SeamCarver seamCarver = new SeamCarver(picture);
        seamCarver.removeVerticalSeam(new int[]{1});
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldThrowForIncorrectHeight() {
        Picture picture = new Picture("./in/3x7.png");
        SeamCarver seamCarver = new SeamCarver(picture);
        seamCarver.removeVerticalSeam(new int[]{1, 2, 3});
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldThrowForUnallowableWidth() {
        Picture picture = new Picture("./in/3x7.png");
        SeamCarver seamCarver = new SeamCarver(picture);
        seamCarver.removeHorizontalSeam(new int[]{2, 3, 9});
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldThrowForUnallowableHeight() {
        Picture picture = new Picture("./in/3x7.png");
        SeamCarver seamCarver = new SeamCarver(picture);
        seamCarver.removeVerticalSeam(new int[]{9, 2, 3, 5, 6, 7, 1});
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldThrowForLargeJump() {
        Picture picture = new Picture("./in/3x7.png");
        SeamCarver seamCarver = new SeamCarver(picture);
        seamCarver.removeVerticalSeam(new int[]{1, 2, 0, 1, 1, 1, 1});
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldThrowForBadColInputEnergy() {
        Picture picture = new Picture("./in/1x1.png");
        SeamCarver seamCarver = new SeamCarver(picture);
        seamCarver.energy(1,0);
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldThrowForBadRowInputEnergy() {
        Picture picture = new Picture("./in/1x1.png");
        SeamCarver seamCarver = new SeamCarver(picture);
        seamCarver.energy(0,1);
    }

    @Test
    public void shouldGetMaxBorderEnergy() {
        Picture picture = new Picture("./in/1x1.png");
        SeamCarver seamCarver = new SeamCarver(picture);
        Assert.assertEquals(1000, seamCarver.energy(0,0), 0);
    }

    @Test
    public void shouldGetExpectedEnergy() {
        Picture picture = new Picture("./in/3x4.png");
        SeamCarver seamCarver = new SeamCarver(picture);
        Assert.assertEquals(1000, seamCarver.energy(0,0), 0);
        Assert.assertEquals(1000, seamCarver.energy(1,0), 0);
        Assert.assertEquals(1000, seamCarver.energy(2,0), 0);
        Assert.assertEquals(1000, seamCarver.energy(0,1), 0);
        Assert.assertEquals(1000, seamCarver.energy(0,2), 0);
        Assert.assertEquals(1000, seamCarver.energy(0,3), 0);
        Assert.assertEquals(1000, seamCarver.energy(1,3), 0);
        Assert.assertEquals(1000, seamCarver.energy(2,3), 0);
        Assert.assertEquals(Math.sqrt(52225), seamCarver.energy(1,1), 0);
        Assert.assertEquals(Math.sqrt(52024), seamCarver.energy(1,2), 0);
    }
}