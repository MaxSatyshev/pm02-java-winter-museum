package museum.calc;

import java.math.BigDecimal;
import java.math.MathContext;

public final class Calculator {
	private Calculator() {}

	public static String calculate(String first, String second, String operation) {
		BigDecimal a = parse(first);
		BigDecimal b = parse(second);
		BigDecimal result = switch (operation) {
			case "+" -> a.add(b);
			case "−" -> a.subtract(b);
			case "×" -> a.multiply(b);
			case "÷" -> divide(a, b);
			default -> throw new IllegalArgumentException("Неизвестная операция.");
		};
		return result.stripTrailingZeros().toPlainString();
	}

	private static BigDecimal parse(String value) {
		String normalized = value.strip().replace(',', '.');
		if (normalized.length() > 32 || !normalized.matches("[+-]?(?:\\d+(?:\\.\\d*)?|\\.\\d+)")) {
			throw new IllegalArgumentException("Введите два числа, например 12 и 4.");
		}
		return new BigDecimal(normalized);
	}

	private static BigDecimal divide(BigDecimal a, BigDecimal b) {
		if (b.signum() == 0) {
			throw new IllegalArgumentException("Деление на ноль невозможно.");
		}
		return a.divide(b, MathContext.DECIMAL64);
	}
}
