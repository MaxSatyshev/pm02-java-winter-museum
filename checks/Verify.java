import museum.calc.Calculator;
import museum.winter.SnowField;

public final class Verify {
	private static int passed;

	private static void check(boolean value, String label) {
		if (!value) throw new AssertionError(label);
		passed++;
		System.out.println("OK: " + label);
	}

	private static void fails(Runnable action, String label) {
		try {
			action.run();
			throw new AssertionError(label);
		} catch (IllegalArgumentException ex) {
			check(true, label);
		}
	}

	public static void main(String[] args) {
		check(Calculator.calculate("12", "4", "+").equals("16"), "12 + 4 = 16");
		check(Calculator.calculate("12", "4", "−").equals("8"), "12 − 4 = 8");
		check(Calculator.calculate("12", "4", "×").equals("48"), "12 × 4 = 48");
		check(Calculator.calculate("12", "4", "÷").equals("3"), "12 ÷ 4 = 3");
		check(Calculator.calculate("2,5", "0,5", "+").equals("3"), "дробный ввод с запятой");
		fails(() -> Calculator.calculate("12", "0", "÷"), "деление на ноль");
		fails(() -> Calculator.calculate("abc", "4", "+"), "нечисловой ввод");

		SnowField snow = new SnowField(54, 2206);
		var first = snow.snapshot();
		snow.step(1.25);
		check(!snow.snapshot().equals(first), "снежинки изменили координаты");
		var inMotion = snow.snapshot();
		snow.setPaused(true);
		snow.step(1);
		check(snow.snapshot().equals(inMotion), "пауза сохраняет кадр");
		snow.setPaused(false);
		snow.step(20);
		check(snow.snapshot().stream().allMatch(f -> f.x() >= 0 && f.x() < 1 && f.y() >= 0 && f.y() < 1),
			"частицы возвращаются в область сцены");
		check(!snow.snapshot().equals(inMotion), "движение продолжается после паузы");
		var beforeInvalid = snow.snapshot();
		snow.step(Double.NaN);
		snow.step(-1);
		check(snow.snapshot().equals(beforeInvalid), "некорректный шаг не меняет состояние");
		System.out.println("Итого: " + passed + " проверок пройдено.");
	}
}
