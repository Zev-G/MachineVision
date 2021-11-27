import java.awt.image.BufferedImage;
import java.lang.Math;
import java.util.Arrays;

public class GroupShape {
	public static double[][] getShape(int x, int y, int points, BufferedImage image, int threshold) {
		int[] p0 = Colours.split(image.getRGB(x, y));
		
		double[][] shape = new double[2][points];
		
		for (int i = 0; i < points; i++) {
			double angle = Math.PI * 2 * i / (double)points;
			
			int len = 1;
			double[] vec = {Math.cos(angle), Math.sin(angle)};
			int[] point = new int[2];
			int[] p1;
			
			do {
				p1 = Colours.split(image.getRGB(point[0], point[1]));

				point[0] = (int)(x + len * vec[0]);
				point[1] = (int)(y + len * vec[1]);
				
				len++;
			} while (point[0] >= 0 && point[0] < image.getWidth() && point[1] >= 0 && point[1] < image.getHeight() && Colours.compareColours(p0, p1, threshold));

			shape[0][i] = point[0];
			shape[1][i] = point[1];
		}
		
		return shape;
	}
}
