package museum.calc;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.Files;
import java.nio.file.Path;
import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;

public final class CalcWindow {
	private final JTextField first = new JTextField("12");
	private final JTextField second = new JTextField("4");
	private final JLabel result = new JLabel("Выберите действие");

	public JPanel content() {
		JPanel root = new JPanel(new BorderLayout(0, 16));
		root.setBorder(BorderFactory.createEmptyBorder(20, 22, 20, 22));
		root.setBackground(new Color(243, 246, 251));
		JLabel title = new JLabel("Мини-калькулятор");
		title.setFont(new Font("Segoe UI", Font.BOLD, 23));
		root.add(title, BorderLayout.NORTH);

		JPanel work = new JPanel(new GridLayout(2, 1, 0, 8));
		work.setOpaque(false);
		JPanel values = new JPanel(new GridLayout(2, 2, 10, 4));
		values.setOpaque(false);
		values.add(new JLabel("Первое число"));
		values.add(new JLabel("Второе число"));
		values.add(first);
		values.add(second);
		work.add(values);

		JPanel operations = new JPanel(new FlowLayout(FlowLayout.LEFT, 9, 6));
		operations.setOpaque(false);
		for (String symbol : new String[]{"+", "−", "×", "÷"}) {
			JButton button = new JButton(symbol);
			button.getAccessibleContext().setAccessibleName("Операция " + symbol);
			button.addActionListener(event -> evaluate(symbol));
			operations.add(button);
		}
		work.add(operations);
		root.add(work, BorderLayout.CENTER);
		result.setFont(new Font("Segoe UI", Font.BOLD, 16));
		root.add(result, BorderLayout.SOUTH);
		return root;
	}

	private void evaluate(String operation) {
		try {
			result.setForeground(new Color(27, 82, 71));
			result.setText("Результат: " + Calculator.calculate(first.getText(), second.getText(), operation));
		} catch (IllegalArgumentException ex) {
			result.setForeground(new Color(150, 40, 45));
			result.setText(ex.getMessage());
		}
	}

	private static void layoutAll(java.awt.Container parent) {
		parent.doLayout();
		for (java.awt.Component child : parent.getComponents()) {
			if (child instanceof java.awt.Container inner) layoutAll(inner);
		}
	}

	private static void saveExamples(Path folder) throws IOException {
		Files.createDirectories(folder);
		CalcWindow calculator = new CalcWindow();
		JPanel panel = calculator.content();
		panel.setSize(510, 252);
		layoutAll(panel);
		String[] operations = {"+", "−", "×", "÷", "÷"};
		for (int index = 0; index < operations.length; index++) {
			if (index == 4) calculator.second.setText("0");
			calculator.evaluate(operations[index]);
			BufferedImage image = new BufferedImage(panel.getWidth(), panel.getHeight(), BufferedImage.TYPE_INT_RGB);
			Graphics2D graphics = image.createGraphics();
			panel.printAll(graphics);
			graphics.dispose();
			ImageIO.write(image, "png", folder.resolve("calc-" + index + ".png").toFile());
		}
	}

	public static void main(String[] args) {
		if (args.length == 2 && "--capture".equals(args[0])) {
			SwingUtilities.invokeLater(() -> {
				try {
					saveExamples(Path.of(args[1]));
				} catch (IOException ex) {
					throw new UncheckedIOException(ex);
				}
			});
			return;
		}
		SwingUtilities.invokeLater(() -> {
			JFrame window = new JFrame("TicketCalculator · Java Swing");
			window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			window.setContentPane(new CalcWindow().content());
			window.setSize(510, 285);
			window.setLocationByPlatform(true);
			window.setVisible(true);
		});
	}
}
