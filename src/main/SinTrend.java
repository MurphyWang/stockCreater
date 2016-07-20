package main;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class SinTrend {

	public static void main(String args[]) {
		double yvalue = 1;
		int range = 100;
		double deltavariance = 10;
		double delta;
		double sine_index = 0;
		double wavelength = 0.33;
		double periodmin = 0;
		double periodmax = 0;
		double direction = 1;
		double olddirection = 0;
		String trend = "positive";
		int i = 0;
		int maxi = 100;
		Map<Double, Double> data = new HashMap<>();

		while (i < maxi) {
			olddirection = direction;
			direction = Math.sin(sine_index);
			direction = direction < 0 ? Math.floor(direction) : Math.ceil(direction);

			delta = getRandomDouble(0, deltavariance);
			yvalue += delta * direction;
			if (trend.equals("positive")) {
				if (yvalue < periodmin) {
					yvalue = periodmin;
				}
				if (olddirection < direction) {
					periodmin = yvalue;
				}
			} else {
				if (yvalue > periodmax) {
					yvalue = periodmax;
				}
				if (olddirection > direction) {
					periodmax = yvalue;
				}
			}

			data.put(sine_index, yvalue);
			System.out.println("sine_index = " + sine_index + ", yvalue = " + yvalue);
			sine_index += Math.sin(getRandomDouble(-1, 1));
			i += 1;
		}
	}

	private static double getRandomDouble(double min, double max) {
		Random random = new Random();
		double result = random.nextDouble() * (max - min) + min;
		return result;
	}
}
