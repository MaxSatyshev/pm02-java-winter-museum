package museum.winter;

import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public final class SnowField {
	public record Flake(double x, double y, double speed, double drift, int size) {}

	private final List<Flake> flakes;
	private boolean paused;

	public SnowField(int count, long seed) {
		if (count < 0) throw new IllegalArgumentException("Число частиц не может быть отрицательным.");
		Random random = new Random(seed);
		flakes = new ArrayList<>(count);
		for (int i = 0; i < count; i++) {
			flakes.add(new Flake(random.nextDouble(), random.nextDouble(),
				0.045 + random.nextDouble() * 0.14, (random.nextDouble() - 0.5) * 0.10,
				9 + random.nextInt(12)));
		}
	}

	public void step(double seconds) {
		if (paused || !Double.isFinite(seconds) || seconds <= 0) return;
		for (int i = 0; i < flakes.size(); i++) {
			Flake flake = flakes.get(i);
			flakes.set(i, new Flake(wrap(flake.x() + seconds * flake.drift()),
				wrap(flake.y() + seconds * flake.speed()),
				flake.speed(), flake.drift(), flake.size()));
		}
	}

	private static double wrap(double value) {
		return value - Math.floor(value);
	}

	public void setPaused(boolean paused) {
		this.paused = paused;
	}

	public List<Flake> snapshot() {
		return List.copyOf(flakes);
	}

	public Point2D locationOf(int index, int width, int height) {
		Flake flake = flakes.get(index);
		return new Point2D.Double(flake.x() * width, flake.y() * height);
	}
}
